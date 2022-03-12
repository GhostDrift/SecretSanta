package Server;

import Common.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

public class DrawNames extends Thread{
    //variables
    private UserDatabase usrdb;
    private WishListDatabase wldb;
    private Server.ServerGUI.FieldPanel textArea;
    private boolean go;

    //constructor
    public DrawNames(UserDatabase usrdb, WishListDatabase wldb, Server.ServerGUI.FieldPanel textArea){
        this.usrdb = usrdb;
        this.wldb = wldb;
        this.textArea = textArea;
        this.go = true;
        this.start();
    }
    //run method
    public void run(){
        System.out.println("Drawing Names");
        textArea.addToTextArea("Drawing Names.....");
        while(go){
            this.drawNames();
        }
    }

    //Method to draw names and assign them to the users
    public String drawNames(){
        String result = "success";
//        UserDatabase usrdb = getUserDatabase();
//        WishListDatabase wldb = getSystemDatabase();
        ArrayList<Integer> ids = usrdb.getIds();
        System.out.println("List of ids: " + ids );
        if(ids.size() <=1){
            result = "there needs to be at least two registered users";
        }else{
            ArrayList<Integer> shuffled = usrdb.getIds();
            Collections.shuffle(shuffled);
            System.out.println("list of shuffled ids: " + shuffled);
            boolean reshuffle = true;
            while(reshuffle){
                if(checkShuffle(ids,shuffled)){
                    reshuffle = false;
                }
                Collections.shuffle(shuffled);
            }
            User usr = new User();
            for(int i = 0; i< ids.size(); i++){
                try {
                    usr = usrdb.getUserById(ids.get(i));
                    usr.setSsrid(shuffled.get(i));
                    System.out.println("User to be updated: " + usr);
                    usrdb.updateUser(usr);
                    wldb.unconfirmWishList(usr);
                } catch (SQLException e) {
                    result = "Sql error";
                }
            }

        }
        this.go = false;
        return result;

    }
    //method to check and see if each user doesn't have themselves when names are drawn; returns true if no user has themselves
    private boolean checkShuffle(ArrayList<Integer> ids, ArrayList<Integer> shuffled){
        for(int i = 0; i< ids.size(); i++){
            if(ids.get(i) == shuffled.get(i)){
                return false;
            }
        }
        return true;
    }
}
