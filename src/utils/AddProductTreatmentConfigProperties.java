package utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import enums.ProductTreatmentConfigurations;

public class AddProductTreatmentConfigProperties {

	public static void main(String[] args) {
		InputStream is = null;
		OutputStream os = null;
		try {
			Properties prop = new Properties();
			String path = "./configs/product-treatment_config.properties";
			is = new FileInputStream(path);
			os = new FileOutputStream(path);
			prop.load(is);
			prop.setProperty(ProductTreatmentConfigurations.products_folder.name(), "D:/Sentinel-2");
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