import Client.Connections;

import java.io.*;
import java.util.ArrayList;

public class Test implements Serializable {
   public ArrayList<String> names;

    public static void main(String[] args) {
        Connections.initialize();


    }
    public void Test(){
    }
    private void loadNames(ArrayList<String> names){
        try {
            System.out.println(Connections.getNames());
            names = Connections.getNames();
        } catch (Connections.ConnectionsNotInitialized e) {
            names = new ArrayList<String>();
        }
    }
}
