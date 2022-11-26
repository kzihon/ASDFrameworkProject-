package framework.resources;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ConfigReader {
	static public Map<String, String> readFincoSettings(String configFile) {
		try {
			Map<String, String> config = new HashMap<>();
			Element eElement = null;
		    File file = new File(configFile);
			System.out.println(file);
		    
		    //an instance of factory that gives a document builder
		    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		    
		    //an instance of builder to parse the specified xml file
		    DocumentBuilder db = dbf.newDocumentBuilder();
		    Document doc = db.parse(file);
		    doc.getDocumentElement().normalize();
		    
		    NodeList nodeList = doc.getElementsByTagName("finco");
		    // nodeList is not iterable, so we are using for loop
		    
		    for (int itr = 0; itr < nodeList.getLength(); itr++) {
		        Node node = nodeList.item(itr);
		        if (node.getNodeType() == Node.ELEMENT_NODE) {
		            eElement = (Element) node;
		            break;
		        }
		    }

		    config.put("name", eElement.getElementsByTagName("name").item(0).getTextContent());
		    config.put("email", eElement.getElementsByTagName("email").item(0).getTextContent());
		    config.put("legalname", eElement.getElementsByTagName("legalname").item(0).getTextContent());
		    config.put("street", eElement.getElementsByTagName("street").item(0).getTextContent());
		    config.put("city", eElement.getElementsByTagName("city").item(0).getTextContent());
		    config.put("state", eElement.getElementsByTagName("state").item(0).getTextContent());
		    config.put("zip", eElement.getElementsByTagName("zip").item(0).getTextContent());
		    config.put("phonenumber", eElement.getElementsByTagName("phonenumber").item(0).getTextContent());
		    config.put("urllogo", eElement.getElementsByTagName("urllogo").item(0).getTextContent());
		    config.put("establishedyear", eElement.getElementsByTagName("establishedyear").item(0).getTextContent());
		    config.put("type", eElement.getElementsByTagName("type").item(0).getTextContent());

			return config;
		} catch (Exception e) {
			return null;
		}
	}
}
