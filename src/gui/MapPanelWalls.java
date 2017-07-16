package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import mapHelper.MapHelper;

public class MapPanelWalls extends JPanel
{
	IsoRoomMaker main;
	MapHelper mh;
	TilesHelper th;

	public MapPanelWalls(IsoRoomMaker main)
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
			if (main.isShowGrid())
			{
				drawTopLeftWallGrid(g);
				drawBotLeftWallGrid(g);

				drawTopRightWallGrid(g);
				drawBotRightWallGrid(g);
			}
		}
		if (main.isShowWalls())
		{
			// Only draw if tileset loaded.
			if (th.isTileSet())
			{
				drawLeftWallTiles(g, mh.getLeftWallGidArray(), mh.getWallTileWidth(), mh.getWallTileHeight(), mh.getFloorLength(), mh.getWallHeight());
				drawRightWallTiles(g, mh.getRightWallGidArray(), mh.getWallTileWidth(), mh.getWallTileHeight(), mh.getFloorWidth(), mh.getWallHeight());
			}
		}

	}

	public void drawTopLeftWallGrid(Graphics g)
	{
		Polygon[][] polygonArray = mh.getLeftWallTileArray();
		Polygon polygon;
		for (int y = 0; y < mh.getWallHeight(); y++)
		{
			for (int x = 0; x < mh.getFloorLength(); x++)
			{
				polygon = polygonArray[y][x];
				g.drawPolygon(polygon);
				if (main.getMouseHelper().isDrawOnDoorsUpper())
				{
					g.setColor(Color.YELLOW);
				}
				else
				{
					g.setColor(Color.WHITE);
				}
			}
		}
	}

	public void drawBotLeftWallGrid(Graphics g)
	{
		Polygon[][] polygonArray = mh.getBotLeftDoorArray();
		Polygon polygon;
		for (int y = 0; y < mh.getWallHeight(); y++)
		{
			for (int x = 0; x < mh.getFloorWidth(); x++)
			{
				polygon = polygonArray[y][x];
				g.drawPolygon(polygon);
				if (main.getMouseHelper().isDrawOnDoorsLower())
				{
					g.setColor(Color.RED);
				} else
				{
					g.setColor(Color.WHITE);
				}
			}
		}
	}

	public void drawTopRightWallGrid(Graphics g)
	{
		Polygon[][] polygonArray = mh.getRightWallTileArray();
		Polygon polygon;
		for (int y = 0; y < mh.getWallHeight(); y++)
		{
			for (int x = 0; x < mh.getFloorWidth(); x++)
			{
				polygon = polygonArray[y][x];
				g.drawPolygon(polygon);
				if (main.getMouseHelper().isDrawOnDoorsUpper())
				{
					g.setColor(Color.YELLOW);
				} else
				{
					g.setColor(Color.WHITE);
				}
			}
		}
	}

	public void drawBotRightWallGrid(Graphics g)
	{
		Polygon[][] polygonArray = mh.getBotRightDoorArray();
		Polygon polygon;
		for (int y = 0; y < mh.getWallHeight(); y++)
		{
			for (int x = 0; x < mh.getFloorLength(); x++)
			{
				polygon = polygonArray[y][x];
				g.drawPolygon(polygon);
				if (main.getMouseHelper().isDrawOnDoorsLower())
				{
					g.setColor(Color.RED);
				} else
				{
					g.setColor(Color.WHITE);
				}
			}
		}
	}

	public void drawLeftWallTiles(Graphics g, int[][] gidArray, int tileWidth, int tileHeight, int areaWidth, int areaHeight)
	{
		int y = 0;
		int x = 0;
		int xOffset = mh.getFloorCornerNorthX() - tileWidth;
		int yOffset = mh.getFloorCornerNorthY() - ((tileWidth / 2) + tileHeight);
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

	public void drawRightWallTiles(Graphics g, int[][] gidArray, int tileWidth, int tileHeight, int areaWidth, int areaHeight)
	{
		int y = 0;
		int x = 0;
		int xOffset = mh.getFloorCornerNorthX();
		int yOffset = mh.getFloorCornerNorthY() - ((tileWidth / 2) + tileHeight);
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

}
