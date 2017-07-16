package gui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import mapHelper.MapHelper;

public class MapPanelObject extends JPanel
{
	IsoRoomMaker main;
	MapHelper mh;
	TilesHelper th;

	public MapPanelObject(IsoRoomMaker main)
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
				
			}
		}
		
		if (main.isShowObjects())
		{
			if (th.isTileSet())
			{
				drawObjectTiles(g, mh.getObjectGidArray(), mh.getFloorTileWidth(), mh.getFloorTileHeight(), mh.getFloorWidth(), mh.getFloorLength());
			}
		}
	}
	
	public void drawObjectTiles(Graphics g, int[][] gidArray, int tileWidth, int tileHeight, int areaWidth, int areaHeight)
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

}
