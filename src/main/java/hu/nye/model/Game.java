package hu.nye.model;

import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

/**
 * Represents the game logic for a connect-four style game.
 * Handles player and computer turns,
 * checks for a winner, and saves or loads the game state.
 */
public final class Game {

    /** The number of columns in the game board. */
    private static final int BOARD_COLUMNS = 7;

    /** The maximum column index allowed for moves. */
    private static final int COLUMN_RANGE = 6;

    /** The game board where moves are made. */
    private final Board gameBoard;

    /** The human player participating in the game. */
    private final Player humanPlayer;

    /** The computer opponent in the game. */
    private final Player computerPlayer;

    /** Scanner to read player input from the console. */
    private final Scanner inputScanner;

    /** Random number generator to decide the computer's moves. */
    private final Random randomGenerator;

    /**
     * Constructs a new Game object.
     *
     * @param player the player of the game
     * @param board the game board
     * @param scanner a scanner for reading input
     * @param random a random number generator for computer moves
     */
    public Game(final Player player, final Board board,
                final Scanner scanner, final Random random) {
        // Assigning the parameters to the instance fields
        this.gameBoard = board;
        this.humanPlayer = player;
        this.computerPlayer = new Player("Computer", 'Y');
        this.inputScanner = scanner;
        this.randomGenerator = random;
    }

    /**
     * Starts the game, alternating between player
     * and computer turns until the board is full
     * or there is a winner.
     */
    public void start() {
        boolean isPlayerTurn = true;

        while (!gameBoard.isFull()) {
            if (isPlayerTurn) {
                playerTurn();
            } else {
                computerTurn();
            }

            // Check for a winner after the move
            if (gameBoard.checkWin(isPlayerTurn ? humanPlayer.symbol()
                    : computerPlayer.symbol())) {
                gameBoard.display();
                System.out.println((isPlayerTurn ? humanPlayer.name()
                        : "Computer") + (isPlayerTurn ? ", you won!"
                        : " wins! Better luck next time."));
                saveFinalGame();
                return;
            }

            isPlayerTurn = !isPlayerTurn;  // Switch turns
            gameBoard.display();  // Display board after each move
        }

        System.out.println("The game is a draw!");
        saveFinalGame();
    }

    /**
     * Handles the player's turn by prompting
     * them to select a column to make a move.
     * If the column is full, it retries until a valid move is made.
     */
    private void playerTurn() {
        int col = getPlayerMove();  // Input validation happens in getPlayerMove
        if (gameBoard.makeMove(col, humanPlayer.symbol())) {
            System.out.println("Column is full, try another one.");
            playerTurn();  // Retry if the column is full
        }
    }

    /**
     * Handles the computer's turn
     * by randomly selecting a column to make a move.
     * If the selected column is full, it retries until a valid move is made.
     */
    private void computerTurn() {
        System.out.println("Computer's turn.");
        int col;
        do {
            col = randomGenerator.nextInt(BOARD_COLUMNS);
        } while (gameBoard.makeMove(col, computerPlayer.symbol()));
    }

    /**
     * Prompts the player to enter a valid column number for their move.
     *
     * @return the column number selected by the player
     */
    private int getPlayerMove() {
        int col = -1;
        boolean validInput = false;

        while (!validInput) {
            try {
                System.out.print("Enter column (0-" + COLUMN_RANGE + ") "
                        + "to make your move: ");
                col = inputScanner.nextInt();  // Reading input from the player
                if (col < 0 || col > COLUMN_RANGE) {
                    System.out.println("Invalid column. Please enter "
                            + "a number between 0 and " + COLUMN_RANGE + ".");
                } else {
                    validInput = true;  // Valid column, exit loop
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a "
                        + "valid number between 0 and " + COLUMN_RANGE + ".");
                inputScanner.next();  // Clear the invalid input
            }
        }

        return col;
    }

    /**
     * Saves the final game state to a file named 'saved_game.txt'.
     * Notifies the user if the save was successful or if it failed.
     */
    public void saveFinalGame() {
        try {
            gameBoard.saveFinalGame("saved_game.txt");
            System.out.println("Game result saved to 'saved_game.txt'.");
        } catch (IOException e) {
            System.out.println("Failed to save game result: " + e.getMessage());
        }
    }

    /**
     * Loads the initial board state from a file named 'board_input.txt'.
     * Displays the loaded board if successful
     * or starts with a default board if not.
     */
    public void loadInitialBoard() {
        try {
            gameBoard.loadInitialBoard("board_input.txt");
            System.out.println("Loaded initial board from 'board_input.txt'.");
            gameBoard.display();  // Display the loaded board
        } catch (IOException e) {
            System.out.println("No initial board found. "
                    + "Starting with default empty board.");
        }
    }
}
