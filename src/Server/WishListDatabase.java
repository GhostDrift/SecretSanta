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
        System.out.println("Entry: " + entry);
        String result = "sql error";
        //check to see if the entry contains an apostrophe
        if(entry.contains("'")){
            //double's the apostrophes in the entry to avoid sql errors
            entry = doubleApostrophes(entry);
        }
        System.out.println("Entry: " + entry);
        try{
            //check to make sure the entry isn't already on the list
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
    //method to add an entry to the wishList index table
    public String addIndex(User usr){
        String result = "SQL error";
        try{
            rset = query("Select * from wishlistindex where ownerId = '" + usr.getId()+"';");
            if(rset.next()){
                result = "User already has a wish list";
            }
            else{
                update("INSERT INTO `wishlistindex` (`ownerId`) VALUES ('"+ usr.getId() + "');");
                result = "success";
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }
    //method to remove a given entry in a wishlist
    public void removeEntry(User usr, String entry){
        int listId = getListID(usr);
        System.out.println(listId);
        int entryId = 0;
        ArrayList<String> wl = getWishList(usr);
//        System.out.println(wl.get(0));
        entry = wl.get(Integer.parseInt(entry));
        if(entry.contains("'")){
            entry = doubleApostrophes(entry);
        }
        System.out.println("Entry to be removed: " + entry);
//        try{
//            rset = query("select pk from wishlistentries where id = '" + listId+"' and description = '" + entry + "';");
//            while(rset.next()){
//                entryId = rset.getInt(1);
//            }
//            update("delete from wishlistentries where pk = '" + entryId + "';");
            update("delete from wishlistentries where id = '" + listId+"' and description = '" + entry + "';");
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
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
        update("UPDATE `wishlistindex` SET `confirmed` = '1' WHERE (`ownerId` = '" + usr.getId() +"');");
    }
    //method to unconfirm wish list
    public void unconfirmWishList(User usr){
        update("UPDATE `wishlistindex` SET `confirmed` = '0' WHERE (`ownerId` = '" + usr.getId() +"');");
    }
    //method to get wishList conformation
    public boolean getWishListConformation(User usr){
        boolean confirmed = false;
//        System.out.println("User who's list conformation is being checked: " + usr);
        int result;
        try{
            rset = query("Select confirmed from wishlistindex where ownerId = '" + usr.getId() + "';");
            while(rset.next()){
                result = rset.getInt(1);
                System.out.println("Confomation number: " + result);
                System.out.println("Does result == 1?: " + (result == 1));
                if(result == 1){
                    confirmed = true;
                }

//                if(rset.getInt(1) == 1){
//                    confirmed = true;
//                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        System.out.println("Status of Confirmed: " + confirmed);
        return confirmed;
    }
    //method to clear a wish list
    public void clearWishList(User usr){
        int listId = getListID(usr);
        update("delete from wishlistentries where id = '" + listId + "';");
        unconfirmWishList(usr);
    }
    //method for dealing with apostrophes
    private static String doubleApostrophes(String test){
        String[] split = test.split("'");
        String s = "";
        for(int i = 0; i< split.length; i++){
            if(i == split.length-1) {
//                if(split[i].contains("'"))
                s += split[i];
            }
            else{
                s += split[i] + "''";
            }

        }
        String check = "" + test.charAt(test.length()-1);
        if(check.equals("'")){
            s += "'";
        }
        return s;
    }
    //main method for testing
    public static void main(String[] args) {
        Config.initializeConfig("ServerConfiguration.conf");
        try {
            UserDatabase usrdb = new UserDatabase(Config.getUserDatabaseServerAddress(),Config.getDatabaseUsername(),Config.getDatabasePassword());
            WishListDatabase wlDB = new WishListDatabase(Config.getSystemDatabaseServerAddress(),Config.getDatabaseUsername(),Config.getDatabasePassword());
            User usr = new User();
            usr.setId(10);
//            System.out.println(wlDB.addEntry(usr, "This item should be removed"));
//            wlDB.removeEntry(usr,"this item is to be removed");
            System.out.println(wlDB.addIndex(usr));
//            ArrayList<String> wl = wlDB.getWishList(usr);
//            System.out.println(wl);
//           wlDB.addEntry(usr,"This is an example entry");
//            wlDB.clearWishList(usr);
//            System.out.println(wlDB.getWishList(usr));
        } catch (ConfigNotInitializedException e) {
            e.printStackTrace();
        }
    }
}
