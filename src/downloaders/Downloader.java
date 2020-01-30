/**
 * 
 */
package downloaders;



/**
 * @author Igor
 *
 */
public interface Downloader {
	public static final String COPERNICUS_XML_URL = "https://scihub.copernicus.eu/dhus/search";
	public static final String COPERNICUS_PRODUCT_URL = "https://scihub.copernicus.eu/dhus/odata/v1/Products('%s')/$value";
	public static final String COPERNICUS_FOOTPRINT_FORMAT = "footprint:\"Intersects(%s)\"AND beginPosition:[%sT00:00:00.000Z TO %sT23:59:59.999Z] AND endPosition:[%sT00:00:00.000Z TO %sT23:59:59.999Z] AND platformname:%s AND producttype:%s AND cloudcoverpercentage:%s";
	public static final int COPERNICUS_MAX_ROWS_PER_REQUEST = 100;
	public void downloadSentinel2Products();
}