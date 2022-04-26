package Server;

import Common.Message;
import Common.NetworkAccess;
import Common.User;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;
import java.util.logging.*;

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
	ServerGUI servergui;
	
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
	private final Vector<ClientHandler> clientConnections;

	/**
	 * the user and system databases
	 */
	private UserDatabase userDatabase;
	private WishListDatabase systemDatabase;
	private LocalDateTime startTime;
	private LocalDateTime currentTime;
    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private String lastLogFile = "";
    private Logger logger;
    private FileHandler fh;
	
	public int getConnections()
	{
		return clientConnections.size();
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
	    new DrawNames(getUserDatabase(),getSystemDatabase(),textArea,button);
    }
    //method to return the GUI asociated with the server
    public ServerGUI getServergui(){
	    return this.servergui;
    }


    /**
	 * constructor creates the list of clients and
	 * starts the server listening on the port
	 */
	public Server (ServerGUI gui) {
		this.servergui = gui;
		this.startTime = LocalDateTime.now();
		//System.out.println("yay");
        System.out.println("Gui added");
        //next line of code from https://www.logicbig.com/tutorials/core-java-tutorial/logging/customizing-default-format.html
        System.setProperty("java.util.logging.SimpleFormatter.format","[%1$tF %1$tT] [%4$-7s] %5$s %n");
        setupLogger();
		// -- construct the list of active client threads
		clientConnections = new Vector<>();

		try {
			// -- construct the user and system databases
			this.userDatabase = new UserDatabase(
					Config.getUserDatabaseServerAddress(),
					Config.getDatabaseUsername(),
					Config.getDatabasePassword());
			System.out.println("UserDatabase added");
			this.systemDatabase = new WishListDatabase(
					Config.getSystemDatabaseServerAddress(),
					Config.getDatabaseUsername(),
					Config.getDatabasePassword());
			System.out.println("systemDatabase added");
		}
		catch (ConfigNotInitializedException e)
		{
			System.out.println("Config not initialized!");
			System.exit(1);
		}
		catch (Exception e)
		{
			System.out.println(Arrays.toString(e.getStackTrace()));
			System.exit(1);
		}
	}
    //method for setting up the logging system
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
				    checkIfSaveLog();
                    // -- block until a client comes along (listen for the phone to ring)
                    Socket socket = serversocket.accept();

                    // -- connection accepted, create a peer-to-peer socket
                    //    between the server (thread) and client (route the call to the requested extension)
                    peerConnection(socket);
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

    private void checkIfSaveLog() {
	    this.currentTime = LocalDateTime.now();
	    int currentDay = currentTime.getDayOfYear();
	    int startDay = startTime.getDayOfYear();
	    if(startDay == 365){
	        if(currentDay != 365){
	            saveLog();
            }
        }
	    else if(currentDay > startDay){
	        saveLog();
        }
    }

    private void saveLog() {
        try {
            String fileName = "\\Logs\\"+ dtf.format(currentTime) + "Log.txt";
            File logFile = new File(fileName);
            if (logFile.createNewFile()) {
                System.out.println("File created: " + logFile.getName());
                FileWriter fw = new FileWriter(fileName);
                fw.write(servergui.getLogText());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }


    /**
	 * creates a direct (peer-to-peer) connection between the client and the server
	 * via a thread
	 * 
	 * @param socket: socket from the ServerSocket.listen() call
	 */
	public void peerConnection(Socket socket)
	{		
		// -- when a client arrives, create a thread for their communication
		ClientHandler connection = new ClientHandler(nextId, socket, this, this.servergui);

		// -- add the thread to the active client threads list
		clientConnections.add(connection);
		
		// -- start the thread
		connection.start();

		// -- place some text in the area to let the server operator know
		//    what is going on
		System.out.println("SERVER: connection received for id " + nextId + "\n");
		servergui.addToTextArea("SERVER: connection received for id " + nextId);
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
		for (int i = 0; i < clientConnections.size(); ++i) {
			ClientHandler cc = clientConnections.get(i);
			long x = cc.getID();
			if (x == id) {
				// -- remove it from the active threads list
				//    the thread will terminate itself
				clientConnections.remove(i);
				
				// -- place some text in the area to let the server operator know
				//    what is going on
				System.out.println("SERVER: connection closed for client id " + id + "\n");
				servergui.addToTextArea("SERVER: connection closed for client id " + id);
				//decrements the ids of all the other connections after removed id
				for(int j = i; j < clientConnections.size(); j++){
				    clientConnections.get(j).decId();
                }
				nextId--;
				break;
			}
		}
	}
	//method to close the server socket
	protected void removeServerSocket(){
        try {
            this.serversocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //method to log out and  disconnect all the clients from the server
    protected void logoutAndDisconnectClients(){
	    int i = getConnections()-1;
        User usr;
        NetworkAccess na;
        while(getConnections()>0){
            System.out.println("Connection #" + i);
            na = clientConnections.get(i).getNetworkAccess();
            usr = clientConnections.get(i).getUser();
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
//            System.out.println("# of connections: " + getConnections());
            i = getConnections()-1;
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
	    //notifies the users that names have been cleared
	    new DrawNames(getUserDatabase());
    }

}
