package Server;

import Common.User;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

//A class that provides all the interaction methods for the user database
class UserDatabase extends Database {

    //Default Constructor
    protected UserDatabase() {
    }

    //Overloaded constructor
    protected UserDatabase(String url, String username, String password) {
        super(url, username, password);
    }

    // method to get a user
    // throws a sql exception that will be dealt with by the server depending on
    // the exception
    protected User getUser(String username) throws SQLException {
//        username = username.toLowerCase(Locale.ROOT);
        rset = this.query("SELECT * FROM users WHERE username = '" + username + "';");
//        this.printResultSet(rset);
        ResultSetMetaData rsmd = rset.getMetaData();
        User usr = new User();
        int numberOfColumns = rsmd.getColumnCount(); // get the number of columns in the result set
        String data;
        while (rset.next()) { // I don't know why this while loop has to be here but it does
            // -- loop through the columns of the ResultSet
            for (int i = 1; i <= numberOfColumns; ++i) {
//                System.out.print(rset.getString(i) + "\t\t");
                data = rset.getString(i);
                if(i == 1){
                    usr.setUsername(data);
//                    System.out.println(usr.getUsername());
                }
                else if (i == 2){
                    usr.setPassword(data);
//                    System.out.println(usr.getPassword());
                }
                else if (i == 3){
                    usr.setEmail(data);
//                    System.out.println(usr.getEmail());
                }
                else if(i == 4){
                    usr.setLockCount(Integer.parseInt(data));
//                    System.out.println(usr.getLockCount());
                }
                else{
                    usr.setLoggedIn(Integer.parseInt(data));
//                    System.out.println(usr.getLoggedIn());
                }

            }
            usr.print();
//            System.out.println(usr.getUsername());
//            System.out.println(rset.getString(numberOfColumns));
        }
        return usr;
    }
    //method to update a user
    protected void updateUser(User usr){
        String username = usr.getUsername();
        this.update("UPDATE `users` SET `password` = '"+ usr.getPassword() + "', `email` = '" + usr.getEmail() + "', `lockCount` = '" + usr.getLockCount() + "', `loggedIn` = '" + usr.getLoggedIn() + "' WHERE (`username` = '" + username +"');");
    }
    //method to login a user
    protected void login(User usr) {
//        System.out.println(usr.getLoggedIn());
        usr.setLoggedIn(usr.getLoggedIn() + 1);
//        System.out.println(usr.getLoggedIn());
//        this.update("UPDATE users SET `loggedIn` = '" + usr.getLoggedIn() + "' WHERE (`username` = '" + usr.getUsername() +"');");
//        System.out.println(usr.getUsername());
        this.update("UPDATE `users` SET `loggedIn` = '" + usr.getLoggedIn() + "' WHERE (`username` = '"+ usr.getUsername() + "');");
    }

    //method to log out a user
    protected void logout(User usr) {
        int loggedIn = usr.getLoggedIn();
        if(loggedIn > 0) {
            usr.setLoggedIn(loggedIn - 1);
            this.update("UPDATE `userdb`.`users` SET `loggedIn` = '" + usr.getLoggedIn() + "' WHERE (`username` = '" + usr.getUsername() + "');");
        }
    }
    //method to add a new user to the database
    protected void addUser(User usr){
        this.update("INSERT INTO `users` (`username`, `password`, `email`, `lockCount`, `loggedIn`) VALUES ('" + usr.getUsername() + "', '" + usr.getPassword() + "', '" + usr.getEmail() + "', '0', '0');");
    }

    //method to return the number of logged in clients
    protected int getNumberOfLoggedIn() throws SQLException {
        int count = 0;
        rset = this.query("Select loggedIn from users;");
        ResultSetMetaData rsmd = rset.getMetaData();
        int numberOfColumns = rsmd.getColumnCount();
        while(rset.next()){
            for(int i =1; i <= numberOfColumns; i++){
//                System.out.println(rset.getString(i));
//                if(Integer.parseInt(rset.getString(i)) >= 3){
//                    count++;
//                }
                count += Integer.parseInt(rset.getString(i));
            }
        }
        return count;
    }

    //method that returns an array of the usernames of the logged in accounts
    protected ArrayList<String> getWhoLoggedIn() throws SQLException {
        ArrayList loggedInUsers = new ArrayList();
        rset = this.query("Select loggedIn, username from users;");
//        ResultSetMetaData rsmd = rset.getMetaData();
//        int numberOfColumns = rsmd.getColumnCount();
        int loggedIn;
        while(rset.next()){
//            System.out.print(rset.getString(1) + " ");
            loggedIn = Integer.parseInt(rset.getString(1));
            if(loggedIn >= 1){
                loggedInUsers.add(rset.getString(2));
            }
//            System.out.println(rset.getString(2));

//            System.out.println();
        }
        return loggedInUsers;
    }

    //method to get the number of registered accounts
    protected int getNumRegistered() throws SQLException {
        int count = 0;
        rset = this.query("Select username from users;");
        while(rset.next()){
            count++;
        }
//        System.out.println(count);
        return count;
    }

    //method that returns an array of the usernames of the locked out accounts
    protected ArrayList<String> getWhoLockedOut() throws SQLException {
        ArrayList lockedOutUsers = new ArrayList();
        rset = this.query("Select lockCount, username from users;");
        int lockCount = 0;
        while(rset.next()){
//            System.out.print(rset.getString(1) + " ");
            lockCount = Integer.parseInt(rset.getString(1));
//            System.out.print(lockCount + " ");
//            System.out.println(rset.getString(2));
            if(lockCount >= 3){
                lockedOutUsers.add(rset.getString(2));
            }
//            System.out.println(rset.getString(2));

//            System.out.println();
        }
//        System.out.println(lockedOutUsers);
        return lockedOutUsers;
    }

    //method to reset a user's lock count
    protected void resetLockCount(User usr) {

    }

    public static void main(String[] args) throws ConfigNotInitializedException {
//        ConfigPopulator.populate();
        Config.initializeConfig("ServerConfiguration.conf");
        UserDatabase usrDB = new UserDatabase(Config.getUserDatabaseServerAddress(), Config.getDatabaseUsername(), Config.getDatabasePassword());
//        usrDB.printResultSet(usrDB.query("SELECT * FROM users;"));
        try {
//            User usr = usrDB.getUser("Jessica");
//            User usr = new User("Jessica", "test123", "someEmail@gmail.com");
//            usrDB.logout(user);
//            usrDB.addUser(usr);
//            usrDB.getUser("jstojkovic");
//            usr.setUsername("Stojkovic");
//            usr.setPassword("123test");
//            usr.setEmail("fakeEmail@gmail.com");
//            usr.setLoggedIn(1);
//            usr.setLockCount(1);
//            usrDB.updateUser(usr);
//            usrDB.getUser("Stojkovic");
//            usrDB.getUser("Jessica");
//            System.out.println(usrDB.getNumberOfLoggedIn());
//            System.out.println(usrDB.getWhoLoggedIn());
//            usrDB.getNumRegistered();
            usrDB.getWhoLockedOut();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}