package server;

import java.util.logging.Logger;

public class GameController {
    private final Game game;
    private final ServerBroadcastThread serverBroadcastThread;
    private static final Logger logger = Logger.getLogger(GameController.class.getName());

    public GameController(Game game, ServerBroadcastThread serverBroadcastThread) {
        this.game = game;
        this.serverBroadcastThread = serverBroadcastThread;
    }

    /**
     * Starts the game by picking a new number-choosing player and notifying everyone.
     */
    public void startGame() {
        game.clearNumber();
        String choosingPlayer = game.pickNewChoosingPlayer();
        serverBroadcastThread.broadcastMessage("Game started. " + choosingPlayer + " will choose a number.");
    }

    /**
     * Allows the current number-choosing player to set the number to guess.
     *
     * @param username The player attempting to choose the number.
     * @param number   The number chosen.
     */
    public void chooseNumber(String username, int number) {
        if (!username.equals(game.getCurrentNumberChoosingPlayer())) {
            serverBroadcastThread.sendMessageToUser(username, username + ", you are not the designated number chooser.");
            return;
        }
        game.chooseNumber(number);
        serverBroadcastThread.broadcastMessage(username + " has chosen a number. Let the guessing begin!");
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
            serverBroadcastThread.broadcastMessage(username + " guessed correctly! The number was " + guess + ".");
            // Restart the game with a new chooser.
            startGame();
        } else {
            serverBroadcastThread.sendMessageToUser(username, "Wrong guess, try again!");
        }
    }
}
