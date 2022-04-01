package Server;

import Common.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilities {
    public static String getStringFromArray(char[] chars){
        StringBuilder str = new StringBuilder();
        for (char aChar : chars) {
            str.append(aChar);
        }
        return str.toString();
    }
    public static Boolean containsCharacters(String test, char[] characters){
        boolean stop = false;
        int i = 0;
        String testVal;
        while ((!stop) && (i < characters.length)){
            testVal = Character.toString(characters[i]);
//                        System.out.println(testVal);
            if(test.contains(testVal)){
//                            na.sendMessage(new Message(null, "Usernames cannot contain the following: " + Converters.getStringFromArray(illegalChars)),false);
                stop = true;
            }
            i++;
        }
        return stop;
    }
    public static boolean containsLowercase(String test){
        boolean result = false;
        int i = 0;
        while((!result)&&(i < test.length())){
            result = Character.isLowerCase(test.charAt(i));
            i++;
        }
        return result;
    }
    public static boolean containsUppercase(String test){
        boolean result = false;
        int i = 0;
        while((!result)&&(i < test.length())){
            result = Character.isUpperCase(test.charAt(i));
            i++;
        }
        return result;
    }
    public static boolean containsNumbers(String test){
        boolean result = false;
        int i = 0;
        while((!result)&&(i < test.length())){
            result = Character.isDigit(test.charAt(i));
            i++;
        }
        return result;
    }
    public static boolean containsSymbols(String test){
        //Modified code from
        //https://stackoverflow.com/questions/1795402/check-if-a-string-contains-a-special-character
        Pattern symbols = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~-]");
        Matcher hasSymbol = symbols.matcher(test);
        return hasSymbol.find();
    }
    //method to check the format of the Email
    public static boolean goodEmail(String email) throws ConfigNotInitializedException {
       return Pattern.matches(Config.getValidEmailFormat(), email);
    }
    //method to send account creation conformation email
    public static void accountCreated(User usr) throws ConfigNotInitializedException {
        String message = "Dear " + usr.getUsername() + ",\n\nThank you for creating an account with the secret santa management system. you will receive an email with your recipient when names are drawn.\n\nWith regards,Stojkovic Technical Solutions.";
        new SendEmailUsingGMailSMTP(usr.getEmail(),"Account Created",message);
    }
    //method to send account recovery email
    public static void accountRecovery(User usr){
        String message = "Dear " + usr.getUsername() + ",\n\nYour account has been recovered.\nYour new password is:\n" + usr.getPassword() + "\n Please login and reset it as soon as possible.\n\nWith regards,Stojkovic Technical Solutions.";
        new SendEmailUsingGMailSMTP(usr.getEmail(),"Account Recovery",message);
    }
    //method to send email with recipient username
    public static void sendRecipient(User usr,String recipient){
        String message = "Dear " + usr.getUsername() + ",\n\nNames have been drawn.\nYour recipient is:\n" + recipient + "\nWe will notify you when they have confirmed their wish list.\n\nWith regards,Stojkovic Technical Solutions.";
        new SendEmailUsingGMailSMTP(usr.getEmail(),"Your recipient is.....",message);
    }
    //method to notify user of account settings update
    public static void accountUpdate(User usr){
        String message = "Dear " + usr.getUsername() + ",\n\nYour account settings have been updated.\nIf this was not you, please recover your account and change your password.\n\nWith regards,Stojkovic Technical Solutions.";
        new SendEmailUsingGMailSMTP(usr.getEmail(),"Account has been updated",message);
    }
    //method to notify user that their recipient has confirmed their wish list
    public static void ssrWishListConfirmed(User usr, String recipient){
        String message = "Dear " + usr.getUsername() + ",\n\n" + recipient + " has confirmed their wish list!\nCome check it out!\n\nWith regards,Stojkovic Technical Solutions.";
        new SendEmailUsingGMailSMTP(usr.getEmail(),"Recipient Wish list",message);
    }
    //method to notify user that they have been locked out
    public static void lockedOutNotification(User usr){
        String message = "Dear " + usr.getUsername() + ",\n\nYour account has been locked.\nYou must recover your account in order to use the system.\n\nWith regards,Stojkovic Technical Solutions.";
        new SendEmailUsingGMailSMTP(usr.getEmail(),"Account locked",message);
    }
    //method to notify a user that names have been reset
    public static void namesClearedNotification(User usr){
        String message = "Dear " + usr.getUsername() + ",\n\nNames have been reset.\nYou will be notified when you have received a new recipient.\n\nWith regards, Stojkovic Technical Solutions.";
        new SendEmailUsingGMailSMTP(usr.getEmail(),"Names reset",message);
    }
    //method to send verification code to provided email for email validation
    public static void verifyEmail(String email, String code){
        String message =  "Hello,\nThank you for making an account with the Secret Santa Management System.\n Your verification code is\n" + code + "\nIf you received this email by mistake please ignore it.\n\nWith regards, Stojkovic Technical Solutions.";
        new SendEmailUsingGMailSMTP(email,"Verify Your Email", message);
    }

    public static void main(String[] args) throws ConfigNotInitializedException {
        Config.initializeConfig("ServerConfiguration.conf");
        String test = "jstojkovic@Callutheran";
        System.out.println(goodEmail(test));
    }
}
