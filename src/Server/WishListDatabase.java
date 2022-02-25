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
        try {
            rset = query("select id from wishlistindex where ownerId = '" + usr.getId() + "';");
            while (rset.next()){
                System.out.println(rset.getString(1));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return wishList;
    }
    //main method for testing
    public static void main(String[] args) {
        Config.initializeConfig("ServerConfiguration.conf");
        try {
            UserDatabase usrdb = new UserDatabase(Config.getUserDatabaseServerAddress(),Config.getDatabaseUsername(),Config.getDatabasePassword());
            WishListDatabase wlDB = new WishListDatabase(Config.getSystemDatabaseServerAddress(),Config.getDatabaseUsername(),Config.getDatabasePassword());
            User usr = usrdb.getUser("test");
            wlDB.getWishList(usr);
        } catch (ConfigNotInitializedException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
