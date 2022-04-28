package Server;

import Common.User;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class DrawNames extends Thread{
    //variables
    private final UserDatabase usrdb;
    private WishListDatabase wldb;
    private ServerGUI.FieldPanel textArea;
    private boolean go;
    private JButton button;
    private final boolean cleared;

    //constructor
    public DrawNames(UserDatabase usrdb, WishListDatabase wldb, ServerGUI.FieldPanel textArea, JButton button){
        this.usrdb = usrdb;
        this.wldb = wldb;
        this.textArea = textArea;
        this.go = true;
        this.button = button;
        this.button.setEnabled(false);
        this.cleared = false;
        textArea.addToTextArea("Drawing Names.....",true);
        this.start();
    }
    //constructor that sends the names reset email out to users
    public DrawNames(UserDatabase usrdb){
        this.cleared = true;
        this.usrdb = usrdb;
        this.go = true;
        this.start();
    }
    //run method
    public void run(){
        if(cleared){
            System.out.println("Names have been cleared");
            while(go){
                this.clearNotification();
            }
        }
        else{
            System.out.println("Drawing Names");
            while(go){
                this.drawNames();
            }
            this.button.setEnabled(true);
            this.button.setText("Clear Names");
        }
    }

    private void clearNotification() {
        ArrayList<Integer> ids = usrdb.getIds();
        if(ids.size() >= 2) {
            try {
                User usr;
                for (Integer id : ids) {
                    usr = usrdb.getUserById(id);
                    Utilities.namesClearedNotification(usr);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        this.go = false;
    }

    //Method to draw names and assign them to the users
    public void drawNames(){
        ArrayList<Integer> ids = usrdb.getIds();
        System.out.println("List of ids: " + ids );
        if(ids.size() >=1){
            ArrayList<Integer> shuffled = usrdb.getIds();
            Collections.shuffle(shuffled);
            System.out.println("list of shuffled ids: " + shuffled);
            boolean reshuffle = true;
            while(reshuffle){
                if(checkShuffle(ids,shuffled)){
                    reshuffle = false;
                }
                else {
                    Collections.shuffle(shuffled);
                }
            }
            User usr;
            String recipient;
            for(int i = 0; i< ids.size(); i++){
                try {
                    usr = usrdb.getUserById(ids.get(i));
                    usr.setSsrid(shuffled.get(i));
                    System.out.println("User to be updated: " + usr);
                    usrdb.updateUser(usr);
                    wldb.unconfirmWishList(usr);
                    recipient = usrdb.getUserById(usr.getSsrid()).getName();
                    Utilities.sendRecipient(usr,recipient);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
        this.textArea.addToTextArea("Names have been drawn.",true);
        this.go = false;

    }
    //method to check and see if each user doesn't have themselves when names are drawn; returns true if no user has themselves
    private boolean checkShuffle(ArrayList<Integer> ids, ArrayList<Integer> shuffled){
        for(int i = 0; i< ids.size(); i++){
            System.out.println("ids: " + ids.get(i));
            System.out.println("shuffled: " + shuffled.get(i));
            if(Objects.equals(ids.get(i), shuffled.get(i))){
                System.out.println("Shuffled: " + false);
                return false;
            }
        }
        return true;
    }
}
