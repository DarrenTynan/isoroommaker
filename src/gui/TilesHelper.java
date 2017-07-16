package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class TilesHelper
{
	private IsoRoomMaker main;

	private BufferedImage image;
	private int imageWidth, imageHeight;
	private int tileImageWidth = 64;
	private int tileImageHeight = 64;
	private int imageRows;
	private int imageCols;
	private BufferedImage tileBufferedImageArray[][];
	private int tileInHand = 999;
	private boolean tileSetLoaded = false;

	public TilesHelper(IsoRoomMaker irm)
	{
		this.main = irm;
	}

	public void initialiseTileset()
	{
		imageWidth = image.getWidth();
		imageHeight = image.getHeight();

		imageRows = imageHeight / tileImageHeight;
		imageCols = imageWidth / tileImageWidth;

		buildTileArray();
	}

	/**
	 * Build an array of BuffereImage tiles, cut from the main BufferedImage
	 * tile sheet.
	 * 
	 * N.B. I saved out the images just to test my routines.
	 */
	private void buildTileArray()
	{
		// Build the array to hold tiles.
		tileBufferedImageArray = new BufferedImage[imageRows][imageCols];

		for (int y = 0; y < imageRows; y++)
		{
			for (int x = 0; x < imageCols; x++)
			{
				tileBufferedImageArray[y][x] = image.getSubimage(x * tileImageWidth, y * tileImageHeight, tileImageWidth, tileImageHeight);

				// try {
				// ImageIO.write(tileArray[y][x], "png", new
				// File("Assets/tiles/tile-" + (i++) + ".png"));
				// } catch (IOException e) {
				// e.printStackTrace();
				// System.out.println("Files not saved...");
				// }
			}
		}
	}

	/**
	 * Generate new buttons for all the tiles and set the icon for image. Adding
	 * an action listener.
	 * 
	 * @param jpanel
	 */
	public void generateButtons(JPanel panel)
	{
		BufferedImage[][] bi = getTileBufferedImageArray();
		ActionListener[] al = new ActionListener[getTileBufferedImageArray().length];
		// ID counter.
		int i = 0;

		for (int y = 0; y < getImageRows(); y++)
		{
			for (int x = 0; x < getImageCols(); x++)
			{
				ImageIcon myIcon = new ImageIcon(bi[y][x]);

				JButton but = new JButton(myIcon);
				but.setActionCommand(String.valueOf(i++));
				panel.add(but);
				al[x] = new ActionListener()
				{

					@Override
					public void actionPerformed(ActionEvent e)
					{
						tileInHand = Integer.parseInt(e.getActionCommand());
						main.getMapPanelWalls().repaint();
					}
				};

				but.addActionListener(al[x]);
			}
		}
	}

	public BufferedImage getImage()
	{
		return image;
	}

	public int getImageWidth()
	{
		return imageWidth;
	}

	public int getImageHeight()
	{
		return imageHeight;
	}

	public int getTileImageWidth()
	{
		return tileImageWidth;
	}

	public int getTileImageHeight()
	{
		return tileImageHeight;
	}

	public int getImageRows()
	{
		return imageRows;
	}

	public int getImageCols()
	{
		return imageCols;
	}

	// public BufferedImage[][] getTileArray() {
	// return tileBufferedImageArray;
	// }

	public int getTileInHand()
	{
		return tileInHand;
	}

	public BufferedImage[][] getTileBufferedImageArray()
	{
		return tileBufferedImageArray;
	}

	public boolean isTileSet()
	{
		return tileSetLoaded;
	}

	public void setTileSet(boolean tileSet)
	{
		this.tileSetLoaded = tileSet;
	}

	public void setImage(BufferedImage image)
	{
		this.image = image;
	}

	public IsoRoomMaker getMain()
	{
		return main;
	}

	public void setMain(IsoRoomMaker main)
	{
		this.main = main;
	}

	public boolean isTileSetLoaded()
	{
		return tileSetLoaded;
	}

	public void setTileSetLoaded(boolean tileSetLoaded)
	{
		this.tileSetLoaded = tileSetLoaded;
	}

	public void setImageWidth(int imageWidth)
	{
		this.imageWidth = imageWidth;
	}

	public void setImageHeight(int imageHeight)
	{
		this.imageHeight = imageHeight;
	}

	public void setImageRows(int imageRows)
	{
		this.imageRows = imageRows;
	}

	public void setImageCols(int imageCols)
	{
		this.imageCols = imageCols;
	}

	public void setTileBufferedImageArray(BufferedImage[][] tileBufferedImageArray)
	{
		this.tileBufferedImageArray = tileBufferedImageArray;
	}

	public void setTileInHand(int tileInHand)
	{
		this.tileInHand = tileInHand;
	}

}
