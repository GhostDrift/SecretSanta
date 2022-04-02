package Common;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class NetworkAccess {
    /**
     * socket variable for peer to peer communication
     * this is a peer-to-peer connection, either TCP/IP or UDP
     */
    private Socket socket;

    /**
     * stream variables for peer to peer communication
     * to be opened on top of the socket
     */
    private ObjectInputStream dataIn;
    private ObjectOutputStream dataOut;

    /**
     * Constructor performs connection construction for the client
     * this will create a socket based on the IP address and the port number
     *
     * @param ip:   IP address of the server
     * @param port: port number on which the server is listening
     */
    public NetworkAccess(String ip, int port) throws ConnectException {
        try {
            // -- construct the peer to peer socket
            //    check if the server is available and connects if it is,
            //    if not throw an exception
            socket = new Socket(ip, port);
            // create object input and output streams
            dataOut = new ObjectOutputStream(socket.getOutputStream());
            dataIn = new ObjectInputStream(socket.getInputStream());

        } catch (UnknownHostException e) {

            System.out.println("Host " + ip + " at port " + port + " is unavailable.");
            System.exit(1);

        } catch (ConnectException e) {
            System.out.println("Host " + ip + " at port " + port + " is unreachable.");
            throw e;
        } catch (IOException e) {

            System.out.println("Unable to create I/O streams.");
            System.exit(1);
        }

    }

    /**
     * Constructor performs connection construction for the server.
     * the server will provide the socket as received from the ServerSocket.listen()
     *
     * @param socket: socket provided by the server ServerSocket
     */
    public NetworkAccess(Socket socket) {
        try {
            // -- construct the peer to peer socket
            //    check if the server is available and connects if it is,
            //    if not throw an exception
            this.socket = socket;

            // -- Create object input and output streams
            dataIn = new ObjectInputStream(socket.getInputStream());
            dataOut = new ObjectOutputStream(socket.getOutputStream());

        } catch (IOException e) {

            System.out.println("Unable to create I/O streams.");
            System.exit(1);
        }
    }

    /**
     * reads message object from the input data stream
     *
     * @return string from the stream
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public Message readMessage() throws IOException {
        try {
            Message in = (Message) dataIn.readObject();
            System.out.println("readMessage got: " + in);
            return in;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }

    /**
     * send a Message object to the server and return the received hand-shake Message
     *
     * @param _msg:        the Message to be sent
     * @param acknowledge: whether to expect an acknowledgment string
     *                     client will set this to true except for disconnect
     *                     server will set it to false
     * @return
     */
    public Message sendMessage(Message _msg, boolean acknowledge) {
        Message rtnmsg = null;
        System.out.println("Message to be sent: " + _msg);

        // -- the protocol is this:
        //    client sends a Message object to the server.
        //    server receives Message, processes it, and returns a Message Object
        //    this is called a "hand-shake" system
        try {
            // -- send the Message making sure to flush the buffer
            dataOut.writeObject(_msg);
            dataOut.flush();

            if (acknowledge) {
                // -- receive the response from the server
                //    The do/while makes this a blocking read. Normally BufferedReader.readLine() is non-blocking.
                //    That is, if there is no Message to read, it will read null. Doing it this way does not allow
                //    that to occur. We must get a response from the server. Time out could be implemented with
                //    a counter.
                do {
                    // -- this is a non-blocking read
                    rtnmsg = (Message) dataIn.readObject();
                    System.out.println("reply was " + rtnmsg );
                } while (rtnmsg == null);
            }
        }
        catch(SocketException e){
            rtnmsg = new Message(null,"false");
        }catch (IOException | ClassNotFoundException e) {

            e.printStackTrace();
            System.exit(1);

        }

        return rtnmsg;

    }

    /**
     * close the connection (socket)
     */
    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            System.out.println("close: invalid socket");
        }
    }
    public boolean testConnection(){
        Message result = sendMessage(new Message(null,"hello"), true);
        System.out.println("Test result: " + result.message);
        return result.message.equals("world!");
    }
}
