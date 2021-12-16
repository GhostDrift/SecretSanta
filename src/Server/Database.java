package Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

//A class used to access a mysql database with the provided url, username and password.
class Database {
    //database login variables
    private String url;
    private String username;
    private String password;
    //database query variables
    private Connection conn = null;
    private Statement stmt = null;
    protected ResultSet rset = null;

    //default constructor
    protected Database() {
    }

    //constructor with url, username and password
    protected Database(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
        connect();
    }

    //connects the object to the specified server
    protected void connect() {
        try {
            conn = DriverManager.getConnection(url, username, password);
            stmt = conn.createStatement();
        } catch (SQLException ex) {
            // handle any errors
            ex.printStackTrace();
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }

    //getters and setters
    protected String getUrl() {
        return url;
    }

    protected void setUrl(String url) {
        this.url = url;
    }

    protected String getUsername() {
        return username;
    }

    protected void setUsername(String username) {
        this.username = username;
    }

    protected String getPassword() {
        return password;
    }

    protected void setPassword(String password) {
        this.password = password;
    }

    //database accessing methods
    // used with DELETE and INSERT
    protected void update(String command, String table, String condition) {
        try {
            stmt.executeUpdate(command + " FROM " + table + " WHERE " + condition + ";");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
    }

    //overloaded update method where you can pass a complete sql command
    protected void update(String command) {
        try {
            stmt.executeUpdate(command);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
    }

    // used with SELECT
    //this is the version with a condition
    // used for queries like SELECT * FROM users WHERE username = `tim`
    protected ResultSet query(String command, String columns, String table, String condition) {
        try {
            rset = stmt.executeQuery(command + " " + columns + " FROM " + table + " WHERE " + condition + ";");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return rset;
    }

    //this is the version without a condition
    protected ResultSet query(String command, String columns, String table) {
        try {
            rset = stmt.executeQuery(command + " " + columns + " FROM " + table + ";");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return rset;
    }

    //update method where you can pass the complete sql command
    protected ResultSet query(String command) {
        try {
            rset = stmt.executeQuery(command);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
        return rset;
    }

    public void printResultSet(ResultSet rset) {
        try {
            // -- the metadata tells us how many columns in the data
            ResultSetMetaData rsmd = rset.getMetaData();
            int numberOfColumns = rsmd.getColumnCount();
            System.out.println("columns: " + numberOfColumns);

            // -- loop through the ResultSet one row at a time
            //    Note that the ResultSet starts at index 1
            while (rset.next()) {
                // -- loop through the columns of the ResultSet
                for (int i = 1; i < numberOfColumns; ++i) {
                    System.out.print(rset.getString(i) + "\t\t");
                }
                System.out.println(rset.getString(numberOfColumns));
            }
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }

    public static void main(String[] args) throws ConfigNotInitializedException {
//        ConfigPopulator.populate();
        Database dbase = new Database(Config.getSystemDatabaseServerAddress(), Config.getDatabaseUsername(), Config.getDatabasePassword());
        dbase.printResultSet(dbase.query("select * from data;"));
    }

}