package hu.nye.model;

import java.util.Random;
import java.util.Scanner;

/**
 * Represents a Connect-4-like game with a human player versus an AI.
 */
public final class Game {
    /** The human player in the game. */
    private final Player human;

    /** The AI player in the game. */
    private final Player ai;

    /** The game board. */
    private final Board board;

    /** Flag to determine if it's the human's turn. */
    private boolean humanTurn;

    /** Number of rows on the board. */
    private static final int BOARD_ROWS = 6;

    /** Number of columns on the board. */
    private static final int BOARD_COLS = 7;

    /**
     * Constructs a new Game instance with a human player and an AI player,
     * and initializes the game board.
     */
    public Game() {
        this.human = new Player("Human", 'Y');
        this.ai = new Player("Computer", 'R');
        this.board = new Board(BOARD_ROWS, BOARD_COLS);
        this.humanTurn = true;
    }

    /**
     * Getter for the game board.
     *
     * @return the current state of the game board.
     */
    public Board getBoard() {
        return this.board;
    }

    /**
     * Executes the AI's move. The AI randomly
     * selects a column and places its disc.
     */
    public void aiMove() {
        int column = new Random().nextInt(BOARD_COLS);
        while (!board.placeDisc(column, ai.getSymbol())) {
            column = new Random().nextInt(BOARD_COLS);
        }
    }

    /**
     * Starts the game loop where players take
     * turns until there is a winner or a draw.
     * The human player and the AI alternate turns to place discs on the board.
     * When the game ends, the board is saved to a file.
     */
    public void start() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            board.printBoard();
            if (board.isFull()) {
                System.out.println("The board is full! It's a draw.");
                break;
            }

            if (humanTurn) {
                System.out.println(human.getName() + "'s turn. Enter column (0-"
                        + (BOARD_COLS - 1) + "): ");

                if (scanner.hasNextInt()) {
                    int column = scanner.nextInt();
                    if (!board.placeDisc(column, human.getSymbol())) {
                        System.out.println("Column is full! Try another one.");
                        continue;
                    }
                    if (board.checkWin(human.getSymbol())) {
                        board.printBoard();
                        System.out.println(human.getName() + " wins!");
                        break;
                    }
                } else {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.next();  // Clear the invalid input
                }
            } else {
                System.out.println(ai.getName() + "'s turn.");
                int column = new Random().nextInt(BOARD_COLS);
                while (!board.placeDisc(column, ai.getSymbol())) {
                    column = new Random().nextInt(BOARD_COLS);
                }
                if (board.checkWin(ai.getSymbol())) {
                    board.printBoard();
                    System.out.println(ai.getName() + " wins!");
                    break;
                }
            }
            humanTurn = !humanTurn;
        }

        // Save the game board to a save file when the game ends
        board.saveBoardToFile();
    }
}
