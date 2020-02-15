package products;

import java.net.URL;

public class L1CProduct extends Sentinel2Product {

	public L1CProduct(String name, String size, URL link) {
		super(name, size, link);
	}

	@Override
	public boolean isValidProductName(String productName) {
		return getProductNameComponents()[1].equals(SENTINEL2_PRODUCT_LEVEL_L1C) && checkProductName();
	}
}