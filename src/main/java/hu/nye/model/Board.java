package hu.nye.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.IntStream;

/**
 * Represents the game board for a connect-4-like game.
 */
public final class Board {
    /** The grid representing the game board. */
    private final char[][] grid;

    /** Number of rows on the board. */
    private final int rows;

    /** Number of columns on the board. */
    private final int cols;

    /** Number of consecutive symbols to win. */
    private static final int WINNING_COUNT = 4;

    /** Filename to load the board from. */
    private static final String LOAD_FILENAME = "loadBoard.txt";

    /** Filename to save the board to. */
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
        loadBoardFromFile(); // Try loading the board from the load file
    }

    /**
     * Loads the board state from a file.
     * If the file does not exist or is invalid,
     * the board is initialized to be empty.
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
     * Clears the board to empty, and fills all the cells with '.'.
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
                return false;
            }
        }
        return true;
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
     * @return true if the board is full, false if there is still an empty cell.
     */
    public boolean isFull() {
        return IntStream.range(0, cols).noneMatch(col -> grid[0][col] == '.');
    }

    /**
     * Checks if a player has won by getting
     * the needed number of consecutive symbols.
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
     * Checks for the presence of the required number of consecutive
     * symbols in a specified direction.
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


            if (r >= 0 && r < rows && c >= 0 && c < cols) {
                if (grid[r][c] == symbol) {
                    count++;
                } else {
                    break;
                }
            } else {
                // Ha a sor vagy oszlop érvénytelen, megszakítjuk a ciklust
                break;
            }
        }
        return count == WINNING_COUNT;
    }


    /**
     * Saves the current board state to a file when the game is over.
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
     * Gets the grid representing the current state of the board.
     * This method can be overridden in subclasses to provide
     * a custom board representation.
     *
     * @return The 2D character array representing the board grid.
     */
    public char[][] getGrid() {
        return this.grid;
    }
} // Ensure there's a newline at the end of the file.
