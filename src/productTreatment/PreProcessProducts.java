package productTreatment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import productTreatment.Sentinel2.Sentinel2PreProductProcessing;

public class PreProcessProducts {

	private static final String BAD_ARGS = "Invalid arguments. Usage: PreProcessProducts.java [/../product-treatment_config.properties]";
	private static final String CONFIG_PATH = "./configs/product-treatment_config.properties";

	public static void main(String[] args) {
		if(args.length > 1) {
			System.out.println(BAD_ARGS);
			System.exit(1);
		}
		String configPath = null;
		if(args.length == 1)
			configPath = args[0];
		else
			configPath = CONFIG_PATH;
		File file = new File(configPath);
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			Properties prop = new Properties();
			prop.load(is);
			ProductPreProcessment ppp = new Sentinel2PreProductProcessing();
			ppp.decompressJP2Files();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				if(is != null)
					is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}