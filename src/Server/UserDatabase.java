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
                    usr.setId(Integer.parseInt(data));
                }
                else if(i == 2){
                    usr.setUsername(data);
//                    System.out.println(usr.getUsername());
                }
                else if (i == 3){
                    usr.setPassword(data);
//                    System.out.println(usr.getPassword());
                }
                else if (i == 4){
                    usr.setEmail(data);
//                    System.out.println(usr.getEmail());
                }
                else if(i == 5){
                    usr.setLockCount(Integer.parseInt(data));
//                    System.out.println(usr.getLockCount());
                }
                else if(i == 6){
                    usr.setLoggedIn(Integer.parseInt(data));
//                    System.out.println(usr.getLoggedIn());
                }
                else if(i == 7){
                    usr.setSsrid(Integer.parseInt(data));
                }

            }
//            usr.print();
//            System.out.println(usr.getUsername());
//            System.out.println(rset.getString(numberOfColumns));
        }
        return usr;
    }
    //method to get a user by their id
    protected User getUserById(int id) throws SQLException {
//        username = username.toLowerCase(Locale.ROOT);
        rset = this.query("SELECT * FROM users WHERE id = '" + id + "';");
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
                    usr.setId(Integer.parseInt(data));
                }
                else if(i == 2){
                    usr.setUsername(data);
//                    System.out.println(usr.getUsername());
                }
                else if (i == 3){
                    usr.setPassword(data);
//                    System.out.println(usr.getPassword());
                }
                else if (i == 4){
                    usr.setEmail(data);
//                    System.out.println(usr.getEmail());
                }
                else if(i == 5){
                    usr.setLockCount(Integer.parseInt(data));
//                    System.out.println(usr.getLockCount());
                }
                else if(i == 6){
                    usr.setLoggedIn(Integer.parseInt(data));
//                    System.out.println(usr.getLoggedIn());
                }
                else if(i == 7){
                    usr.setSsrid(Integer.parseInt(data));
                }

            }
//            usr.print();
//            System.out.println(usr.getUsername());
//            System.out.println(rset.getString(numberOfColumns));
        }
        return usr;
    }
    //method to get a list of all the user ids
    protected ArrayList<Integer> getIds(){
        ArrayList<Integer> ids = new ArrayList<>();
        try{
            rset = query("select id from users");
            while(rset.next()){
                ids.add(rset.getInt(1));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return ids;
    }
    //method to update a user
    protected void updateUser(User usr){
        String username = usr.getUsername();
        this.update("UPDATE `users` SET `password` = '"+ usr.getPassword() + "', `email` = '" + usr.getEmail() + "', `lockCount` = '" + usr.getLockCount() + "', `loggedIn` = '" + usr.getLoggedIn() + "', `recipientId` = '" + usr.getSsrid() + "' WHERE (`username` = '" + username +"');");
    }
    //method to login a user
    protected void login(User usr) {
//        System.out.println(usr.getLoggedIn());
        usr.setLoggedIn(usr.getLoggedIn() + 1);
//        System.out.println(usr.getLoggedIn());
//        this.update("UPDATE users SET `loggedIn` = '" + usr.getLoggedIn() + "' WHERE (`username` = '" + usr.getUsername() +"');");
//        System.out.println(usr.getUsername());
        this.update("UPDATE `users` SET `loggedIn` = '" + usr.getLoggedIn() + "', `lockCount` = '" + 0 + "' WHERE (`username` = '"+ usr.getUsername() + "');");
    }

    //method to log out a user
    protected boolean logout(User usr) {
        try {
            usr = getUser(usr.getUsername());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int loggedIn = usr.getLoggedIn();
        System.out.println("logout in userDatabase");
        System.out.println( usr.getUsername()+ " logged in count: " + loggedIn);
        if(loggedIn > 0) {
            usr.setLoggedIn(loggedIn - 1);
            this.update("UPDATE `userdb`.`users` SET `loggedIn` = '" + usr.getLoggedIn() + "' WHERE (`username` = '" + usr.getUsername() + "');");
            return true;
        }
        else {
            return false;
        }
    }
    //method to add a new user to the database
    protected void addUser(User usr){
        this.update("INSERT INTO `users` (`username`, `password`, `email`, `lockCount`, `loggedIn`) VALUES ('" + usr.getUsername() + "', '" + usr.getPassword() + "', '" + usr.getEmail() + "', '0', '0');");
        try {
            if (Config.getEnforcePasswordHistory()){
                this.addPassHistoryEntry(getUser(usr.getUsername()));
            }
        } catch (ConfigNotInitializedException | SQLException e) {
            e.printStackTrace();
        }
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
        ArrayList<String> lockedOutUsers = new ArrayList<String>();
        rset = this.query("Select lockCount, username from users;");
        int lockCount = 0;
        while(rset.next()){
//            System.out.print(rset.getString(1) + " ");
            lockCount = Integer.parseInt(rset.getString(1));
//            System.out.print(lockCount + " ");
//            System.out.println(rset.getString(2));
            try {
                if(lockCount >= Config.getLockoutThreshold()){
                    lockedOutUsers.add(rset.getString(2));
                }
            } catch (ConfigNotInitializedException e) {
                e.printStackTrace();
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
    //method that returns an arraylist containing all the user's previous passwords
    protected ArrayList<String> getPassHistory(User usr){
        ArrayList<String> history = new ArrayList<String>();
        try{
        usr.setId(getUser(usr.getUsername()).getId());
        rset = this.query("SELECT * FROM passwordhistory WHERE userid = '" + usr.getId() + "';");
            ResultSetMetaData rsmd = rset.getMetaData();
            int numberOfColumns = rsmd.getColumnCount();
            while (rset.next()) { // I don't know why this while loop has to be here but it does
                  history.add(rset.getString(3));
            }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        System.out.println(history);
        return history;
    }
    //method to check if a password has already been used by a user
    //returns true if the password has been used before
    protected boolean checkPassHistory(User usr){
        boolean result = false;
        try {
            usr.setId(getUser(usr.getUsername()).getId());
            System.out.println("Id: " + usr.getId() + "password to be checked: " + usr.getPassword());
            rset = this.query("SELECT password FROM passwordhistory WHERE userid = '" + usr.getId() + "' and password = '" +usr.getPassword() +"';");
            ResultSetMetaData rsmd = rset.getMetaData();
            String s;
            if(rset.next()) {
                result = true;
            }
//            while(rset.next()){
//
//            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    //method to clear password history
    public void clearPassHistory(){
        ArrayList<Integer> ids = new ArrayList<Integer>();
        try {
            rset = query("select * from passwordhistory;");
            while (rset.next()) {
                ids.add(rset.getInt(1));
//                update("delete from passwordhistory where id = '" + id + "';");
            }
            for(int i = 0; i < ids.size(); i++){
                update("delete from passwordhistory where id = '" + ids.get(i) + "';");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    //method to populate password history
    public void populatePassHistory(){
        ArrayList<User> users = new ArrayList<>();
        User usr;
        try{
            rset = query("select id, password from users;");
            while(rset.next()){
                usr = new User();
                usr.setId(rset.getInt(1));
                usr.setPassword(rset.getString(2));
                users.add(usr);
            }
            for(int i = 0; i< users.size(); i++){
                addPassHistoryEntry(users.get(i));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    //method to add an entry to the password history table
    protected void addPassHistoryEntry(User usr){
//        "INSERT INTO `users` (`username`, `password`, `email`, `lockCount`, `loggedIn`) VALUES ('" + usr.getUsername() + "', '" + usr.getPassword() + "', '" + usr.getEmail() + "', '0', '0');"
        this.update("INSERT INTO `passwordhistory` (`userId`, `password`) VALUES ('" + usr.getId() + "', '" + usr.getPassword() + "');");
    }
//main method used for testing class code
    public static void main(String[] args) throws ConfigNotInitializedException {
//        ConfigPopulator.populate();
        Config.initializeConfig("ServerConfiguration.conf");
        UserDatabase usrDB = new UserDatabase(Config.getUserDatabaseServerAddress(), Config.getDatabaseUsername(), Config.getDatabasePassword());
//        usrDB.printResultSet(usrDB.query("SELECT * FROM users;"));
//        try {
//             usrDB.clearPassHistory();
//        usrDB.populatePassHistory();
//            User usr = usrDB.getUser("test");
//            usr.setPassword("Example");
//            usrDB.updateUser(usr);
//            usrDB.addPassHistoryEntry(usr);
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
//            System.out.println(usrDB.getPassHistory(usr));
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }

}