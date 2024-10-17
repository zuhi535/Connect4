package hu.nye;

import hu.nye.model.Game;
import hu.nye.model.Player;
import hu.nye.model.Board;

import java.util.Random;
import java.util.Scanner;

/**
 * Main class for starting the Connect4 game.
 */
public final class App {

    /**
     * Private constructor to prevent instantiation.
     */
    private App() {
        // This constructor is intentionally empty to prevent instantiation.
    }

    /**
     * Main method for starting the Connect4 game.
     *
     * @param args the command line arguments (not used)
     */
    public static void main(final String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();  // Initialize Random object

        System.out.println("Welcome to Connect4!");
        System.out.print("Enter player name: ");
        String playerName = scanner.nextLine();

        Player player = new Player(playerName, 'R');

        // Create a new board object
        Board board = new Board();

        // Pass Scanner and Random to the Game object
        Game game = new Game(player, board, scanner, random);

        // Load the initial board state from 'board_input.txt'
        game.loadInitialBoard();

        // Start the game
        game.start();
    }
}
