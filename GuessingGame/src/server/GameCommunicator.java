package server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameCommunicator {
    private final Game game;
    private final Map<String, DataOutputStream> clientMap;
    private static final Logger logger = Logger.getLogger(GameCommunicator.class.getName());

    public GameCommunicator(Game game, Map<String, DataOutputStream> clientMap) {
        this.game = game;
        this.clientMap = clientMap;
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

    /**
     * Starts the game by picking a new number-choosing player and notifying everyone.
     */
    public void startGame() {
        game.clearNumber();
        String choosingPlayer = game.pickNewChoosingPlayer();
        broadcastMessage("Game started. " + choosingPlayer + " will choose a number.");
    }

    /**
     * Allows the current number-choosing player to set the number to guess.
     *
     * @param username The player attempting to choose the number.
     * @param number   The number chosen.
     */
    public void chooseNumber(String username, int number) {
        if (!username.equals(game.getCurrentNumberChoosingPlayer())) {
            sendMessageToUser(username, username + ", you are not the designated number chooser.");
            return;
        }
        game.chooseNumber(number);
        broadcastMessage(username + " has chosen a number. Let the guessing begin!");
    }

    /**
     * Processes a guess from a player.
     *
     * @param username The player making the guess.
     * @param guess    The guessed number.
     */
    public void guessNumber(String username, int guess) {
        boolean correct = game.guess(guess);
        if (correct) {
            broadcastMessage(username + " guessed correctly! The number was " + guess + ".");
            // Restart the game with a new chooser.
            startGame();
        } else {
            sendMessageToUser(username, "Wrong guess, try again!");
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
}
