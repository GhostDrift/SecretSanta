package Server;

import java.sql.*;

//A class used to access a mysql database with the provided url, username and password.
class Database {
    //database login variables
    private String url;
    private String username;
    private String password;
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
            //database query variables
            Connection conn = DriverManager.getConnection(url, username, password);
            stmt = conn.createStatement();
        } catch (SQLException ex) {
            // handle any errors
            ex.printStackTrace();
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
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


    public static void main(String[] args) throws ConfigNotInitializedException {
        Config.initializeConfig("ServerConfiguration.conf");
        UserDatabase usrDB = new UserDatabase(Config.getUserDatabaseServerAddress(),Config.getDatabaseUsername(),Config.getDatabasePassword());
        try {
            usrDB.getUser("test");
            //dbase.getWishList(usr);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}