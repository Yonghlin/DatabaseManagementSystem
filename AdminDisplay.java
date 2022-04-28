import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import java.sql.*;


public class AdminDisplay implements ActionListener {
  private JFrame mainFrame;
  private JLabel headerLabel;
  private JLabel statusLabel;
  private JPanel controlPanel;

  /**
   * 
   * 
  public static final String DB_LOCATION = "jdbc:mysql://db.engr.ship.edu:3306/csc471_12?useTimezone=true&serverTimezone=UTC";
  public static final String LOGIN_NAME = "csc471_12";
  public static final String PASSWORD = "Password_12";
  protected static Connection m_dbConn = null;

  /**
   * Creates a connection to the database that you can then send commands to.
   
  static {
      try {
          m_dbConn = DriverManager.getConnection(DB_LOCATION, LOGIN_NAME, PASSWORD);
      } catch (SQLException e) {
          e.printStackTrace();
      }
  }

  /**
   * To get the meta data for the DB.
   
  DatabaseMetaData meta = m_dbConn.getMetaData();
  */
  public AdminDisplay() throws SQLException{
    mainFrame = new JFrame("Admin Display");
    mainFrame.setSize(400, 400);
    mainFrame.setLayout(new GridLayout(3,1));
    
    mainFrame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent windowEvent) {
        System.exit(0);
      }
    });
    
    headerLabel = new JLabel("", JLabel.CENTER);
    statusLabel = new JLabel("", JLabel.CENTER);
    statusLabel.setSize(350, 100);

    controlPanel = new JPanel();
    controlPanel.setLayout(new FlowLayout());

    mainFrame.add(headerLabel);
    mainFrame.add(controlPanel);
    mainFrame.add(statusLabel);
    mainFrame.setVisible(true);
  }

  public static void main(String args[]) throws SQLException {
    AdminDisplay database = new AdminDisplay();
    database.runDemo();
  }

  private void runDemo() {
    //Statement stmt = m_dbConn.createStatement();
      
    headerLabel.setText("Control in action: JList");
    final DefaultListModel<String> playerName = new DefaultListModel<String>();

    /* Gets all the player names from DB and adds them to playerName list
    String names = new String("SELECT PLAYER_NAME FROM PLAYER");
    //stmt.execute(names);
    //ResultSet set = stmt.getResultSet();
    //while(set.next()) {
	//fleetID.addElement(set.getString("Player_Name"));
    }*/

    playerName.addElement("John Deer");
    playerName.addElement("Joe Well");

    final JList<String> playerList = new JList<String>(playerName);
    playerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    playerList.setSelectedIndex(1);
    playerList.setVisibleRowCount(6);
    playerList.setPreferredSize(new Dimension(100, 200));

    JScrollPane ListScrollPane = new JScrollPane(playerList);

    //Show button will show a SQL view of attribues and there current values
    JButton showButton = new JButton("Show");

    showButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String data = "";
        
        if (playerList.getSelectedIndex() != -1) {
          //Statement stmt = m_dbConn.createStatement();
          String view = new String (playerList.getSelectedValue() + ", 100, 10000, 12343, Right");
          //String view = new String ("SELECT Player_Name, Money, Resources, PlCartel_ID, PlOrders FROM PLAYER WHERE Player_Name = " + playerList.getSelectedValue());
          //stmt.execute(view);
          //ResultSet set = stmt.getResultSet();
          data = "Player " + playerList.getSelectedValue() + " Attributes: " + view; //do i put view here or set
          headerLabel.setText("Showing Player " + playerList.getSelectedValue() + " attributes");

          statusLabel.setText(data);
 
        } 
        
        statusLabel.setText(data);
      }
    });
/*
 * ADD BUTTON******************************************************************** 99,999,999.99
 */
    JButton addButton = new JButton("Add Player");

    addButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String currentPlayerName = JOptionPane.showInputDialog(null, "Enter Player name (15 chars): ");
        String currentPlayerMoney = JOptionPane.showInputDialog(null, "Enter Player current Money (Max 99,999,999.99): ");
        String currentPlayerResources = JOptionPane.showInputDialog(null, "Enter Player current number of Resources: ");
        String currentPlayerCratelID = JOptionPane.showInputDialog(null, "Enter Player CartelID number (4 digits): ");
        String currentPlayerOrders = JOptionPane.showInputDialog(null, "Enter Player Orders (25 chars): ");

        playerName.addElement(currentPlayerName);
        headerLabel.setText("Player added: " + currentPlayerName);
        
        /*
         Try {
          	Statement stmt = m_dbConn.createStatement();
          	String add = "INSERT INTO PLAYER" + 
         		"(PLAYER_NAME, Money, Resources, PlCartelID, PlOrders) VALUES ('" + currentPlayerName + "','" + 
         		currentPlayerMoney + "','" + currentPlayerResources + "','" + currentPlayerCratelID + "','" + currentPlayerOrders + "')");	
         
         } catch (SQLException ex) {
             ex.printStackTrace();
         }*/
        
      }
    });
  
/*
 * DELETE BUTTON******************************************************************************
 */
    JButton deleteButton = new JButton("Delete Player");

    deleteButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String data = "";
        if (playerList.getSelectedIndex() != -1) {
          int index = playerList.getSelectedIndex();
          data = "Player Deleted: " + playerList.getSelectedValue();
          headerLabel.setText("Player " + playerList.getSelectedValue() + " deleted");
          playerName.removeElementAt(index);
          
          statusLabel.setText(data);
          
          /*try {
          //    Statement stmt = m_dbConn.createStatement();
              	String delete = "DELETE FROM PLAYER WHERE Player_Name = " + playerList.getSelectedIndex();
              	stmt.execute(delete);
              	stmt.close();	             
          } catch (SQLException ex) {
           	ex.printStackTrace();
          }*/
        }

        statusLabel.setText(data);
      }
    });
    
    controlPanel.add(ListScrollPane);
    controlPanel.add(showButton);
    controlPanel.add(addButton);
    controlPanel.add(deleteButton);
    
    mainFrame.setVisible(true);
    
/*
 * EDIT BUTTON
 */   
    
    String[] attributes = {"Player_Name", "Money", "Resources", "Player_Cartel_ID", "Player_Orders"}; //will be SQL to see current players attributes
    JComboBox editButton = new JComboBox(attributes);
    /* Gets all the player names from DB and adds them to playerName list
    String names = new String("SELECT PLAYER_NAME FROM PLAYER");
    //stmt.execute(names);
    //ResultSet set = stmt.getResultSet();
    //while(set.next()) {
	//playerList.addElement(set.getString("Player_Name"));
    }*/
    
    
    JButton editPlayer = new JButton("Edit Player ");
    editPlayer.addActionListener(new ActionListener() {
	public void actionPerformed(ActionEvent e) {
	    String data = "";
	    
	    if (playerList.getSelectedIndex() != -1) {
		String playerName = playerList.getSelectedValue();
		data = ("Player " + playerName + " edited");
			
			JLabel changeValue = new JLabel("Choose attribute to change ");
			controlPanel.add(changeValue);
			
			editButton.setSelectedIndex(0);
			editButton.addActionListener(new ActionListener() {
		        public void actionPerformed(ActionEvent e) {
		              String data = "";
		                  if(editButton.getSelectedIndex() != -1) {
		                    String title = JOptionPane.showInputDialog(null, "Enter new value for "+editButton.getSelectedItem().toString() +": ");
		                    
		                    // SQL statement to change value to new value
		                    /*
		                     * try {
		                     * 	   Statement stmt = m_dbConn.createStatement();
		                     * 	   
		                     *     String updateValue = "UPDATE PLAYER SET " + editButton.getSelectedValue() + "'='" + title + "WHERE " 
		                     * 
		                     */
		                     data = ("New "+ editButton.getSelectedItem().toString() +" value is " + title + " for player " + playerName);
		                     statusLabel.setText(data);
		          	  }
		            
		        }
		    });
		  controlPanel.add(editButton);
	          statusLabel.setText(data);

	    }
	    
	    statusLabel.setText(data);

	}
    });

    

    controlPanel.add(editPlayer);
    

  }
  
 //******************************************************************************

  
  
  @Override
  public void actionPerformed(ActionEvent e) {
    // TODO Auto-generated method stub

  }


}
