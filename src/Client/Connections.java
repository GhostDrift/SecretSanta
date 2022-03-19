package Client;

import java.io.*;
import java.util.ArrayList;
////////////This class is a Singleton////////////
//a class used to hold the information for the saved connections for the client
class Connections implements Serializable {
    //variables
    private  ArrayList<String> names;
    private  ArrayList<String> ips;
    private  ArrayList<Integer> ports;
    private static final String filePath = "Connections.ser";
    private static Connections connections;

    //constructor
    protected Connections(){
    }
    //method to initialize object
    protected static void initialize(){
        System.out.println("initializing connections");
        if(connections == null){
            System.out.println("Creating new instance of connections");
            connections = new Connections();
            System.out.println("File path: " + filePath);
        }
        load();
    }
    //method to load the connections object
    private static void load(){
        System.out.println("loading connections");
        try{
            FileInputStream fileIn = new FileInputStream(filePath);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            connections = (Connections) in.readObject();
            System.out.println("Loaded values");
            printConnections();
            in.close();
            fileIn.close();
        } catch (FileNotFoundException e) {
            assignDefaultValues();
            saveConnections();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("Loaded values");
        printConnections();
    }
    //method to save the connections object
    public static void saveConnections() {
        try {
            System.out.print("Connections to be saved: ");
            printConnections();
            FileOutputStream fileOut = new FileOutputStream(filePath);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(connections);
            out.close();
            fileOut.close();
//            load();
//            System.out.println("Saved values:");
//            printConnections();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //method to assign the default values to the object
    private static void assignDefaultValues(){
        System.out.println("Assigning default values");
        connections.names = new ArrayList<>();
        connections.ips = new ArrayList<>();
        connections.ports = new ArrayList<>();
    }

    //////////// Getters and Setters ////////////
    protected static ArrayList<String> getNames() throws Connections.ConnectionsNotInitialized {
        if(connections == null){
            throw new Connections.ConnectionsNotInitialized();
        }
        return connections.names;
    }
    protected static void setNames(ArrayList<String> newNames) throws Connections.ConnectionsNotInitialized {
        if(connections == null){
            throw new Connections.ConnectionsNotInitialized();
        }
        connections.names = newNames;
    }
    protected static ArrayList<String> getIps() throws Connections.ConnectionsNotInitialized {
        if(connections == null){
            throw new Connections.ConnectionsNotInitialized();
        }
        return connections.ips;
    }
    protected static void setIps(ArrayList<String> newIps) throws Connections.ConnectionsNotInitialized {
        if(connections == null){
            throw new Connections.ConnectionsNotInitialized();
        }
        connections.ips = newIps;
    }
    protected static ArrayList<Integer> getPorts() throws Connections.ConnectionsNotInitialized {
        if(connections == null){
            throw new Connections.ConnectionsNotInitialized();
        }
        return connections.ports;
    }
    protected static void setPorts(ArrayList<Integer> newPorts) throws Connections.ConnectionsNotInitialized {
        if(connections == null){
            throw new Connections.ConnectionsNotInitialized();
        }
        connections.ports = newPorts;
    }
    ////////////Method to print connections////////////
    public static void printConnections(){
        System.out.println("Names: " +connections.names);
        System.out.println("IPs: " + connections.ips);
        System.out.println("Ports: " + connections.ports);
    }

    public static class ConnectionsNotInitialized extends Throwable {
        public ConnectionsNotInitialized(){
            super("Connections Has not been initialized");
        }
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
