package productTreatment.sentinel2;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.imageio.ImageReadParam;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;

import com.github.jaiimageio.jpeg2000.J2KImageReadParam;
import com.github.jaiimageio.jpeg2000.impl.J2KImageReader;
import com.github.jaiimageio.jpeg2000.impl.J2KImageReaderSpi;

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

	private void loadJP2File() {
		String path = "C:\\Users\\Igor\\Desktop\\selena.jp2";
		J2KImageReader jir = new J2KImageReader(new J2KImageReaderSpi());
		File f = new File(path);
		ImageInputStream iso = null;
		try {
			iso = new FileImageInputStream(f);
			ImageReadParam irp = (J2KImageReadParam) jir.getDefaultReadParam();
			jir.setInput(iso);
			BufferedImage bi = jir.read(0, irp);
			System.out.println(bi.getHeight());
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if(iso != null)
				try {
					iso.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		System.out.println("so far so good");
	}

	//TODO:
	@Override
	public void unzip(String zipPath) {

	}

	//TODO:
	@Override
	public void decompressJP2Files(String productDir) {
		System.out.println(productDir);
		loadJP2File();
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