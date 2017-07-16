package mapHelper;

import gui.IsoRoomMaker;

import java.awt.Dimension;
import java.awt.Polygon;

public class MapHelper
{
	private boolean mapCreated = false;
	private int roomID;
	private String roomName;

	private int floorWidth;
	private int floorLength;
	private int floorTileWidth;
	private int floorTileHeight;
	private int floorTileDepth = 1;

	private int wallWidth;
	private int wallHeight;
	private int wallTileWidth;
	private int wallTileHeight;
	private int wallTileDepth = 1;

	private int doorDepth = 8;
	
	private int floorCornerNorthX;
	private int floorCornerNorthY;

	private int floorCornerEastX;
	private int floorCornerEastY;

	private int floorCornerWestX;
	private int floorCornerWestY;

	private int totalWidth;
	private int totalHeight;

	// Polygon's for drawing.
	private Polygon leftWallTileArray[][];
	private Polygon rightWallTileArray[][];
	private Polygon floorTileArray[][];
	private Polygon objectTileArray[][];

	private Polygon topLeftDoorTileArray[][];
	private Polygon botLeftDoorTileArray[][];
	private Polygon topRightDoorTileArray[][];
	private Polygon botRightDoorTileArray[][];

	// Gid array for map builder save file.
	private int leftWallGidArray[][];
	private int rightWallGidArray[][];
	private int floorGidArray[][];
	private int objectGidArray[][];

	private int topLeftDoorGidArray[][];
	private int botLeftDoorGidArray[][];
	private int topRightDoorGidArray[][];
	private int botRightDoorGidArray[][];

	private int lwGidArraySize, rwGidArraySize, floorGidArraySize, objGidArraySize, tldGidArraySize, bldGidArraySize, trdGidArraySize, brdGidArraySize;

	private int xTranslate = 0;
	private int yTranslate = 0;

	private Polygon poly;
	private boolean onFloorTarget = false;
	private int mapTileID = 999;

	private IsoRoomMaker main;

	public MapHelper(IsoRoomMaker main)
	{
		this.main = main;
	}

	public void initialise()
	{
		int windowWidth = main.getLayeredPane().getWidth();
		int gridWidth = ((floorWidth + floorLength)) * (floorTileWidth / 2);
		int dif = (windowWidth - gridWidth) / 2;

		if (floorLength > floorWidth)
		{
			floorCornerNorthX = floorLength * (floorTileWidth / 2) + dif;
		}

		if (floorWidth > floorLength)
		{
			floorCornerNorthX = (floorLength / 2) * (floorTileWidth) + dif;
		}

		if (floorLength == floorWidth)
		{
			floorCornerNorthX = windowWidth / 2;
		}

		floorCornerNorthY = (wallTileHeight * wallHeight) + wallTileHeight;

		floorCornerEastX = floorCornerNorthX + (floorWidth * (floorTileWidth / 2));
		floorCornerEastY = floorCornerNorthY + (floorWidth * (floorTileHeight / 2));
		
		floorCornerWestX = floorCornerNorthX - (floorLength * (floorTileWidth / 2));
		floorCornerWestY = floorCornerNorthY + (floorLength * (floorTileHeight / 2));
		
		totalWidth = (floorTileWidth * floorWidth) + (floorTileWidth * floorLength) + floorTileWidth;
		totalHeight = (wallTileHeight * wallHeight) + (floorTileWidth * floorLength) + floorTileWidth;

		// Polygon's for drawing.
		leftWallTileArray = new Polygon[wallHeight][floorLength];
		rightWallTileArray = new Polygon[wallHeight][floorWidth];
		floorTileArray = new Polygon[floorLength][floorWidth];
		objectTileArray = new Polygon[floorLength][floorWidth];

		topLeftDoorTileArray = new Polygon[wallHeight][floorLength];
		botLeftDoorTileArray = new Polygon[wallHeight][floorWidth];
		topRightDoorTileArray = new Polygon[wallHeight][floorWidth];
		botRightDoorTileArray = new Polygon[wallHeight][floorLength];

		// Gid array for map builder save file.
		leftWallGidArray = new int[wallHeight][floorLength];
		rightWallGidArray = new int[wallHeight][floorWidth];
		floorGidArray = new int[floorLength][floorWidth];
		objectGidArray = new int[floorLength][floorWidth];
		topLeftDoorGidArray = new int[wallHeight][floorLength];
		botLeftDoorGidArray = new int[wallHeight][floorWidth];
		topRightDoorGidArray = new int[wallHeight][floorWidth];
		botRightDoorGidArray = new int[wallHeight][floorLength];

		buildFloorTileArray(getFloorCornerNorthX(), getFloorCornerNorthY());

		buildObjectTileArray(getFloorCornerNorthX(), getFloorCornerNorthY());

		buildTopLeftTileArray(getFloorCornerNorthX(), getFloorCornerNorthY(), leftWallTileArray);
		buildTopRightTileArray(getFloorCornerNorthX(), getFloorCornerNorthY(), rightWallTileArray);

		buildTopLeftTileArray(getFloorCornerNorthX(), getFloorCornerNorthY(), topLeftDoorTileArray);
		buildTopRightTileArray(getFloorCornerNorthX(), getFloorCornerNorthY(), topRightDoorTileArray);

		buildBotLeftTileArray(getFloorCornerWestX(), getFloorCornerWestY(), botLeftDoorTileArray);
		buildBotRightTileArray(getFloorCornerEastX(), getFloorCornerEastY(), botRightDoorTileArray);


		Dimension dim = main.getLayeredPane().getSize();

		main.getMapPanelFloor().setBounds(0, 0, dim.width, dim.height);
		main.getMapPanelWalls().setBounds(0, 0, dim.width, dim.height);
		main.getMapPanelObjects().setBounds(0, 0, dim.width, dim.height);
		main.getMapPanelDoors().setBounds(0, 0, dim.width, dim.height);

		main.getLayeredPane().setPreferredSize(new Dimension(dim.width,dim.height));

	}

	/**
	 * Translate the mapTileID into x and y coordinates and store in xTranslate
	 * and yTranslate.
	 * 
	 * @param height
	 *            - Floor or wall.
	 * @param width
	 *            - Floor or wall.
	 */
	public void translateGridCoords(int id, int height, int width)
	{
		xTranslate = 0;
		yTranslate = 0;

		for (int i = 0; i < height; i++)
		{
			if (id >= width)
			{
				id -= width;
				yTranslate++;
			}
		}

		if (id <= width - 1)
		{
			xTranslate = id;
		}
	}

	/**
	 * Build the Polygon tile arrays, with the coordinates of the grid. Starting
	 * from the x and y points passed.
	 * 
	 * @param xPoint
	 * @param yPoint
	 */
	public void buildTopLeftTileArray(int xPoint, int yPoint, Polygon array[][])
	{
		for (int y = 0; y < wallHeight; y++)
		{
			int x1 = xPoint;
			int y1 = yPoint - (y * wallTileHeight);

			int x2 = x1;
			int y2 = y1 - wallTileHeight;

			int x3 = x1 - wallTileWidth;
			int y3 = y2 + wallTileHeight / 2;

			int x4 = x3;
			int y4 = y1 + wallTileHeight / 2;

			for (int x = 0; x < floorLength; x++)
			{
				poly = new Polygon();

				poly.addPoint(x1 - (wallTileWidth * x), y1 + ((wallTileHeight / 2) * x));
				poly.addPoint(x2 - (wallTileWidth * x), y2 + ((wallTileHeight / 2) * x));
				poly.addPoint(x3 - (wallTileWidth * x), y3 + ((wallTileHeight / 2) * x));
				poly.addPoint(x4 - (wallTileWidth * x), y4 + ((wallTileHeight / 2) * x));

				array[y][x] = poly;
			}
		}
	}

	/**
	 * Build the Polygon tile arrays, with the coordinates of the grid. Starting
	 * from the x and y points passed.
	 * 
	 * @param xPoint
	 * @param yPoint
	 */
	public void buildBotRightTileArray(int xPoint, int yPoint, Polygon array[][])
	{
		for (int y = 0; y < wallHeight; y++)
		{
			int x1 = xPoint;
			int y1 = yPoint - (y * wallTileHeight);

			int x2 = x1;
			int y2 = y1 - wallTileHeight;

			int x3 = x1 - wallTileWidth;
			int y3 = y2 + wallTileHeight / 2;

			int x4 = x3;
			int y4 = y1 + wallTileHeight / 2;

			for (int x = 0; x < floorLength; x++)
			{
				poly = new Polygon();

				poly.addPoint(x1 - (wallTileWidth * x), y1 + ((wallTileHeight / 2) * x));
				poly.addPoint(x2 - (wallTileWidth * x), y2 + ((wallTileHeight / 2) * x));
				poly.addPoint(x3 - (wallTileWidth * x), y3 + ((wallTileHeight / 2) * x));
				poly.addPoint(x4 - (wallTileWidth * x), y4 + ((wallTileHeight / 2) * x));

				array[y][x] = poly;
			}
		}
	}

	/**
	 * Build the Polygon tile arrays, with the coordinates of the grid. Starting
	 * from the x and y points passed.
	 * 
	 * @param xPoint
	 * @param yPoint
	 */
	public void buildTopRightTileArray(int xPoint, int yPoint, Polygon array[][])
	{
		for (int y = 0; y < wallHeight; y++)
		{
			int x1 = xPoint;
			int y1 = yPoint - (y * wallTileHeight);

			int x2 = x1;
			int y2 = y1 - wallTileHeight;

			int x3 = x1 + wallTileWidth;
			int y3 = y2 + wallTileHeight / 2;

			int x4 = x3;
			int y4 = y1 + wallTileHeight / 2;

			for (int x = 0; x < floorWidth; x++)
			{
				poly = new Polygon();

				poly.addPoint(x1 + (wallTileWidth * x), y1 + ((wallTileHeight / 2) * x));
				poly.addPoint(x2 + (wallTileWidth * x), y2 + ((wallTileHeight / 2) * x));
				poly.addPoint(x3 + (wallTileWidth * x), y3 + ((wallTileHeight / 2) * x));
				poly.addPoint(x4 + (wallTileWidth * x), y4 + ((wallTileHeight / 2) * x));

				array[y][x] = poly;
			}
		}
	}

	/**
	 * Build the Polygon tile arrays, with the coordinates of the grid. Starting
	 * from the x and y points passed.
	 * 
	 * @param xPoint
	 * @param yPoint
	 */
	public void buildBotLeftTileArray(int xPoint, int yPoint, Polygon array[][])
	{
		for (int y = 0; y < wallHeight; y++)
		{
			int x1 = xPoint;
			int y1 = yPoint - (y * wallTileHeight);

			int x2 = x1;
			int y2 = y1 - wallTileHeight;

			int x3 = x1 + wallTileWidth;
			int y3 = y2 + wallTileHeight / 2;

			int x4 = x3;
			int y4 = y1 + wallTileHeight / 2;

			for (int x = 0; x < floorWidth; x++)
			{
				poly = new Polygon();

				poly.addPoint(x1 + (wallTileWidth * x), y1 + ((wallTileHeight / 2) * x));
				poly.addPoint(x2 + (wallTileWidth * x), y2 + ((wallTileHeight / 2) * x));
				poly.addPoint(x3 + (wallTileWidth * x), y3 + ((wallTileHeight / 2) * x));
				poly.addPoint(x4 + (wallTileWidth * x), y4 + ((wallTileHeight / 2) * x));

				array[y][x] = poly;
			}
		}
	}

	/**
	 * Build the Polygon tile arrays, with the coordinates of the grid. Starting
	 * from the x and y points passed.
	 * 
	 * @param xPoint
	 * @param yPoint
	 */
	public void buildFloorTileArray(int xPoint, int yPoint)
	{
		int ftw = floorTileWidth / 2;
		int fth = floorTileHeight / 2;

		for (int y = 0; y < floorLength; y++)
		{
			int x1 = xPoint - (y * ftw);
			int y1 = yPoint + (y * fth);

			int x2 = x1 + ftw;
			int y2 = y1 + fth;

			int x3 = x1;
			int y3 = y2 + fth;

			int x4 = x1 - ftw;
			int y4 = y1 + fth;

			for (int x = 0; x < floorWidth; x++)
			{
				poly = new Polygon();

				poly.addPoint(x1 + (ftw * x), y1 + (fth * x));
				poly.addPoint(x2 + (ftw * x), y2 + (fth * x));
				poly.addPoint(x3 + (ftw * x), y3 + (fth * x));
				poly.addPoint(x4 + (ftw * x), y4 + (fth * x));

				floorTileArray[y][x] = poly;
			}
		}
	}

	/**
	 * Build the Polygon tile arrays, with the coordinates of the grid. Starting
	 * from the x and y points passed.
	 * 
	 * @param xPoint
	 * @param yPoint
	 */
	public void buildObjectTileArray(int xPoint, int yPoint)
	{
		int ftw = floorTileWidth / 2;
		int fth = floorTileHeight / 2;

		for (int y = 0; y < floorLength; y++)
		{
			int x1 = xPoint - (y * ftw);
			int y1 = yPoint + (y * fth);

			int x2 = x1 + ftw;
			int y2 = y1 + fth;

			int x3 = x1;
			int y3 = y2 + fth;

			int x4 = x1 - ftw;
			int y4 = y1 + fth;

			for (int x = 0; x < floorWidth; x++)
			{
				poly = new Polygon();

				poly.addPoint(x1 + (ftw * x), y1 + (fth * x));
				poly.addPoint(x2 + (ftw * x), y2 + (fth * x));
				poly.addPoint(x3 + (ftw * x), y3 + (fth * x));
				poly.addPoint(x4 + (ftw * x), y4 + (fth * x));

				objectTileArray[y][x] = poly;
			}
		}
	}

	public int getFloorTileWidth()
	{
		return floorTileWidth;
	}

	public void setFloorTileWidth(int floorTileWidth)
	{
		this.floorTileWidth = floorTileWidth;
	}

	public int getFloorTileHeight()
	{
		return floorTileHeight;
	}

	public void setFloorTileHeight(int floorTileHeight)
	{
		this.floorTileHeight = floorTileHeight;
	}

	public int getFloorWidth()
	{
		return floorWidth;
	}

	public void setFloorWidth(int floorWidth)
	{
		this.floorWidth = floorWidth;
	}

	public int getFloorLength()
	{
		return floorLength;
	}

	public void setFloorLength(int floorLength)
	{
		this.floorLength = floorLength;
	}

	public int getWallTileWidth()
	{
		return wallTileWidth;
	}

	public void setWallTileWidth(int wallTileWidth)
	{
		this.wallTileWidth = wallTileWidth;
	}

	public int getWallTileHeight()
	{
		return wallTileHeight;
	}

	public void setWallTileHeight(int wallTileHeight)
	{
		this.wallTileHeight = wallTileHeight;
	}

	public int getWallHeight()
	{
		return wallHeight;
	}

	public void setWallHeight(int wallHeight)
	{
		this.wallHeight = wallHeight;
	}

	public int getWallWidth()
	{
		return wallWidth;
	}

	public void setWallWidth(int wallWidth)
	{
		this.wallWidth = wallWidth;
	}

	public int getFloorCornerNorthX()
	{
		return floorCornerNorthX;
	}

	public int getFloorCornerNorthY()
	{
		return floorCornerNorthY;
	}

	public int getFloorCornerWestX()
	{
		return floorCornerWestX;
	}

	public int getFloorCornerWestY()
	{
		return floorCornerWestY;
	}

	public int getFloorCornerEastX()
	{
		return floorCornerEastX;
	}

	public int getFloorCornerEastY()
	{
		return floorCornerEastY;
	}

	public int getTotalWidth()
	{
		return totalWidth;
	}

	public void setTotalWidth(int totalWidth)
	{
		this.totalWidth = totalWidth;
	}

	public int getTotalHeight()
	{
		return totalHeight;
	}

	public void setTotalHeight(int totalHeight)
	{
		this.totalHeight = totalHeight;
	}

	public Polygon[][] getFloorTileArray()
	{
		return floorTileArray;
	}

	public Polygon[][] getLeftWallTileArray()
	{
		return leftWallTileArray;
	}

	public Polygon[][] getRightWallTileArray()
	{
		return rightWallTileArray;
	}

	public boolean isOnTarget()
	{
		return onFloorTarget;
	}

	public void setOnTarget(boolean onTarget)
	{
		this.onFloorTarget = onTarget;
	}

	public int getMapTileID()
	{
		return mapTileID;
	}

	public void setMapTileID(int id)
	{
		this.mapTileID = id;
	}

	public int[][] getLeftWallGidArray()
	{
		return leftWallGidArray;
	}

	public int[][] getRightWallGidArray()
	{
		return rightWallGidArray;
	}

	public int[][] getFloorGidArray()
	{
		return floorGidArray;
	}

	public int getxTranslate()
	{
		return xTranslate;
	}

	public int getyTranslate()
	{
		return yTranslate;
	}

	public int getRoomID()
	{
		return roomID;
	}

	public void setRoomID(int roomID)
	{
		this.roomID = roomID;
	}

	public Polygon getPoly()
	{
		return poly;
	}

	public void setPoly(Polygon poly)
	{
		this.poly = poly;
	}

	public boolean isOnFloorTarget()
	{
		return onFloorTarget;
	}

	public void setOnFloorTarget(boolean onFloorTarget)
	{
		this.onFloorTarget = onFloorTarget;
	}

	public IsoRoomMaker getMain()
	{
		return main;
	}

	public void setMain(IsoRoomMaker main)
	{
		this.main = main;
	}

	public void setFloorCornerNorthX(int floorCornerNorthX)
	{
		this.floorCornerNorthX = floorCornerNorthX;
	}

	public void setFloorCornerNorthY(int floorCornerNorthY)
	{
		this.floorCornerNorthY = floorCornerNorthY;
	}

	public void setFloorTileArray(Polygon[][] floorTileArray)
	{
		this.floorTileArray = floorTileArray;
	}

	public void setLeftWallTileArray(Polygon[][] leftWallTileArray)
	{
		this.leftWallTileArray = leftWallTileArray;
	}

	public void setRightWallTileArray(Polygon[][] rightWallTileArray)
	{
		this.rightWallTileArray = rightWallTileArray;
	}

	public void setLeftWallGidArray(int[][] leftWallGidArray)
	{
		this.leftWallGidArray = leftWallGidArray;
	}

	public void setRightWallGidArray(int[][] rightWallGidArray)
	{
		this.rightWallGidArray = rightWallGidArray;
	}

	public void setFloorGidArray(int[][] floorGidArray)
	{
		this.floorGidArray = floorGidArray;
	}

	public Polygon[][] getObjectTileArray()
	{
		return objectTileArray;
	}

	public Polygon[][] getTopLeftDoorArray()
	{
		return topLeftDoorTileArray;
	}

	public Polygon[][] getBotLeftDoorArray()
	{
		return botLeftDoorTileArray;
	}

	public Polygon[][] getTopRightDoorArray()
	{
		return topRightDoorTileArray;
	}

	public Polygon[][] getBotRightDoorArray()
	{
		return botRightDoorTileArray;
	}

	public int[][] getObjectGidArray()
	{
		return objectGidArray;
	}

	public int[][] getTopLeftDoorGidArray()
	{
		return topLeftDoorGidArray;
	}

	public int[][] getBotLeftDoorGidArray()
	{
		return botLeftDoorGidArray;
	}

	public int[][] getTopRightDoorGidArray()
	{
		return topRightDoorGidArray;
	}

	public int[][] getBotRightDoorGidArray()
	{
		return botRightDoorGidArray;
	}

	public void setxTranslate(int xTranslate)
	{
		this.xTranslate = xTranslate;
	}

	public void setyTranslate(int yTranslate)
	{
		this.yTranslate = yTranslate;
	}

	public boolean isMapCreated()
	{
		return mapCreated;
	}

	public void setMapCreated(boolean mapCreated)
	{
		this.mapCreated = mapCreated;
	}

	public String getRoomName()
	{
		return roomName;
	}

	public void setRoomName(String roomName)
	{
		this.roomName = roomName;
	}

	public int getLwGidArraySize()
	{
		return lwGidArraySize;
	}

	public void setLwGidArraySize(int lwGidArraySize)
	{
		this.lwGidArraySize = lwGidArraySize;
	}

	public int getRwGidArraySize()
	{
		return rwGidArraySize;
	}

	public void setRwGidArraySize(int rwGidArraySize)
	{
		this.rwGidArraySize = rwGidArraySize;
	}

	public int getfGidArraySize()
	{
		return floorGidArraySize;
	}

	public void setfGidArraySize(int fGidArraySize)
	{
		this.floorGidArraySize = fGidArraySize;
	}

	public int getObjGidArraySize()
	{
		return objGidArraySize;
	}

	public void setObjGidArraySize(int objGidArraySize)
	{
		this.objGidArraySize = objGidArraySize;
	}

	public int getTldGidArraySize()
	{
		return tldGidArraySize;
	}

	public void setTldGidArraySize(int tldGidArraySize)
	{
		this.tldGidArraySize = tldGidArraySize;
	}

	public int getBldGidArraySize()
	{
		return bldGidArraySize;
	}

	public void setBldGidArraySize(int bldGidArraySize)
	{
		this.bldGidArraySize = bldGidArraySize;
	}

	public int getTrdGidArraySize()
	{
		return trdGidArraySize;
	}

	public void setTrdGidArraySize(int trdGidArraySize)
	{
		this.trdGidArraySize = trdGidArraySize;
	}

	public int getBrdGidArraySize()
	{
		return brdGidArraySize;
	}

	public void setBrdGidArraySize(int brdGidArraySize)
	{
		this.brdGidArraySize = brdGidArraySize;
	}

	public int getDoorDepth()
	{
		return doorDepth;
	}

	public void setDoorDepth(int doorDepth)
	{
		this.doorDepth = doorDepth;
	}

	public int getFloorTileDepth()
	{
		return floorTileDepth;
	}

	public void setFloorTileDepth(int floorTileDepth)
	{
		this.floorTileDepth = floorTileDepth;
	}

	public int getWallTileDepth()
	{
		return wallTileDepth;
	}

	public void setWallTileDepth(int wallTileDepth)
	{
		this.wallTileDepth = wallTileDepth;
	}
	
	

}
