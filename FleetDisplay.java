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
    private JLabel fleetStatusLabel;
    private JLabel shipStatusLabel;
    private JLabel detailsLabel;
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
        //Main Display Object
        mainFrame = new JFrame("Fleets");
        mainFrame.setSize(800, 400);
        mainFrame.setLayout(new GridLayout(3, 1));

        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });

        //All of the Labels and Text
        fleetsLabel = new JLabel("Fleets", JLabel.CENTER);
        shipsLabel = new JLabel("Ships", JLabel.CENTER);
        fleetStatusLabel = new JLabel("", JLabel.CENTER);
        fleetStatusLabel.setSize(350, 200);
        shipStatusLabel = new JLabel("", JLabel.CENTER);
        shipStatusLabel.setSize(350, 200);
        detailsLabel = new JLabel("", JLabel.CENTER);
        detailsLabel.setSize(100, 150);

        //Minor Panel for Lists and Buttons
        controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());

        //Button Panels That Stack the Buttons on top of each other
        fleetButtons = new JPanel();
        fleetButtons.setLayout(new BoxLayout(fleetButtons, BoxLayout.PAGE_AXIS));
        shipButtons = new JPanel();
        shipButtons.setLayout(new BoxLayout(shipButtons, BoxLayout.PAGE_AXIS));

        //Lefthand Fleet Section that stacks the label and the list
        fleetsTable = new JPanel();
        fleetsTable.setLayout(new BoxLayout(fleetsTable, BoxLayout.PAGE_AXIS));
        fleetsTable.add(fleetsLabel);

        //Righthand Fleet Section that stacks the label and the list
        shipsTable = new JPanel();
        shipsTable.setLayout(new BoxLayout(shipsTable, BoxLayout.PAGE_AXIS));
        shipsTable.add(shipsLabel);

        mainFrame.add(controlPanel);
        mainFrame.add(detailsLabel);
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

        //Asks for Player Name to only get Fleets that they own
        String newPlayerID = JOptionPane.showInputDialog(null, "Enter Player Name:");

        //Gets all of the Fleets from the Database and Adds them to the FleetID List
        String fleets = new String("SELECT * FROM FLEET WHERE FleetOwner_ID='" + newPlayerID) +"'";
        stmt.execute(fleets);
        ResultSet set = stmt.getResultSet();
        while(set.next()) {
            fleetID.addElement(set.getString("Fleet_ID"));
        }
        //Prints out Information on Selected Fleet
        String firstFleet = fleetID.getElementAt(0);
        fleetStatusLabel.setText("Fleet Selected: " + firstFleet);

        //Constructs the Fleet List to be Displayed out of the FleetID List
        final JList<String> fleetList = new JList<String>(fleetID);
        fleetList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fleetList.setSelectedIndex(1);
        fleetList.setVisibleRowCount(6);
        fleetList.setPreferredSize(new Dimension(100, 200));

        //Executes Stored Procedure to Get The Ships from the First Fleet In List
        shipProcedure.setString(1, firstFleet);
        shipProcedure.execute();
        set = shipProcedure.getResultSet();

        //Adds Ships to Ship ID List
        while(set.next()) {
            shipID.addElement(set.getString("Ship_ID"));
        }

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
                    fleetStatusLabel.setText("Fleet Selected: " + data);
                    shipStatusLabel.setText("");
                    detailsLabel.setText("");
                }

                //Executes Stored Procedure to Get The Ships from the Selected Fleet
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
                //Adds to list and displays confirmation message
                String newFleetID = JOptionPane.showInputDialog(null, "Enter Fleet ID:");
                fleetID.addElement(newFleetID);
                fleetStatusLabel.setText("Fleet Added: " + newFleetID);

                try {
                    //Executes SQL Statement to Get OrderID for making a new Fleet
                    Statement stmt = m_dbConn.createStatement();
                    String getOrderID = "SELECT FleetOrder_ID FROM FLEET_ORDERS";
                    stmt.execute(getOrderID);
                    ResultSet set = stmt.getResultSet();
                    set.next();
                    String orderID = set.getString(1);

                    //Executes SQL Statement to Add to Database
                    String add = "INSERT INTO FLEET" +
                            "(Fleet_ID, Order_Num, FleetOwner_ID) VALUES ('"
                            + newFleetID + "','" + orderID + "','" + newPlayerID +"')";
                    stmt.execute(add);
                    stmt.close();
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
                String currentFleetID = "";
                if (fleetList.getSelectedIndex() != -1) {
                    int index = fleetList.getSelectedIndex();
                    currentFleetID = fleetList.getSelectedValue();
                    fleetID.removeElementAt(index);
                    fleetStatusLabel.setText("Fleet Deleted: " + currentFleetID);

                    try {
                        Statement stmt = m_dbConn.createStatement();

                        //Removes and Deletes All Ships From Fleet Before Deleting the Fleet
                        String delete = "DELETE FROM SHIP WHERE Fl_ID='" + currentFleetID + "'";
                        stmt.execute(delete);
                        shipID.clear();

                        //Executes SQL Statement to Delete The Fleet
                        delete = "DELETE FROM FLEET WHERE Fleet_ID='" + currentFleetID + "'";
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

        selectShip.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Variables to help properly fill New Ship Entry
                String selectedShipID = "";
                String resources = "";
                String location = "";
                String type = "";
                String speed = "";
                String cargo = "";
                String hull = "";
                String weapons = "";
                String engine = "";

                //Makes sure that user has selected a Ship
                if (shipList.getSelectedIndex() != -1) {
                    selectedShipID = shipList.getSelectedValue();
                    shipStatusLabel.setText("Ship Selected: " + selectedShipID);

                    try {
                        //Gets all of the Ship Info from Database and sets it to variables
                        String shipInfo = "SELECT * FROM SHIP WHERE Ship_ID='" + selectedShipID + "'";
                        Statement stmt = m_dbConn.createStatement();
                        stmt.execute(shipInfo);
                        ResultSet set = stmt.getResultSet();
                        set.next();

                        resources = set.getString("Resources");
                        location = set.getString("Location");
                        type = set.getString("Type");
                        speed = set.getString("Speed");
                        cargo = set.getString("Cargo_Tech");
                        hull = set.getString("Hull_Tech");
                        weapons = set.getString("Weapons_Tech");
                        engine = set.getString("Engine_Tech");
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }

                    //Presents Information on the Ship to the User
                    detailsLabel.setText("Resources: " + resources + ", Location: " + location
                            + ", Type: " + type + ",\n Speed: " + speed + ", Cargo: " + cargo +
                            ", Hull: " + hull + ",\n Weapons: " + weapons + ", Engine: " + engine);
                }
            }
        });

        /*
         * ADD SHIP BUTTON********************************************************************
         */
        JButton addShip = new JButton("  Add Ship   ");

        addShip.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //Allows user to enter new Ship Information
                String currentShipID = JOptionPane.showInputDialog(null, "Enter Ship ID:");
                String currentShipType = JOptionPane.showInputDialog(null, "Enter Ship Type:");
                String currentShipLoc = JOptionPane.showInputDialog(null, "Enter Ship Location:");
                shipID.addElement(currentShipID);
                shipStatusLabel.setText("Ship Added: " + currentShipID);
                int currentFleetID = 0;

                if (fleetList.getSelectedIndex() != -1) {
                    currentFleetID = Integer.parseInt(fleetList.getSelectedValue());
                }

                //Executes SQL Statement To Add a New Ship
                try {
                    Statement stmt = m_dbConn.createStatement();
                    String add = "INSERT INTO SHIP" +
                            "(ShipOwner_ID, Resources, Ship_ID, Location, Type, " +
                            "Speed, Cargo_Tech, Hull_Tech, Weapons_Tech, " +
                            "Engine_Tech, Fl_ID) VALUES ('" + newPlayerID + "','" + 0 +
                            "','" + currentShipID + "','" + currentShipLoc + "','" +
                            currentShipType + "','1','1','1','1','1','" + currentFleetID + "')";
                    stmt.execute(add);
                    stmt.close();
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
                String currentShipID = "";
                if (shipList.getSelectedIndex() != -1) {
                    int index = shipList.getSelectedIndex();
                    currentShipID = shipList.getSelectedValue();
                    shipID.removeElementAt(index);
                    shipStatusLabel.setText("Ship Deleted: " + currentShipID);

                    try {
                        Statement stmt = m_dbConn.createStatement();
                        String delete = "DELETE FROM SHIP WHERE Ship_ID='" + currentShipID + "'";
                        stmt.execute(delete);
                        stmt.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        /*
         * MOVE SHIP BUTTON********************************************************************
         */
        JButton moveShip = new JButton("  Move Ship ");

        moveShip.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String currentShipID = "";
                String loc = JOptionPane.showInputDialog(null, "Enter New Location:");

                if (shipList.getSelectedIndex() != -1) {
                    currentShipID = shipList.getSelectedValue();
                }

                try {
                    Statement stmt = m_dbConn.createStatement();
                    String move = "UPDATE SHIP SET Location=('" +
                            loc + "') WHERE Ship_ID=" + currentShipID;
                    stmt.execute(move);
                    stmt.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        fleetsTable.add(fleetListScrollPane);
        fleetsTable.add(fleetStatusLabel);
        controlPanel.add(fleetsTable);
        fleetButtons.add(selectFleet);
        fleetButtons.add(addFleet);
        fleetButtons.add(deleteFleet);
        controlPanel.add(fleetButtons);

        shipsTable.add(shipListScrollPane);
        shipsTable.add(shipStatusLabel);
        shipButtons.add(selectShip);
        shipButtons.add(addShip);
        shipButtons.add(deleteShip);
        shipButtons.add(moveShip);
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
