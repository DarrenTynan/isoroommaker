package gui;

import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class FileHelper
{
	private IsoRoomMaker main;

	// The DOM xml document.
	private Document document;

	// JFileChooser and file handler
	private JFileChooser mapFileChooser;
	private JFileChooser tileFileChooser;
	private File fileHandle = null;
	private FileInputStream fis;
	private File tileFileHandle;

	public FileHelper(IsoRoomMaker main)
	{
		this.main = main;

		// FileChoser.
		setMapFileChooser(new JFileChooser("/Users/darrentynan/Workspace/Java_src/IsoRoomMaker/Assets/Maps"));
		getMapFileChooser().setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		getMapFileChooser().setAcceptAllFileFilterUsed(false);
		getMapFileChooser().setMultiSelectionEnabled(false);
		FileNameExtensionFilter mapFilter = new FileNameExtensionFilter(".irm files", "irm");
		getMapFileChooser().setFileFilter(mapFilter);

		// FileChoser.
		setTileFileChooser(new JFileChooser("/Users/darrentynan/Workspace/Java_src/IsoRoomMaker/Assets/TileSets"));
		getTileFileChooser().setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		getTileFileChooser().setAcceptAllFileFilterUsed(false);
		getMapFileChooser().setMultiSelectionEnabled(false);
		FileNameExtensionFilter tileFilter = new FileNameExtensionFilter(".png files", "png");
		getTileFileChooser().setFileFilter(tileFilter);
	}

	/**
	 * Purpose - to fill the room specifications with data from the xml file.
	 */
	public void xml2array()
	{
		// Retrieve the tileSet filename, load the tileset and create buttons.
		getTileset(document.getElementsByTagName("tileset"));

		// Retrieve the room name, id and tileSet.
		getRoom(document.getElementsByTagName("room"));

		// Get the room dimensions.
		getDimensions(document.getElementsByTagName("dimensions"));
		
		// Get the gidSize's
		main.getMapHelper().setLwGidArraySize(getGidSizes(document.getElementsByTagName("leftWall")));
		main.getMapHelper().setRwGidArraySize(getGidSizes(document.getElementsByTagName("rightWall")));
		main.getMapHelper().setfGidArraySize(getGidSizes(document.getElementsByTagName("floor")));
		main.getMapHelper().setObjGidArraySize(getGidSizes(document.getElementsByTagName("object")));
		main.getMapHelper().setTldGidArraySize(getGidSizes(document.getElementsByTagName("topLeftDoor")));
		main.getMapHelper().setTrdGidArraySize(getGidSizes(document.getElementsByTagName("topRightDoor")));
		main.getMapHelper().setBldGidArraySize(getGidSizes(document.getElementsByTagName("botLeftDoor")));
		main.getMapHelper().setBrdGidArraySize(getGidSizes(document.getElementsByTagName("botRightDoor")));
		
		// Call to initialise the arrays now we know the area dimensions.
		main.getMapHelper().initialise();

		// Get the gid's.
		getGids(document.getElementsByTagName("tile"));

		// Now it is safe to Toggle map created.
		main.getMapHelper().setMapCreated(true);
	}
	
	/**
	 * Get the integer conversion of the string value of 'gidSize'.
	 * 
	 * @param nList - By tagName.
	 * @return - The integer value of the 'gidSize' of the tagName.
	 */
	public int getGidSizes(NodeList nList)
	{
		Node nNode = nList.item(0);
		
		Element eElement = (Element) nNode;
		return (Integer.parseInt(eElement.getAttribute("gidSize")));
	}
	
	/**
	 * Get the attributes of the room dimensions.
	 * 
	 * @param nList - By tagName.
	 */
	public void getDimensions(NodeList nList)
	{
		for (int i = 0; i < nList.getLength(); i++)
		{

			Node nNode = nList.item(i);

			if (nNode.getNodeType() == Node.ELEMENT_NODE)
			{
				Element eElement = (Element) nNode;

				main.getMapHelper().setWallHeight(Integer.parseInt(eElement.getAttribute("wallHeight")));
				main.getMapHelper().setWallTileWidth(Integer.parseInt(eElement.getAttribute("wallTileWidth")));
				main.getMapHelper().setWallTileHeight(Integer.parseInt(eElement.getAttribute("wallTileHeight")));
				
				main.getMapHelper().setFloorWidth(Integer.parseInt(eElement.getAttribute("floorWidth")));
				main.getMapHelper().setFloorLength(Integer.parseInt(eElement.getAttribute("floorLength")));
				main.getMapHelper().setFloorTileWidth(Integer.parseInt(eElement.getAttribute("floorTileWidth")));
				main.getMapHelper().setFloorTileHeight(Integer.parseInt(eElement.getAttribute("floorTileHeight")));
			}
		}
	}

	/**
	 * Iterate over the elements and look for the tileset. Load the tileSet and
	 * set the fileHandle and the loaded flags. Set the panel width and generate
	 * the buttons with images.
	 * 
	 * @param nList
	 */
	public void getTileset(NodeList nList)
	{
		for (int i = 0; i < nList.getLength(); i++)
		{
			Node nNode = nList.item(i);

			if (nNode.getNodeType() == Node.ELEMENT_NODE)
			{
				Element eElement = (Element) nNode;

				File file = new File(eElement.getAttribute("name"));
				setTileFileHandle(file);
				loadTileset(file);
				main.getTilesHelper().initialiseTileset();
				main.getTilesHelper().setTileSet(true);

				setTileFileHandle(file);

				main.getTilesPanel().setPreferredSize(new Dimension(main.getTilesHelper().getImageWidth(), main.getTilesHelper().getImageHeight()));

				main.getTilesHelper().generateButtons(main.getTilesPanel());

				main.getTilesPanel().updateUI();
			}
		}
	}

	/**
	 * Displays ALL the gids in lwdata, rwdata and floordate. You will have to
	 * split into width * height, before moving into the gidArray.
	 * 
	 * @param nList
	 *            - nList points to 'tile'
	 */
	public void getGids(NodeList nList)
	{
		// Because we are dividing the gid list into three parts
		// we need a counter.
		int counter;

		// Pointers to are gid arrays.
		int[][] lwGidArray = main.getMapHelper().getLeftWallGidArray();
		int[][] rwGidArray = main.getMapHelper().getRightWallGidArray();
		int[][] floorGidArray = main.getMapHelper().getFloorGidArray();
		int[][] objectGidArray = main.getMapHelper().getObjectGidArray();
		int[][] tldGidArray = main.getMapHelper().getTopLeftDoorGidArray();
		int[][] trdGidArray = main.getMapHelper().getTopRightDoorGidArray();
		int[][] bldGidArray = main.getMapHelper().getBotLeftDoorGidArray();
		int[][] brdGidArray = main.getMapHelper().getBotRightDoorGidArray();

		int lwSize = main.getMapHelper().getLwGidArraySize();
		int rwSize = main.getMapHelper().getRwGidArraySize();
		int floorSize = main.getMapHelper().getfGidArraySize();
		int objectSize = main.getMapHelper().getObjGidArraySize();
		int tldSize = main.getMapHelper().getTldGidArraySize();
		int trdSize = main.getMapHelper().getTrdGidArraySize();
		int bldSize = main.getMapHelper().getBldGidArraySize();
		int brdSize = main.getMapHelper().getBrdGidArraySize();

		// List of pointers the next is the current + current size.
		int lwPtr = 0;
		int rwPtr = lwPtr + lwSize;
		int floorPtr = rwPtr + rwSize;
		int objectPtr = floorPtr + floorSize;
		int tldPtr = objectPtr + floorSize;
		int trdPtr = tldPtr + tldSize;
		int bldPtr = trdPtr + trdSize;
		int brdPtr = bldPtr + bldSize;
		
		counter = 0;
		for (int i = lwPtr; i < lwPtr + lwSize; i++)
		{
			Node nNode = nList.item(i);

			if (nNode.getNodeType() == Node.ELEMENT_NODE)
			{
				Element eElement = (Element) nNode;
				main.getMapHelper().translateGridCoords(counter++, main.getMapHelper().getWallHeight(), main.getMapHelper().getFloorLength());
				lwGidArray[main.getMapHelper().getyTranslate()][main.getMapHelper().getxTranslate()] = Integer.parseInt(eElement.getAttribute("gid"));
			}
		}

		counter = 0;
		for (int i = rwPtr; i < rwPtr + rwSize; i++)
		{
			Node nNode = nList.item(i);

			if (nNode.getNodeType() == Node.ELEMENT_NODE)
			{
				Element eElement = (Element) nNode;

				main.getMapHelper().translateGridCoords(counter++, main.getMapHelper().getWallHeight(), main.getMapHelper().getFloorWidth());
				rwGidArray[main.getMapHelper().getyTranslate()][main.getMapHelper().getxTranslate()] = Integer.parseInt(eElement.getAttribute("gid"));
			}
		}

		counter = 0;
		for (int i = floorPtr; i < floorPtr + floorSize; i++)
		{
			Node nNode = nList.item(i);

			if (nNode.getNodeType() == Node.ELEMENT_NODE)
			{
				Element eElement = (Element) nNode;

				main.getMapHelper().translateGridCoords(counter++, main.getMapHelper().getFloorLength(), main.getMapHelper().getFloorWidth());
				floorGidArray[main.getMapHelper().getyTranslate()][main.getMapHelper().getxTranslate()] = Integer.parseInt(eElement.getAttribute("gid"));
			}
		}
//------
		counter = 0;
		for (int i = objectPtr; i < objectPtr + objectSize; i++)
		{
			Node nNode = nList.item(i);

			if (nNode.getNodeType() == Node.ELEMENT_NODE)
			{
				Element eElement = (Element) nNode;

				main.getMapHelper().translateGridCoords(counter++, main.getMapHelper().getFloorLength(), main.getMapHelper().getFloorWidth());
				objectGidArray[main.getMapHelper().getyTranslate()][main.getMapHelper().getxTranslate()] = Integer.parseInt(eElement.getAttribute("gid"));
			}
		}
//------

		counter = 0;
		for (int i = tldPtr; i < tldPtr + tldSize; i++)
		{
			Node nNode = nList.item(i);

			if (nNode.getNodeType() == Node.ELEMENT_NODE)
			{
				Element eElement = (Element) nNode;

				main.getMapHelper().translateGridCoords(counter++, main.getMapHelper().getWallHeight(), main.getMapHelper().getFloorLength());
				tldGidArray[main.getMapHelper().getyTranslate()][main.getMapHelper().getxTranslate()] = Integer.parseInt(eElement.getAttribute("gid"));
			}
		}

		counter = 0;
		for (int i = trdPtr; i < trdPtr + trdSize; i++)
		{
			Node nNode = nList.item(i);

			if (nNode.getNodeType() == Node.ELEMENT_NODE)
			{
				Element eElement = (Element) nNode;

				main.getMapHelper().translateGridCoords(counter++, main.getMapHelper().getWallHeight(), main.getMapHelper().getFloorWidth());
				trdGidArray[main.getMapHelper().getyTranslate()][main.getMapHelper().getxTranslate()] = Integer.parseInt(eElement.getAttribute("gid"));
			}
		}

		counter = 0;
		for (int i = bldPtr; i < bldPtr + bldSize; i++)
		{
			Node nNode = nList.item(i);

			if (nNode.getNodeType() == Node.ELEMENT_NODE)
			{
				Element eElement = (Element) nNode;

				main.getMapHelper().translateGridCoords(counter++, main.getMapHelper().getWallHeight(), main.getMapHelper().getFloorWidth());
				bldGidArray[main.getMapHelper().getyTranslate()][main.getMapHelper().getxTranslate()] = Integer.parseInt(eElement.getAttribute("gid"));
			}
		}

		counter = 0;
		for (int i = brdPtr; i < brdPtr + brdSize; i++)
		{
			Node nNode = nList.item(i);

			if (nNode.getNodeType() == Node.ELEMENT_NODE)
			{
				Element eElement = (Element) nNode;

				main.getMapHelper().translateGridCoords(counter++, main.getMapHelper().getWallHeight(), main.getMapHelper().getFloorWidth());
				brdGidArray[main.getMapHelper().getyTranslate()][main.getMapHelper().getxTranslate()] = Integer.parseInt(eElement.getAttribute("gid"));
			}
		}
	}

	/**
	 * Iterate over the room element and get the roomID and roomName. Also set
	 * the room title, if there is a room name. Call methods to update: walls,
	 * floor and doors.
	 * 
	 * @param nList
	 */
	public void getRoom(NodeList nList)
	{
		for (int i = 0; i < nList.getLength(); i++)
		{
			Node nNode = nList.item(i);

			if (nNode.getNodeType() == Node.ELEMENT_NODE)
			{
				Element eElement = (Element) nNode;

				main.getMapHelper().setRoomID(Integer.parseInt(eElement.getAttribute("roomID")));
				main.getMapHelper().setRoomName(eElement.getAttribute("roomName"));
				main.getTitledBorder().setTitle("Room " + main.getMapHelper().getRoomID() + " - " + main.getMapHelper().getRoomName());
				main.getMapPanelWalls().updateUI();
				main.getMapPanelFloor().updateUI();
				main.getMapPanelDoors().updateUI();
			}
		}
	}


	/**
	 * Method to create a new document. Load and pass the xml file into 'doc'.
	 * 
	 * @param file
	 *            - Pointer to the file handle.
	 */
	public void loadXML(File file)
	{
		// Get the file handle.
		File fileHandle = new File(file.getPath());

		// Get the DOM Builder Factory
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

		// Try and create a new dBuilder.
		DocumentBuilder dBuilder = null;

		try
		{
			dBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e)
		{
			e.printStackTrace();
		}

		try
		{
			document = dBuilder.parse(fileHandle);
		} catch (SAXException | IOException e)
		{
			e.printStackTrace();
		}

		xml2array();
	}

	/**
	 * Build the xml document from our variables, before saving out to the same
	 * name as opened.
	 */
	public void saveXML()
	{
		// if no map created then, nothing to save.
		if (!main.getMapHelper().isMapCreated())
		{
			return;
		}

		buildXML();

		try
		{
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);

			// Either comment out StreamResults to file or console.
			// Output to file.
			StreamResult result = new StreamResult(getFileHandle());

			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);

			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer t = tf.newTransformer();
			t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			t.setOutputProperty(OutputKeys.INDENT, "yes");
			t.transform(source, result);

			// System.out.println("File saved!");

		} catch (TransformerException tfe)
		{
			tfe.printStackTrace();
		}
	}

	/**
	 * Create a document and build the xml file using the room variables.
	 */
	public void buildXML()
	{
		Attr attr;
		int[][] gid;
		Element data;

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = null;
		try
		{
			docBuilder = docFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Now create a new element, the root.
		document = docBuilder.newDocument();

		// Append the new child element to the root.
		Element room = document.createElement("room");
		document.appendChild(room);

		// And set its attributes.
		attr = document.createAttribute("roomID");
		attr.setValue(String.valueOf(main.getMapHelper().getRoomID()));
		room.setAttributeNode(attr);

		// And set its attributes.
		attr = document.createAttribute("roomName");
		attr.setValue(String.valueOf(main.getMapHelper().getRoomName()));
		room.setAttributeNode(attr);

		if (main.getTilesHelper().isTileSetLoaded())
		{
			// Append the new child element to the root.
			Element tileset = document.createElement("tileset");
			room.appendChild(tileset);

			// And set its attribute.
			attr = document.createAttribute("name");
			attr.setValue(getTileFileHandle().getAbsolutePath());
			tileset.setAttributeNode(attr);

			// At this point file contains the full path.
//			System.out.println("file " + getTileFileHandle());
		}

		// --------------------------------------------------------------------------------

		// Create the dimensions element.
		Element dimensions = document.createElement("dimensions");
		room.appendChild(dimensions);

		attr = document.createAttribute("wallHeight");
		attr.setValue(String.valueOf(main.getMapHelper().getWallHeight()));
		dimensions.setAttributeNode(attr);

		attr = document.createAttribute("wallTileWidth");
		attr.setValue(String.valueOf(main.getMapHelper().getWallTileWidth()));
		dimensions.setAttributeNode(attr);

		attr = document.createAttribute("wallTileHeight");
		attr.setValue(String.valueOf(main.getMapHelper().getWallTileHeight()));
		dimensions.setAttributeNode(attr);

		attr = document.createAttribute("floorWidth");
		attr.setValue(String.valueOf(main.getMapHelper().getFloorWidth()));
		dimensions.setAttributeNode(attr);

		attr = document.createAttribute("floorLength");
		attr.setValue(String.valueOf(main.getMapHelper().getFloorLength()));
		dimensions.setAttributeNode(attr);

		attr = document.createAttribute("floorTileWidth");
		attr.setValue(String.valueOf(main.getMapHelper().getFloorTileWidth()));
		dimensions.setAttributeNode(attr);

		attr = document.createAttribute("floorTileHeight");
		attr.setValue(String.valueOf(main.getMapHelper().getFloorTileHeight()));
		dimensions.setAttributeNode(attr);

		attr = document.createAttribute("doorHeight");
		attr.setValue(String.valueOf(main.getMapHelper().getWallHeight()));
		dimensions.setAttributeNode(attr);

		attr = document.createAttribute("doorTileWidth");
		attr.setValue(String.valueOf(main.getMapHelper().getWallTileWidth()));
		dimensions.setAttributeNode(attr);

		attr = document.createAttribute("doorTileHeight");
		attr.setValue(String.valueOf(main.getMapHelper().getWallTileHeight()));
		dimensions.setAttributeNode(attr);

		attr = document.createAttribute("doorDepth");
		attr.setValue(String.valueOf(main.getMapHelper().getDoorDepth()));
		dimensions.setAttributeNode(attr);

		attr = document.createAttribute("wallTileDepth");
		attr.setValue(String.valueOf(main.getMapHelper().getWallTileDepth()));
		dimensions.setAttributeNode(attr);

		attr = document.createAttribute("floorTileDepth");
		attr.setValue(String.valueOf(main.getMapHelper().getFloorTileDepth()));
		dimensions.setAttributeNode(attr);

		// --------------------------------------------------------------------------------

		// Create the left wall element.
		Element lw = document.createElement("leftWall");
		room.appendChild(lw);

		attr = document.createAttribute("gidSize");
		attr.setValue(String.valueOf(main.getMapHelper().getWallHeight() * main.getMapHelper().getFloorLength()));
		lw.setAttributeNode(attr);

		gid = main.getMapHelper().getLeftWallGidArray();

		for (int y = 0; y < main.getMapHelper().getWallHeight(); y++)
		{
			for (int x = 0; x < main.getMapHelper().getFloorLength(); x++)
			{
				Element tile = document.createElement("tile");
				lw.appendChild(tile);
				attr = document.createAttribute("gid");
				int intGid = gid[y][x];
				attr.setValue(String.valueOf(intGid));
				tile.setAttributeNode(attr);
			}
		}

		// --------------------------------------------------------------------------------

		// Create the right wall element.
		Element rw = document.createElement("rightWall");
		room.appendChild(rw);

		attr = document.createAttribute("gidSize");
		attr.setValue(String.valueOf(main.getMapHelper().getWallHeight() * main.getMapHelper().getFloorWidth()));
		rw.setAttributeNode(attr);

		gid = main.getMapHelper().getRightWallGidArray();

		for (int y = 0; y < main.getMapHelper().getWallHeight(); y++)
		{
			for (int x = 0; x < main.getMapHelper().getFloorWidth(); x++)
			{
				Element tile = document.createElement("tile");
				rw.appendChild(tile);
				attr = document.createAttribute("gid");
				int intGid = gid[y][x];
				attr.setValue(String.valueOf(intGid));
				tile.setAttributeNode(attr);
			}
		}

		// --------------------------------------------------------------------------------

		// Create the floor element.
		Element floor = document.createElement("floor");
		room.appendChild(floor);

		attr = document.createAttribute("gidSize");
		attr.setValue(String.valueOf(main.getMapHelper().getFloorLength() * main.getMapHelper().getFloorWidth()));
		floor.setAttributeNode(attr);

		gid = main.getMapHelper().getFloorGidArray();

		for (int y = 0; y < main.getMapHelper().getFloorLength(); y++)
		{
			for (int x = 0; x < main.getMapHelper().getFloorWidth(); x++)
			{
				Element tile = document.createElement("tile");
				floor.appendChild(tile);
				attr = document.createAttribute("gid");
				int intGid = gid[y][x];
				attr.setValue(String.valueOf(intGid));
				tile.setAttributeNode(attr);
			}
		}
		// --------------------------------------------------------------------------------

		// Create the floor element.
		Element object = document.createElement("object");
		room.appendChild(object);

		attr = document.createAttribute("gidSize");
		attr.setValue(String.valueOf(main.getMapHelper().getFloorLength() * main.getMapHelper().getFloorWidth()));
		object.setAttributeNode(attr);

		gid = main.getMapHelper().getObjectGidArray();

		for (int y = 0; y < main.getMapHelper().getFloorLength(); y++)
		{
			for (int x = 0; x < main.getMapHelper().getFloorWidth(); x++)
			{
				Element tile = document.createElement("tile");
				object.appendChild(tile);
				attr = document.createAttribute("gid");
				int intGid = gid[y][x];
				attr.setValue(String.valueOf(intGid));
				tile.setAttributeNode(attr);
			}
		}

		// --------------------------------------------------------------------------------

		// Create the upper left door element.
		Element tld = document.createElement("topLeftDoor");
		room.appendChild(tld);

		attr = document.createAttribute("gidSize");
		attr.setValue(String.valueOf(main.getMapHelper().getWallHeight() * main.getMapHelper().getFloorLength()));
		tld.setAttributeNode(attr);

		gid = main.getMapHelper().getTopLeftDoorGidArray();

		for (int y = 0; y < main.getMapHelper().getWallHeight(); y++)
		{
			for (int x = 0; x < main.getMapHelper().getFloorLength(); x++)
			{
				Element tile = document.createElement("tile");
				tld.appendChild(tile);
				attr = document.createAttribute("gid");
				int intGid = gid[y][x];
				attr.setValue(String.valueOf(intGid));
				tile.setAttributeNode(attr);
			}
		}

		// --------------------------------------------------------------------------------

		// Create the upper right door element.
		Element trd = document.createElement("topRightDoor");
		room.appendChild(trd);

		attr = document.createAttribute("gidSize");
		attr.setValue(String.valueOf(main.getMapHelper().getWallHeight() * main.getMapHelper().getFloorWidth()));
		trd.setAttributeNode(attr);

		gid = main.getMapHelper().getTopRightDoorGidArray();

		for (int y = 0; y < main.getMapHelper().getWallHeight(); y++)
		{
			for (int x = 0; x < main.getMapHelper().getFloorWidth(); x++)
			{
				Element tile = document.createElement("tile");
				trd.appendChild(tile);
				attr = document.createAttribute("gid");
				int intGid = gid[y][x];
				attr.setValue(String.valueOf(intGid));
				tile.setAttributeNode(attr);
			}
		}

		// --------------------------------------------------------------------------------

		// Create the bottom left door element.
		Element bld = document.createElement("botLeftDoor");
		room.appendChild(bld);

		attr = document.createAttribute("gidSize");
		attr.setValue(String.valueOf(main.getMapHelper().getWallHeight() * main.getMapHelper().getFloorWidth()));
		bld.setAttributeNode(attr);

		gid = main.getMapHelper().getBotLeftDoorGidArray();

		for (int y = 0; y < main.getMapHelper().getWallHeight(); y++)
		{
			for (int x = 0; x < main.getMapHelper().getFloorWidth(); x++)
			{
				Element tile = document.createElement("tile");
				bld.appendChild(tile);
				attr = document.createAttribute("gid");
				int intGid = gid[y][x];
				attr.setValue(String.valueOf(intGid));
				tile.setAttributeNode(attr);
			}
		}

		// --------------------------------------------------------------------------------

		// Create the upper left door element.
		Element brd = document.createElement("botRightDoor");
		room.appendChild(brd);

		attr = document.createAttribute("gidSize");
		attr.setValue(String.valueOf(main.getMapHelper().getWallHeight() * main.getMapHelper().getFloorLength()));
		brd.setAttributeNode(attr);

		gid = main.getMapHelper().getBotRightDoorGidArray();

		for (int y = 0; y < main.getMapHelper().getWallHeight(); y++)
		{
			for (int x = 0; x < main.getMapHelper().getFloorLength(); x++)
			{
				Element tile = document.createElement("tile");
				brd.appendChild(tile);
				attr = document.createAttribute("gid");
				int intGid = gid[y][x];
				attr.setValue(String.valueOf(intGid));
				tile.setAttributeNode(attr);
			}
		}
	}

	/**
	 * Method to load a tileset.
	 * 
	 * @param file
	 *            - Pointer to the file handle.
	 */
	public void loadTileset(File file)
	{
		// Get the file handle.
		file = new File(file.getPath());

		try
		{
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		try
		{
			main.getTilesHelper().setImage(ImageIO.read(fis));
			// System.out.println("TilesHelper - Tileset loaded...");
		} catch (IOException e)
		{
			e.printStackTrace();
			// System.out.println("TilesHelper - Tileset load error!");
		}
	}

	public JFileChooser getTileFileChooser()
	{
		return tileFileChooser;
	}

	public void setTileFileChooser(JFileChooser tileFileChooser)
	{
		this.tileFileChooser = tileFileChooser;
	}

	public JFileChooser getMapFileChooser()
	{
		return mapFileChooser;
	}

	public void setMapFileChooser(JFileChooser mapFileChooser)
	{
		this.mapFileChooser = mapFileChooser;
	}

	public File getFileHandle()
	{
		return fileHandle;
	}

	public void setFileHandle(File fileHandle)
	{
		this.fileHandle = fileHandle;
	}

	public File getTileFileHandle()
	{
		return tileFileHandle;
	}

	public void setTileFileHandle(File tileFileHandle)
	{
		this.tileFileHandle = tileFileHandle;
	}

}
