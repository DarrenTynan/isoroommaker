package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;

import mapHelper.MapHelper;

import javax.swing.JSpinner;
import javax.swing.ScrollPaneConstants;

public class IsoRoomMaker extends JFrame
{ // Main
	static IsoRoomMaker window;

	// main frame pointer.
	private JFrame mainFrame;

	// GUI for creating a new room.
	private NewRoom newRoom;

	// GUI for editing map properties.
	private RoomProperties roomProperties;

	// The main left and right panels.
	private JPanel tilesPanel;

	private MapPanelWalls mapPanelWalls;
	private MapPanelFloor mapPanelFloor;
	private MapPanelObject mapPanelObject;
	private MapPanelDoors mapPanelDoors;

	private TitledBorder titledBorder;

	// The helper classes.
	private MapHelper mapHelper;
	private TilesHelper tilesHelper;
	private MouseHelper mouseHelper;
	private FileHelper fileHelper;

	private int windowWidth;
	private int windowHeight;
	private int windowDivider;

	private JMenuItem saveFile;
	private JMenuItem saveAsFile;
	private JMenuItem closeFile;
	private JMenuItem closeAllFiles;
	private JMenuItem newTileset;
	private JMenuItem roomProperty;

	private JLayeredPane layeredPane;
	private boolean showWalls = true;
	private boolean showFloor = true;
	private boolean showObjects = true;
	private boolean showDoors = true;
	private boolean showGrid = true;

	// Used for debugging.
	public JSpinner debugX;
	public JSpinner debugY;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					window = new IsoRoomMaker();
					window.mainFrame.setVisible(true);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Constructor.
	 */
	public IsoRoomMaker()
	{
		initialiseHelper();
	}

	/**
	 * Create the application.
	 */
	public void initialiseHelper()
	{
		// FileHelper is responsible for all file actions.
		fileHelper = new FileHelper(this);

		// TilesHelper is responsible for cutting the tile set into separates.
		tilesHelper = new TilesHelper(this);

		// MapHelper is responsible for rendering the grids.
		mapHelper = new MapHelper(this);

		initialiseMapLayers();

		// The main tiles panel.
		tilesPanel = new JPanel(new FlowLayout());

		initializeGUI();

		// MouseHelper deals with all mouse events.
		mouseHelper = new MouseHelper(this);

		// Add the mouse listeners to the map panel.
		mapPanelDoors.addMouseListener(mouseHelper);
		mapPanelDoors.addMouseMotionListener(mouseHelper);
	}

	/**
	 * Instantiate the layers: mapPanelWalls, mapPanelFloor, mapPanelObject,
	 * mapPanelDoors. Give it a border and a title.
	 */
	public void initialiseMapLayers()
	{
		// The main MapPanel extends JPanel.
		mapPanelWalls = new MapPanelWalls(this);
		mapPanelWalls.setOpaque(false);

		// Create a new transparent mid ground panel.
		mapPanelFloor = new MapPanelFloor(this);
		mapPanelFloor.setOpaque(false);

		// Create a new transparent foreground panel.
		mapPanelObject = new MapPanelObject(this);
		mapPanelObject.setOpaque(false);

		// Create a new transparent foreground panel.
		mapPanelDoors = new MapPanelDoors(this);
		mapPanelDoors.setOpaque(false);

		// Decorate the background panel.
		titledBorder = new TitledBorder(null, "Room", TitledBorder.LEADING, TitledBorder.TOP, null, Color.LIGHT_GRAY);
		mapPanelDoors.setBorder(titledBorder);
	}

	/**
	 * Initialize the contents of the frame! In other words all thing gui!
	 */
	private void initializeGUI()
	{
		mainFrame = new JFrame("Iso Room Maker");
		windowWidth = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width - 150;
		windowHeight = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height - 110;
		windowDivider = 700;

		mainFrame.setBounds(0, 0, windowWidth, windowHeight);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Centre window
		mainFrame.setLocationRelativeTo(null);

		// Create the JMenuBar entries.
		JMenuBar menuBar = new JMenuBar();
		mainFrame.getContentPane().add(menuBar, BorderLayout.NORTH);

		/*
		 * Create JMenu 'Room Maker'.
		 */
		JMenu roomMakerMenu = new JMenu("Room Maker");
		menuBar.add(roomMakerMenu);

		JMenuItem mntmAbout = new JMenuItem("About");
		roomMakerMenu.add(mntmAbout);

		JMenuItem mntmQuit = new JMenuItem("Quit");
		mntmQuit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		});

		JSeparator separator_0 = new JSeparator();
		roomMakerMenu.add(separator_0);
		roomMakerMenu.add(mntmQuit);

		/*
		 * Create JMenu 'File'.
		 */
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);

		JMenuItem newFile = new JMenuItem("New");
		newFile.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				newRoom = new NewRoom(window);
				newRoom.setVisible(true);
				enableMenus();
			}
		});
		fileMenu.add(newFile);

		// Create the JMenuItems
		JMenuItem openFile = new JMenuItem("Open");
		openFile.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				int returnValue = fileHelper.getMapFileChooser().showOpenDialog(mainFrame);
				if (returnValue == JFileChooser.APPROVE_OPTION)
				{
					// Set the file handle.
					fileHelper.setFileHandle(fileHelper.getMapFileChooser().getSelectedFile());

					// Load the xml file.
					fileHelper.loadXML(fileHelper.getFileHandle());

					enableMenus();

				} else
				{
					System.out.println("Open command cancelled by user.");
				}
			}
		});
		fileMenu.add(openFile);

		JSeparator separator_1 = new JSeparator();
		fileMenu.add(separator_1);

		saveFile = new JMenuItem("Save");
		saveFile.setEnabled(false);
		saveFile.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (fileHelper.getFileHandle() != null)
				{
					fileHelper.saveXML();
				}
			}
		});
		fileMenu.add(saveFile);

		saveAsFile = new JMenuItem("Save As...");
		saveAsFile.setEnabled(false);
		saveAsFile.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				int returnValue = fileHelper.getMapFileChooser().showSaveDialog(mainFrame);
				if (returnValue == JFileChooser.APPROVE_OPTION)
				{
					File file = fileHelper.getMapFileChooser().getSelectedFile();

					if (!file.getAbsolutePath().endsWith(".irm"))
					{
						file = new File(file + ".irm");
					}

					// Set the file handle.
					fileHelper.setFileHandle(fileHelper.getMapFileChooser().getSelectedFile());

					fileHelper.saveXML();

					// Now the file has a handle - allow to save.
					saveFile.setEnabled(true);

				} else
				{
					System.out.println("Save As command cancelled by user.");
				}
			}
		});
		fileMenu.add(saveAsFile);

		JSeparator separator_2 = new JSeparator();
		fileMenu.add(separator_2);

// DEBUG****************************************************************************************************

		closeFile = new JMenuItem("Close");
		closeFile.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				disableMenus();
				mapHelper.setMapCreated(false);
				initialiseMapLayers();
				mapPanelWalls.updateUI();
				mapPanelFloor.updateUI();
				mapPanelDoors.updateUI();
			}
		});
		closeFile.setEnabled(false);
		fileMenu.add(closeFile);

		closeAllFiles = new JMenuItem("Close All...");
		closeAllFiles.setEnabled(false);
		fileMenu.add(closeAllFiles);

		/*
		 * Create JMenu 'View'.
		 */
		JMenu viewMenu = new JMenu("View");
		menuBar.add(viewMenu);

		JCheckBox cbShowGrid = new JCheckBox("Show Grid");
		cbShowGrid.setSelected(true);
		cbShowGrid.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				showGrid = !showGrid;
			}
		});
		viewMenu.add(cbShowGrid);

		JCheckBox cbShowBGW = new JCheckBox("Show Walls");
		viewMenu.add(cbShowBGW);
		cbShowBGW.setSelected(true);

		JCheckBox cbShowFloor = new JCheckBox("Show Floor");
		viewMenu.add(cbShowFloor);
		cbShowFloor.setSelected(true);

		JCheckBox cbShowObject = new JCheckBox("Show Objects");
		viewMenu.add(cbShowObject);
		cbShowObject.setSelected(true);

		JCheckBox cbShowFGW = new JCheckBox("Show Doors");
		viewMenu.add(cbShowFGW);
		cbShowFGW.setSelected(true);
		cbShowFGW.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				showDoors = !showDoors;
				mouseHelper.updateLayers();
			}
		});

		cbShowObject.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				showObjects = !showObjects;
				mouseHelper.updateLayers();
			}
		});

		cbShowFloor.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				showFloor = !showFloor;
				mouseHelper.updateLayers();
			}
		});

		cbShowBGW.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				showWalls = !showWalls;
				mouseHelper.updateLayers();
			}
		});

		/*
		 * Create JMenu 'Map'.
		 */
		JMenu roomMenu = new JMenu("Room");
		menuBar.add(roomMenu);

		newTileset = new JMenuItem("New Tileset...");
		newTileset.setEnabled(false);
		newTileset.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				int returnValue = fileHelper.getTileFileChooser().showOpenDialog(mainFrame);
				if (returnValue == JFileChooser.APPROVE_OPTION)
				{
					File file = fileHelper.getTileFileChooser().getSelectedFile();

					fileHelper.loadTileset(file);
					tilesHelper.initialiseTileset();
					tilesHelper.setTileSet(true);

					fileHelper.setTileFileHandle(file);

					tilesPanel.setPreferredSize(new Dimension(tilesHelper.getImageWidth(), tilesHelper.getImageHeight()));

					tilesHelper.generateButtons(tilesPanel);

					tilesPanel.updateUI();

				} else
				{
					System.out.println("Open command cancelled by user.");
				}
			}
		});
		roomMenu.add(newTileset);

		roomProperty = new JMenuItem("Room Properties");
		roomProperty.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				roomProperties = new RoomProperties(window);
				roomProperties.setVisible(true);
			}
		});
		roomProperty.setEnabled(false);
		roomMenu.add(roomProperty);

		/*
		 * Create JMenu 'Layer'.
		 */
		JMenu layerMenu = new JMenu("Draw On Layers");
		menuBar.add(layerMenu);

		ButtonGroup menuGrp = new ButtonGroup();

		JRadioButton drawOnWalls = new JRadioButton("Draw On Walls");
		drawOnWalls.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				mouseHelper.setDrawOnFloor(false);
				mouseHelper.setDrawOnWalls(true);
				mouseHelper.setDrawOnObject(false);
				mouseHelper.setDrawOnDoorsUpper(false);
				mouseHelper.setDrawOnDoorsLower(false);
			}
		});

		JRadioButton drawOnFloor = new JRadioButton("Draw On Floor");
		drawOnFloor.setSelected(true);
		drawOnFloor.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				mouseHelper.setDrawOnFloor(true);
				mouseHelper.setDrawOnWalls(false);
				mouseHelper.setDrawOnObject(false);
				mouseHelper.setDrawOnDoorsUpper(false);
				mouseHelper.setDrawOnDoorsLower(false);
			}
		});

		JRadioButton drawOnObject = new JRadioButton("Draw On Object");
		drawOnObject.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				mouseHelper.setDrawOnFloor(false);
				mouseHelper.setDrawOnWalls(false);
				mouseHelper.setDrawOnObject(true);
				mouseHelper.setDrawOnDoorsUpper(false);
				mouseHelper.setDrawOnDoorsLower(false);
			}
		});

		JRadioButton drawOnDoorsTop = new JRadioButton("Draw On Top Doors");
		drawOnDoorsTop.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				mouseHelper.setDrawOnFloor(false);
				mouseHelper.setDrawOnWalls(false);
				mouseHelper.setDrawOnObject(false);
				mouseHelper.setDrawOnDoorsUpper(true);
				mouseHelper.setDrawOnDoorsLower(false);
				System.out.println("top " + mouseHelper.isDrawOnDoorsUpper());
			}
		});

		JRadioButton drawOnDoorsBot = new JRadioButton("Draw On Bot Doors");
		drawOnDoorsBot.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				mouseHelper.setDrawOnFloor(false);
				mouseHelper.setDrawOnWalls(false);
				mouseHelper.setDrawOnObject(false);
				mouseHelper.setDrawOnDoorsUpper(false);
				mouseHelper.setDrawOnDoorsLower(true);
				System.out.println("bot " + mouseHelper.isDrawOnDoorsLower());
			}
		});

		menuGrp.add(drawOnFloor);
		menuGrp.add(drawOnWalls);
		menuGrp.add(drawOnObject);
		menuGrp.add(drawOnDoorsTop);
		menuGrp.add(drawOnDoorsBot);

		layerMenu.add(drawOnFloor);
		layerMenu.add(drawOnWalls);
		layerMenu.add(drawOnObject);
		layerMenu.add(drawOnDoorsTop);
		layerMenu.add(drawOnDoorsBot);
		
		debugX = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
		menuBar.add(debugX);
		
		debugY = new JSpinner(new SpinnerNumberModel(0, -100, 100, 1));
		menuBar.add(debugY);

		/*
		 * Sort the layers.
		 */

		// 1. LayeredPane
		layeredPane = new JLayeredPane();
		layeredPane.setOpaque(true);
		layeredPane.setBackground(Color.BLACK);
		layeredPane.setPreferredSize(new Dimension(1000,1000));

		// 2. The classes MapPanelBG, MapPanelMG and MapPanelFG are instantiated
		// in the constructor.

		// 3. Add the mapPanel*G classes to the layeredPane. 0(bgw), 1(floor),
		// 2(fgw) WORKS GREAT!
		layeredPane.add(mapPanelFloor, new Integer(0));
		layeredPane.add(mapPanelWalls, new Integer(1));
		layeredPane.add(mapPanelObject, new Integer(2));
		layeredPane.add(mapPanelDoors, new Integer(3));

		// Create the JScrollPane and add the panels.
		JScrollPane tileScrollPane = new JScrollPane(tilesPanel);
		JScrollPane mapScrollPane = new JScrollPane(layeredPane);
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tileScrollPane, mapScrollPane);
		splitPane.setDividerLocation(windowDivider);

		mainFrame.getContentPane().add(splitPane, BorderLayout.CENTER);
	}

	public void enableMenus()
	{
		saveFile.setEnabled(true);
		saveAsFile.setEnabled(true);
		closeFile.setEnabled(true);
		closeAllFiles.setEnabled(true);
		newTileset.setEnabled(true);
		roomProperty.setEnabled(true);
	}

	public void disableMenus()
	{
		saveFile.setEnabled(false);
		saveAsFile.setEnabled(false);
		closeFile.setEnabled(false);
		closeAllFiles.setEnabled(false);
		roomProperty.setEnabled(false);
	}

	public JPanel getTilesPanel()
	{
		return tilesPanel;
	}

	public MapHelper getMapHelper()
	{
		return mapHelper;
	}

	public TilesHelper getTilesHelper()
	{
		return tilesHelper;
	}

	public MouseHelper getMouseHelper()
	{
		return mouseHelper;
	}

	public TitledBorder getTitledBorder()
	{
		return titledBorder;
	}

	public FileHelper getFileHelper()
	{
		return fileHelper;
	}

	public MapPanelWalls getMapPanelWalls()
	{
		return mapPanelWalls;
	}

	public MapPanelFloor getMapPanelFloor()
	{
		return mapPanelFloor;
	}

	public MapPanelObject getMapPanelObjects()
	{
		return mapPanelObject;
	}

	public MapPanelDoors getMapPanelDoors()
	{
		return mapPanelDoors;
	}

	public JLayeredPane getLayeredPane()
	{
		return layeredPane;
	}

	public boolean isShowWalls()
	{
		return showWalls;
	}

	public boolean isShowFloor()
	{
		return showFloor;
	}

	public boolean isShowObjects()
	{
		return showObjects;
	}

	public boolean isShowDoors()
	{
		return showDoors;
	}

	public boolean isShowGrid()
	{
		return showGrid;
	}
}
