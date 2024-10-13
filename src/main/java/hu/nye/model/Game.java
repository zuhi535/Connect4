package hu.nye.model;

import java.util.Random;
import java.util.Scanner;

/** Connect-4 like game between human and AI player. */
public final class Game {
    /** Human player of the current game. */
    private final Player human;

    /** AI player of the current game. */
    private final Player ai;

    /** Game board. */
    private final Board board;

    /** Flag to determine if it's the human's turn. */
    private boolean humanTurn;

    /** Number of rows on the board. */
    private static final int BOARD_ROWS = 6;

    /** Number of columns on the board. */
    private static final int BOARD_COLS = 7;

    /**
     * Constructor that initializes a new Game with one
     * human player and one artificial intelligent player,
     * and prepares the game board.
     */
    public Game() {
        this.human = new Player("Human", 'Y');
        this.ai = new Player("Computer", 'R');
        this.board = new Board(BOARD_ROWS, BOARD_COLS);
        this.humanTurn = true;
    }

    /**
     * Starts the main game loop.
     * Alternates turns between the human and AI player until the game ends.
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
                    if (board.placeDisc(column, human.getSymbol())) {
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
                while (board.placeDisc(column, ai.getSymbol())) {
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

        // Save game board to a save file when the game is over.
        board.saveBoardToFile();
    }

    /**
     * Returns the current game board.
     *
     * @return the game board.
     */
    public Board getBoard() {
        return this.board;
    }

    /**
     * Placeholder method for AI move logic.
     */
    public void aiMove() {
        Random random = new Random();
        int column;
        do {
            column = random.nextInt(BOARD_COLS);
        } while (!board.placeDisc(column, ai.getSymbol()));
    }

    /**
     * Placeholder method for starting the game logic.
     */
    public void startGame() {
        // Game start logic can be implemented here.
    }
}
