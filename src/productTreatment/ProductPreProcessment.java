package productTreatment;

public interface ProductPreProcessment {
	public static final String SENTINEL2_L1C_IMG_DATA_PATH = "/GRANULE/%s/IMG_DATA";
	public static final String SENTINEL2_L2A_IMG_DATA_PATH = "/GRANULE/%s/IMG_DATA/R10m";
	public static final String SENTINEL2_MISSION_IDS_REGEX = "S2A|S2B";
	public static final String SENTINEL2_PRODUCT_LEVELS_REGEX = "MSIL1C|MSIL2A";
	public static final String SENTINEL2_PRODUCT_PDGS_PROCESSING_BASELINE_NUMBER_REGEX = "N\\d{4}";
	public static final String SENTINEL2_PRODUCT_RELATIVE_ORBIT_NUMBER_REGEX = "R\\d{3}";
	public static final String SENTINEL2_PRODUCT_TILE_NUMBER_FIELD_REGEX = "T[A-Z0-9]{5}";
	public static final String SENTINEL2_PRODUCT_DATE_FORMAT = "yyyyMMddHHmmss";
	public static final int SENTINEL2_PRODUCT_NAME_NUM_COMPONENTS = 7;
	public static final int SENTINEL2_PRODUCT_NAME_LENGTH = 65;
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