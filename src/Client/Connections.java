package Client;

import java.io.*;
import java.util.ArrayList;
////////////This class is a Singleton////////////
//a class used to hold the information for the saved connections for the client
class Connections implements Serializable {
    //variables
    private static ArrayList<String> names;
    private static ArrayList<String> ips;
    private static ArrayList<Integer> ports;
    private static final String filePath = "Connections.txt";
    private static Connections connections;

    //constructor
    protected Connections(){
        initialize();
    }
    //method to initialize object
    private void initialize(){
        if(connections == null){
            connections = new Connections();
        }
        load();
    }
    //method to load the connections object
    private void load(){
        try{
            FileInputStream fileIn = new FileInputStream(filePath);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            connections = (Connections) in.readObject();
            in.close();
            fileIn.close();
        } catch (FileNotFoundException e) {
            assignDefaultValues();
            saveConnections();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    //method to save the connections object
    public static void saveConnections() {
        try {
            FileOutputStream fileOut = new FileOutputStream(filePath);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(connections);
            out.close();
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //method to assign the default values to the object
    private void assignDefaultValues(){
        names = new ArrayList<>();
        ips = new ArrayList<>();
        ports = new ArrayList<>();
    }

    //////////// Getters and Setters ////////////
    protected static ArrayList<String> getNames() throws Connections.ConnectionsNotInitialized {
        if(connections == null){
            throw new Connections.ConnectionsNotInitialized();
        }
        return names;
    }
    protected static void setNames(ArrayList<String> newNames) throws Connections.ConnectionsNotInitialized {
        if(connections == null){
            throw new Connections.ConnectionsNotInitialized();
        }
        names = newNames;
    }
    protected static ArrayList<String> getIps() throws Connections.ConnectionsNotInitialized {
        if(connections == null){
            throw new Connections.ConnectionsNotInitialized();
        }
        return ips;
    }
    protected static void setIps(ArrayList<String> newIps) throws Connections.ConnectionsNotInitialized {
        if(connections == null){
            throw new Connections.ConnectionsNotInitialized();
        }
        ips = newIps;
    }
    protected static ArrayList<Integer> getPorts() throws Connections.ConnectionsNotInitialized {
        if(connections == null){
            throw new Connections.ConnectionsNotInitialized();
        }
        return ports;
    }
    protected static void setPorts(ArrayList<Integer> newPorts) throws Connections.ConnectionsNotInitialized {
        if(connections == null){
            throw new Connections.ConnectionsNotInitialized();
        }
        ports = newPorts;
    }

    private static class ConnectionsNotInitialized extends Throwable {
        public ConnectionsNotInitialized(){
            super("Connections Has not been initialized");
        }
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
