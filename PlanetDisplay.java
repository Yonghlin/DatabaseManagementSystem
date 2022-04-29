import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;
import javax.swing.*;

public class PlanetDisplay implements ActionListener {
  private JFrame mainFrame;
  private JLabel headerLabel;
  private JLabel statusLabel;
  private JPanel controlPanel;

  /**
   * You MUST change these values based on the DB you are assigned to work with.
   */
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

  public PlanetDisplay() throws SQLException {
    mainFrame = new JFrame("Planet Display");
    mainFrame.setSize(1000, 600);
    mainFrame.setLayout(new GridLayout(3, 1));

    mainFrame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent windowEvent) {
        System.exit(0);
      }
    });

    headerLabel = new JLabel("", JLabel.CENTER);
    statusLabel = new JLabel("", JLabel.CENTER);
    statusLabel.setSize(1000, 1000);

    controlPanel = new JPanel();
    controlPanel.setLayout(new FlowLayout());

    mainFrame.add(headerLabel);
    mainFrame.add(controlPanel);
    mainFrame.add(statusLabel);
    mainFrame.setVisible(true);
  }

  public static void main(String args[]) throws SQLException {
    PlanetDisplay database = new PlanetDisplay();
    database.runDemo();
  }

  private void runDemo() throws SQLException {
    headerLabel.setText("Planet List");
    final DefaultListModel<String> planetName = new DefaultListModel<String>();
    Statement stmt = m_dbConn.createStatement();

    String sqlPlanet = "CALL planet_info(?)";
    CallableStatement planetProcedure = m_dbConn.prepareCall(sqlPlanet);

    String planets = new String("SELECT * FROM PLANET");
    planetProcedure.execute(planets);
    ResultSet set = planetProcedure.getResultSet();
    while (set.next()) {
      planetName.addElement(set.getString("Planet_ID"));
    }

    final JList<String> planetList = new JList<String>(planetName);
    planetList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    planetList.setSelectedIndex(1);
    planetList.setVisibleRowCount(6);
    planetList.setPreferredSize(new Dimension(100, 200));

    JScrollPane ListScrollPane = new JScrollPane(planetList);

    /*
     * SHOW********************************************************************
     */
    JButton showButton = new JButton("Show Attributes");

    showButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String data = "";

        if (planetList.getSelectedIndex() != -1) {

          try {
            Statement stmt = m_dbConn.createStatement();

            String view = new String(
                "SELECT * FROM PLANET WHERE Planet_ID = " + "'" + planetList.getSelectedValue() + "'");
            stmt.execute(view);
            ResultSet set = stmt.getResultSet();

            String planetID = "";
            String Star = "";
            String Resource = "";
            String PlanetOwner = "";
            String NumBuilding = "";

            while (set.next()) {
              planetID = set.getString("Planet_ID");
              Star = set.getString("Star_System");
              Resource = set.getString("Resources");
              PlanetOwner = set.getString("PlanetOwner_ID");
              NumBuilding = set.getString("Num_Of_Buildings");
            }

            data = "Planet_ID: " + planetID + ", StarSystem: " + Star + ", Resources: " + Resource
                + ", PlanetOwner_ID: " + PlanetOwner + ", Number of Buildings: " + NumBuilding;
            headerLabel.setText("Showing Planet " + planetList.getSelectedValue() + " attributes");
          } catch (SQLException ex) {
            ex.printStackTrace();
          }
          statusLabel.setText(data);

        }

        statusLabel.setText(data);

      }
    });

    /*
     * EDIT
     * BUTTON********************************************************************
     */

    JButton editButton = new JButton("Edit Planet");

    editButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String data = "";
        if (planetList.getSelectedIndex() != -1) {
          data = "Planet Selected: " + planetList.getSelectedValue();
          statusLabel.setText(data);

          JFrame editFrame = new JFrame("Edit " + planetList.getSelectedValue());

          String[] attributes = { "Planet_ID", "Star_System", "Resources", "PlanetOwner_ID", "Num_Of_Buildings" };
          JComboBox editButton = new JComboBox(attributes);

          JLabel changeValue = new JLabel("Choose attribute to change ");

          editFrame.add(changeValue);
          editFrame.add(editButton);
          editFrame.setSize(400, 400);
          editFrame.setLayout(new GridLayout(3, 1));
          editFrame.setVisible(true);

          editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              String data = "";
              if (editButton.getSelectedIndex() != -1) {
                String title = JOptionPane.showInputDialog(null,
                    "Enter new value for " + editButton.getSelectedItem().toString() + ": ");

                // SQL statement to change value to new value
                try {
                  Statement stmt = m_dbConn.createStatement();

                  String updateValue = "UPDATE PLANET SET " + editButton.getSelectedItem().toString() + "=" + "'"
                      + title + "'" + "WHERE Planet_ID =" + "'" + planetList.getSelectedValue() + "'";
                  stmt.execute(updateValue);
                  stmt.close();

                  statusLabel.setText(data);
                } catch (SQLException ex) {
                  ex.printStackTrace();
                }

                data = ("The new value for " + editButton.getSelectedItem().toString() + " is " + title + " for Planet "
                    + planetList.getSelectedValue());
                statusLabel.setText(data);
              }

            }
          });
        }
      }
    });
    /*
     * ADD
     * BUTTON********************************************************************
     */
    JButton addButton = new JButton("Add new Planet info");

    addButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String Planet_ID = JOptionPane.showInputDialog(null, "Enter Planet ID (4 digits):");
        String Star_System = JOptionPane.showInputDialog(null, "Enter Star System (1 digit):");
        String Resources = JOptionPane.showInputDialog(null, "Enter Resource:");
        String PlanetOwner_ID = JOptionPane.showInputDialog(null, "Enter Planet Owner ID (9 chars):");
        String Number_Of_Buildings = JOptionPane.showInputDialog(null, "Enter Number of Buildings (2 digits):");

        planetName.addElement(Planet_ID);
        String data = "Planet Added: " + Planet_ID;
        statusLabel.setText(data);

        try {
          Statement stmt = m_dbConn.createStatement();
          String add = new String("insert INTO PLANET" + "(Planet_ID, Star_System, Resources, PlanetOwner_ID, "
              + "Num_Of_Buildings) VALUES ('" + Planet_ID + "','" + Star_System + "','" + Resources + "','"
              + PlanetOwner_ID + "','" + Number_Of_Buildings + "')");
          stmt.executeUpdate(add);
          stmt.close();
        } catch (SQLException ex) {
          ex.printStackTrace();
        }

      }
    });

    /*
     * DELETE
     * BUTTON***********************************************************************
     * *******
     */
    JButton deleteButton = new JButton("Delete");

    deleteButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String data = "";
        if (planetList.getSelectedIndex() != -1) {

          int index = planetList.getSelectedIndex();
          data = "Planet Deleted: " + planetList.getSelectedValue();
          planetName.removeElementAt(index);
          statusLabel.setText(data);

          try {
            Statement stmt = m_dbConn.createStatement();
            
            
            String delete = "DELETE FROM PLANET WHERE Planet_ID=" + planetList.getSelectedValue();
            stmt.executeUpdate(delete);
            stmt.close();
          } catch (SQLException ex) {
            ex.printStackTrace();
          }

        }
      }
    });

    controlPanel.add(ListScrollPane);
    controlPanel.add(showButton);
    controlPanel.add(editButton);
    controlPanel.add(addButton);
    controlPanel.add(deleteButton);

    mainFrame.setVisible(true);

  }

  // ******************************************************************************

  @Override
  public void actionPerformed(ActionEvent e) {
    // TODO Auto-generated method stub

  }
}
