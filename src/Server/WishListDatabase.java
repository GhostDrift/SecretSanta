package Server;

import Common.User;

import java.sql.SQLException;
import java.util.ArrayList;

public class WishListDatabase extends Database{
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
            }
            else{
                update("INSERT INTO `wishlistentries` (`id`, `description`) VALUES ('" + listId + "', '" + entry + "');");
                result = "success";
            }
            return result;
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
    //method to obtain the wishlist ID
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
        update("UPDATE `wishlistindex` SET `confirmed` = '1' WHERE (`ownerId` = '" + usr.getId() +"');");
    }
    //method to unconfirm wish list
    public void unconfirmWishList(User usr){
        update("UPDATE `wishlistindex` SET `confirmed` = '0' WHERE (`ownerId` = '" + usr.getId() +"');");
    }
    //method to get wishList conformation
    public boolean getWishListConformation(User usr){
        boolean confirmed = false;
//        System.out.println("User whose list conformation is being checked: " + usr);
        int result;
        try{
            rset = query("Select confirmed from wishlistindex where ownerId = '" + usr.getId() + "';");
            while(rset.next()){
                result = rset.getInt(1);
                System.out.println("Conformation number: " + result);
                System.out.println("Does result == 1?: " + (result == 1));
                if(result == 1){
                    confirmed = true;
                }

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
        StringBuilder s = new StringBuilder();
        for(int i = 0; i< split.length; i++){
            if(i == split.length-1) {
//                if(split[i].contains("'"))
                s.append(split[i]);
            }
            else{
                s.append(split[i]).append("''");
            }

        }
        String check = "" + test.charAt(test.length()-1);
        if(check.equals("'")){
            s.append("'");
        }
        return s.toString();
    }
    //method to delete a user's wishlist index
    private void deleteWishList(User usr){
        clearWishList(usr);
        update("delete from wishlistindex where ownerId = '" + usr.getId() + "';");
    }
    //main method for testing
    public static void main(String[] args) {
        Config.initializeConfig("ServerConfiguration.conf");
        try {
            WishListDatabase wlDB = new WishListDatabase(Config.getSystemDatabaseServerAddress(),Config.getDatabaseUsername(),Config.getDatabasePassword());
            User usr = new User();
            usr.setId(2);
            //System.out.println(wlDB.addIndex(usr));
            wlDB.deleteWishList(usr);
        } catch (ConfigNotInitializedException e) {
            e.printStackTrace();
        }
    }
}
