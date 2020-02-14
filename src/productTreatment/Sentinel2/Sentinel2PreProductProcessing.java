package productTreatment.Sentinel2;

import java.io.File;
import java.nio.file.Paths;
import java.util.Properties;

import enums.ProductTreatmentConfigurations;
import productTreatment.PreProductProcessing;

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
	}

	//TODO:
	private boolean isL1C(String productName) {
		return false;
	}

	@Override
	public void decompressJP2Files() {
		String productsPath = prop.getProperty(ProductTreatmentConfigurations.products_folder.name());
		File[] dir = new File(productsPath).listFiles();
		for (File file : dir)
			if(file.isDirectory())
				decompressJP2Files(file.getAbsolutePath());
	}
}