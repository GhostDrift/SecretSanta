import Client.Connections;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Test implements Serializable {
    Logger logger;
    FileHandler fh;
    private LocalDateTime startTime;
    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void main(String[] args) {
        new Test();
//        try {
//            //String fileName = "Logs\\"+i+ "Log.txt";
//            String fileName = "Logs";
//            File logFile = new File(fileName);
//            logFile.mkdir();
//            fileName = fileName + "\\"+i+ "Log.txt";
//            logFile = new File(fileName);
//            if (logFile.createNewFile()) {
//                System.out.println("File created: " + logFile.getName());
//                FileWriter fw = new FileWriter(fileName);
//                fw.write("This is a test of the fileWriteMethod");
//                fw.close();
//            } else {
//                System.out.println("File already exists.");
//            }
//        } catch (IOException e) {
//            System.out.println("An error occurred.");
//            e.printStackTrace();
//        }

    }
    public Test(){
        startTime = LocalDateTime.now();
        setupLogger();
    }
    private void setupLogger() {
        String fileName = "Logs";
        File logFile = new File(fileName);
        logFile.mkdir();
        logger = Logger.getLogger("serverLog");
        String fileSeparator = System.getProperty("file.separator");
        try {
            fh = new FileHandler(System.getProperty("user.dir") + fileSeparator + fileName + fileSeparator + "Log" + dtf.format(startTime) + ".log",true);
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            logger.info("Logger initialised");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
