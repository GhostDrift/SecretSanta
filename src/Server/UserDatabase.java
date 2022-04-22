package Server;

import Common.User;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

//A class that provides all the interaction methods for the user database
class UserDatabase extends Database {

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
        while (rset.next()) { // I don't know why this while loop has to be here, but it does
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
                else if(i == 8){
                    usr.setName(data);
                }

            }
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
        while (rset.next()) { // I don't know why this while loop has to be here, but it does
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
                else if(i == 8){
                    usr.setName(data);
                }

            }
        }
        return usr;
    }
    //method to get a user by their recipient id
    protected User getUserByRecipient(int ssrid) throws SQLException{
        rset = query("select * from users where recipientId = '" + ssrid + "';");
        ResultSetMetaData rsmd = rset.getMetaData();
        User usr = new User();
        int numberOfColumns = rsmd.getColumnCount(); // get the number of columns in the result set
        String data;
        while (rset.next()) { // I don't know why this while loop has to be here, but it does
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
                else if(i == 8){
                    usr.setName(data);
                }

            }
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
        this.update("UPDATE `users` SET `password` = '"+ usr.getPassword() + "', `email` = '" + usr.getEmail() + "', `lockCount` = '" + usr.getLockCount() + "', `loggedIn` = '" + usr.getLoggedIn() + "', `recipientId` = '" + usr.getSsrid() + "', `name` = '" + usr.getName() + "' WHERE (`username` = '" + username +"');");
    }
    //method to login a user
    protected void login(User usr) {
//        System.out.println(usr.getLoggedIn());
        usr.setLoggedIn(usr.getLoggedIn() + 1);
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
//        System.out.println("logout in userDatabase");
//        System.out.println( usr.getUsername()+ " logged in count: " + loggedIn);
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
        this.update("INSERT INTO `users` (`username`, `password`, `email`, `lockCount`, `loggedIn`,`name`) VALUES ('" + usr.getUsername() + "', '" + usr.getPassword() + "', '" + usr.getEmail() + "', '0', '0','" + usr.getName() + "');");
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
                count += Integer.parseInt(rset.getString(i));
            }
        }
        return count;
    }

    //method that returns an array of the usernames of the logged in accounts
    protected ArrayList<String> getWhoLoggedIn() throws SQLException {
        ArrayList<String> loggedInUsers = new ArrayList<>();
        rset = this.query("Select loggedIn, username from users;");
        int loggedIn;
        while(rset.next()){
//            System.out.print(rset.getString(1) + " ");
            loggedIn = Integer.parseInt(rset.getString(1));
            if(loggedIn >= 1){
                loggedInUsers.add(rset.getString(2));
            }

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
        ArrayList<String> lockedOutUsers = new ArrayList<>();
        rset = this.query("Select lockCount, username from users;");
        int lockCount;
        while(rset.next()){
//            System.out.print(rset.getString(1) + " ");
            lockCount = Integer.parseInt(rset.getString(1));
            try {
                if(lockCount >= Config.getLockoutThreshold()){
                    lockedOutUsers.add(rset.getString(2));
                }
            } catch (ConfigNotInitializedException e) {
                e.printStackTrace();
            }

        }
//        System.out.println(lockedOutUsers);
        return lockedOutUsers;
    }

    //method to check if a password has already been used by a user
    //returns true if the password has been used before
    protected boolean checkPassHistory(User usr){
        boolean result = false;
        try {
            usr.setId(getUser(usr.getUsername()).getId());
            System.out.println("Id: " + usr.getId() + "password to be checked: " + usr.getPassword());
            rset = this.query("SELECT password FROM passwordhistory WHERE userid = '" + usr.getId() + "' and password = '" +usr.getPassword() +"';");
            if(rset.next()) {
                result = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    //method to clear password history
    public void clearPassHistory(){
        ArrayList<Integer> ids = new ArrayList<>();
        try {
            rset = query("select * from passwordhistory;");
            while (rset.next()) {
                ids.add(rset.getInt(1));
//                update("delete from passwordhistory where id = '" + id + "';");
            }
            for (Integer id : ids) {
                update("delete from passwordhistory where id = '" + id + "';");
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
            for (User user : users) {
                addPassHistoryEntry(user);
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
    //method to clear recipient id's
    protected void clearRecipientIDS(){
       this.update("update users set recipientId = 0");
    }
    //method to delete a user
    protected void deleteUser(User usr){
        try {
            usr = getUser(usr.getUsername());
            update("Delete from passwordhistory where userId = '" + usr.getId()+ "';");
            update("Delete from users where id = '" + usr.getId()+ "';");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //main method used for testing class code
    public static void main(String[] args) throws ConfigNotInitializedException {
//        ConfigPopulator.populate();
        Config.initializeConfig("ServerConfiguration.conf");
        UserDatabase usrDB = new UserDatabase(Config.getUserDatabaseServerAddress(), Config.getDatabaseUsername(), Config.getDatabasePassword());
        User usr = new User();
//        usr.setEmail("testEmail");
        usr.setUsername("hello");
        usrDB.deleteUser(usr);

//        System.out.println(usr);
    }

}