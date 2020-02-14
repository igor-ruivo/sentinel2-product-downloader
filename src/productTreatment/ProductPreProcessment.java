package productTreatment;

public interface ProductPreProcessment {
	public static final String SENTINEL2_L1C_IMG_DATA_PATH = "/GRANULE/%s/IMG_DATA";
	public static final String SENTINEL2_L2A_IMG_DATA_PATH = "/GRANULE/%s/IMG_DATA/R10m";
	void unzip(String zipPath);
	/**
	 * Decompresses all JP2 files in all the products available in the previously configured products' folder. 
	 */
	void decompressJP2Files();
	/**
	 * Decompresses all JP2 files in the specified product directory (productDir). 
	 */
	void decompressJP2Files(String productDir);
}