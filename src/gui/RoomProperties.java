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
import javax.swing.SpinnerModel;

public class RoomProperties extends JDialog
{
	private final JPanel contentPanel = new JPanel();
	private JTextField roomName;
	private IsoRoomMaker main;
	private MapHelper mh;
	private JSpinner roomidSpinner;
	JSpinner doorDepthSpinner;
	JSpinner wallTileDepthSpinner;
	JSpinner floorTileDepthSpinner;

	/**
	 * Create the dialog.
	 */
	public RoomProperties(IsoRoomMaker irm)
	{
		this.main = irm;
		this.mh = main.getMapHelper();

		setTitle("Room Properties");
		setBounds(100, 100, 454, 200);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JPanel idPanel = new JPanel();
		idPanel.setBounds(19, 6, 202, 120);
		idPanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true), "Room ID", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPanel.add(idPanel);
		idPanel.setLayout(null);

		JLabel idLabel = new JLabel("RoomID");
		idLabel.setBounds(18, 30, 61, 16);
		idPanel.add(idLabel);

		JLabel nameLabel = new JLabel("Name");
		nameLabel.setBounds(18, 58, 56, 16);
		idPanel.add(nameLabel);

		roomidSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
		roomidSpinner.setBounds(125, 24, 65, 28);
		roomidSpinner.setValue(mh.getRoomID());
		idPanel.add(roomidSpinner);

		roomName = new JTextField();
		roomName.setText(mh.getRoomName());
		roomName.setBounds(18, 78, 172, 28);
		idPanel.add(roomName);
		roomName.setColumns(10);
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0), 1, true), "Tile Depth", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(233, 6, 202, 120);
		contentPanel.add(panel);
		
		JLabel lblDoorDepth = new JLabel("Door Depth");
		lblDoorDepth.setBounds(18, 30, 108, 16);
		panel.add(lblDoorDepth);
		
		doorDepthSpinner = new JSpinner(new SpinnerNumberModel(8, 0, 16, 1));
		doorDepthSpinner.setBounds(125, 24, 65, 28);
		doorDepthSpinner.setValue(mh.getDoorDepth());
		panel.add(doorDepthSpinner);
		
		JLabel lblWallTile = new JLabel("Wall Tile Depth");
		lblWallTile.setBounds(18, 58, 108, 16);
		panel.add(lblWallTile);
		
		JLabel lblFloorTile = new JLabel("Floor Tile Depth");
		lblFloorTile.setBounds(18, 86, 108, 16);
		panel.add(lblFloorTile);
		
		wallTileDepthSpinner = new JSpinner(new SpinnerNumberModel(1, 0, 16, 1));
		wallTileDepthSpinner.setBounds(125, 52, 65, 28);
		wallTileDepthSpinner.setValue(mh.getWallTileDepth());
		panel.add(wallTileDepthSpinner);
		
		floorTileDepthSpinner = new JSpinner(new SpinnerNumberModel(1, 0, 16, 1));
		floorTileDepthSpinner.setBounds(125, 80, 65, 28);
		floorTileDepthSpinner.setValue(mh.getFloorTileDepth());
		panel.add(floorTileDepthSpinner);

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
				mh.setRoomName(roomName.getText());

				// Is there a room name?
				if (mh.getRoomName() != null)
				{
					main.getTitledBorder().setTitle("Room " + mh.getRoomID() + " - " + mh.getRoomName());
				} else
				{
					main.getTitledBorder().setTitle("Room " + mh.getRoomID());
				}

				main.getFileHelper().buildXML();
				
				mh.setDoorDepth((int) doorDepthSpinner.getValue());
				

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
