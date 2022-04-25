import java.sql.*;
import java.text.DecimalFormat;
import java.util.Random;
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

    System.out.println("Would you like to Drop the tables (0), Create the tables (1), or Fill Tables (2)?");
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
      else if (input == 2) {
        fillTables();
        System.out.println("Filled");
      }
      else {
        System.out.println("Invalid Input");
      }

      System.out.println("Would you like to Drop the tables (0), Create the tables (1), or Fill Tables (2)?");
      System.out.println("Enter -1 to quit");
      input = scanner.nextInt();
    }
    scanner.close();
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

  //Fills tables with junk. Must be in order.
  public static void fillTables() throws SQLException {
    Random rand = new Random();
    Statement stmt = m_dbConn.createStatement();

    for(int i = 1; i <= 5; i++) {
      int Cartel_ID = rand.nextInt(10000);
      int Admin_ID = rand.nextInt(10000);
      String Player_Name = randString();
      String orders = randString();
      int FleetOrder_ID = rand.nextInt(10000);
      int Fleet_ID = rand.nextInt(10000);
      int Planet_ID = rand.nextInt(10000);

      double money = rand.nextDouble();
      DecimalFormat df = new DecimalFormat("#.00");

      String insertData = new String("INSERT INTO CARTEL" +
              "(Cartel_ID, Message_Board, Num_Of_Members) VALUES ('" +
              Cartel_ID + "','" + randString() + "','"
              + rand.nextInt(50) + "')");
      stmt.executeUpdate(insertData);

      System.out.println("CARTEL");

      insertData = new String("INSERT INTO ADMINISTRATOR" +
              "(Admin_ID) VALUES ('" + Admin_ID + "')");
      stmt.executeUpdate(insertData);

      System.out.println("ADMIN");

      insertData = new String("INSERT INTO PLAYER" +
              "(Player_Name, Money, Resources, PlCartel_ID, PlOrders) VALUES ('" +
              Player_Name + "','" + df.format(money) + "','" + rand.nextInt(10000) +
              "','" + Cartel_ID + "','" + orders + "')");
      stmt.executeUpdate(insertData);

      System.out.println("PLAYER");

      insertData = new String("INSERT INTO OVERSEEN_BY" +
              "(A_ID, Play_ID, Game) VALUES ('" + Admin_ID + "','"
              + Player_Name + "','" + randString() + "')");
      stmt.executeUpdate(insertData);

      System.out.println("OVERSEEN");

      insertData = new String("INSERT INTO FLEET_ORDERS" +
              "(FleetOrder_ID, Orders) VALUES ('" +
              FleetOrder_ID + "','" + orders + "')");
      stmt.executeUpdate(insertData);

      System.out.println("ORDERS");

      insertData = new String("INSERT INTO FLEET" +
              "(Fleet_ID, Order_Num) VALUES ('" + Fleet_ID
              + "','" + FleetOrder_ID + "')");
      stmt.executeUpdate(insertData);

      System.out.println("FLEET");

      insertData = new String("INSERT INTO SHIP" +
              "(ShipOwner_ID, Resources, Ship_ID, Location, Type, " +
              "Speed, Cargo_Tech, Hull_Tech, Weapons_Tech, " +
              "Engine_Tech, Fl_ID) VALUES ('" + Player_Name + "','" +
              rand.nextInt(100) + "','" + rand.nextInt(10000) + "','" + randString()
              + "','" + randString() + "','" + rand.nextInt() + "','" +
              rand.nextInt() + "','" + rand.nextInt() + "','" +
              rand.nextInt() + "','" + rand.nextInt() + "','" + Fleet_ID + "')");
      stmt.executeUpdate(insertData);

      insertData = new String("INSERT INTO PLANET" +
              "(Planet_ID, Star_System, Resources, PlanetOwner_ID, Num_Of_Buildings" +
              ") VALUES ('" + Planet_ID + "','" + rand.nextInt(10) + "','" + rand.nextInt(100)
              + "','" + Player_Name + "','" + rand.nextInt(100) + "')");
      stmt.executeUpdate(insertData);

      insertData = new String("INSERT INTO FACTORY" +
              "(FaPlanet_ID, Factory_ID, Baubles, Resources) VALUES ('" +
              Planet_ID + "','" + rand.nextInt(100) + "','" + rand.nextInt(1000)
              + "','" + rand.nextInt(100) + "')");
      stmt.executeUpdate(insertData);

      insertData = new String("INSERT INTO RESEARCH_CENTER" +
              "(RePlanet_ID, Research_ID, Research_Type, Resources) VALUES ('" +
              Planet_ID + "','" + rand.nextInt(100) + "','" + randString()
              + "','" + rand.nextInt(1000) + "')");
      stmt.executeUpdate(insertData);

      insertData = new String("INSERT INTO SHIPYARD" +
              "(ShPlanet_ID, Shipyard_ID, Ships, Resources) VALUES ('" +
              Planet_ID + "','" + rand.nextInt(100) + "','" + rand.nextInt(100)
              + "','" + rand.nextInt(100) + "')");
      stmt.executeUpdate(insertData);

      insertData = new String("INSERT INTO MINE" +
              "(MiPlanet_ID, Mine_ID, Resources) VALUES ('" +
              Planet_ID + "','" + rand.nextInt(100) + "','" + rand.nextInt(100) + "')");
      stmt.executeUpdate(insertData);
    }

    stmt.close();
  }

  //Produces a random String intended to fill the database.
  public static String randString() {
    String stringJunk = "";
    String[] alphabet = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l",
            "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "x", "y", "z"};
    Random rand = new Random();

    for (int x = 0; x < 9; x++) {
      stringJunk = stringJunk + alphabet[rand.nextInt(alphabet.length)];
    }

    return stringJunk;
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
