package server;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    private final List<String> players = new ArrayList<>();
    private String currentNumberChoosingPlayer;
    private int numberToGuess;
    private final Random rand = new Random();

    public void addPlayer(String player) throws PlayerAlreadyExistsException {
        if (players.contains(player)) {
            throw new PlayerAlreadyExistsException();
        }
        players.add(player);
    }

    public String pickNewChoosingPlayer() {
        if (players.isEmpty()) {
            return null;
        }
        // If no player has been chosen yet, simply pick one.
        if (currentNumberChoosingPlayer == null) {
            currentNumberChoosingPlayer = players.get(rand.nextInt(players.size()));
            return currentNumberChoosingPlayer;
        }
        // Otherwise, pick a new player until it's different from the current chooser.
        String newChoosingPlayer;
        do {
            newChoosingPlayer = players.get(rand.nextInt(players.size()));
        } while (currentNumberChoosingPlayer.equals(newChoosingPlayer));
        currentNumberChoosingPlayer = newChoosingPlayer;
        return newChoosingPlayer;
    }

    public String getCurrentNumberChoosingPlayer() {
        return currentNumberChoosingPlayer;
    }

    public void chooseNumber(int number) {
        numberToGuess = number;
    }

    public Boolean guess(int number) {
        if (number < 0) return false;
        return numberToGuess == number;
    }

    public void clearNumber() {
        numberToGuess = -1;
    }
}
