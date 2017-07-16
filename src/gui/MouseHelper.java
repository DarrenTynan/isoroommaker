package gui;

import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import mapHelper.MapHelper;

public class MouseHelper implements MouseListener, MouseMotionListener
{
	IsoRoomMaker main;
	MapHelper mh;
	MapPanelWalls mapPanelWalls;
	MapPanelFloor mapPanelFloor;
	MapPanelObject mapPanelObject;
	MapPanelDoors mapPanelDoors;

	Polygon targetPoly;
	boolean locationLeftWall = false;
	boolean locationRightWall = false;
	boolean locationFloor = false;
	boolean locationTopLeftDoor = false;
	boolean locationBotLeftDoor = false;
	boolean locationTopRightDoor = false;
	boolean locationBotRightDoor = false;
	boolean locationObject = false;

	// Radio buttons.
	boolean drawOnFloor = true;
	boolean drawOnWalls = false;
	boolean drawOnObject = false;
	boolean drawOnDoorsUpper = false;
	boolean drawOnDoorsLower = false;

	int mouseX, mouseY;

	public MouseHelper(IsoRoomMaker main)
	{
		this.main = main;
		this.mh = main.getMapHelper();
		this.mapPanelWalls = main.getMapPanelWalls();
		this.mapPanelFloor = main.getMapPanelFloor();
		this.mapPanelObject = main.getMapPanelObjects();
		this.mapPanelDoors = main.getMapPanelDoors();
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		if (mh.isMapCreated() && main.getTilesHelper().getTileInHand() != 999)
		{
			if (mh.isOnTarget() && locationLeftWall)
			{
				mh.translateGridCoords(mh.getMapTileID(), mh.getWallHeight(), mh.getFloorLength());
				int[][] array = mh.getLeftWallGidArray();
				array[mh.getyTranslate()][mh.getxTranslate()] = main.getTilesHelper().getTileInHand();
				updateLayers();
			}

			if (mh.isOnTarget() && locationRightWall)
			{
				mh.translateGridCoords(mh.getMapTileID(), mh.getWallHeight(), mh.getFloorWidth());
				int[][] array = mh.getRightWallGidArray();
				array[mh.getyTranslate()][mh.getxTranslate()] = main.getTilesHelper().getTileInHand();
				updateLayers();
			}

			if (mh.isOnTarget() && locationFloor)
			{
				mh.translateGridCoords(mh.getMapTileID(), mh.getFloorLength(), mh.getFloorWidth());
				int[][] array = mh.getFloorGidArray();
				array[mh.getyTranslate()][mh.getxTranslate()] = main.getTilesHelper().getTileInHand();
				updateLayers();
			}

			if (mh.isOnTarget() && locationTopLeftDoor)
			{
				mh.translateGridCoords(mh.getMapTileID(), mh.getWallHeight(), mh.getFloorLength());
				int[][] array = mh.getTopLeftDoorGidArray();
				array[mh.getyTranslate()][mh.getxTranslate()] = main.getTilesHelper().getTileInHand();
				updateLayers();
			}

			if (mh.isOnTarget() && locationTopRightDoor)
			{
				mh.translateGridCoords(mh.getMapTileID(), mh.getWallHeight(), mh.getFloorWidth());
				int[][] array = mh.getTopRightDoorGidArray();
				array[mh.getyTranslate()][mh.getxTranslate()] = main.getTilesHelper().getTileInHand();
				updateLayers();
			}

			if (mh.isOnTarget() && locationBotLeftDoor)
			{
				mh.translateGridCoords(mh.getMapTileID(), mh.getWallHeight(), mh.getFloorLength());
				int[][] array = mh.getBotLeftDoorGidArray();
				array[mh.getyTranslate()][mh.getxTranslate()] = main.getTilesHelper().getTileInHand();
				updateLayers();
			}

			if (mh.isOnTarget() && locationBotRightDoor)
			{
				mh.translateGridCoords(mh.getMapTileID(), mh.getWallHeight(), mh.getFloorWidth());
				int[][] array = mh.getBotRightDoorGidArray();
				array[mh.getyTranslate()][mh.getxTranslate()] = main.getTilesHelper().getTileInHand();
				updateLayers();
			}

			if (mh.isOnTarget() && locationObject)
			{
				mh.translateGridCoords(mh.getMapTileID(), mh.getFloorLength(), mh.getFloorWidth());
				int[][] array = mh.getObjectGidArray();
				array[mh.getyTranslate()][mh.getxTranslate()] = main.getTilesHelper().getTileInHand();
				updateLayers();
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

	/**
	 * Check to see if mouse pointer is in a map area.
	 */
	@Override
	public void mouseMoved(MouseEvent e)
	{
		if (mh.isMapCreated())
		{
			// 999 means off or no selection.
			mh.setOnTarget(false);
			mh.setMapTileID(999);

			mouseX = e.getX();
			mouseY = e.getY();

			if (drawOnWalls)
			{
				locationLeftWall = checkTarget(e, mh.getLeftWallTileArray(), mh.getFloorLength(), mh.getWallHeight());
				locationRightWall = checkTarget(e, mh.getRightWallTileArray(), mh.getFloorWidth(), mh.getWallHeight());
			}

			if (drawOnFloor)
			{
				locationFloor = checkTarget(e, mh.getFloorTileArray(), mh.getFloorWidth(), mh.getFloorLength());
			}

			if (drawOnDoorsUpper)
			{
				locationTopLeftDoor = checkTarget(e, mh.getTopLeftDoorArray(), mh.getFloorLength(), mh.getWallHeight());
				locationTopRightDoor = checkTarget(e, mh.getTopRightDoorArray(), mh.getFloorWidth(), mh.getWallHeight());
			}

			if (drawOnDoorsLower)
			{
				locationBotLeftDoor = checkTarget(e, mh.getBotLeftDoorArray(), mh.getFloorLength(), mh.getWallHeight());
				locationBotRightDoor = checkTarget(e, mh.getBotRightDoorArray(), mh.getFloorWidth(), mh.getWallHeight());
			}

			if (drawOnObject)
			{
				locationObject = checkTarget(e, mh.getObjectTileArray(), mh.getFloorWidth(), mh.getFloorLength());
			}

		}
	}

	/**
	 * Iterate over the polygons of the array and check for, contains mouse x/y
	 * coordinates. Update the paint.
	 * 
	 * @param e - Copy of the mouse event.
	 * @param polygon - Pointer to the polygon array.
	 * @param width - The width of the tiles.
	 * @param height - The height of the tiles.
	 */
	private boolean checkTarget(MouseEvent e, Polygon[][] polygon, int width, int height)
	{
		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
				if (polygon[y][x].contains(e.getX(), e.getY()))
				{
					mh.setMapTileID(((y * width) + x));
					// Update the graphics.
					setTargetPoly(polygon[y][x]);
					mh.setOnTarget(true);
					updateLayers();
					return true;
				}
			}
		}
		updateLayers();
		return false;
	}

	public Polygon getTargetPoly()
	{
		return targetPoly;
	}

	public void setTargetPoly(Polygon targetPoly)
	{
		this.targetPoly = targetPoly;
	}

	public boolean isLocationLeftWall()
	{
		return locationLeftWall;
	}

	public boolean isLocationRightWall()
	{
		return locationRightWall;
	}

	public boolean isLocationFloor()
	{
		return locationFloor;
	}

	public void updateLayers()
	{
		mapPanelWalls.updateUI();
		mapPanelFloor.updateUI();
		mapPanelObject.updateUI();
		mapPanelDoors.updateUI();
	}

	public int getMouseX()
	{
		return mouseX;
	}

	public int getMouseY()
	{
		return mouseY;
	}

//	public boolean isDrawOnFloor()
//	{
//		return drawOnFloor;
//	}

	public void setDrawOnFloor(boolean drawOnFloor)
	{
		this.drawOnFloor = drawOnFloor;
	}

//	public boolean isDrawOnWalls()
//	{
//		return drawOnWalls;
//	}

	public boolean isLocationTopLeftDoor()
	{
		return locationTopLeftDoor;
	}

	public boolean isLocationBotLeftDoor()
	{
		return locationBotLeftDoor;
	}

	public boolean isLocationTopRightDoor()
	{
		return locationTopRightDoor;
	}

	public boolean isLocationBotRightDoor()
	{
		return locationBotRightDoor;
	}

	public boolean isLocationObject()
	{
		return locationObject;
	}
	
	public void setDrawOnWalls(boolean drawOnWalls)
	{
		this.drawOnWalls = drawOnWalls;
	}

//	public boolean isDrawOnObject()
//	{
//		return drawOnObject;
//	}

	public void setDrawOnObject(boolean drawOnObject)
	{
		this.drawOnObject = drawOnObject;
	}

	public boolean isDrawOnDoorsUpper()
	{
		return drawOnDoorsUpper;
	}

	public void setDrawOnDoorsUpper(boolean drawOnDoors)
	{
		this.drawOnDoorsUpper = drawOnDoors;
	}

	public boolean isDrawOnDoorsLower()
	{
		return drawOnDoorsLower;
	}

	public void setDrawOnDoorsLower(boolean drawOnDoors)
	{
		this.drawOnDoorsLower = drawOnDoors;
	}
}
