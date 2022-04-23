import Client.Connections;

import java.io.*;
import java.util.ArrayList;

public class Test implements Serializable {
    public static void main(String[] args) {
        int i = 1;
        try {
            //String fileName = "Logs\\"+i+ "Log.txt";
            String fileName = "Logs";
            File logFile = new File(fileName);
            logFile.mkdir();
            fileName = fileName + "\\"+i+ "Log.txt";
            logFile = new File(fileName);
            if (logFile.createNewFile()) {
                System.out.println("File created: " + logFile.getName());
                FileWriter fw = new FileWriter(fileName);
                fw.write("This is a test of the fileWriteMethod");
                fw.close();
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
