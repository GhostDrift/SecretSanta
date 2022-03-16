import java.util.Arrays;

public class test {
    public static void main(String[] args) {
        String test = "Hellos wishs list";
        String[] split = test.split("'");
        String s = "";
        for(int i = 0; i< split.length; i++){
            if(i == split.length-1) {
//                if(split[i].contains("'"))
                s += split[i];
            }
            else{
                s += split[i] + "''";
            }

        }
        String check = "" + test.charAt(test.length()-1);
        if(check.equals("'")){
            s += "'";
        }
        System.out.println(s);
    }
}
