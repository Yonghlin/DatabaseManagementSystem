import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
	
	public CartelDisplay() throws SQLException {
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
		CartelDisplay database;
		try {
			database = new CartelDisplay();
			database.runDemo();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void runDemo() throws SQLException {
		Statement stmt = m_dbConn.createStatement();
    
		final DefaultListModel<String> cartelName = new DefaultListModel<String>();
		final DefaultListModel<String> playerName = new DefaultListModel<String>();
		final DefaultListModel<String> messageBoardName = new DefaultListModel<String>();
		
		String cartelGetter = "CALL cartel_info(?)";
		@SuppressWarnings("unused")
		CallableStatement cartelProcedure = m_dbConn.prepareCall(cartelGetter);
		
	  String cartels = new String("SELECT * FROM CARTEL");
    stmt.execute(cartels);
    ResultSet setCartel = stmt.getResultSet();
    while(setCartel.next()) {
        cartelName.addElement(setCartel.getString("Cartel_ID"));
    }
		
		final JList<String> cartelList = new JList<String>(cartelName);
		cartelList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		cartelList.setSelectedIndex(1);
		cartelList.setVisibleRowCount(6);
		cartelList.setPreferredSize(new Dimension(100, 200));

		JScrollPane ListScrollPane = new JScrollPane(cartelList);
		
		final JList<String> playerList = new JList<String>(playerName);
		playerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		playerList.setSelectedIndex(1);
		playerList.setVisibleRowCount(6);
		playerList.setPreferredSize(new Dimension(100, 200));
		
		JScrollPane ListPlayerScrollPane = new JScrollPane(playerList);
		
		final JList<String> messagesList = new JList<String>(messageBoardName);
		messagesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		messagesList.setSelectedIndex(1);
		messagesList.setVisibleRowCount(6);
		messagesList.setPreferredSize(new Dimension(100, 200));
		
		JScrollPane ListSelectedPlayerScrollPane = new JScrollPane(messagesList);
		
		JButton deletePlayerButton = new JButton("Delete Player");
		deletePlayerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int currentPlayer = 0;
				if (playerList.getSelectedIndex() != -1) {
					currentPlayer = playerList.getSelectedIndex();
					String playerDeleted = "Player deleted: " + playerList.getSelectedValue();
					try {
						Statement stmt = m_dbConn.createStatement();
						String deletePlayer = "DELETE FROM PLAYER WHERE Player_Name='" + playerList.getSelectedValue() + "'";
						stmt.execute(deletePlayer);
						stmt.close();
					} catch (SQLException ex) {
						ex.printStackTrace();
					}
					playerName.removeElementAt(currentPlayer);
					messageBoardName.removeElementAt(currentPlayer);
					statusLabel.setText(playerDeleted);
				}
			}
		});
			   
		JButton sendMessagesButton = new JButton("Send Messages");
		sendMessagesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (playerList.getSelectedIndex() != -1) {
					String message = JOptionPane.showInputDialog(null, "Enter message:");
					System.out.println("Message sent:" + message);
					messageBoardName.addElement(message);
				}
			}
		});
		
		JButton addPlayerButton = new JButton("Add Player");
		addPlayerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String player_name = JOptionPane.showInputDialog(null, "Enter player name:");
				String money = JOptionPane.showInputDialog(null, "Enter money:");
				String resources = JOptionPane.showInputDialog(null, "Enter resources:");
				String player_cartel = JOptionPane.showInputDialog(null, "Enter cartel ID:");
				String orders = JOptionPane.showInputDialog(null, "Enter orders");
				System.out.println("Player added:" + player_name);
				playerName.addElement(player_name);
				
        try {
            Statement stmt = m_dbConn.createStatement();
            String addPlayer = "INSERT INTO PLAYER" +
                    "(Player_Name, Money, Resources, PlCartel_ID, PlOrders) " +
                    "VALUES ('" + player_name + "','" + money + "','" + resources + "','" +
                    player_cartel + "','" + orders + "')";
            stmt.execute(addPlayer);
            stmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
			}
		});
		
		JButton showSelectedCartelButton = new JButton("Show Selected Cartel");
		showSelectedCartelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cartelList.getSelectedIndex() != -1) {
					playerName.removeAllElements();
					String players = new String("SELECT * FROM PLAYER WHERE PlCartel_ID='" + cartelList.getSelectedValue()) + "'";
			    try {
						stmt.execute(players);
						ResultSet setPlayers = stmt.getResultSet();
				    while(setPlayers.next()) {
				        playerName.addElement(setPlayers.getString("Player_Name"));
				    }
				    } catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});

		JButton addCartelButton = new JButton("Add Cartel");
		addCartelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String cartel = JOptionPane.showInputDialog(null, "Enter cartel name:");
				String messageBoard = JOptionPane.showInputDialog(null, "Enter message board:");
				String numOfMembers = JOptionPane.showInputDialog(null, "Enter number of members:");
				System.out.println("Cartel added:" + cartel);
				cartelName.addElement(cartel);
				
        try {
            Statement stmt = m_dbConn.createStatement();
            String addCartel = "INSERT INTO CARTEL" +
                    "(Cartel_ID, Message_Board, Num_Of_Members) " +
                    "VALUES ('" + cartel + "','" + messageBoard + "','" + 
                    numOfMembers + "')";
            stmt.execute(addCartel);
            stmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
			}
		});
		
		JButton deleteCartelButton = new JButton("Delete Cartel");
		deleteCartelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String data = "";
				if (cartelList.getSelectedIndex() != -1) {
					int currentCartel = cartelList.getSelectedIndex();
					data = "Cartel deleted: " + cartelList.getSelectedValue();
					try {
						Statement stmt = m_dbConn.createStatement();
						String deleteCartel = "DELETE FROM CARTEL WHERE Cartel_ID='" + cartelList.getSelectedValue() + "'";
						stmt.execute(deleteCartel);
						stmt.close();
					} catch (SQLException ex) {
						ex.printStackTrace();
					}
					cartelName.removeElementAt(currentCartel);
					statusLabel.setText(data);
				}
			}
		});

		controlPanel.add(ListScrollPane);
		controlPanel.add(addCartelButton);
		controlPanel.add(showSelectedCartelButton);
		controlPanel.add(deleteCartelButton);
		controlPanel.add(ListPlayerScrollPane);
		controlPanel.add(addPlayerButton);
		controlPanel.add(deletePlayerButton);
		controlPanel.add(ListSelectedPlayerScrollPane);
		controlPanel.add(sendMessagesButton);
		
		mainFrame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
	}
}
