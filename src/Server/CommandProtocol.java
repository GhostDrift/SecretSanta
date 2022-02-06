package Server;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Set;

import Common.Message;
import Common.NetworkAccess;
import Common.User;


/**
 * @author reinhart
 */
public class CommandProtocol {

    /**
     * commands and their responses
     */
    private static final HashMap<String, String> commands;

    static {
        commands = new HashMap<>();
        commands.put("disconnect", "");
        commands.put("hello", "world!");
    }

    /**
     * process commands sent to the server
     *
     * @param cmd: command to be processed
     * @param na:  NetworkAccess object for communication
     * @param ch:  ClientHandler object requesting the processing
     * @return
     */
    public static void processCommand(Message cmd, NetworkAccess na, ClientHandler ch) {
        if (cmd.message.equals("disconnect")) {

            // -- no response to the client is necessary
            na.close();
            ch.getServer().removeID(ch.getID());
            ch.Stop();
        } else if (cmd.message.equals("hello")) {

            // -- client is expecting a response
            na.sendMessage(new Message(null, "world!"), false);

        }else if(cmd.message.equals("login")){
//                    login(cmd.user);
            if(login(cmd.user,ch)){
                na.sendMessage(new Message(null,"success"),false);
            }
            else {
                na.sendMessage(new Message(null, "fail"),false);
            }
        }else if(cmd.message.equals("logout")){
            System.out.println(cmd.user.getUsername());
            if(logout(cmd.user,ch)){
                na.sendMessage(new Message(null,"success"),false);
            }
            else {
                na.sendMessage(new Message(null,"fail"), false);
            }

        } else {

            na.sendMessage(cmd, false);

        }
    }
    //method to login a user
    public static boolean login(Common.User usr, ClientHandler ch){
        UserDatabase userDB = ch.getServer().getUserDatabase();
        String username = usr.getUsername();
        String password = usr.getPassword();
        if(username.equals("")){
            return false;
        }
        try {
            Common.User result = userDB.getUser(username);
            if(result.getPassword().equals(password)){
                userDB.login(usr);
                return true;
            }
            else return false;
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }
    //method to log out a user
    public static boolean logout(User usr, ClientHandler ch){
        UserDatabase userDB = ch.getServer().getUserDatabase();
        String username = usr.getUsername();
        System.out.println(username + " logout method CommandProtocol");
        //            User result = userDB.getUser(username);

        return userDB.logout(usr);
    }
}
