package Server;

import java.sql.SQLException;
import java.util.HashMap;

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
                ch.setUser(cmd.user);
            }
            else {
                na.sendMessage(new Message(null, "fail"),false);
            }
        }else if(cmd.message.equals("logout")){
            System.out.println(cmd.user.getUsername());
            if(logout(cmd.user,ch)){
                na.sendMessage(new Message(null,"success"),false);
                ch.setUser(null);
            }
            else {
                na.sendMessage(new Message(null,"fail"), false);
            }

        } else if(cmd.message.equals("Register")){
            na.sendMessage(register(cmd.user,ch),false);
        }else {

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
    //method to register a user
    public static Message register(User usr, ClientHandler ch){
        Message result = new Message(null,"");
        try {
            if(Config.getMinUsernameLength() >usr.getUsername().length()){
//                na.sendMessage(new Message(null,"Username must be at least " + Config.getMinUsernameLength() + " characters long"),false);
                result.message = "Username must be at least " + Config.getMinUsernameLength() + " characters long";
                return result;
            }
            else if(Config.getMaxUsernameLength() < usr.getUsername().length()){
//                na.sendMessage(new Message(null,"Username must be less than or equal to " + Config.getMaxUsernameLength() + " characters long"),false);
                result.message = "Username must be less than or equal to " + Config.getMaxUsernameLength() + " characters long";
                return result;
            }
            else {
                char[] illegalChars = Config.getIllegalUsernameCharacters();
                Boolean stop = Utilities.containsCharacters(usr.getUsername(),illegalChars);
                if(stop){
//                    na.sendMessage(new Message(null, "Usernames cannot contain the following: " + Utilities.getStringFromArray(illegalChars)),false);
                    result.message = "Usernames cannot contain the following: " + Utilities.getStringFromArray(illegalChars);
                    return result;
                }
                else{
                    User test = ch.getServer().getUserDatabase().getUser(usr.getUsername());
                    if(test.getUsername() != null){
//                        na.sendMessage(new Message(null, "Username already exists"),false);
                        result.message = "Username already exists";
                        return result;
                    }
                    else if(usr.getPassword().length() > Config.getMaxPasswordLength()){
//                        na.sendMessage(new Message(null, "Password must be less than " + Config.getMaxPasswordLength() + " characters long"),false);
                        result.message = "Password must be less than or equal to " + Config.getMaxPasswordLength() + " characters long";
                        return result;
                    }
                    else if(usr.getPassword().length() < Config.getMinPasswordLength()){
//                        na.sendMessage(new Message(null, "Password must be at least " + Config.getMinPasswordLength() + " characters long"),false);
                        result.message = "Password must be at least " + Config.getMinPasswordLength() + " characters long";
                        return result;
                    }
                    else{
                        stop = Utilities.containsCharacters(usr.getPassword(),Config.getIllegalPasswordCharacters().toCharArray());
                        if(stop){
//                            na.sendMessage(new Message(null, "Passwords cannot contain the following: " + Config.getIllegalPasswordCharacters()),false);
                            result.message = "Passwords cannot contain the following: " + Config.getIllegalPasswordCharacters();
                            return result;
                        }
                        else {
                            System.out.println("Testing password required sets");
                            boolean[] requiredTypes = Config.getRequiredCharacterSets();
                            stop = false;
                            int i = 0;
                            while((!stop) && (i < requiredTypes.length)){
                                if(requiredTypes[i]){
                                    if(i ==0){
                                        if(!Utilities.containsLowercase(usr.getPassword())){
                                            stop = true;
                                        }
                                    }
                                    else if(i == 1){
                                        if(!Utilities.containsUppercase(usr.getPassword())){
                                            stop = true;
                                        }
                                    }
                                    else if(i == 2){
                                        if(!Utilities.containsNumbers(usr.getPassword())){
                                            stop = true;
                                        }
                                    }
                                    else{
                                        if(!Utilities.containsSymbols(usr.getPassword())){
                                            stop = true;
                                        }
                                    }
                                }
                                i++;
                            }
                            if(stop){
                                String required = "";
                                if(requiredTypes[0]){
                                    required += " A lowercase letter.";
                                }
                                if(requiredTypes[1]){
                                    required += " An uppercase letter.";
                                }
                                if(requiredTypes[2]){
                                    required += " A number.";
                                }
                                if(requiredTypes[3]){
                                    required += " A special character.";
                                }
//                                na.sendMessage(new Message(null,"Passwords must contain:" + required),false);
                                result.message = "Passwords must contain:" + required;
                                return result;
                            }
                            else{
//                                na.sendMessage(new Message(null,"Password is fine"),false);
                                if(Utilities.goodEmail(usr.getEmail())){
                                    result.message = "success";
                                    ch.getServer().getUserDatabase().addUser(usr);
                                    Utilities.accountCreated(usr);
                                    return result;
                                }
                                else{
                                    result.message = "Invalid Email. Must be of the format: example@test.com";
                                    return result;
                                }
                            }
                        }
                    }
                }

            }
        } catch (ConfigNotInitializedException | SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

}
