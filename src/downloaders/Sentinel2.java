package downloaders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.Properties;

public class Sentinel2 {

	public static void main(String[] args) {
		try {
			Properties prop = new Properties();
			File file = new File("./config/config.properties");
			InputStream is = new FileInputStream(file);
			prop.load(is);
			String usr = prop.getProperty("username");
			String pwd = prop.getProperty("password");
			URL url = new URL("https://scihub.copernicus.eu/dhus/search?q=footprint:%22Intersects(POLYGON((-8.673529684250335%2039.273646287051236,-8.671995928237623%2039.273646287051236,-8.671995928237623%2039.27440185583549,-8.673529684250335%2039.27440185583549,-8.673529684250335%2039.273646287051236)))%22AND%20(%20beginPosition:[2020-01-01T00:00:00.000Z%20TO%202020-01-18T23:59:59.999Z]%20AND%20endPosition:[2020-01-01T00:00:00.000Z%20TO%202020-01-18T23:59:59.999Z]%20)%20AND%20(%20(platformname:Sentinel-2%20AND%20producttype:S2MSI1C))");
			String encoding = Base64.getEncoder().encodeToString((usr + ":" + pwd).getBytes("UTF-8"));

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setRequestProperty  ("Authorization", "Basic " + encoding);
			InputStream content = (InputStream)connection.getInputStream();
			BufferedReader in   = 
					new BufferedReader (new InputStreamReader (content));
			String line;
			while ((line = in.readLine()) != null) {
				System.out.println(line);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}