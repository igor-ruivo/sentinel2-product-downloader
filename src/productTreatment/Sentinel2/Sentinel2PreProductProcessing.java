package productTreatment.sentinel2;

import java.io.File;
import java.util.Properties;

import enums.ProductTreatmentConfigurations;
import productTreatment.PreProductProcessing;
import utils.Sentinel2ProductUtils;

public class Sentinel2PreProductProcessing extends PreProductProcessing {

	Properties prop;

	public Sentinel2PreProductProcessing() {
		prop = null;
	}

	public Sentinel2PreProductProcessing(Properties prop) {
		this.prop = prop;
	}

	//private void loadJP2File() {

	//}

	//TODO:
	@Override
	public void unzip(String zipPath) {

	}

	//TODO:
	@Override
	public void decompressJP2Files(String productDir) {
		System.out.println(productDir);
	}

	@Override
	public void decompressJP2Files() {
		String productsPath = prop.getProperty(ProductTreatmentConfigurations.products_folder.name());
		File[] dir = new File(productsPath).listFiles();
		for(File file : dir)
			if(file.isDirectory() && Sentinel2ProductUtils.checkProductName(file.getName(), null))
				decompressJP2Files(file.getAbsolutePath());
	}
}