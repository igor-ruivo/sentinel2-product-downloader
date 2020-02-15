package products;

import java.net.URL;

public interface SatelliteProduct {
	public static final String SENTINEL2_MISSION_IDS_REGEX = "S2A|S2B";
	public static final String SENTINEL2_PRODUCT_LEVEL_L1C = "MSIL1C";
	public static final String SENTINEL2_PRODUCT_LEVEL_L2A = "MSIL2A";
	public static final String SENTINEL2_PRODUCT_PDGS_PROCESSING_BASELINE_NUMBER_REGEX = "N\\d{4}";
	public static final String SENTINEL2_PRODUCT_RELATIVE_ORBIT_NUMBER_REGEX = "R\\d{3}";
	public static final String SENTINEL2_PRODUCT_TILE_NUMBER_FIELD_REGEX = "T[A-Z0-9]{5}";
	public static final String SENTINEL2_PRODUCT_DATE_FORMAT = "yyyyMMddHHmmss";
	public static final int SENTINEL2_PRODUCT_NAME_NUM_COMPONENTS = 7;
	public static final int SENTINEL2_PRODUCT_NAME_LENGTH = 60;
	String getProductName();
	String getProductFileName();
	URL getProductDownloadLink();
	float getProductSize();
	abstract boolean isValidProductName(String productName);
}