package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerClientThread implements Runnable {
    private final String username;
    private final Socket clientSocket;
    private final DataInputStream input;
    private final LinkedBlockingQueue<String> messageQueue;
    private final HashMap<String, DataOutputStream> clientMap;
    private final ArrayList<String> messageHistory;
    private final GameCommunicator gameComm;
    private boolean connected = true;
    private static final Logger logger = Logger.getLogger(ServerClientThread.class.getName());

    public ServerClientThread(String username, Socket clientSocket, DataInputStream input,
                              LinkedBlockingQueue<String> messageQueue,
                              HashMap<String, DataOutputStream> clientMap,
                              ArrayList<String> messageHistory,
                              GameCommunicator gameComm) {
        this.username = username;
        this.clientSocket = clientSocket;
        this.input = input;
        this.messageQueue = messageQueue;
        this.clientMap = clientMap;
        this.messageHistory = messageHistory;
        this.gameComm = gameComm;
    }

    @Override
    public void run() {
        try {
            while (connected) {
                String received = input.readUTF();
                processMessage(received);
            }
        } catch (SocketException e) {
            logger.log(Level.WARNING, String.format("Client [%s] disconnected unexpectedly: %s", username, e.getMessage()), e);
        } catch (EOFException e) {
            logger.info(String.format("Client [%s] closed connection (EOF)", username));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to read message from " + username, e);
        } finally {
            clientMap.remove(username);
            try {
                clientSocket.close();
            } catch (IOException e) {
                logger.log(Level.WARNING, "Failed to close client socket for " + username, e);
            }
        }
    }

    private void processMessage(String received) {
        logger.info("Received from " + username + ": " + received);

        // Process game-specific commands.
        if (received.startsWith("choose ")) {
            try {
                int number = Integer.parseInt(received.split(" ")[1]);
                gameComm.chooseNumber(username, number);
            } catch (NumberFormatException e) {
                gameComm.sendMessageToUser(username, "Invalid number format for choose command.");
            }
        } else if (received.startsWith("guess ")) {
            try {
                int guess = Integer.parseInt(received.split(" ")[1]);
                gameComm.guessNumber(username, guess);
            } catch (NumberFormatException e) {
                gameComm.sendMessageToUser(username, "Invalid number format for guess command.");
            }
        } else {
            // For all other messages, add them to the chat and message history.
            messageQueue.add(username + ": " + received);
            messageHistory.add(received);
        }
    }
}
