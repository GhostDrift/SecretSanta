package Server;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

import Common.Message;
import Common.NetworkAccess;
import Common.User;


public class ClientHandler extends Thread {
    /**
     * provide access to the GUI for displaying messages
     */
    ServerGUI servergui = null;

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
    private final int id;

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

    /**
     * getter function for the private name field
     *
     * @return name
     */
    public String getname() {
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
//method to login a user
//    public boolean login(Common.User usr){
//        UserDatabase userDB = this.server.getUserDatabase();
//        String username = usr.getUsername();
//        String password = usr.getPassword();
//        try {
//            Common.User result = userDB.getUser(username);
//            if(result.getPassword().equals(password)){
//                userDB.login(usr);
//                return true;
//            }
//            else return false;
//        }
//        catch (SQLException throwables) {
//            throwables.printStackTrace();
//            return false;
//        }
//    }
    //method to log out a user
//    public boolean logout(User usr){
//        UserDatabase userDB = this.server.getUserDatabase();
//        String username = usr.getUsername();
//        System.out.println(username + " logout method clientHandler");
//        //            User result = userDB.getUser(username);
//        userDB.logout(usr);
//        return true;
//    }

    // -- similar to a main() function in that it is the entry point of
    //    the thread
    public void run() {

        // -- server thread runs until the client terminates the connection
        while (go) {
            try {
                // -- always receives a String object with a newline (\n)
                //    on the end due to how BufferedReader readLine() works.
                //    The client adds it to the user's string but the BufferedReader
                //    readLine() call strips it off
                Message cmd = networkaccess.readMessage();
                //logic for login
//                if(cmd.message.equals("login")){
////                    login(cmd.user);
//                    if(login(cmd.user)){
//                        networkaccess.sendMessage(new Message(null,"success"),false);
//                    }
//                    else {
//                        networkaccess.sendMessage(new Message(null, "fail"),false);
//                    }
//                }
//                else

                // -- if it is not the termination message, send it back adding the
                //    required (by readLine) "\n"

                // -- if the disconnect string is received then
                //    close the socket, remove this thread object from the
                //    server's active client thread list, and terminate the thread
                //    this is the server side "command processor"
                //    you will need to define a communication protocol (language) to be used
                //    between the client and the server
                //    e.g. client sends "LOGIN;<username>;<password>\n"
                //         server parses it to "LOGIN", "<username>", "<password>" and performs login function
                //         server responds with "SUCCESS\n"
                //    this is where all the server side Use Cases will be handled
                //	this.servergui.addToTextArea(cmd);
                CommandProtocol.processCommand(cmd, networkaccess, this);
            } catch (IOException e) {

                e.printStackTrace();
                go = false;

            }

        }
    }
    protected NetworkAccess getNetworkaccess(){
        return this.networkaccess;
    }
}
