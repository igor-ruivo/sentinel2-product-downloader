package downloaders;

import java.net.URL;

public class Product {
	
	private String name;
	private float size;
	private URL link;
	
	protected Product(String name, String size, URL link) {
		this.name = name;
		this.size = parseSize(size);
		this.link = link;
	}
	
	protected String getName() {
		return name;
	}
	
	protected String getFileName() {
		return name + ".zip";
	}
	
	protected float getSize() {
		return size;
	}
	
	protected URL getLink() {
		return link;
	}
	
	private float parseSize(String size) {
		return Float.parseFloat(size.split(" ")[0]) * (size.endsWith("GB") ? 1000 : 1);
	}
	
	public String toString() {
		return getName();
	}
}