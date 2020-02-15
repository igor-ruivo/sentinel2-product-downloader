package downloaders;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Base64;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.http.client.utils.URIBuilder;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import enums.DownloaderConfigurations;
import enums.HttpMethods;
import exceptions.InvalidProductNameException;
import products.SatelliteProduct;
import products.sentinel2.L1CProduct;
import products.sentinel2.L2AProduct;
import utils.Sentinel2ProductUtils;
import utils.XMLParser;

import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class DownloadManager implements Downloader {

	Properties prop;
	Queue<SatelliteProduct> buffer;
	long[] timer;
	int counter;
	
	public DownloadManager() {
		prop = null;
		initializeVariables();
	}

	public DownloadManager(Properties prop) {
		this.prop = prop;
		initializeVariables();
	}

	public class DownloaderThread extends Thread {

		public DownloaderThread() {

		}

		@Override
		public void run() {
			SatelliteProduct product;
			while((product = buffer.poll()) != null)
				download(product, prop.getProperty(DownloaderConfigurations.products_folder.name()));
		}
	}

	private void initializeVariables() {
		timer = new long[Integer.parseInt(prop.getProperty(DownloaderConfigurations.max_connections_per_time_window.name()))];
		counter = 0;
		buffer = new ConcurrentLinkedQueue<SatelliteProduct>();
	}

	private void setupThreads() {
		int nThreads = Integer.parseInt(prop.getProperty(DownloaderConfigurations.max_concurrent_downloads.name()));
		for(int i = 0; i < nThreads; i++) {
			DownloaderThread t = new DownloaderThread();
			t.start();
		}
	}

	private boolean canMakeNewRequest() {
		return timer[counter] == 0 || System.currentTimeMillis() - timer[counter] >= Float.parseFloat(prop.getProperty(DownloaderConfigurations.time_window.name()));
	}

	private void setRequestTimer() {
		timer[counter] = System.currentTimeMillis();
		counter = (counter + 1) % timer.length;
	}

	private synchronized InputStream httpConnection(URL url, HttpMethods method) throws IOException, InterruptedException {
		while(!canMakeNewRequest())
			Thread.sleep(3000);
		setRequestTimer();
		String usr = prop.getProperty(DownloaderConfigurations.username.name());
		String pwd = prop.getProperty(DownloaderConfigurations.password.name());
		String encoding = Base64.getEncoder().encodeToString((usr + ":" + pwd).getBytes("UTF-8"));
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		connection.setRequestMethod(method.name());
		connection.setDoOutput(true);
		connection.setRequestProperty("Authorization", "Basic " + encoding);
		return connection.getInputStream();
	}

	@Override
	public void downloadSentinel2Products() {
		InputStream content = null;
		try {
			URIBuilder uri;
			int requests = 0;
			String footprint = String.format(COPERNICUS_FOOTPRINT_FORMAT,
					prop.getProperty(DownloaderConfigurations.intersectingPoint.name()),
					prop.getProperty(DownloaderConfigurations.dateStart.name()),
					prop.getProperty(DownloaderConfigurations.dateEnd.name()),
					prop.getProperty(DownloaderConfigurations.dateStart.name()),
					prop.getProperty(DownloaderConfigurations.dateEnd.name()),
					prop.getProperty(DownloaderConfigurations.platformName.name()),
					prop.getProperty(DownloaderConfigurations.productType.name()),
					prop.getProperty(DownloaderConfigurations.cloudCoverPercentage.name()));
			do {
				uri = new URIBuilder(COPERNICUS_XML_URL);
				uri.addParameter("start", Integer.toString(requests * COPERNICUS_MAX_ROWS_PER_REQUEST));
				uri.addParameter("rows", Integer.toString(COPERNICUS_MAX_ROWS_PER_REQUEST));
				uri.addParameter("q", footprint);
				URL url = uri.build().toURL();
				content = httpConnection(url, HttpMethods.POST);
				requests++;
			}
			while(digestCopernicusXMLResponse(content) == COPERNICUS_MAX_ROWS_PER_REQUEST);
			setupThreads();
		} catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				if(content != null)
					content.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void download(SatelliteProduct product, String dstFolderPath) {
		FileOutputStream fos = null;
		try {
			ReadableByteChannel rbc = Channels.newChannel(httpConnection(product.getProductDownloadLink(), HttpMethods.GET));
			fos = new FileOutputStream(dstFolderPath + (dstFolderPath.endsWith("\\") ? "" : "\\") +  product.getProductFileName());
			System.out.println("Downloading " + product.getProductFileName() + ".");
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			System.out.println("Finished downloading " + product.getProductFileName() + ".");
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if(fos != null)
					fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private String getNodeContext(NodeList nodes, String itemName) {
		for(int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if(node.getAttributes().getNamedItem("name").getNodeValue().equals(itemName))
				return node.getTextContent();
		}
		return null;
	}

	private int digestCopernicusXMLResponse(InputStream xml) throws SAXException, IOException, ParserConfigurationException, InvalidProductNameException {
		XMLParser responseParser = new XMLParser(xml);
		NodeList nList = responseParser.getNodeListByTagName("entry");
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				String title = eElement.getElementsByTagName("title").item(0).getTextContent();
				String id = eElement.getElementsByTagName("id").item(0).getTextContent();
				URL link = new URL(String.format(COPERNICUS_PRODUCT_URL, id));
				String size = getNodeContext(eElement.getElementsByTagName("str"), "size");
				SatelliteProduct product = null;
				if(Sentinel2ProductUtils.isProductL1C(title))
					product = new L1CProduct(title, size, link);
				if(Sentinel2ProductUtils.isProductL2A(title))
					product = new L2AProduct(title, size, link);
				assert(product != null);
				buffer.add(product);
			}
		}
		return nList.getLength();
	}
}