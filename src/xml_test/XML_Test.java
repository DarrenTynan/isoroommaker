package xml_test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XML_Test {

	JFileChooser fileChooser;
	Document doc;

	public static void main(String[] args) {
		XML_Test xml_test = new XML_Test();
		xml_test.go();
	}

	public void go()
	{
		loadXML(null);
//		buildXML();
//		saveXML();
		rebuildXML();
	}
	
//	1	ELEMENT_NODE
//	2	ATTRIBUTE_NODE
//	3	TEXT_NODE
//	4	CDATA_SECTION_NODE
//	5	ENTITY_REFERENCE_NODE
//	6	ENTITY_NODE
//	7	PROCESSING_INSTRUCTION_NODE
//	8	COMMENT_NODE
//	9	DOCUMENT_NODE
//	10	DOCUMENT_TYPE_NODE
//	11	DOCUMENT_FRAGMENT_NODE
//	12	NOTATION_NODE

	public void rebuildXML()
	{
		Element rootElement = doc.getDocumentElement();
		NodeList childElements = rootElement.getChildNodes();
		
		System.out.println("Root element: " + rootElement.getNodeName());
		
		System.out.println("-----------------");

		System.out.println("number of child elements: " + childElements.getLength());
		System.out.println("---------------------------");

		for(int i = 0; i < rootElement.getChildNodes().getLength(); i++)
		{
		    if (rootElement.getChildNodes().item(i).getNodeType() == Node.ELEMENT_NODE)
		    {
		    	System.out.println("child element: " + rootElement.getChildNodes().item(i).getNodeName() + " \t\t type: " + rootElement.getChildNodes().item(i).getNodeType());
		    }
		}
		
		getWall(doc.getElementsByTagName("leftWall"));
		getWall(doc.getElementsByTagName("rightWall"));
		getFloor(doc.getElementsByTagName("floor"));
		getData(doc.getElementsByTagName("tile"));
	}
	
	/**
	 * Displays ALL the gids in lwdata, rwdata and floordate.
	 * You will have to split into width * height, before moving into the gidArray.
	 * 
	 * @param nList
	 */
	public void getData(NodeList nList)
	{
		System.out.println("");
		for (int i = 0; i < nList.getLength(); i++)
		{
			Node nNode = nList.item(i);
	 
			if (nNode.getNodeType() == Node.ELEMENT_NODE)
			{
				Element eElement = (Element) nNode;
	 
				System.out.println("gid: " + eElement.getAttribute("gid"));
			}
		}
	}
	
	public void getFloor(NodeList nList)
	{
		for (int i = 0; i < nList.getLength(); i++)
		{
			 
			Node nNode = nList.item(i);
	 
			System.out.println("\nCurrent Element: " + nNode.getNodeName());
	 
			if (nNode.getNodeType() == Node.ELEMENT_NODE)
			{
				Element eElement = (Element) nNode;
	 
				System.out.println("FloorHeight: " + eElement.getAttribute("floorHeight"));
				System.out.println("FloorWidth: " + eElement.getAttribute("floorWidth"));
				System.out.println("FloorTileWidth: " + eElement.getAttribute("floorTileWidth"));
				System.out.println("FloorTileHeight: " + eElement.getAttribute("floorTileHeight"));
				
			}
		}
	}
	
	public void getWall(NodeList nList)
	{
		for (int i = 0; i < nList.getLength(); i++)
		{
			 
			Node nNode = nList.item(i);
	 
			System.out.println("\nCurrent Element: " + nNode.getNodeName());
	 
			if (nNode.getNodeType() == Node.ELEMENT_NODE)
			{
				Element eElement = (Element) nNode;
	 
				System.out.println("WallHeight: " + eElement.getAttribute("wallHeight"));
				System.out.println("WallWidth: " + eElement.getAttribute("wallWidth"));
				System.out.println("WallTileWidth: " + eElement.getAttribute("wallTileWidth"));
				System.out.println("WallTileHeight: " + eElement.getAttribute("wallTileHeight"));
			}
		}
	}

	public void loadXML(File file)
	{
		// Get the file handle.
		File fileHandle = new File("/Users/darrentynan/Workspace/Java_src/IsoRoomMaker/Assets/tiles/MYTESTFILE_V2.xml");

       	// Get the DOM Builder Factory
       	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
       	
       	// Try and create a new dBuilder.
       	DocumentBuilder dBuilder = null;

       	try {
			dBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e)
       	{
			e.printStackTrace();
		}
       	
		try
		{
			doc = dBuilder.parse(fileHandle);
		} catch (SAXException | IOException e)
		{
			e.printStackTrace();
		}
	}

	public void saveXML()
	{
		try {
		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);

		// Either comment out StreamResults to file or console.
		// Output to file.
//		StreamResult result = new StreamResult(new File("/Users/darrentynan/Workspace/Java_src/IsoRoomMaker/Assets/tiles/MYTESTFILE_V2.xml"));
		// Output to console for testing
		StreamResult result = new StreamResult(System.out);
		
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer t = tf.newTransformer();
		t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		t.setOutputProperty(OutputKeys.INDENT, "yes");
		t.transform(source, result);
	 
		System.out.println("\nFile saved!");
	 
		} catch (TransformerException tfe)
		{
			tfe.printStackTrace();
		}
	}

	public void buildXML()
	{
		Attr attr;
		int[][] gid = new int[4][4];

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = null;
		try {
			docBuilder = docFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Now create a new document, the root.
		doc = docBuilder.newDocument();
			
		Node root = doc.createElement("map");
		doc.appendChild(root);
		
		Element room = doc.createElement("room");
		root.appendChild(room);
		
		attr = doc.createAttribute("roomID");
		attr.setValue(String.valueOf("001"));
		room.setAttributeNode(attr);
			
			Element lw = doc.createElement("leftWall");
			room.appendChild(lw);

				attr = doc.createAttribute("wallWidth");
				attr.setValue(String.valueOf("99"));
				lw.setAttributeNode(attr);

				attr = doc.createAttribute("wallHeight");
				attr.setValue(String.valueOf("99"));
				lw.setAttributeNode(attr);

				attr = doc.createAttribute("wallTileWidth");
				attr.setValue(String.valueOf("99"));
				lw.setAttributeNode(attr);

				attr = doc.createAttribute("wallTileHeight");
				attr.setValue(String.valueOf("99"));
				lw.setAttributeNode(attr);

				Element lwdata = doc.createElement("lwdata");
				lw.appendChild(lwdata);
				
			Element rw = doc.createElement("rightWall");
			room.appendChild(rw);

				attr = doc.createAttribute("wallWidth");
				attr.setValue(String.valueOf("199"));
				rw.setAttributeNode(attr);

				attr = doc.createAttribute("wallHeight");
				attr.setValue(String.valueOf("199"));
				rw.setAttributeNode(attr);
				
				attr = doc.createAttribute("wallTileWidth");
				attr.setValue(String.valueOf("199"));
				rw.setAttributeNode(attr);
	
				attr = doc.createAttribute("wallTileHeight");
				attr.setValue(String.valueOf("199"));
				rw.setAttributeNode(attr);

				Element rwdata = doc.createElement("rwdata");
				rw.appendChild(rwdata);
			
			Element floor = doc.createElement("floor");
			room.appendChild(floor);
		
				attr = doc.createAttribute("floorWidth");
				attr.setValue(String.valueOf("299"));
				floor.setAttributeNode(attr);
	
				attr = doc.createAttribute("floorHeight");
				attr.setValue(String.valueOf("299"));
				floor.setAttributeNode(attr);
				
				attr = doc.createAttribute("floorTileWidth");
				attr.setValue(String.valueOf("299"));
				floor.setAttributeNode(attr);
	
				attr = doc.createAttribute("floorTileHeight");
				attr.setValue(String.valueOf("299"));
				floor.setAttributeNode(attr);
	
			Element floordata = doc.createElement("floordata");
	
			floor.appendChild(floordata);
				
//			gid = main.getMapHelper().getLeftWallGidArray();

			for(int y = 0; y < 4; y++)
			{
				for(int x = 0; x < 4; x++)
				{
					Element tile = doc.createElement("tile");
					floordata.appendChild(tile);
					attr = doc.createAttribute("gid");
					int intGid = gid[y][x];
					attr.setValue(String.valueOf(intGid));
					tile.setAttributeNode(attr);
				}
			}
	}
	
}
