package downloaders;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.Properties;

import javax.swing.ProgressMonitor;
import javax.swing.ProgressMonitorInputStream;

//parser xml
import org.w3c.dom.NodeList;

import utils.XMLParser;

import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class DownloadManager implements Downloader {

	InputStream configFile;

	public DownloadManager() {
		configFile = null;
	}

	public DownloadManager(InputStream configFile) {
		this.configFile = configFile;
	}

	@Override
	public void downloadSentinel2Product() {
		InputStream content = null;
		Properties prop = new Properties();
		try {
			prop.load(configFile);
			String usr = prop.getProperty("username");
			String pwd = prop.getProperty("password");
			//URL url = new URL("https://scihub.copernicus.eu/dhus/search?q=footprint:%22Intersects(POLYGON((-8.673529684250335%2039.273646287051236,-8.671995928237623%2039.273646287051236,-8.671995928237623%2039.27440185583549,-8.673529684250335%2039.27440185583549,-8.673529684250335%2039.273646287051236)))%22AND%20(%20beginPosition:[2020-01-01T00:00:00.000Z%20TO%202020-01-18T23:59:59.999Z]%20AND%20endPosition:[2020-01-01T00:00:00.000Z%20TO%202020-01-18T23:59:59.999Z]%20)%20AND%20(%20(platformname:Sentinel-2%20AND%20producttype:S2MSI1C))");
			URL url = new URL("https://scihub.copernicus.eu/dhus/odata/v1/Products('297f58c3-246b-4508-8689-c7ccf852998d')/$value");
			String encoding = Base64.getEncoder().encodeToString((usr + ":" + pwd).getBytes("UTF-8"));
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("GET");
			connection.setDoOutput(true);
			connection.setRequestProperty("Authorization", "Basic " + encoding);
			content = connection.getInputStream();
			System.out.println("done");
			java.nio.file.Files.copy(
				      content, 
				      new File("C:\\Users\\Igor\\Desktop\\download.zip").toPath(), 
				      StandardCopyOption.REPLACE_EXISTING);
			
		        
		        
			System.out.println("done2");
			//xmlParser(content);
		} catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				content.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void download(URL website) throws IOException {
		System.out.println("Downloading");
		ReadableByteChannel rbc = Channels.newChannel(website.openStream());
		FileOutputStream fos = new FileOutputStream("C:\\Users\\Igor\\Desktop\\file.zip");
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		System.out.println("complete.");
		fos.close();
	}

	private void xmlParser(InputStream xml) {
		try {
			XMLParser parser = new XMLParser(xml);
			NodeList nList = parser.getNodeListByTagName("entry");
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				System.out.println("\nCurrent Element :" + nNode.getNodeName());

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					System.out.println("Title : " 
							+ eElement
							.getElementsByTagName("title")
							.item(0)
							.getTextContent());
					System.out.println(((Element)nNode.getChildNodes().item(3)).getAttribute("href"));
					download(new URL(((Element)nNode.getChildNodes().item(3)).getAttribute("href")));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}