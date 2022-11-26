package ccard.resources;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class JdbcConfigReader {
	static public Map<String, String> readConfig(String configFile) {
		Map<String, String> config = new HashMap<>();
		Element eElement = null;

		try {
		    File file = new File(configFile);
		    
		    //an instance of factory that gives a document builder
		    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		    
		    //an instance of builder to parse the specified xml file
		    DocumentBuilder db = dbf.newDocumentBuilder();
		    Document doc = db.parse(file);
		    doc.getDocumentElement().normalize();
		    
		    NodeList nodeList = doc.getElementsByTagName("database");
		    // nodeList is not iterable, so we are using for loop
		    
		    for (int itr = 0; itr < nodeList.getLength(); itr++) {
		        Node node = nodeList.item(itr);
		        if (node.getNodeType() == Node.ELEMENT_NODE) {
		            eElement = (Element) node;
		            break;
		        }
		    }

		    config.put("driverName", eElement.getElementsByTagName("driverName").item(0).getTextContent());
			config.put("userName", eElement.getElementsByTagName("userName").item(0).getTextContent());
			config.put("password", eElement.getElementsByTagName("password").item(0).getTextContent());
			config.put("host", eElement.getElementsByTagName("host").item(0).getTextContent());
			config.put("port", eElement.getElementsByTagName("port").item(0).getTextContent());
			config.put("dbName", eElement.getElementsByTagName("dbName").item(0).getTextContent());

		} catch (Exception e) {
			return null;
		}
		return config;
	}
}
