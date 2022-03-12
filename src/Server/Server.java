package Server;

import Common.Message;
import Common.NetworkAccess;
import Common.User;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

/*
 * The server class must run in its own thread since the ServerSocket.accept() method
 * is blocking.
 */
public class Server extends Thread {
	/*
	 The client will need the server's IP address. Here is how you find it for your system.
	 
	 For use on a single computer with loopback, use 127.0.0.1 or localhost
	 
	 Windows From ipconfig:
	 
	 Wireless LAN adapter Wireless Network Connection:

    Connection-specific DNS Suffix  . : clunet.edu
    Link-local IPv6 Address . . . . . : fe80::1083:3e22:f5a1:a3ec%11
    IPv4 Address. . . . . . . . . . . : 199.107.222.115 <======= This address works
    Subnet Mask . . . . . . . . . . . : 255.255.240.0
    Default Gateway . . . . . . . . . : 199.107.210.2
    
    MacOS From System preferences    
    Network category, read the IP address directly
    
	 */
	/**
	 * provide access to the GUI
	 */
	ServerGUI servergui = null;
	
	/**
	 * the port number used for client communication
	 */
	private static final int PORT = 8000;

	/**
	 * used for shutting down the server thread
	 */
	private boolean running = true;
	
	/**
	 * unique ID for each client connection
	 */
	private int nextId = 0;
	
	/**
	 * the socket that waits for client connections
	 */
	private ServerSocket serversocket;
			
	/**
	 * list of active client threads by ID number
	 * Vector is a "thread safe" ArrayList
	 */
	private final Vector<ClientHandler> clientconnections;

	/**
	 * the user and system databases
	 */
	private UserDatabase userDatabase;
	private WishListDatabase systemDatabase;
	
	public int getconnections ()
	{
		return clientconnections.size();
	}
	//method to get the number of logged in clients
	public int getNumLoggedIn(){
	    int count = 0;
        try {
           count = userDatabase.getNumberOfLoggedIn();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
    //method to get the usernames of the logged in accounts
    public ArrayList<String> getWhoLoggedIn() throws SQLException {
	    return userDatabase.getWhoLoggedIn();
    }
    //method to get the number of registered accounts
    public int getNumRegistered() throws SQLException {
	    return userDatabase.getNumRegistered();
    }
    //method to get usernames of the locked out accounts
    public ArrayList<String> getWhoLockedOut() throws SQLException{
	    return userDatabase.getWhoLockedOut();
    }
    //returns the userDatabase object
    public UserDatabase getUserDatabase() {
        return userDatabase;
    }
    //returns the systemDatabase object
    public WishListDatabase getSystemDatabase(){
	    return this.systemDatabase;
    }

    //returns the value of running
    public boolean isRunning(){
	    return this.running;
    }
    //method to draw names
    public void drawNames(ServerGUI.FieldPanel textArea, JButton button){
	    new DrawNames(getUserDatabase(),getSystemDatabase(),textArea);
    }
//    //Method to draw names and assign them to the users
//    public String drawNames(){
//	    String result = "success";
//	    UserDatabase usrdb = getUserDatabase();
//	    WishListDatabase wldb = getSystemDatabase();
//	    ArrayList<Integer> ids = usrdb.getIds();
//	    System.out.println("List of ids: " + ids );
//	    if(ids.size() <=1){
//	        result = "there needs to be at least two registered users";
//        }else{
//            ArrayList<Integer> shuffled = usrdb.getIds();
//            Collections.shuffle(shuffled);
//            System.out.println("list of shuffled ids: " + shuffled);
//            boolean reshuffle = true;
//            while(reshuffle){
//                if(checkShuffle(ids,shuffled)){
//                    reshuffle = false;
//                }
//                Collections.shuffle(shuffled);
//            }
//            User usr = new User();
//            for(int i = 0; i< ids.size(); i++){
//                try {
//                    usr = usrdb.getUserById(ids.get(i));
//                    usr.setSsrid(shuffled.get(i));
//                    System.out.println("User to be updated: " + usr);
//                    usrdb.updateUser(usr);
//                    wldb.unconfirmWishList(usr);
//                } catch (SQLException e) {
//                    result = "Sql error";
//                }
//            }
//
//        }
//
//        return result;
//    }
//    //method to check and see if each user doesn't have themselves when names are drawn; returns true if no user has themselves
//    private boolean checkShuffle(ArrayList<Integer> ids, ArrayList<Integer> shuffled){
//	    for(int i = 0; i< ids.size(); i++){
//	        if(ids.get(i)== shuffled.get(i)){
//	            return false;
//            }
//        }
//	    return true;
//    }

    /**
	 * constructor creates the list of clients and
	 * starts the server listening on the port
	 */
	public Server (ServerGUI gui) {
		this.servergui = gui;
		//System.out.println("yay");
		
		// -- construct the list of active client threads
		clientconnections = new Vector<ClientHandler>();

		try {
			// -- construct the user and system databases
			this.userDatabase = new UserDatabase(
					Config.getUserDatabaseServerAddress(),
					Config.getDatabaseUsername(),
					Config.getDatabasePassword());
			this.systemDatabase = new WishListDatabase(
					Config.getSystemDatabaseServerAddress(),
					Config.getDatabaseUsername(),
					Config.getDatabasePassword());
		}
		catch (ConfigNotInitializedException e)
		{
			System.out.println("Config not initialized!");
			System.exit(1);
		}
		catch (Exception e)
		{
			System.out.println(e.getStackTrace());
			System.exit(1);
		}
	}

	/**
	 * listen for incoming client connections
	 * analogous to a telephone operator
	 * listens for the phone to ring
	 * routes the incoming call to the requested extension
	 * goes back to listening for the phone to ring
	 * 
	 * This is in its own thread because the .accept() method is blocking
	 */
	public void run () {
		
		try {
			System.out.println("Server is running...");
			
			// -- open the server socket
			serversocket = new ServerSocket(PORT);
			
			// -- server runs until we manually shut it down
			while (running) {
				
				try {
                    // -- block until a client comes along (listen for the phone to ring)
                    Socket socket = serversocket.accept();

                    // -- connection accepted, create a peer-to-peer socket
                    //    between the server (thread) and client (route the call to the requested extension)
                    peerconnection(socket);
                } catch(Exception e){
                    System.out.println("Server has been stopped");
                }
			}
		}
		catch (IOException e) {
			
			e.printStackTrace();
			System.exit(1);
			
		}
	}


	/**
	 * creates a direct (peer-to-peer) connection between the client and the server
	 * via a thread
	 * 
	 * @param socket: socket from the ServerSocket.listen() call
	 */
	public void peerconnection (Socket socket)
	{		
		// -- when a client arrives, create a thread for their communication
		ClientHandler connection = new ClientHandler(nextId, socket, this, this.servergui);

		// -- add the thread to the active client threads list
		clientconnections.add(connection);
		
		// -- start the thread
		connection.start();

		// -- place some text in the area to let the server operator know
		//    what is going on
		System.out.println("SERVER: connection received for id " + nextId + "\n");
		++nextId;
	}
	
		
	/**
	 * called by a ServerThread when a client is terminated to remove
	 * the connection from the list
	 * 
	 * @param id: the unique ID assigned by the server
	 */
	public void removeID(int id)
	{
		// -- find the object belonging to the client thread being terminated
		for (int i = 0; i < clientconnections.size(); ++i) {
			ClientHandler cc = clientconnections.get(i);
			long x = cc.getID();
			if (x == id) {
				// -- remove it from the active threads list
				//    the thread will terminate itself
				clientconnections.remove(i);
				
				// -- place some text in the area to let the server operator know
				//    what is going on
				System.out.println("SERVER: connection closed for client id " + id + "\n");
				break;
			}
		}
	}
	//method to close the server socket
	protected void removeServersocket(){
        try {
            this.serversocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //method to log out and  disconnect all the clients from the server
    protected void logoutAndDisconnectClients(){
	    int i = getconnections()-1;
        User usr;
        NetworkAccess na;
        while(getconnections()>0){
            na = clientconnections.get(i).getNetworkaccess();
            usr = clientconnections.get(i).getUser();
            if(usr != null) {
                try {
                    System.out.println(userDatabase.getUser(usr.getUsername()));
                    usr = userDatabase.getUser(usr.getUsername());
                    if (usr.getLoggedIn() > 0) {
                        userDatabase.logout(usr);
                    }
                } catch (SQLException e) {
                    System.out.println("Cannot find user in database");
                    e.printStackTrace();
                }
            }
            na.sendMessage(new Message(null,"disconnect"),false);
            removeID(i);
            i = getconnections()-1;
        }

    }
    //method to stop the server
    protected void stopServer(){
	    this.running = false;
	    logoutAndDisconnectClients();
    }
    //method to reset the recipient ID's
    protected void resetRecipientIDS(){
	    getUserDatabase().clearRecipientIDS();
    }

}
