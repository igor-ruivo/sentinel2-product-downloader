package utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import enums.DownloaderConfigurations;

public class AddDownloaderConfigProperties {

	public static void main(String[] args) {
		InputStream is = null;
		OutputStream os = null;
		try {
			Properties prop = new Properties();
			String path = "./configs/downloader_config.properties";
			is = new FileInputStream(path);
			os = new FileOutputStream(path);
			prop.load(is);
			prop.setProperty(DownloaderConfigurations.username.name(), "cropdetection");
			prop.setProperty(DownloaderConfigurations.password.name(), "password");
			prop.setProperty(DownloaderConfigurations.max_concurrent_downloads.name(), "2");
			prop.setProperty(DownloaderConfigurations.products_folder.name(), "D:/Sentinel-2");
			prop.setProperty(DownloaderConfigurations.intersectingPoint.name(), "39.273646287051236, -8.673529684250335");
			prop.setProperty(DownloaderConfigurations.dateStart.name(), "2019-01-01");
			prop.setProperty(DownloaderConfigurations.dateEnd.name(), "2019-12-31");
			prop.setProperty(DownloaderConfigurations.platformName.name(), "Sentinel-2");
			prop.setProperty(DownloaderConfigurations.productType.name(), "S2MSI1C");
			prop.setProperty(DownloaderConfigurations.cloudCoverPercentage.name(), "[0 TO 100]");
			prop.setProperty(DownloaderConfigurations.max_connections_per_time_window.name(), "5");
			prop.setProperty(DownloaderConfigurations.time_window.name(), "300000");
			prop.store(os, null);
		} catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				is.close();
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}