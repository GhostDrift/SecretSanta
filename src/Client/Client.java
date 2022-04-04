package Client;

import Common.Message;
import Common.NetworkAccess;
import Common.User;

import java.net.ConnectException;
import java.util.ArrayList;

public class Client {
	
	/*
	 
	 For use on a single computer with loopback, use 127.0.0.1 or localhost
	 
	 Windows From ipconfig:
	 
	 Wireless LAN adapter Wireless Network Connection:

     Connection-specific DNS Suffix  . : clunet.edu
     Link-local IPv6 Address . . . . . : fe80::1083:3e22:f5a1:a3ec%11
     IPv4 Address. . . . . . . . . . . : 199.107.222.115 <======= This address works
     Subnet Mask . . . . . . . . . . . : 255.255.240.0
     Default Gateway . . . . . . . . . : 199.107.210.2
     
     MacOS From System preferences     
     Network category, read the IP address directly
     
	 */

    /**
     * provides a peer-to-peer connection to the server
     */
    final NetworkAccess networkaccess;

    /**
     * Creates a peer-to-peer connection to the server
     *
     * @param ip:   the IP address of the server
     * @param port: the port on which the server is listening
     */
    public Client(String ip, int port) throws ConnectException {
        networkaccess = new NetworkAccess(ip, port);
    }


    /**
     * Disconnects the client from the server
     */
    public void disconnect() {
        String text = "disconnect";
        networkaccess.sendMessage(new Message(null, text), false);
        networkaccess.close();
    }

    //sends login message to the server
    public Message login(String username, String password){
        User usr = new User();
        usr.setUsername(username);
        usr.setPassword(password);
        Message msg = new Message(usr,"login");
        return networkaccess.sendMessage(msg,true);

    }
    //sends log out message to server
    public boolean logout(User usr){
        Message msg1 = new Message(usr, "logout");
        System.out.println("Message being sent " + msg1.message);
        System.out.println(msg1.user.getUsername() + "client logout");
        Message result = networkaccess.sendMessage(msg1,true);
        System.out.println("result = " +result.message );
        return result.message.equals("success");
    }
    //registers a new user
    public String register(String username, String email, String password, String rePassword,String name){
        if(!password.equals(rePassword)){
            return "Passwords do not match";

        }
        User usr = new User(username,password,email);
        usr.setName(name);
        Message answer = networkaccess.sendMessage(new Message(usr,"Register"), true);
        return answer.message;
    }
    public String register(){
        Message result = networkaccess.sendMessage(new Message(null,"Register"),true);
        return result.message;
    }
    //account recovery
    public String recover(String username){
        User usr = new User(username);
        Message msg = new Message(usr,"recover");
        Message result = networkaccess.sendMessage(msg,true);
        return result.message;
    }
    //method to update the settings of a user
    public String updateSettings(User usr){
        Message msg = new Message(usr, "updateSettings");
        return networkaccess.sendMessage(msg,true).message;
    }
    //method to get the details of a user
    public User getUser(User usr){
        Message msg = new Message(usr, "getUser");
        return networkaccess.sendMessage(msg,true).user;
    }
    //method to get the wish list of a user
    public ArrayList<String> getWishList(User usr){
        Message msg = new Message(usr,"getWishList");
        msg = networkaccess.sendMessage(msg,true);
        System.out.println(msg.message);
        System.out.println(msg.user.getWishList());
        return msg.user.getWishList();
    }
    //method to add an item to a wish list
    public String addItem(User usr){
        Message msg = new Message(usr,"add");
        return networkaccess.sendMessage(msg,true).message;
    }
    //method to remove an item from a wish list
    public String removeItem(User usr){
        System.out.println("index of item to be removed from the wish list: " + usr.getEntry());
        Message msg = new Message(usr,"remove");
        return networkaccess.sendMessage(msg,true).message;
    }
    //method to clear a users wish list
    public String clearWishList(){
        Message msg = new Message(null,"clear");
        return networkaccess.sendMessage(msg,true).message;
    }
    //method to get a user's wish list conformation status
    public boolean getWishListConformation(){
        Message msg = new Message(null,"confirmed?");
        msg = networkaccess.sendMessage(msg,true);
        return msg.message.equals("true");
    }
    //method to confirm a user's wish list
    public boolean confirmWishList(){
        Message msg = new Message(null,"confirm");
        msg = networkaccess.sendMessage(msg,true);
        return msg.message.equals("success");
    }
    //method to unconfirm a user's wish list
    public void unconfirmWishList(){
        Message msg = new Message(null, "unconfirm");
        networkaccess.sendMessage(msg,true);
    }
    //method to get a user's recipient's wish list
    public Message getRecipientWishList(){
        Message msg = new Message(null,"getRecipientWishList");
        msg = networkaccess.sendMessage(msg,true);
        return msg;
    }
    //method to get a user's recipient
    public Message getRecipient(){
        Message msg = new Message(null,"getRecipient");
        msg = networkaccess.sendMessage(msg,true);
        return msg;
    }
    //method to send verification code to provided email
    public void sendVerificationCode(String email){
//        User usr = new User();
//        usr.setEmail(email);
        Message msg = new Message(null,"verifyEmail");
        msg = networkaccess.sendMessage(msg,true);
//        return msg;
    }
    //method to check provided verification code
    public Message checkCode(String code){
        User usr = new User();
        //saving the code to the password field to be verified.
        usr.setPassword(code);
        Message msg = new Message(usr,"checkCode");
        msg = networkaccess.sendMessage(msg,true);
        return msg;
    }
    //method to validate User credentials
    public String validateUser(String username, String email, String password, String rePassword,String name){
        if(!password.equals(rePassword)){
            return "Passwords do not match";
        }
        User usr = new User(username,password,email,name);
        Message msg = new Message(usr,"validate");
        msg = networkaccess.sendMessage(msg,true);
        return msg.message;
    }
}
