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
                    wishList.add(rset.getString(1));
                }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return wishList;
    }
    //method to add an entry into a wishlist; no duplicate items allowed
    public String addEntry(User usr, String entry){
        int listId = getListID(usr);
        String result = "sql error";
        //check to make sure the entry isn't already on the list
        try{
            rset = query("select description from wishlistentries where id = '" + listId + "' and description = '" + entry + "';");
            if(rset.next()){
                result = "Item already in list";
                return result;
            }
            else{
                update("INSERT INTO `wishlistentries` (`id`, `description`) VALUES ('" + listId + "', '" + entry + "');");
                result = "success";
                return result;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }
    //method to remove a given entry in a wishlist
    public void removeEntry(User usr, int index){
        int listId = getListID(usr);
        int entryId = 0;
        ArrayList<String> wl = getWishList(usr);
        try{
            rset = query("select pk from wishlistentries where id = '" + listId+"' and description = '" + wl.get(index) + "';");
            while(rset.next()){
                entryId = rset.getInt(1);
            }
            update("delete from wishlistentries where pk = '" + entryId + "';");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    //method to obtain the wishlist Id
    private int getListID(User usr){
        int listId = 0;
        try{
            rset = query("select id from wishlistindex where ownerId = '" + usr.getId() + "';");
            while (rset.next()) {
                listId = rset.getInt(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return listId;
    }
    //method to confirm wishlist
    public void confirmWishList(User usr){
        int listId = getListID(usr);
        update("UPDATE `wishlistindex` SET `confirmed` = '1' WHERE (`id` = '" + listId +"');");
    }
    //method to unconfirm wish list
    public void unconfirmWishList(User usr){
        int listId = getListID(usr);
        update("UPDATE `wishlistindex` SET `confirmed` = '0' WHERE (`id` = '" + listId +"');");
    }
    //main method for testing
    public static void main(String[] args) {
        Config.initializeConfig("ServerConfiguration.conf");
        try {
            UserDatabase usrdb = new UserDatabase(Config.getUserDatabaseServerAddress(),Config.getDatabaseUsername(),Config.getDatabasePassword());
            WishListDatabase wlDB = new WishListDatabase(Config.getSystemDatabaseServerAddress(),Config.getDatabaseUsername(),Config.getDatabasePassword());
            User usr = usrdb.getUser("test");
//            System.out.println(wlDB.addEntry(usr, "This item should be removed"));
////            wlDB.removeEntry(usr,1);
//            ArrayList<String> wl = wlDB.getWishList(usr);
//            System.out.println(wl);
            wlDB.unconfirmWishList(usr);
        } catch (ConfigNotInitializedException | SQLException e) {
            e.printStackTrace();
        }
    }
}
