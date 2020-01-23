package downloaders;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Base64;
import java.util.Properties;

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
		BufferedReader in = null;
		Properties prop = new Properties();
		try {
			prop.load(configFile);
			String usr = prop.getProperty("username");
			String pwd = prop.getProperty("password");
			URL url = new URL("https://scihub.copernicus.eu/dhus/search?q=footprint:%22Intersects(POLYGON((-8.673529684250335%2039.273646287051236,-8.671995928237623%2039.273646287051236,-8.671995928237623%2039.27440185583549,-8.673529684250335%2039.27440185583549,-8.673529684250335%2039.273646287051236)))%22AND%20(%20beginPosition:[2020-01-01T00:00:00.000Z%20TO%202020-01-18T23:59:59.999Z]%20AND%20endPosition:[2020-01-01T00:00:00.000Z%20TO%202020-01-18T23:59:59.999Z]%20)%20AND%20(%20(platformname:Sentinel-2%20AND%20producttype:S2MSI1C))");
			String encoding = Base64.getEncoder().encodeToString((usr + ":" + pwd).getBytes("UTF-8"));
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setRequestProperty("Authorization", "Basic " + encoding);
			content = connection.getInputStream();
			in = new BufferedReader(new InputStreamReader(content));
			String line;
			while((line = in.readLine()) != null)
				System.out.println(line);
			//download(url);
		} catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				content.close();
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void download(URL website) throws IOException {
		ReadableByteChannel rbc = Channels.newChannel(website.openStream());
		FileOutputStream fos = new FileOutputStream("file.zip");
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
	}
}