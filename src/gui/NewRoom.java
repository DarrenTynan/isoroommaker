package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import mapHelper.MapHelper;
import javax.swing.SwingConstants;

public class NewRoom extends JDialog
{
	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private IsoRoomMaker main;

	private MapHelper mh;

	private JSpinner roomidSpinner;
	private JSpinner floorWidthSpinner;
	private JSpinner floorLengthSpinner;
	private JSpinner floorTileWidthSpinner;
	private JSpinner floorTileHeightSpinner;
	private JSpinner floorTileDepthSpinner;
	private JSpinner doorTileDepthSpinner;
	private JSpinner wallHeightSpinner;
	private JSpinner wallTileWidthSpinner;
	private JSpinner wallTileHeightSpinner;
	private JSpinner wallTileDepthSpinner;

	/**
	 * Create the dialog.
	 */
	public NewRoom(IsoRoomMaker irm)
	{
		this.main = irm;
		this.mh = main.getMapHelper();

		setTitle("New Room");
		setBounds(100, 100, 631, 254);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JPanel floorSizePanel = new JPanel();
		floorSizePanel.setBounds(422, 5, 200, 173);
		floorSizePanel.setName("");
		floorSizePanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true), "Floor Size", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPanel.add(floorSizePanel);
		floorSizePanel.setLayout(null);

		JLabel floorWidth = new JLabel("Width(x)");
		floorWidth.setBounds(16, 26, 100, 16);
		floorSizePanel.add(floorWidth);

		floorWidthSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
		floorWidthSpinner.setBounds(123, 20, 65, 28);
		floorSizePanel.add(floorWidthSpinner);

		JLabel floorLength = new JLabel("Length(z)");
		floorLength.setBounds(16, 53, 100, 16);
		floorSizePanel.add(floorLength);

		floorLengthSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
		floorLengthSpinner.setBounds(123, 47, 65, 28);
		floorSizePanel.add(floorLengthSpinner);

		JLabel lblTileWidth = new JLabel("Tile Width(x)");
		lblTileWidth.setBounds(16, 80, 100, 16);
		floorSizePanel.add(lblTileWidth);

		floorTileWidthSpinner = new JSpinner(new SpinnerNumberModel(64, 1, 100, 1));
		floorTileWidthSpinner.setBounds(123, 74, 65, 28);
		floorSizePanel.add(floorTileWidthSpinner);

		JLabel lblTileLength = new JLabel("Tile Length(z)");
		lblTileLength.setBounds(16, 107, 100, 16);
		floorSizePanel.add(lblTileLength);

		floorTileHeightSpinner = new JSpinner(new SpinnerNumberModel(32, 1, 100, 1));
		floorTileHeightSpinner.setBounds(123, 101, 65, 28);
		floorSizePanel.add(floorTileHeightSpinner);

		JLabel floorTileDepth = new JLabel("Tile Depth(y)");
		floorTileDepth.setBounds(16, 135, 100, 16);
		floorSizePanel.add(floorTileDepth);

		floorTileDepthSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
		floorTileDepthSpinner.setBounds(123, 129, 65, 28);
		floorSizePanel.add(floorTileDepthSpinner);

		JPanel idPanel = new JPanel();
		idPanel.setBounds(6, 5, 202, 120);
		idPanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true), "Room ID", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPanel.add(idPanel);
		idPanel.setLayout(null);

		JLabel lblNewLabel_1 = new JLabel("RoomID");
		lblNewLabel_1.setBounds(18, 30, 61, 16);
		idPanel.add(lblNewLabel_1);

		JLabel lblNewLabel = new JLabel("Name");
		lblNewLabel.setBounds(18, 58, 56, 16);
		idPanel.add(lblNewLabel);

		roomidSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
		roomidSpinner.setBounds(125, 24, 65, 28);
		idPanel.add(roomidSpinner);

		textField = new JTextField();
		textField.setBounds(18, 78, 172, 28);
		idPanel.add(textField);
		textField.setColumns(10);

		JPanel wallSizePanel = new JPanel();
		wallSizePanel.setBounds(216, 5, 200, 173);
		wallSizePanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true), "Wall Size", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPanel.add(wallSizePanel);
		wallSizePanel.setLayout(null);

		JLabel wallHeight = new JLabel("Height(y)");
		wallHeight.setBounds(16, 26, 100, 16);
		wallSizePanel.add(wallHeight);

		wallHeightSpinner = new JSpinner(new SpinnerNumberModel(4, 1, 100, 1));
		wallHeightSpinner.setBounds(123, 20, 65, 28);
		wallSizePanel.add(wallHeightSpinner);

		JLabel tileWidth = new JLabel("Tile Width(x)");
		tileWidth.setBounds(16, 53, 100, 16);
		wallSizePanel.add(tileWidth);

		wallTileWidthSpinner = new JSpinner(new SpinnerNumberModel(32, 1, 100, 1));
		wallTileWidthSpinner.setBounds(123, 49, 65, 28);
		wallSizePanel.add(wallTileWidthSpinner);

		JLabel tileLength = new JLabel("Tile Length(z)");
		tileLength.setBounds(16, 80, 100, 16);
		wallSizePanel.add(tileLength);

		wallTileHeightSpinner = new JSpinner(new SpinnerNumberModel(32, 1, 100, 1));
		wallTileHeightSpinner.setBounds(123, 76, 65, 28);
		wallSizePanel.add(wallTileHeightSpinner);

		JLabel wallTileDepth = new JLabel("Tile Depth(y)");
		wallTileDepth.setBounds(16, 107, 100, 16);
		wallSizePanel.add(wallTileDepth);

		wallTileDepthSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
		wallTileDepthSpinner.setBounds(123, 103, 65, 28);
		wallSizePanel.add(wallTileDepthSpinner);

		JPanel doorSizePanel = new JPanel();
		doorSizePanel.setLayout(null);
		doorSizePanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true), "Door Size", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		doorSizePanel.setBounds(6, 128, 202, 50);
		contentPanel.add(doorSizePanel);

		JLabel doorTileDepth = new JLabel("Tile Depth");
		doorTileDepth.setBounds(18, 22, 100, 16);
		doorSizePanel.add(doorTileDepth);

		doorTileDepthSpinner = new JSpinner(new SpinnerNumberModel(8, 1, 100, 1));
		doorTileDepthSpinner.setBounds(125, 16, 65, 28);
		doorSizePanel.add(doorTileDepthSpinner);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);

		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				mh.setRoomID((int) roomidSpinner.getValue());
				mh.setRoomName(textField.getText());

				mh.setFloorWidth((int) floorWidthSpinner.getValue());
				mh.setFloorLength((int) floorLengthSpinner.getValue());
				mh.setFloorTileWidth((int) floorTileWidthSpinner.getValue());
				mh.setFloorTileHeight((int) floorTileHeightSpinner.getValue());

				mh.setWallHeight((int) wallHeightSpinner.getValue());
				mh.setWallTileWidth((int) wallTileWidthSpinner.getValue());
				mh.setWallTileHeight((int) wallTileHeightSpinner.getValue());

				mh.setDoorDepth((int) doorTileDepthSpinner.getValue());

				mh.setWallTileDepth((int) wallTileDepthSpinner.getValue());

				mh.setFloorTileDepth((int) floorTileDepthSpinner.getValue());

				// Call to initialise the arrays now we know the area
				// dimensions.
				mh.initialise();

				// Now it is safe to Toggle map created.
				mh.setMapCreated(true);

				// Is there a room name?
				if (mh.getRoomName() != null)
				{
					main.getTitledBorder().setTitle("Room " + mh.getRoomID() + " - " + mh.getRoomName());
				} else
				{
					main.getTitledBorder().setTitle("Room " + mh.getRoomID());
				}

				main.getFileHelper().buildXML();

				dispose();
			}
		});

		okButton.setActionCommand("OK");
		buttonPanel.add(okButton);
		getRootPane().setDefaultButton(okButton);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dispose();
			}
		});

		cancelButton.setActionCommand("Cancel");
		buttonPanel.add(cancelButton);
	}
}
