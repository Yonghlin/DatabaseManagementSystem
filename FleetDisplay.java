import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;
import javax.swing.*;

public class FleetDisplay implements ActionListener {
    private JFrame mainFrame;
    private JPanel fleetsTable;
    private JPanel shipsTable;
    private JLabel fleetsLabel;
    private JLabel shipsLabel;
    private JLabel statusLabel;
    private JPanel controlPanel;
    private JPanel fleetButtons;
    private JPanel shipButtons;

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

    public FleetDisplay() throws SQLException {
        mainFrame = new JFrame("Fleets");
        mainFrame.setSize(800, 400);
        mainFrame.setLayout(new GridLayout(3, 1));

        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });

        fleetsLabel = new JLabel("Fleets", JLabel.CENTER);
        shipsLabel = new JLabel("Ships", JLabel.CENTER);
        statusLabel = new JLabel("", JLabel.CENTER);
        statusLabel.setSize(350, 100);

        controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());

        fleetButtons = new JPanel();
        fleetButtons.setLayout(new BoxLayout(fleetButtons, BoxLayout.PAGE_AXIS));
        shipButtons = new JPanel();
        shipButtons.setLayout(new BoxLayout(shipButtons, BoxLayout.PAGE_AXIS));

        fleetsTable = new JPanel();
        fleetsTable.setLayout(new BoxLayout(fleetsTable, BoxLayout.PAGE_AXIS));
        fleetsTable.add(fleetsLabel);

        shipsTable = new JPanel();
        shipsTable.setLayout(new BoxLayout(shipsTable, BoxLayout.PAGE_AXIS));
        shipsTable.add(shipsLabel);

        mainFrame.add(controlPanel);
        mainFrame.add(statusLabel);
        mainFrame.setVisible(true);
    }

    public static void main(String args[]) throws SQLException {
        FleetDisplay database = new FleetDisplay();
        database.runDemo();
    }

    private void runDemo() throws SQLException {
        Statement stmt = m_dbConn.createStatement();
        //My Two Lists for Fleets and Ships
        final DefaultListModel<String> fleetID = new DefaultListModel<String>();
        final DefaultListModel<String> shipID = new DefaultListModel<String>();
        //Stored Procedure for Getting the Ships in Selected Fleets
        String shipGetter = "CALL getShipsInFleet(?)";
        CallableStatement shipProcedure = m_dbConn.prepareCall(shipGetter);

        //Gets all of the Fleets from the Database and Adds them to the FleetID List
        String fleets = new String("SELECT * FROM FLEET");
        stmt.execute(fleets);
        ResultSet set = stmt.getResultSet();
        while(set.next()) {
            fleetID.addElement(set.getString("Fleet_ID"));
        }

        //Constructs the Fleet List to be Displayed out of the FleetID List
        final JList<String> fleetList = new JList<String>(fleetID);
        fleetList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fleetList.setSelectedIndex(1);
        fleetList.setVisibleRowCount(6);
        fleetList.setPreferredSize(new Dimension(100, 200));

        //Executes Stored Procedure to Get The Ships from the First Fleet In List
        String data = (fleetList.getModel()).getElementAt(0);
        shipProcedure.setString(1, data);
        shipProcedure.execute();
        set = shipProcedure.getResultSet();

        //Adds Ships to Ship ID List
        while(set.next()) {
            shipID.addElement(set.getString("Ship_ID"));
        }
        statusLabel.setText("Fleet Selected: " + data);

        //Constructs the Fleet List to be Displayed out of the FleetID List
        final JList<String> shipList = new JList<String>(shipID);
        shipList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        shipList.setSelectedIndex(1);
        shipList.setVisibleRowCount(6);
        shipList.setPreferredSize(new Dimension(100, 200));

        //Constructs Panes out of Lists
        JScrollPane fleetListScrollPane = new JScrollPane(fleetList);
        JScrollPane shipListScrollPane = new JScrollPane(shipList);
        stmt.close();

        /*
         * SELECT FLEET BUTTON********************************************************************
         */
        JButton selectFleet = new JButton("Select Fleet ");

        selectFleet.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String data = "";

                //Makes sure that user has selected a Fleet
                if (fleetList.getSelectedIndex() != -1) {
                    data = fleetList.getSelectedValue();
                    statusLabel.setText("Fleet Selected: " + data);
                }

                //Executes Stored Procedure to Get The Ships from the Selected Fleet
                //!!!EDIT TO ALSO GET FLEET INFO SOMEWHERE
                try {
                    shipProcedure.setString(1, data);
                    shipProcedure.execute();
                    ResultSet set = shipProcedure.getResultSet();
                    shipID.clear();

                    while(set.next()) {
                        shipID.addElement(set.getString("Ship_ID"));
                    }
                    set.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        /*
         * ADD FLEET BUTTON********************************************************************
         */
        JButton addFleet = new JButton("  Add Fleet   ");

        addFleet.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String data = JOptionPane.showInputDialog(null, "Enter Fleet ID:");
                System.out.println("ID is:" + data);
                fleetID.addElement(data);
                statusLabel.setText("Fleet Added: " + data);

                try {
                    Statement stmt = m_dbConn.createStatement();
                    String add = "INSERT INTO FLEET" +
                            "(Fleet_ID, Order_Num) VALUES ('" + data
                            + "','" + 0 + "')";
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        /*
         * DELETE FLEET BUTTON******************************************************************************
         */
        JButton deleteFleet = new JButton("Delete Fleet");

        deleteFleet.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String data = "";
                if (fleetList.getSelectedIndex() != -1) {
                    int index = fleetList.getSelectedIndex();
                    data = fleetList.getSelectedValue();
                    fleetID.removeElementAt(index);
                    statusLabel.setText("Fleet Deleted: " + data);

                    try {
                        Statement stmt = m_dbConn.createStatement();

                        for(int i = 0; !shipID.isEmpty(); i++) {
                            String delete = "DELETE FROM SHIP WHERE Ship_ID=" + shipID.getElementAt(i);
                            shipID.removeElementAt(0);
                            stmt.execute(delete);
                        }
                        shipID.clear();

                        String delete = "DELETE FROM FLEET WHERE Fleet_ID=" + data;
                        stmt.execute(delete);
                        stmt.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        /*
         * SELECT SHIP BUTTON********************************************************************
         */
        JButton selectShip = new JButton("Select Ship ");

        selectFleet.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String data = "";

                //Makes sure that user has selected a Fleet
                if (shipList.getSelectedIndex() != -1) {
                    data = shipList.getSelectedValue();
                    statusLabel.setText("Ship Selected: " + data);
                }

                //!!!CHANGE TO GET SHIP INFO
                /*try {
                    shipProcedure.setString(1, data);
                    shipProcedure.execute();
                    ResultSet set = shipProcedure.getResultSet();
                    shipID.clear();

                    while(set.next()) {
                        shipID.addElement(set.getString("Ship_ID"));
                    }
                    set.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }*/
            }
        });

        /*
         * ADD SHIP BUTTON********************************************************************
         */
        JButton addShip = new JButton("  Add Ship    ");

        addShip.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String currentShipID = JOptionPane.showInputDialog(null, "Enter Ship ID:");
                String currentShipType = JOptionPane.showInputDialog(null, "Enter Ship Type:");
                String currentShipLoc = JOptionPane.showInputDialog(null, "Enter Ship Location:");
                shipID.addElement(currentShipID);
                statusLabel.setText("Ship Added: " + currentShipID);
                int currentFleetID = 0;

                if (fleetList.getSelectedIndex() != -1) {
                    currentFleetID = Integer.parseInt(fleetList.getSelectedValue());
                }

                try {
                    Statement stmt = m_dbConn.createStatement();
                    String playerNameGetter = "SELECT FleetOwner_ID FROM FLEET WHERE Fleet_ID="
                                            + currentFleetID;
                    stmt.execute(playerNameGetter);
                    ResultSet set = stmt.getResultSet();
                    String playerName = set.getString(0);
                    String add = "INSERT INTO SHIP" +
                            "(ShipOwner_ID, Resources, Ship_ID, Location, Type, " +
                            "Speed, Cargo_Tech, Hull_Tech, Weapons_Tech, " +
                            "Engine_Tech, Fl_ID) VALUES ('" + playerName + "','" + 0 +
                            "','" + currentShipID + "','" + currentShipType + "','" +
                            currentShipLoc + "','1','1','1','1','1','" + currentFleetID + "')";
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        /*
         * DELETE SHIP BUTTON******************************************************************************
         */
        JButton deleteShip = new JButton("Delete Ship");

        deleteShip.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String data = "";
                if (shipList.getSelectedIndex() != -1) {
                    int index = shipList.getSelectedIndex();
                    data = shipList.getSelectedValue();
                    shipID.removeElementAt(index);
                    statusLabel.setText("Ship Deleted: " + data);

                    try {
                        Statement stmt = m_dbConn.createStatement();
                        String delete = "DELETE FROM SHIP WHERE Ship_ID=" + shipList.getSelectedValue();
                        stmt.execute(delete);
                        stmt.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        fleetsTable.add(fleetListScrollPane);
        controlPanel.add(fleetsTable);
        fleetButtons.add(selectFleet);
        fleetButtons.add(addFleet);
        fleetButtons.add(deleteFleet);
        controlPanel.add(fleetButtons);

        shipsTable.add(shipListScrollPane);
        shipButtons.add(selectShip);
        shipButtons.add(addShip);
        shipButtons.add(deleteShip);
        controlPanel.add(shipsTable);
        controlPanel.add(shipButtons);

        mainFrame.setVisible(true);
    }

    //******************************************************************************

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub

    }
}
