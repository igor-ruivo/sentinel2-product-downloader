package products;

import java.net.URL;

public class L2AProduct extends Sentinel2Product {

	public L2AProduct(String name, String size, URL link) {
		super(name, size, link);
	}

	@Override
	public boolean isValidProductName(String productName) {
		return getProductNameComponents()[1].equals(SENTINEL2_PRODUCT_LEVEL_L2A) && checkProductName();
	}
}