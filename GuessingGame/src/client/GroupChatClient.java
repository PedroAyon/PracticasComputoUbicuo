package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GroupChatClient {
    private static final String HOST = "localhost";
    private static final int PORT = 10001;
    private static final Logger logger = Logger.getLogger(GroupChatClient.class.getName());

    public static void main(String[] args) {
        AtomicBoolean connected = new AtomicBoolean(false);

        try {
            Socket clientSocket = new Socket(HOST, PORT);
            logger.info("Connected to server at " + HOST + ":" + PORT);

            // Streams will be closed when the socket is closed
            DataInputStream is = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream os = new DataOutputStream(clientSocket.getOutputStream());
            connected.set(true);

            // Start sender and receiver threads
            new Thread(new ClientSenderThread(connected, os)).start();
            new Thread(new ClientReceiverThread(connected, is)).start();
        } catch (UnknownHostException e) {
            logger.log(Level.SEVERE, "Unknown host: " + HOST, e);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Couldn't get I/O for the connection to host", e);
        }
    }
}
