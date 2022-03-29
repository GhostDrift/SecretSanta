package Server;

import Common.Message;
import Common.NetworkAccess;
import Common.User;

import java.io.IOException;
import java.net.Socket;


public class ClientHandler extends Thread {
    /**
     * provide access to the GUI for displaying messages
     */
    ServerGUI servergui;

    /**
     * provides a peer-to-peer connection to the client
     */
    private final NetworkAccess networkaccess;

    /**
     * controls the run thread
     */
    private boolean go;

    /**
     * the name of this client
     */
    private final String name;

    /**
     * the unique id of this client
     */
    private int id;

    /**
     * a reference to the server that "has" this ClientHandler
     */
    private final Server server;

    private User usr = new User();

    /**
     * Constructor saves the ID, socket, and reference to the server
     * then construct the NetworkAccess object
     *
     * @param id:     the unique ID for this ClientHandler
     * @param socket: the peer-to-peer socket for connection to the client
     * @param server: reference to the server that "has" this ClientHandler
     */
    public ClientHandler(int id, Socket socket, Server server, ServerGUI gui) {
        this.server = server;
        this.servergui = gui;
        this.id = id;
        this.name = Integer.toString(id);
        go = true;

        networkaccess = new NetworkAccess(socket);
    }


    public String toString() {
        return name;
    }

    public void Stop() {
        go = false;
    }

    public int getID() {
        return id;
    }

    public Server getServer() {
        return server;
    }

    //method to set the user
    protected void setUser(User usr){
        this.usr = usr;
    }
    //method to get the user
    protected User getUser(){
        return this.usr;
    }
    //method to decrement the id of the client handler
    public void decId(){
        this.id--;
    }

    // -- similar to a main() function in that it is the entry point of
    //    the thread
    public void run() {

        // -- server thread runs until the client terminates the connection
        while (go) {
            try {
                Message cmd = networkaccess.readMessage();
                System.out.println("server got " + cmd);
                CommandProtocol.processCommand(cmd, networkaccess, this);
            } catch (IOException e) {

                e.printStackTrace();
                go = false;

            }

        }
    }
    protected NetworkAccess getNetworkAccess(){
        return this.networkaccess;
    }
}
