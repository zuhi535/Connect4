package hu.nye.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Represents the game board for a connect-4-like game.
 */
public class Board {
    /** The grid representing the game board. */
    private final char[][] grid;

    /** Number of rows on the board. */
    private final int rows;

    /** Number of columns on the board. */
    private final int cols;

    /** The number of consecutive symbols required to win the game. */
    private static final int WINNING_COUNT = 4;

    /** The filename used to load the game board. */
    private static final String LOAD_FILENAME = "loadBoard.txt";

    /** The filename used to save the game board. */
    private static final String SAVE_FILENAME = "saveBoard.txt";

    /**
     * Constructs a new Board with the given number of rows and columns.
     *
     * @param boardRows The number of rows on the board.
     * @param boardCols The number of columns on the board.
     */
    public Board(final int boardRows, final int boardCols) {
        this.rows = boardRows;
        this.cols = boardCols;
        this.grid = new char[rows][cols];
        loadBoardFromFile();  // Try loading the board from the load file
    }

    /**
     * Loads the board state from a file.
     * If the file doesn't exist or is invalid,
     * the board is initialized as empty.
     */
    private void loadBoardFromFile() {
        File file = new File(LOAD_FILENAME);
        if (file.exists()) {
            try (Scanner scanner = new Scanner(file)) {
                for (int i = 0; i < rows; i++) {
                    String line = scanner.nextLine();
                    for (int j = 0; j < cols; j++) {
                        grid[i][j] = line.charAt(j);
                    }
                }
                System.out.println("Board loaded from " + LOAD_FILENAME);
            } catch (FileNotFoundException e) {
                System.out.println("Load file not found, "
                        + "starting with an empty board.");
                initializeEmptyBoard();
            }
        } else {
            System.out.println("No load file found, "
                    + "starting with an empty board.");
            initializeEmptyBoard();
        }
    }

    /**
     * Initializes the board to an empty state, filling all cells with '.'.
     */
    private void initializeEmptyBoard() {
        for (char[] row : grid) {
            Arrays.fill(row, '.');
        }
    }

    /**
     * Places a disc in the specified column.
     *
     * @param col The column in which to place the disc.
     * @param symbol The symbol representing the player placing the disc.
     * @return True if the disc was placed successfully,
     * false if the column is full or invalid.
     */
    public boolean placeDisc(final int col, final char symbol) {
        if (col < 0 || col >= cols) {
            return false;
        }

        for (int row = rows - 1; row >= 0; row--) {
            if (grid[row][col] == '.') {
                grid[row][col] = symbol;
                return true;
            }
        }
        return false;
    }

    /**
     * Prints the current state of the game board.
     */
    public void printBoard() {
        for (char[] row : grid) {
            for (char cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }

    /**
     * Checks if the board is full.
     *
     * @return True if the board is full, false if there is still an empty cell.
     */
    public boolean isFull() {
        for (int col = 0; col < cols; col++) {
            if (grid[0][col] == '.') {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if a player has won by getting
     * the required number of consecutive symbols.
     *
     * @param symbol The symbol of the player to check for a win.
     * @return True if the player has won, false otherwise.
     */
    public boolean checkWin(final char symbol) {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (checkDirection(row, col, 1, 0, symbol)
                        || checkDirection(row, col, 0, 1, symbol)
                        || checkDirection(row, col, 1, 1, symbol)
                        || checkDirection(row, col, 1, -1, symbol)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if there are the required number of consecutive
     * symbols in the specified direction.
     *
     * @param row Starting row.
     * @param col Starting column.
     * @param deltaRow Row increment direction.
     * @param deltaCol Column increment direction.
     * @param symbol The symbol to check for consecutive discs.
     * @return True if there are consecutive symbols, false otherwise.
     */
    private boolean checkDirection(final int row, final int col,
                                   final int deltaRow, final int deltaCol,
                                   final char symbol) {
        int count = 0;
        for (int i = 0; i < WINNING_COUNT; i++) {
            int r = row + i * deltaRow;
            int c = col + i * deltaCol;
            if (r >= 0 && r < rows && c >= 0 && c < cols
                    && grid[r][c] == symbol) {
                count++;
            } else {
                break;
            }
        }
        return count == WINNING_COUNT;
    }

    /**
     * Saves the current board state to a file at the end of the game.
     */
    public void saveBoardToFile() {
        try (FileWriter writer = new FileWriter(SAVE_FILENAME)) {
            for (char[] row : grid) {
                for (char cell : row) {
                    writer.write(cell);
                }
                writer.write("\n");
            }
            System.out.println("Board saved to " + SAVE_FILENAME);
        } catch (IOException e) {
            System.out.println("An error occurred while saving the board.");
        }
    }

    /**
     * Getter for the grid.
     * @return the grid of the board.
     */
    public char[][] getGrid() {
        return this.grid;
    }
}
