import java.sql.*;
import java.util.Scanner;

public class BuildTable {
  public BuildTable() throws SQLException {
  }

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

  public static void main(String[] args) throws SQLException {
    //Declare Variables
    Scanner scanner = new Scanner(System.in);
    int input = 0;
    activateJDBC();

    System.out.println("Would you like to create the tables (1) or drop the tables (0)?");
    System.out.println("Enter -1 to quit");
    input = scanner.nextInt();

    //Allows for user to try multiple times, specifically if they entered the wrong input
    while(input >= 0) {
      if (input == 0) {
        dropTables();
        System.out.println("Dropped");
      }
      else if (input == 1) {
        createTables();
        System.out.println("Created");
      }
      else {
        System.out.println("Invalid Input");
      }

      System.out.println("Would you like to create the tables (1) or drop the tables (0)?");
      System.out.println("Enter -1 to quit");
      input = scanner.nextInt();
    }
  }


  //Creates the tables for the entire database. Must be in order.
  public static void createTables() throws SQLException {
    Statement stmt = m_dbConn.createStatement();

    String createTable = new String("CREATE TABLE CARTEL" +
            "(Cartel_ID int NOT NULL, Message_Board varchar(1000), " +
            "Num_Of_Members int NOT NULL, PRIMARY KEY(Cartel_ID))");
    stmt.executeUpdate(createTable);

    createTable = new String("CREATE TABLE ADMINISTRATOR" +
            "(Admin_ID int NOT NULL, PRIMARY KEY(Admin_ID))");
    stmt.executeUpdate(createTable);

    createTable = new String("CREATE TABLE PLAYER" +
            "(Player_Name varchar(15) NOT NULL, Money Decimal(10, 2) NOT NULL, " +
            "Resources int NOT NULL, PlCartel_ID int NOT NULL, PlOrders varchar(25), " +
            "PRIMARY KEY(Player_Name), UNIQUE(PlOrders), " +
            "FOREIGN KEY(PlCartel_ID) REFERENCES CARTEL(Cartel_ID))");
    stmt.executeUpdate(createTable);

    createTable = new String("CREATE TABLE OVERSEEN_BY" +
            "(A_ID int NOT NULL, Play_ID varchar(15) NOT NULL, " +
            "Game varchar(15) NOT NULL, PRIMARY KEY(A_ID, Play_ID), " +
            "FOREIGN KEY(A_ID) REFERENCES ADMINISTRATOR(Admin_ID), " +
            "FOREIGN KEY(Play_ID) REFERENCES PLAYER(Player_Name))");
    stmt.executeUpdate(createTable);

    createTable = new String("CREATE TABLE FLEET_ORDERS" +
            "(FleetOrder_ID int NOT NULL, Orders varchar(25), " +
            "PRIMARY KEY(FleetOrder_ID), " +
            "FOREIGN KEY(Orders) REFERENCES PLAYER(PlOrders))");
    stmt.executeUpdate(createTable);

    createTable = new String("CREATE TABLE FLEET" +
            "(Fleet_ID int NOT NULL, Order_Num int, " +
            "PRIMARY KEY(Fleet_ID), " +
            "FOREIGN KEY(Order_Num) REFERENCES FLEET_ORDERS(FleetOrder_ID))");
    stmt.executeUpdate(createTable);

    createTable = new String("CREATE TABLE SHIP" +
            "(ShipOwner_ID varchar(15) NOT NULL, Resources int NOT NULL, " +
            "Ship_ID int NOT NULL, Location varchar(25) NOT NULL, " +
            "Type char(10), Speed int NOT NULL, Cargo_Tech int NOT NULL, " +
            "Hull_Tech int NOT NULL, Weapons_Tech int NOT NULL, " +
            "Engine_Tech int NOT NULL, Fl_ID int NOT NULL, " +
            "PRIMARY KEY(Ship_ID, Fl_ID), " +
            "FOREIGN KEY(ShipOwner_ID) REFERENCES PLAYER(Player_Name), " +
            "FOREIGN KEY(Fl_ID) REFERENCES FLEET(Fleet_ID))");
    stmt.executeUpdate(createTable);

    createTable = new String("CREATE TABLE PLANET" +
            "(Planet_ID int NOT NULL, Star_System int NOT NULL, " +
            "Resources int NOT NULL, PlanetOwner_ID varchar(15) NOT NULL, " +
            "Num_Of_Buildings int NOT NULL, PRIMARY KEY(Planet_ID, Star_System), " +
            "FOREIGN KEY(PlanetOwner_ID) REFERENCES PLAYER(Player_Name))");
    stmt.executeUpdate(createTable);

    createTable = new String("CREATE TABLE FACTORY" +
            "(FaPlanet_ID int NOT NULL, Factory_ID int NOT NULL, " +
            "Baubles int NOT NULL, Resources int NOT NULL, " +
            "PRIMARY KEY(Factory_ID, FaPlanet_ID), " +
            "FOREIGN KEY(FaPlanet_ID) REFERENCES PLANET(Planet_ID))");
    stmt.executeUpdate(createTable);

    createTable = new String("CREATE TABLE RESEARCH_CENTER" +
            "(RePlanet_ID int NOT NULL, Research_ID int NOT NULL, " +
            "Research_Type varchar(10), Resources int NOT NULL, " +
            "PRIMARY KEY(Research_ID, RePlanet_ID), " +
            "FOREIGN KEY(RePlanet_ID) REFERENCES PLANET(Planet_ID))");
    stmt.executeUpdate(createTable);

    createTable = new String("CREATE TABLE SHIPYARD" +
            "(ShPlanet_ID int NOT NULL, Shipyard_ID int NOT NULL, " +
            "Ships int NOT NULL, Resources int NOT NULL, " +
            "PRIMARY KEY(Shipyard_ID, ShPlanet_ID), " +
            "FOREIGN KEY(ShPlanet_ID) REFERENCES PLANET(Planet_ID))");
    stmt.executeUpdate(createTable);

    createTable = new String("CREATE TABLE MINE" +
            "(MiPlanet_ID int NOT NULL, Mine_ID int NOT NULL, " +
            "Resources int NOT NULL, " +
            "PRIMARY KEY(Mine_ID, MiPlanet_ID), " +
            "FOREIGN KEY(MiPlanet_ID) REFERENCES PLANET(Planet_ID))");
    stmt.executeUpdate(createTable);

    stmt.close();
  }

  //Drops the tables for the entire database. Must be in order.
  public static void dropTables() throws SQLException {
    Statement stmt = m_dbConn.createStatement();

    String dropTable = new String("DROP TABLE MINE");
    stmt.executeUpdate(dropTable);

    dropTable = new String("DROP TABLE SHIPYARD");
    stmt.executeUpdate(dropTable);

    dropTable = new String("DROP TABLE RESEARCH_CENTER");
    stmt.executeUpdate(dropTable);

    dropTable = new String("DROP TABLE FACTORY");
    stmt.executeUpdate(dropTable);

    dropTable = new String("DROP TABLE PLANET");
    stmt.executeUpdate(dropTable);

    dropTable = new String("DROP TABLE SHIP");
    stmt.executeUpdate(dropTable);

    dropTable = new String("DROP TABLE FLEET");
    stmt.executeUpdate(dropTable);

    dropTable = new String("DROP TABLE FLEET_ORDERS");
    stmt.executeUpdate(dropTable);

    dropTable = new String("DROP TABLE OVERSEEN_BY");
    stmt.executeUpdate(dropTable);

    dropTable = new String("DROP TABLE PLAYER");
    stmt.executeUpdate(dropTable);

    dropTable = new String("DROP TABLE ADMINISTRATOR");
    stmt.executeUpdate(dropTable);

    dropTable = new String("DROP TABLE CARTEL");
    stmt.executeUpdate(dropTable);

    stmt.close();
  }

  //Necessary in order for accessing the Database
  public static boolean activateJDBC() {
    try {
      Driver myDriver = new com.mysql.cj.jdbc.Driver();
      DriverManager.registerDriver(myDriver);
    } catch (SQLException sqle) {
      sqle.printStackTrace();
    }

    return true;
  }
}