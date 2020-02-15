package products;

import java.net.URL;

import utils.Sentinel2ProductUtils;

public abstract class Sentinel2Product extends Product {

	private String[] productNameComponents; 
	
	protected Sentinel2Product(String name, String size, URL link) {
		super(name, size, link);
		productNameComponents = name.split("_");
	}
	
	protected String[] getProductNameComponents() {
		return productNameComponents;
	}
	
	protected boolean checkProductName() {
		return Sentinel2ProductUtils.checkProductName(getProductName(), productNameComponents);
	}
}