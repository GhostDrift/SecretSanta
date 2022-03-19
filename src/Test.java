import java.io.*;
import java.util.ArrayList;

public class Test implements Serializable {
    private ArrayList<String> list1;
    private static String filePath = "test.txt";
    private static Test test;
    public static void main(String[] args) {
        initilize();
        print();
    }
    public Test(){
    }
    public static void initilize(){
        if(test == null){
            test = new Test();
        }
        load();
    }
    public static void save(){
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath));
            out.writeObject(test);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        load();
//        System.out.println("Saved values:");
//        print();
    }
    public static void load(){
        try{
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath));
            Object ob = in.readObject();
            if (ob instanceof Test){
                System.out.println("Is Test");
            }
            test = (Test) ob;
            in.close();
        } catch (FileNotFoundException e) {
            assignDefaultValues();
            save();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static  void assignDefaultValues(){
        test.list1 = new ArrayList<String>();
    }
    public static void print(){
        System.out.println("List1" + test.list1);
        System.out.println("File path: " + filePath);
    }
}
