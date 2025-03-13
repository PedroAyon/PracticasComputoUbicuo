package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameServer {
    private static final int PORT = 10001;
    private static final Logger logger = Logger.getLogger(GameServer.class.getName());
    private static final int MIN_PLAYERS = 2;
    private static boolean gameStarted = false;

    public static void main(String[] args) {
        Game gameInstance = new Game();

        LinkedBlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();
        HashMap<String, DataOutputStream> clientMap = new HashMap<>();


        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            logger.info("The server started on port " + PORT + ". To stop it press <CTRL><C>.");

            // Start the thread that handles broadcasting generic messages.
            ServerBroadcastThread serverBroadcastThread = new ServerBroadcastThread(messageQueue, clientMap);
            new Thread(serverBroadcastThread).start();
            GameController gameController = new GameController(gameInstance, serverBroadcastThread);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                DataInputStream input = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());

                String username = input.readUTF();
                logger.info(username + " connected");

                try {
                    gameInstance.addPlayer(username);
                    clientMap.put(username, output);
                    output.writeUTF("Welcome " + username + "! You are now connected.\n" +
                            "Use 'choose <number>' to set the secret number\n" +
                            "or 'guess <number>' to guess.");
                } catch (PlayerAlreadyExistsException e) {
                    logger.warning("El usuario " + username + " ya existe.");
                    output.writeUTF("El usuario ya existe, por favor elige otro nombre de usuario.");
                    clientSocket.close();
                    return;                 }

                if (clientMap.size() >= MIN_PLAYERS && !gameStarted) {
                    gameController.startGame();
                    gameStarted =true;
                }

                // Start a thread to handle communication with this client,
                // passing the GameController for game-specific commands.
                new Thread(new ServerClientThread(username, clientSocket, input,
                        messageQueue, clientMap, gameController, serverBroadcastThread)).start();
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Server encountered an error", ex);
        }
    }
}
