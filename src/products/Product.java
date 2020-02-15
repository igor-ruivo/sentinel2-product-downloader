package products;

import java.net.URL;

public abstract class Product implements SatelliteProduct {
	
	private String name;
	private float size;
	private URL link;
	
	protected Product(String name, String size, URL link) {
		assert(isValidProductName(name));
		this.name = name;
		this.size = parseSize(size);
		this.link = link;
	}
	
	protected Product(String name) {
		this.name = name;
		size = -1;
		link = null;
	}
	
	public String getProductName() {
		return name;
	}
	
	public String getProductFileName() {
		return name + ".zip";
	}
	
	public float getProductSize() {
		return size;
	}
	
	public URL getProductDownloadLink() {
		return link;
	}
	
	private float parseSize(String size) {
		return Float.parseFloat(size.split(" ")[0]) * (size.endsWith("GB") ? 1000 : 1);
	}
	
	public String toString() {
		return getProductName();
	}
}