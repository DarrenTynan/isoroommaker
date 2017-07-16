package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import mapHelper.MapHelper;

public class MapPanelDoors extends JPanel
{
	IsoRoomMaker main;
	MapHelper mh;
	TilesHelper th;

	public MapPanelDoors(IsoRoomMaker main)
	{
		this.main = main;
		this.mh = main.getMapHelper();
		this.th = main.getTilesHelper();
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		if (mh.isMapCreated())
		{
			if (main.isShowDoors())
			{
				drawText(g);
				drawTargetTile(g);
			}
		}

		if (main.isShowDoors())
		{
			// Only draw if tileset loaded.
			if (th.isTileSet())
			{
				drawTopLeftDoorTiles(g, mh.getTopLeftDoorGidArray(), mh.getWallTileWidth(), mh.getWallTileHeight(), mh.getFloorLength(), mh.getWallHeight());
				drawTopRightDoorTiles(g, mh.getTopRightDoorGidArray(), mh.getWallTileWidth(), mh.getWallTileHeight(), mh.getFloorWidth(), mh.getWallHeight());

				drawBotLeftDoorTiles(g, mh.getBotLeftDoorGidArray(), mh.getWallTileWidth(), mh.getWallTileHeight(), mh.getFloorLength(), mh.getWallHeight());
				drawBotRightDoorTiles(g, mh.getBotRightDoorGidArray(), mh.getWallTileWidth(), mh.getWallTileHeight(), mh.getFloorWidth(), mh.getWallHeight());
			}
		}
	}

	public void drawTargetTile(Graphics g)
	{
		Polygon polygon = main.getMouseHelper().getTargetPoly();
		if (mh.isOnTarget())
		{
			g.setColor(Color.PINK);
			g.fillPolygon(polygon);
			// g.setColor(Color.WHITE);
		}
	}

	public void drawText(Graphics g)
	{
		g.setColor(Color.WHITE);
		int yOffset = 400;
		g.drawString("Map Tile ID: " + mh.getMapTileID(), 10, yOffset);
		g.drawString("Fill: " + mh.isOnTarget(), 10, yOffset + 20);
		g.drawString("InHand: " + th.getTileInHand(), 10, yOffset + 40);

		if (main.getMouseHelper().isLocationLeftWall())
		{
			g.drawString("Location: left wall", 10, yOffset + 60);
		}

		if (main.getMouseHelper().isLocationRightWall())
		{
			g.drawString("Location: right wall", 10, yOffset + 60);
		}

		if (main.getMouseHelper().isLocationFloor())
		{
			g.drawString("Location: floor", 10, yOffset + 60);
		}

		if (main.getMouseHelper().isLocationTopLeftDoor())
		{
			g.drawString("Location: top left door", 10, yOffset + 60);
		}

		if (main.getMouseHelper().isLocationTopRightDoor())
		{
			g.drawString("Location: top right door", 10, yOffset + 60);
		}

		if (main.getMouseHelper().isLocationBotLeftDoor())
		{
			g.drawString("Location: bot left door", 10, yOffset + 60);
		}

		if (main.getMouseHelper().isLocationBotRightDoor())
		{
			g.drawString("Location: bot right door", 10, yOffset + 60);
		}

		if (main.getMouseHelper().isLocationObject())
		{
			g.drawString("Location: object", 10, yOffset + 60);
		}

		g.drawString("mouse x: " + main.getMouseHelper().getMouseX(), 10, yOffset + 80);
		g.drawString("mouse y: " + main.getMouseHelper().getMouseY(), 10, yOffset + 100);

		g.drawString("debugX: " + (int)main.debugX.getValue(), 10, yOffset + 120);
		g.drawString("debugY: " + (int)main.debugY.getValue(), 10, yOffset + 140);

	}

	public void drawTopLeftDoorTiles(Graphics g, int[][] gidArray, int tileWidth, int tileHeight, int areaWidth, int areaHeight)
	{
		int y = 0;
		int x = 0;
		int xOffset = mh.getFloorCornerNorthX() - tileWidth;
		int yOffset = mh.getFloorCornerNorthY() - tileHeight;
		BufferedImage[][] bi;

		for (y = 0; y < areaHeight; y++)
		{
			for (x = 0; x < areaWidth; x++)
			{
				bi = th.getTileBufferedImageArray();
				mh.translateGridCoords(gidArray[y][x], th.getImageRows(), th.getImageCols());

				g.drawImage(bi[mh.getyTranslate()][mh.getxTranslate()], (xOffset - (x * tileWidth)), (yOffset + (x * tileHeight / 2)), this);
			}
			yOffset -= tileHeight;
		}
	}

	public void drawTopRightDoorTiles(Graphics g, int[][] gidArray, int tileWidth, int tileHeight, int areaWidth, int areaHeight)
	{
		int y = 0;
		int x = 0;
		int xOffset = mh.getFloorCornerNorthX() - (tileWidth / 2) - 2;
		int yOffset = mh.getFloorCornerNorthY() - (tileWidth + (tileHeight / 2)) + mh.getDoorDepth();// - (int)main.debugY.getValue();
		BufferedImage[][] bi;

		for (y = 0; y < areaHeight; y++)
		{
			for (x = 0; x < areaWidth; x++)
			{
				bi = th.getTileBufferedImageArray();
				mh.translateGridCoords(gidArray[y][x], th.getImageRows(), th.getImageCols());

				g.drawImage(bi[mh.getyTranslate()][mh.getxTranslate()], (xOffset + (x * tileWidth)), (yOffset + (x * tileHeight / 2)), this);
			}
			yOffset -= tileHeight;
		}
	}

	public void drawBotLeftDoorTiles(Graphics g, int[][] gidArray, int tileWidth, int tileHeight, int areaWidth, int areaHeight)
	{
		int y = 0;
		int x = 0;
		int xOffset = mh.getFloorCornerWestX() - 1;
		int yOffset = mh.getFloorCornerWestY() - ((tileWidth + (tileHeight / 2)));
		BufferedImage[][] bi;
		
		for (y = 0; y < areaHeight; y++)
		{
			for (x = 0; x < areaWidth; x++)
			{
				bi = th.getTileBufferedImageArray();
				mh.translateGridCoords(gidArray[y][x], th.getImageRows(), th.getImageCols());

				g.drawImage(bi[mh.getyTranslate()][mh.getxTranslate()], (xOffset + (x * tileWidth)), (yOffset + (x * tileHeight / 2)), this);
			}
			yOffset -= tileHeight;
		}
	}

	public void drawBotRightDoorTiles(Graphics g, int[][] gidArray, int tileWidth, int tileHeight, int areaWidth, int areaHeight)
	{
		int y = 0;
		int x = 0;
		int xOffset = mh.getFloorCornerEastX() - ((tileWidth + (tileHeight / 2)));
		int yOffset = mh.getFloorCornerEastY() - ((tileWidth + (tileHeight / 2)));
		BufferedImage[][] bi;

		for (y = 0; y < areaHeight; y++)
		{
			for (x = 0; x < areaWidth; x++)
			{
				bi = th.getTileBufferedImageArray();
				mh.translateGridCoords(gidArray[y][x], th.getImageRows(), th.getImageCols());

				g.drawImage(bi[mh.getyTranslate()][mh.getxTranslate()], (xOffset - (x * tileWidth)), (yOffset + (x * tileHeight / 2)), this);
			}
			yOffset -= tileHeight;
		}
	}

}
