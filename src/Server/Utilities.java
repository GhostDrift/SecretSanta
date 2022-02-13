package Server;

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
}
