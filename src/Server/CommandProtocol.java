package Server;

import java.util.HashMap;
import java.util.Set;

import Common.Message;
import Common.NetworkAccess;


/**
 * @author reinhart
 */
public class CommandProtocol {

    /**
     * commands and their responses
     */
    private static final HashMap<String, String> commands;

    static {
        commands = new HashMap<>();
        commands.put("disconnect", "");
        commands.put("hello", "world!");
    }

    /**
     * process commands sent to the server
     *
     * @param cmd: command to be processed
     * @param na:  NetworkAccess object for communication
     * @param ch:  ClientHandler object requesting the processing
     * @return
     */
    public static void processCommand(Message cmd, NetworkAccess na, ClientHandler ch) {
        if (cmd.message.equals("disconnect")) {

            // -- no response to the client is necessary
            na.close();
            ch.getServer().removeID(ch.getID());
            ch.Stop();
        } else if (cmd.message.equals("hello")) {

            // -- client is expecting a response
            na.sendMessage(new Message(null, "world!" + "\n"), false);

        } else {

            na.sendMessage(cmd, false);

        }
    }
}
