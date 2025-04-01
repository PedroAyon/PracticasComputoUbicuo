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

    public void startGame() {
        game.clearNumber();
        String choosingPlayer = game.pickNewChoosingPlayer();
        serverBroadcastThread.broadcastMessage("Game started. " + choosingPlayer + " will choose a number.");
    }


    public void chooseNumber(String username, int number) {
        if (!username.equals(game.getCurrentNumberChoosingPlayer())) {
            serverBroadcastThread.sendMessageToUser(username, username + ", you are not the designated number chooser.");
            return;
        }
        game.chooseNumber(number);
        serverBroadcastThread.broadcastMessage(username + " has chosen a number. Let the guessing begin!");
    }

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
