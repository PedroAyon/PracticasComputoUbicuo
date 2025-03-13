package server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerBroadcastThread implements Runnable {
    private final HashMap<String, DataOutputStream> clientMap;
    private final LinkedBlockingQueue<String> messageQueue;
    private static final Logger logger = Logger.getLogger(ServerBroadcastThread.class.getName());

    public ServerBroadcastThread(LinkedBlockingQueue<String> messageQueue,
                                 HashMap<String, DataOutputStream> clientMap) {
        this.clientMap = clientMap;
        this.messageQueue = messageQueue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                // Take a message from the queue (this call blocks if the queue is empty)
                String message = messageQueue.take();
                logger.info("Broadcasting message: " + message);
                String sender = message.split(":")[0];

                // Send the message to all clients except the sender
                for (String user : clientMap.keySet()) {
                    if (!user.equals(sender)) {
                        DataOutputStream stream = clientMap.get(user);
                        stream.writeUTF(message);
                    }
                }
            } catch (InterruptedException e) {
                logger.log(Level.WARNING, "Broadcast thread interrupted", e);
                Thread.currentThread().interrupt();
                break;
            } catch (IOException ex) {
                logger.log(Level.SEVERE, "Error sending message", ex);
            }
        }
    }

    /**
     * Sends a message to a specific user.
     */
    public void sendMessageToUser(String username, String message) {
        DataOutputStream out = clientMap.get(username);
        if (out != null) {
            try {
                out.writeUTF(message);
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Error sending message to " + username, e);
            }
        }
    }

    /**
     * Broadcasts a message to all connected clients.
     */
    public void broadcastMessage(String message) {
        clientMap.forEach((user, out) -> {
            try {
                out.writeUTF(message);
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Error sending message to " + user, e);
            }
        });
    }
}
