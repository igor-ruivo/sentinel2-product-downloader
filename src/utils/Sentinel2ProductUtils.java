package utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import exceptions.InvalidProductNameException;
import products.SatelliteProduct;

public class Sentinel2ProductUtils {
	
	public static boolean isProductL1C(String productName) throws InvalidProductNameException {
		String[] components = splitProductName(productName);
		if(!checkProductName(productName, components))
			throw new InvalidProductNameException();
		return components[1].equals(SatelliteProduct.SENTINEL2_PRODUCT_LEVEL_L1C);
	}
	
	public static boolean isProductL2A(String productName) throws InvalidProductNameException {
		String[] components = splitProductName(productName);
		if(!checkProductName(productName, components))
			throw new InvalidProductNameException();
		return components[1].equals(SatelliteProduct.SENTINEL2_PRODUCT_LEVEL_L2A);
	}
	
	private static boolean checkValidNameComponents(String[] productNameComponents) {
		if(productNameComponents.length != SatelliteProduct.SENTINEL2_PRODUCT_NAME_NUM_COMPONENTS)
			return false;
		if(!productNameComponents[0].matches(SatelliteProduct.SENTINEL2_MISSION_IDS_REGEX))
			return false;
		if(!productNameComponents[1].matches(SatelliteProduct.SENTINEL2_PRODUCT_LEVEL_L1C + "|" + SatelliteProduct.SENTINEL2_PRODUCT_LEVEL_L2A))
			return false;
		DateFormat df = new SimpleDateFormat(SatelliteProduct.SENTINEL2_PRODUCT_DATE_FORMAT);
		try {
			df.parse(productNameComponents[2].replace("T", ""));
			df.parse(productNameComponents[6].replace("T", ""));
		} catch (ParseException e) {
			return false;
		}
		if(!productNameComponents[3].matches(SatelliteProduct.SENTINEL2_PRODUCT_PDGS_PROCESSING_BASELINE_NUMBER_REGEX))
			return false;
		if(!productNameComponents[4].matches(SatelliteProduct.SENTINEL2_PRODUCT_RELATIVE_ORBIT_NUMBER_REGEX))
			return false;
		if(!productNameComponents[5].matches(SatelliteProduct.SENTINEL2_PRODUCT_TILE_NUMBER_FIELD_REGEX))
			return false;
		return true;
	}

	public static boolean checkProductName(String productName, String[] productNameComponents) {
		if(productName.replace(".SAFE", "").length() != SatelliteProduct.SENTINEL2_PRODUCT_NAME_LENGTH)
			return false;
		if(productNameComponents == null)
			productNameComponents = splitProductName(productName);
		return checkValidNameComponents(productNameComponents);
	}
	
	public static String[] splitProductName(String productName) {
		return productName.split("_");
	}
}