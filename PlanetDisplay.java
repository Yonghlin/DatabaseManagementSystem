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
    mainFrame.setSize(400, 400);
    mainFrame.setLayout(new GridLayout(3, 1));

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
    stmt.execute(planets);
    ResultSet set = stmt.getResultSet();
    while (set.next()) {
      planetName.addElement(set.getString("PlanetOwner_ID"));
    }

    final JList<String> planetList = new JList<String>(planetName);
    planetList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    planetList.setSelectedIndex(1);
    planetList.setVisibleRowCount(6);
    planetList.setPreferredSize(new Dimension(100, 200));

    String data = (planetList.getModel()).getElementAt(0);
    planetProcedure.setString(1, data);
    planetProcedure.execute();
    set = planetProcedure.getResultSet();

    JScrollPane ListScrollPane = new JScrollPane(planetList);

    /*
     * EDIT
     * BUTTON********************************************************************
     */

    JButton editButton = new JButton("Edit");

    editButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String data = "";
        if (planetList.getSelectedIndex() != -1) {
          data = "Planet Selected: " + planetList.getSelectedValue();
          statusLabel.setText(data);

          JFrame sideFrame = new JFrame(planetList.getSelectedValue());
          sideFrame.setSize(400, 400);
          sideFrame.setLayout(new GridLayout(3, 1));

          final DefaultListModel<String> planetID = new DefaultListModel<String>();

          planetID.addElement("123456");
          planetID.addElement("420420");

          final JList<String> IDList = new JList<String>(planetID);
          IDList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
          IDList.setSelectedIndex(1);
          IDList.setVisibleRowCount(6);
          IDList.setPreferredSize(new Dimension(100, 200));

          JScrollPane IDScrollPane = new JScrollPane(IDList);

          JButton deleteButton = new JButton("delete");

          deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              String data = "";
              if (IDList.getSelectedIndex() != -1) {
                int index = IDList.getSelectedIndex();
                data = "Planet Deleted: " + IDList.getSelectedValue();
                planetID.removeElementAt(index);
                statusLabel.setText(data);
              }
            }
          });

          sideFrame.add(IDScrollPane);
          sideFrame.add(deleteButton);
          sideFrame.setLayout(new FlowLayout());
          sideFrame.setVisible(true);
        }
      }
    });
    /*
     * ADD
     * BUTTON********************************************************************
     */
    JButton addButton = new JButton("add");

    addButton.addActionListener(new ActionListener() {
      String data = "";

      public void actionPerformed(ActionEvent e) {
        String title = JOptionPane.showInputDialog(null, "Enter planet name:");
        planetName.addElement(title);
        data = "Planet Added: " + title;
        statusLabel.setText(data);
      }
    });

    /*
     * DELETE
     * BUTTON***********************************************************************
     * *******
     */
    JButton deleteButton = new JButton("delete");

    deleteButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String data = "";
        if (planetList.getSelectedIndex() != -1) {
          int index = planetList.getSelectedIndex();
          data = "Planet Deleted: " + planetList.getSelectedValue();
          planetName.removeElementAt(index);
          statusLabel.setText(data);
        }
      }
    });

    controlPanel.add(ListScrollPane);
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
