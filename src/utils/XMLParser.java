package utils;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLParser {
	
	DocumentBuilderFactory dbFactory;
	DocumentBuilder dBuilder;
	Document doc;
	
	public XMLParser(InputStream xml) throws SAXException, IOException, ParserConfigurationException {
		dbFactory = DocumentBuilderFactory.newInstance();
        dBuilder = dbFactory.newDocumentBuilder();
        doc = dBuilder.parse(xml);
        doc.getDocumentElement().normalize();
	}
	
	public NodeList getNodeListByTagName(String tagName) {
		return doc.getElementsByTagName(tagName);
	}
}