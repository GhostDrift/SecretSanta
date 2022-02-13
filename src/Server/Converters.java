package Server;

public class Converters {
    public static String getStringFromArray(char[] chars){
        String str = "";
        for(int i = 0; i < chars.length; i ++){
            str = str + chars[i];
        }
        return str;
    }
}
