import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.*;

public class CartelDisplay implements ActionListener {
	public static final String DB_LOCATION = "jdbc:mysql://db.engr.ship.edu:3306/csc471_12?useTimezone=true&serverTimezone=UTC";
  public static final String LOGIN_NAME = "csc471_12";
  public static final String PASSWORD = "Password_12";
  protected static Connection m_dbConn = null;

  /**
   * Creates a connection to the database that you can then send commands to.
   */
  static {
      try {
          m_dbConn = DriverManager.getConnection(DB_LOCATION, LOGIN_NAME, PASSWORD);
      } catch (SQLException e) {
          e.printStackTrace();
      }
  }

  /**
   * To get the meta data for the DB.
   */
  DatabaseMetaData meta = m_dbConn.getMetaData();
  
	private JFrame mainFrame;
	private JLabel statusLabel;
	private JPanel controlPanel;
	
	public CartelDisplay() {
		mainFrame = new JFrame("Cartel Display");
		mainFrame.setSize(700, 700);
		mainFrame.setLayout(new GridLayout(3, 1));
		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				System.exit(0);
			}
		});
		
		statusLabel = new JLabel("", JLabel.CENTER);
		statusLabel.setSize(350, 100);

		controlPanel = new JPanel();
		controlPanel.setLayout(new FlowLayout());

		mainFrame.add(controlPanel);
		mainFrame.add(statusLabel);
		mainFrame.setVisible(true);
	}

	public static void main(String args[]) {
		CartelDisplay database = new CartelDisplay();
		database.runDemo();
	}

	private void runDemo() {
		final DefaultListModel<String> cartelName = new DefaultListModel<String>();
		cartelName.addElement("Cartel 1");
		cartelName.addElement("Cartel 2");
		
		final JList<String> cartelList = new JList<String>(cartelName);
		cartelList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		cartelList.setSelectedIndex(1);
		cartelList.setVisibleRowCount(6);
		cartelList.setPreferredSize(new Dimension(100, 200));

		JScrollPane ListScrollPane = new JScrollPane(cartelList);

		DefaultListModel<String> playerName = new DefaultListModel<String>();
		playerName.addElement("Player 1");
		playerName.addElement("Player 2");
		
		JList<String> playerList = new JList<String>(playerName);
		playerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		playerList.setSelectedIndex(1);
		playerList.setVisibleRowCount(6);
		playerList.setPreferredSize(new Dimension(100, 200));
		
		JScrollPane ListPlayerScrollPane = new JScrollPane(playerList);
		
		DefaultListModel<String> selectedPlayerName = new DefaultListModel<String>();
		
		JList<String> selectedPlayerList = new JList<String>(selectedPlayerName);
		playerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		playerList.setSelectedIndex(1);
		playerList.setVisibleRowCount(6);
		playerList.setPreferredSize(new Dimension(100, 200));
		
		JScrollPane ListSelectedPlayerScrollPane = new JScrollPane(selectedPlayerList);
		
		JButton deletePlayerButton = new JButton("Delete Player");
		deletePlayerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String data = "";
				if (playerList.getSelectedIndex() != -1) {
					int index = playerList.getSelectedIndex();
					data = "Player deleted: " + playerList.getSelectedValue();
					playerName.removeElementAt(index);
					selectedPlayerName.removeElementAt(index);
					statusLabel.setText(data);
				}
				statusLabel.setText(data);
			}
		});
		
		JButton showPlayerButton = new JButton("Show Selected Player");
		showPlayerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (playerList.getSelectedIndex() != -1) {
					selectedPlayerName.addElement(playerList.getSelectedValue());
				}
			}
		});
		
		JButton addPlayerButton = new JButton("Add Player");
		addPlayerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String title = JOptionPane.showInputDialog(null, "Enter player name:");
				System.out.println("Title is:" + title);
				playerName.addElement(title);
			}
		});
		
		JButton showSelectedCartelButton = new JButton("Show Selected Cartel");
		showSelectedCartelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cartelList.getSelectedIndex() != -1) {
					playerName.addElement(cartelList.getSelectedValue());
				}
			}
		});

		JButton addCartelButton = new JButton("Add Cartel");
		addCartelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String title = JOptionPane.showInputDialog(null, "Enter cartel name:");
				System.out.println("Title is:" + title);
				cartelName.addElement(title);
			}
		});
		
		JButton deleteCartelButton = new JButton("Delete Cartel");
		deleteCartelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String data = "";
				if (cartelList.getSelectedIndex() != -1) {
					int index = cartelList.getSelectedIndex();
					data = "Cartel deleted: " + cartelList.getSelectedValue();
					cartelName.removeElementAt(index);
					statusLabel.setText(data);
				}
				statusLabel.setText(data);
			}
		});

		controlPanel.add(ListScrollPane);
		controlPanel.add(addCartelButton);
		controlPanel.add(showSelectedCartelButton);
		controlPanel.add(deleteCartelButton);
		controlPanel.add(ListPlayerScrollPane);
		controlPanel.add(addPlayerButton);
		controlPanel.add(showPlayerButton);
		controlPanel.add(ListSelectedPlayerScrollPane);
		controlPanel.add(deletePlayerButton);
		
		mainFrame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
	}
}
