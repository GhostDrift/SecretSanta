package Server;

import Common.User;

import java.sql.SQLException;
import java.util.ArrayList;

public class WishListDatabase extends Database{
    //constructor
    public WishListDatabase(){
    }
    //overloaded constructor
    public WishListDatabase(String url,String username,String password){
        super(url,username,password);
    }
    //method to get a users wish list
    public ArrayList<String> getWishList(User usr){
        ArrayList<String> wishList = new ArrayList<>();
        int listId = 0;
        try {
            rset = query("select id from wishlistindex where ownerId = '" + usr.getId() + "';");
            while (rset.next()){
                listId = rset.getInt(1);
            }
            rset = query("select description from wishlistentries where id = '" + listId +"';");
                while(rset.next()){
                    System.out.println(rset.getString(1));
                }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return wishList;
    }
    //method to add an entry into a wishlist
    public void addEntry(User usr, String entry){
        int listId = 0;
        try {
            rset = query("select id from wishlistindex where ownerId = '" + usr.getId() + "';");
            while (rset.next()) {
                listId = rset.getInt(1);
            }
            update("INSERT INTO `wishlistentries` (`id`, `description`) VALUES ('" + listId + "', '" + entry + "');");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    //main method for testing
    public static void main(String[] args) {
        Config.initializeConfig("ServerConfiguration.conf");
        try {
            UserDatabase usrdb = new UserDatabase(Config.getUserDatabaseServerAddress(),Config.getDatabaseUsername(),Config.getDatabasePassword());
            WishListDatabase wlDB = new WishListDatabase(Config.getSystemDatabaseServerAddress(),Config.getDatabaseUsername(),Config.getDatabasePassword());
            User usr = usrdb.getUser("test");
//            wlDB.addEntry(usr, "Test of the add entry method");
            wlDB.getWishList(usr);
        } catch (ConfigNotInitializedException | SQLException e) {
            e.printStackTrace();
        }
    }
}
