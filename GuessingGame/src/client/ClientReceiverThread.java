package client;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientReceiverThread implements Runnable {
    private final AtomicBoolean connected;
    private final DataInputStream is;
    private static final Logger logger = Logger.getLogger(ClientReceiverThread.class.getName());

    public ClientReceiverThread(AtomicBoolean connected, DataInputStream is) {
        this.connected = connected;
        this.is = is;
    }

    @Override
    public void run() {
        while (connected.get()) {
            try {
                String message = is.readUTF();
                // Stop if message indicates a termination
                if ("Ok".equals(message)) {
                    connected.set(false);
                }
                System.out.println(message);
            } catch (EOFException e) {
                logger.log(Level.INFO, "Reached end of stream.", e);
                connected.set(false);
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Error reading message from server", e);
                connected.set(false);
            }
        }
    }
}
