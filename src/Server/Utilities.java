package Server;

import Common.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilities {
    public static String getStringFromArray(char[] chars){
        String str = "";
        for(int i = 0; i < chars.length; i ++){
            str = str + chars[i];
        }
        return str;
    }
    public static Boolean containsCharacters(String test, char[] characters){
        Boolean stop = false;
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
        String message = "Dear " + usr.getUsername() + ",\n\n Thank you for creating an account with the secret santa management system. you will receive an email with your recipient when names are drawn.\n\n With regards,Stojkovic Technical Solutions.";
        new SendEmailUsingGMailSMTP(usr.getEmail(),"Account Created",message);
    }

    public static void main(String[] args) throws ConfigNotInitializedException {
        Config.initializeConfig("ServerConfiguration.conf");
        String test = "jstojkovic@Callutheran";
        System.out.println(goodEmail(test));
    }
}
