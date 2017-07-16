package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import mapHelper.MapHelper;

public class MapPanelFloor extends JPanel
{
	IsoRoomMaker main;
	MapHelper mh;
	TilesHelper th;

	public MapPanelFloor(IsoRoomMaker main)
	{
		this.main = main;
		this.mh = main.getMapHelper();
		this.th = main.getTilesHelper();
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		// drawSquareGrid(g);

		if (mh.isMapCreated())
		{
			if (main.isShowGrid())
			{
				drawRoomFloor(g);
			}
		}

		if (main.isShowFloor())
		{
			// Only draw if tileset loaded.
			if (th.isTileSet())
			{
				drawFloorTiles(g, mh.getFloorGidArray(), mh.getFloorTileWidth(), mh.getFloorTileHeight(), mh.getFloorWidth(), mh.getFloorLength());
			}
		}

	}

	public void drawFloorTiles(Graphics g, int[][] gidArray, int tileWidth, int tileHeight, int areaWidth, int areaHeight)
	{
		int y = 0;
		int x = 0;
		int xOffset = mh.getFloorCornerNorthX() - tileWidth / 2;
		int yOffset = mh.getFloorCornerNorthY() - tileWidth / 2;
		BufferedImage[][] bi;

		for (y = 0; y < areaHeight; y++)
		{
			for (x = 0; x < areaWidth; x++)
			{

				bi = th.getTileBufferedImageArray();
				mh.translateGridCoords(gidArray[y][x], th.getImageRows(), th.getImageCols());

				g.drawImage(bi[mh.getyTranslate()][mh.getxTranslate()], (xOffset + (x * tileWidth / 2)), (yOffset + (x * tileHeight / 2)), this);
			}
			yOffset += tileHeight / 2;
			xOffset -= tileWidth / 2;
		}
	}

	public void drawRoomFloor(Graphics g)
	{
		Polygon[][] polygonArray = mh.getFloorTileArray();
		Polygon polygon;
		for (int y = 0; y < mh.getFloorLength(); y++)
		{
			for (int x = 0; x < mh.getFloorWidth(); x++)
			{
				polygon = polygonArray[y][x];
				g.drawPolygon(polygon);
				g.setColor(Color.WHITE);
			}
		}
	}

	public void drawSquareGrid(Graphics g)
	{
		int hOffset = mh.getFloorTileWidth();
		int vOffset = mh.getFloorTileHeight();

		int x1h = 0;
		int y1h = 0;
		int x2h = getWidth();
		int y2h = 0;

		int x1v = 0;
		int y1v = 0;
		int x2v = 0;
		int y2v = getHeight();

		g.setColor(Color.WHITE);

		// Draw first line on l/h.
		for (int i = 0; i < getWidth() + 1; i++)
		{
			g.drawLine(x1h, y1h + (i * vOffset), x2h, y2h + (i * vOffset));
		}

		// Draw first line on l/h.
		for (int i = 0; i < getWidth(); i++)
		{
			g.drawLine(x1v + (i * hOffset), y1v, x2v + (i * hOffset), y2v);
		}
		// Draw last line on r/h.
		// g.drawLine(x1v+(tileWidth * roomWidth), y1v, x2v+(tileWidth *
		// roomWidth), y2v);
	}
}
