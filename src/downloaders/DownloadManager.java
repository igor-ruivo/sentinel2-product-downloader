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

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import enums.HttpMethod;
import utils.XMLParser;

import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class DownloadManager implements Downloader {

	Properties prop;
	Queue<Product> buffer;

	public DownloadManager() {
		prop = null;
		buffer = new ConcurrentLinkedQueue<Product>();
	}

	public DownloadManager(Properties prop) {
		this.prop = prop;
		buffer = new ConcurrentLinkedQueue<Product>();
	}

	public class DownloaderThread extends Thread {

		public DownloaderThread() {

		}

		@Override
		public void run() {
			Product product;
			while((product = buffer.poll()) != null)
				download(product, "C:\\Users\\Igor\\Desktop\\Sentinel-2");
		}
	}

	private void setupThreads() {
		DownloaderThread t1 = new DownloaderThread();
		DownloaderThread t2 = new DownloaderThread();
		t1.start();
		t2.start();
	}

	private InputStream httpConnection(URL url, HttpMethod method) throws IOException {
		String usr = prop.getProperty("username");
		String pwd = prop.getProperty("password");
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
			URL url = new URL("https://scihub.copernicus.eu/dhus/search?q=footprint:%22Intersects(POLYGON((-8.673529684250335%2039.273646287051236,-8.671995928237623%2039.273646287051236,-8.671995928237623%2039.27440185583549,-8.673529684250335%2039.27440185583549,-8.673529684250335%2039.273646287051236)))%22AND%20(%20beginPosition:[2020-01-01T00:00:00.000Z%20TO%202020-01-18T23:59:59.999Z]%20AND%20endPosition:[2020-01-01T00:00:00.000Z%20TO%202020-01-18T23:59:59.999Z]%20)%20AND%20(%20(platformname:Sentinel-2%20AND%20producttype:S2MSI1C))");
			//URL url = new URL("https://scihub.copernicus.eu/dhus/odata/v1/Products('297f58c3-246b-4508-8689-c7ccf852998d')/$value");
			content = httpConnection(url, HttpMethod.POST);
			digestCopernicusXMLResponse(content);
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

	private void download(Product product, String dstFolderPath) {
		System.out.println("Downloading " + product.getFileName() + ".");
		FileOutputStream fos = null;
		try {
			ReadableByteChannel rbc = Channels.newChannel(httpConnection(product.getLink(), HttpMethod.GET));
			fos = new FileOutputStream(dstFolderPath + "\\" +  product.getFileName());
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			System.out.println("Finished downloading " + product.getFileName() + ".");
		}
		catch(IOException e) {
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

	private void digestCopernicusXMLResponse(InputStream xml) throws SAXException, IOException, ParserConfigurationException {
		XMLParser parser = new XMLParser(xml);
		NodeList nList = parser.getNodeListByTagName("entry");
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				String title = eElement.getElementsByTagName("title").item(0).getTextContent();
				URL link = new URL(((Element)nNode.getChildNodes().item(3)).getAttribute("href"));
				String size = eElement.getElementsByTagName("str").item(11).getTextContent();
				Product product = new Product(title, size, link);
				buffer.add(product);
			}
		}
	}
}