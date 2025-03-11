package client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientSenderThread implements Runnable {
    private final Scanner keyboard;
    private final DataOutputStream os;
    private final AtomicBoolean connected;
    private static final Logger logger = Logger.getLogger(ClientSenderThread.class.getName());

    public ClientSenderThread(AtomicBoolean connected, DataOutputStream os) {
        this.keyboard = new Scanner(System.in);
        this.os = os;
        this.connected = connected;
    }

    @Override
    public void run() {
        System.out.println("The client started. Type any text. To quit type 'Ok'.");
        while (connected.get()) {
            String message = keyboard.nextLine();
            try {
                os.writeUTF(message);
                // Optionally flush if needed: os.flush();
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Error sending message to server", e);
                connected.set(false);
            }
            if ("Ok".equals(message)) {
                connected.set(false);
            }
        }
        keyboard.close();
    }
}
