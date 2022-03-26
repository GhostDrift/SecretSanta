package Server;

import Common.Message;
import Common.NetworkAccess;
import Common.User;

import java.sql.SQLException;
import java.util.ArrayList;


/**
 * @author reinhart
 */
public class CommandProtocol {

    /**
     * process commands sent to the server
     *
     * @param cmd: command to be processed
     * @param na:  NetworkAccess object for communication
     * @param ch:  ClientHandler object requesting the processing
     */
    public static void processCommand(Message cmd, NetworkAccess na, ClientHandler ch) {
//            System.out.println("CP user: " + cmd.user);
        switch (cmd.message) {
            case "disconnect":

                // -- no response to the client is necessary
                na.close();
                ch.getServer().removeID(ch.getID());
                ch.Stop();
                break;
            case "hello":

                // -- client is expecting a response
                na.sendMessage(new Message(null, "world!"), false);

                break;
            case "login":
//                    login(cmd.user);
                na.sendMessage(login(cmd.user, ch), false);
                break;
            case "logout":
                System.out.println(cmd.user.getUsername());
                if (logout(cmd.user, ch)) {
                    na.sendMessage(new Message(null, "success"), false);
                    ch.setUser(null);
                } else {
                    na.sendMessage(new Message(null, "fail"), false);
                }

                break;
            case "Register":
                na.sendMessage(register(cmd.user, ch), false);
                break;
            case "recover":
                na.sendMessage(accountRecovery(cmd.user, ch), false);
                break;
            case "updateSettings":
                na.sendMessage(updateAccountSettings(cmd.user, ch), false);
                break;
            case "getUser":
                na.sendMessage(getUser(cmd.user, ch), false);
                break;
            case "getWishList":
                na.sendMessage(getWishList(cmd.user, ch), false);
                break;
            case "add":
                System.out.println(cmd.user.getEntry());
                na.sendMessage(addItem(cmd.user, ch), false);
                break;
            case "remove":
                na.sendMessage(removeItem(cmd.user, ch), false);
                break;
            case "clear":
                na.sendMessage(clearWishList(ch), false);
                break;
            case "confirmed?":
                na.sendMessage(getWishListConformation(ch), false);
                break;
            case "confirm":
                na.sendMessage(confirmWishList(ch), false);
                break;
            case "unconfirm":
                na.sendMessage(unconfirmWishList(ch), false);
                break;
            case "getRecipientWishList":
                na.sendMessage(getRecipientWishList(ch), false);
                break;
            case "getRecipient":
                na.sendMessage(getRecipient(ch), false);
                break;
            default:

                na.sendMessage(cmd, false);

                break;
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
            if(result.getPassword() != null) {
                if (result.getLockCount() >= Config.getLockoutThreshold()) {
                    msg.message = "Your account is locked, please recover it to continue";
                }
                else {
                    if(result.getPassword().equals(password)){
                        userDB.login(usr);
                        msg.message = "success";
                        ch.setUser(usr);
                        return msg;
                    }
                    else {
                        msg.message = "Invalid Password";
                        result.setLockCount(result.getLockCount() + 1);
                        userDB.updateUser(result);
                        if (result.getLockCount() == Config.getLockoutThreshold()) {
                            Utilities.lockedOutNotification(result);
                        }
                    }
                }
            }
            else{
                msg.message = "User does not exist";
            }
            return msg;

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
                }
                else{
                    User test = ch.getServer().getUserDatabase().getUser(usr.getUsername());
                    if(test.getUsername() != null){
//                        na.sendMessage(new Message(null, "Username already exists"),false);
                        result.message = "Username already exists";
                    }
                    else{
                        validatePasswordAndEmail(usr,result);
                        if(result.message.equals("success")){
                            ch.getServer().getUserDatabase().addUser(usr);
                            usr = ch.getServer().getUserDatabase().getUser(usr.getUsername());
                            ch.getServer().getSystemDatabase().addIndex(usr);
                            Utilities.accountCreated(usr);
                        }
                    }
                }
                return result;

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
//            System.out.println("User: " + usr);
            if(usr.getUsername() == null){
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
    private static boolean validatePasswordAndEmail(User usr, Message result){
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
            User update = userDb.getUser(ch.getUser().getUsername());
            update.setEmail(usr.getEmail());
            update.setPassword(usr.getPassword());
            update.setName(usr.getName());
            System.out.println(update.getPassword());
            if(validatePasswordAndEmail(update,msg)){
                if(Config.getEnforcePasswordHistory()){
                    if(!usr.getPassword().equals(userDb.getUser(update.getUsername()).getPassword())) {
                        if (userDb.checkPassHistory(update)) {
                            msg.message = "You cannot use a previous password";
                        } else {
                            userDb.updateUser(update);
                            userDb.addPassHistoryEntry(update);
                            Utilities.accountUpdate(update);
                            return msg;
                        }
                    }
                    else{
                        userDb.updateUser(update);
                        Utilities.accountUpdate(update);
                    }
                }else {
                    userDb.updateUser(update);
                    Utilities.accountUpdate(update);
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
            usr = usrDB.getUser(ch.getUser().getUsername());
            WishListDatabase wldb = ch.getServer().getSystemDatabase();
            usr.setWishList(wldb.getWishList(usr));
            System.out.println(usr.getWishList());
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
        System.out.println("CommandProtocol: " + entry);
        try{
            usr = usrDB.getUser(ch.getUser().getUsername());
            WishListDatabase wldb = ch.getServer().getSystemDatabase();
            result.message = wldb.addEntry(usr,entry);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            result.message = "error";
        }
        return result;

    }
    //method to remove an item from a user's wish list
    private static Message removeItem(User usr, ClientHandler ch){
        Message result = new Message(usr,"success");
        UserDatabase usrDB = ch.getServer().getUserDatabase();
        String entry = usr.getEntry();
        System.out.println("index of item to be removed from the list: " + entry);
        try{
            usr = usrDB.getUser(ch.getUser().getUsername());
            System.out.println("User to have item removed: " + usr.getUsername());
            WishListDatabase wldb = ch.getServer().getSystemDatabase();
            wldb.removeEntry(usr,entry);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            result.message = "error";
        }
        return result;
    }
    //method to clear a user's wish list
    private static Message clearWishList(ClientHandler ch){
        Message result = new Message(ch.getUser(),"success");
        WishListDatabase wldb = ch.getServer().getSystemDatabase();
        UserDatabase usrDB = ch.getServer().getUserDatabase();
        User usr;
        try{
            usr = usrDB.getUser(ch.getUser().getUsername());
            wldb.clearWishList(usr);
        } catch (SQLException throwables) {
            result.message = "SQL error";
            throwables.printStackTrace();
        }
        return result;
    }
    //method to get the conformation status of a user's wish list
    private static Message getWishListConformation(ClientHandler ch){
        Message result = new Message(ch.getUser(),"false");
        System.out.println(ch.getUser());
        WishListDatabase wldb = ch.getServer().getSystemDatabase();
        UserDatabase usrDB = ch.getServer().getUserDatabase();
        User usr;
        try{
            usr = usrDB.getUser(ch.getUser().getUsername());
            if(wldb.getWishListConformation(usr)){
                result.message ="true";
            }
            else{
                result.message = "false";
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            result.message = "SQL error";
        }
        return result;
    }
    private static Message getWishListConformation(User user,ClientHandler ch){
        Message result = new Message(ch.getUser(),"false");
        System.out.println(ch.getUser());
        WishListDatabase wldb = ch.getServer().getSystemDatabase();
        UserDatabase usrDB = ch.getServer().getUserDatabase();
        System.out.println("User who's list conformation is being checked: " + ch.getUser());
        User usr;
        try{
            usr = usrDB.getUser(user.getUsername());
            if(wldb.getWishListConformation(usr)){
                result.message ="true";
            }
            else{
                result.message = "false";
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            result.message = "SQL error";
        }
        return result;
    }
    //method to confirm a user's wish list
    private static Message confirmWishList(ClientHandler ch){
        Message result = new Message(ch.getUser(),"success");
        WishListDatabase wldb = ch.getServer().getSystemDatabase();
        UserDatabase usrDB = ch.getServer().getUserDatabase();
        User usr;
        try{
            usr = usrDB.getUser(ch.getUser().getUsername());
            Message test = getWishList(usr,ch);
            if(test.user.getWishList().size() == 0){
//                System.out.println("Wish List: " + test.user.getWishList());
                result.message = "Wish List is Empty";
            }
            else{
                wldb.confirmWishList(usr);
                String recipient = usr.getName();
                int id = usr.getId();
                usr = usrDB.getUserByRecipient(id);
                Utilities.ssrWishListConfirmed(usr, recipient);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }
    //method to unconfirm a user's wish list
    private static Message unconfirmWishList(ClientHandler ch){
        Message result = new Message(ch.getUser(),"success");
        WishListDatabase wldb = ch.getServer().getSystemDatabase();
        UserDatabase usrDB = ch.getServer().getUserDatabase();
        User usr;
        try{
            usr = usrDB.getUser(ch.getUser().getUsername());
            wldb.unconfirmWishList(usr);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }
    //method to get a user's recipient's wish list
    private static Message getRecipientWishList(ClientHandler ch){
        User user = new User();
        Message result = new Message(user,"success");
        WishListDatabase wldb = ch.getServer().getSystemDatabase();
        UserDatabase usrDB = ch.getServer().getUserDatabase();
        User usr;
        try{
            usr = usrDB.getUser(ch.getUser().getUsername());
            if(usr.getSsrid() == 0){
                result.message = "Names have not been drawn";
            }
            else{
                usr = usrDB.getUserById(usr.getSsrid());
                System.out.println("Recipient: " + usr);
                if(getWishListConformation(usr,ch).message.equals("true")){
//                    ArrayList<String> list = getWishList(usr,ch).user.getWishList();
                    ArrayList<String> list = wldb.getWishList(usr);
                    result.user.setWishList(list);
                }
                else{
                    result.message = "Unconfirmed";
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        System.out.println("User:" + result.user);
        return result;
    }
    //method to get a user's recipient
    private static Message getRecipient(ClientHandler ch){
        User user = new User();
        Message result = new Message(user,"success");
        UserDatabase usrDB = ch.getServer().getUserDatabase();
        User usr;
        try{
            usr = usrDB.getUser(ch.getUser().getUsername());
            if(usr.getSsrid() == 0){
                result.message = "Names have not been drawn";
            }
            else{
                usr = usrDB.getUserById(usr.getSsrid());
                result.user = usr;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return result;
    }

}
