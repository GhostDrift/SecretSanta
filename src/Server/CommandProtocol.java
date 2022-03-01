package Server;

import java.sql.SQLException;
import java.util.ArrayList;
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
            na.sendMessage(login(cmd.user,ch),false);
//            if(login(cmd.user,ch)){
//                na.sendMessage(new Message(null,"success"),false);
//
//            }
//            else {
//                na.sendMessage(new Message(null, "fail"),false);
//            }
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
        }
        else if(cmd.message.equals("recover")){
            na.sendMessage(accountRecovery(cmd.user,ch),false);
        }
        else if (cmd.message.equals("updateSettings")){
            na.sendMessage(updateAccountSettings(cmd.user,ch),false);
        }
        else if(cmd.message.equals("getUser")){
             na.sendMessage(getUser(cmd.user,ch),false);
        }
        else if(cmd.message.equals("getWishList")){
            na.sendMessage(getWishList(cmd.user,ch),false);
        }
        else if(cmd.message.equals("add")){
            na.sendMessage(addItem(cmd.user,ch), false);
        }
        else {

            na.sendMessage(cmd, false);

        }
    }
    //method to login a user
    public static Message login(Common.User usr, ClientHandler ch){
        UserDatabase userDB = ch.getServer().getUserDatabase();
        String username = usr.getUsername();
        String password = usr.getPassword();
        Message msg = new Message(null,"");
        try{
        Common.User result = userDB.getUser(username);
            if(username.equals("")){
                msg.message = "User does not exist";
                return msg;
            }
//            Common.User result = userDB.getUser(username);
            if(result.getPassword() != null) {
                if (result.getPassword().equals(password)) {
                    if(result.getLockCount() >= Config.getLockoutThreshold()){
                        msg.message = "Your account is locked, please recover it to continue";
                        return msg;
                    }
                    else {
                        userDB.login(usr);
                        msg.message = "success";
                        ch.setUser(usr);
                        return msg;
                    }
                }
                else {
                    msg.message = "Invalid Password";
                    result.setLockCount(result.getLockCount()+1);
                    userDB.updateUser(result);
                    return msg;
                }
            }
            else{
                msg.message = "User does not exist";
                return msg;
            }

        }
        catch (SQLException | ConfigNotInitializedException throwables) {
            throwables.printStackTrace();
            return msg;
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
                    else{
                        validatePasswordAndEmail(usr,result,ch);
                        if(result.message.equals("success")){
                            ch.getServer().getUserDatabase().addUser(usr);
                            Utilities.accountCreated(usr);
                        }
                        return result;
                    }
                }

            }
        } catch (ConfigNotInitializedException | SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    private static Message accountRecovery(User usr, ClientHandler ch){
        UserDatabase userDB = ch.getServer().getUserDatabase();
        Message result = new Message(null,"");
        try {
            usr = userDB.getUser(usr.getUsername());
            if(usr == null){
                result.message = "User does not exist";
            }
            else{
                result.message = "success";
                usr.setPassword("xxxx");
                usr.setLockCount(0);
                userDB.updateUser(usr);
                Utilities.accountRecovery(usr);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    //method to validate the password
    private static boolean validatePasswordAndEmail(User usr, Message result, ClientHandler ch){
        try {
            if(usr.getPassword().length() > Config.getMaxPasswordLength()){
    //                        na.sendMessage(new Message(null, "Password must be less than " + Config.getMaxPasswordLength() + " characters long"),false);
                result.message = "Password must be less than or equal to " + Config.getMaxPasswordLength() + " characters long";
                return false;
            }else if(usr.getPassword().length() < Config.getMinPasswordLength()){
//                        na.sendMessage(new Message(null, "Password must be at least " + Config.getMinPasswordLength() + " characters long"),false);
                result.message = "Password must be at least " + Config.getMinPasswordLength() + " characters long";
                return false;
            }else{
                boolean stop = Utilities.containsCharacters(usr.getPassword(),Config.getIllegalPasswordCharacters().toCharArray());
                if(stop){
//                            na.sendMessage(new Message(null, "Passwords cannot contain the following: " + Config.getIllegalPasswordCharacters()),false);
                    result.message = "Passwords cannot contain the following: " + Config.getIllegalPasswordCharacters();
                    return false;
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
                        return false;
                    }
                    else{
//                                na.sendMessage(new Message(null,"Password is fine"),false);
                        if(Utilities.goodEmail(usr.getEmail())){
                            result.message = "success";
                            return true;
                        }
                        else{
                            result.message = "Invalid Email. Must be of the format: example@test.com";
                            return false;
                        }
                    }
                }
            }
        } catch (ConfigNotInitializedException e) {
            e.printStackTrace();
        }
        return true;
    }
    //method to update a users settings
    private static Message updateAccountSettings(User usr, ClientHandler ch){
        UserDatabase userDb = ch.getServer().getUserDatabase();
        Message msg = new Message(null,"");
        try{
            usr.setId(userDb.getUser(ch.getUser().getUsername()).getId());
//            ArrayList<String> passHistory = userDb.getPassHistory(usr);
//            System.out.println(usr.getUsername() + ": " + passHistory);
            User update = userDb.getUser(ch.getUser().getUsername());
            update.setEmail(usr.getEmail());
            update.setPassword(usr.getPassword());
            System.out.println(update.getPassword());
            if(validatePasswordAndEmail(update,msg,ch)){
                if(Config.getEnforcePasswordHistory()){
//                    System.out.println("password to be checked: " + update.getPassword());
//                    System.out.println("Current password: " + userDb.getUser(ch.getUser().getUsername()).getPassword());
                    if(!usr.getPassword().equals(userDb.getUser(update.getUsername()).getPassword())) {
                        if (userDb.checkPassHistory(update)) {
                            msg.message = "You cannot use a previous password";
                        } else {
                            userDb.updateUser(update);
                            userDb.addPassHistoryEntry(update);
                            return msg;
                        }
                    }
                    else{
                        userDb.updateUser(update);
                    }
                }else {
                    userDb.updateUser(update);
                    return msg;
                }
            }

        } catch (SQLException | ConfigNotInitializedException e) {
            e.printStackTrace();
        }
        return msg;
    }
    //method to get a users information
    protected static Message getUser(User usr, ClientHandler ch){
        Message msg = new Message(null,"");
        try {
            usr = ch.getServer().getUserDatabase().getUser(usr.getUsername());
            msg.user = usr;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return msg;
    }
    //method to get a user's wish list
    protected static Message getWishList(User usr, ClientHandler ch){
        Message result = new Message(usr,"success");
        UserDatabase usrDB = ch.getServer().getUserDatabase();
        try {
            usr = usrDB.getUser(usr.getUsername());
            WishListDatabase wldb = ch.getServer().getSystemDatabase();
            usr.setWishList(wldb.getWishList(usr));
            result.user = usr;
        } catch (SQLException e) {
            e.printStackTrace();
            result.message = "error";
        }

        return result;
    }
    //method to add an entry to a user's wish list
    protected static Message addItem(User usr, ClientHandler ch){
        Message result = new Message(usr,"success");
        UserDatabase usrDB = ch.getServer().getUserDatabase();
        String entry = usr.getEntry();
        try{
            usr = usrDB.getUser(usr.getUsername());
            WishListDatabase wldb = ch.getServer().getSystemDatabase();
            result.message = wldb.addEntry(usr,entry);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            result.message = "error";
        }
        return result;

    }

}
