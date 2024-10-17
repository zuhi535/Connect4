package hu.nye.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * Represents a Connect Four game board.
 */
public final class Board {
    /** The number of rows on the game board. */
    private static final int ROWS = 6;

    /** The number of columns on the game board. */
    private static final int COLS = 7;

    /** The number of consecutive pieces required to win. */
    private static final int WIN_SEQUENCE = 4;

    /** The symbol representing an empty slot on the board. */
    private static final char EMPTY_SLOT = '-';

    /** The 2D array representing the game board. */
    private final char[][] board;

    /**
     * Initializes an empty board.
     */
    public Board() {
        board = new char[ROWS][COLS];
        for (char[] row : board) {
            Arrays.fill(row, EMPTY_SLOT);
        }
    }

    /**
     * Loads the initial board from a file.
     *
     * @param filePath the path to the file containing the initial board state
     * @throws IOException if an I/O error occurs
     */
    public void loadInitialBoard(final String filePath) throws IOException {
        try (InputStream inputStream = getClass()
                .getClassLoader()
                .getResourceAsStream(filePath);
             BufferedReader reader = (inputStream != null)
                     ? new BufferedReader(new InputStreamReader(inputStream))
                     : null) {

            if (reader == null) {
                System.out.println("No initial board file found. "
                        + "Starting with an empty board.");
                // Initialize the board with empty cells
                for (int row = 0; row < ROWS; row++) {
                    Arrays.fill(board[row], '-'); // Use '-' for empty cells
                }
                return;
            }

            for (int i = 0; i < ROWS; i++) {
                String line = reader.readLine();
                if (line != null) {
                    line = line.trim();  // Remove any extra whitespace
                    if (line.length() == COLS) {
                        board[i] = line.toCharArray();
                    } else {
                        System.out.println("Invalid row length at line "
                                + (i + 1) + ". Expected " + COLS
                                + " characters, but found " + line.length());
                    }
                } else {
                    // If line is null, fill the
                    // rest of the board with empty cells
                    Arrays.fill(board[i], '-');
                }
            }
        }
    }

    /**
     * Saves the final state of the game to a file.
     *
     * @param filePath the file path where the board will be saved
     * @throws IOException if an I/O error occurs
     */
    public void saveFinalGame(final String filePath)
            throws IOException {
        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter(filePath, true))) {
            for (int i = 0; i < ROWS; i++) {
                writer.write(new String(board[i]));
                writer.newLine();
            }
            // Indicates the end of a game
            writer.write("====\n");
        }
    }

    /**
     * Makes a move in the specified column for the given player.
     *
     * @param col the column to place the piece
     * @param playerSymbol the symbol representing the player
     * @return true if the move fails (column is full), false otherwise
     */
    public boolean makeMove(final int col, final char playerSymbol) {
        for (int i = ROWS - 1; i >= 0; i--) {
            if (board[i][col] == EMPTY_SLOT) {
                board[i][col] = playerSymbol;
                return false;
            }
        }
        return true;  // If the column is full
    }

    /**
     * Checks if the board is full.
     *
     * @return true if the board is full, false otherwise
     */
    public boolean isFull() {
        for (int i = 0; i < COLS; i++) {
            if (board[0][i] == EMPTY_SLOT) {
                return false;
            }
        }
        return true;
    }

    /**
     * Displays the current state of the board.
     */
    public void display() {
        System.out.println("Current board:");
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    /**
     * Checks if the specified player has won the game.
     *
     * @param playerSymbol the symbol representing the player
     * @return true if the player has won, false otherwise
     */
    public boolean checkWin(final char playerSymbol) {
        return checkHorizontalWin(playerSymbol)
                || checkVerticalWin(playerSymbol)
                || checkDiagonalWinAscending(playerSymbol)
                || checkDiagonalWinDescending(playerSymbol);
    }

    private boolean checkHorizontalWin(final char playerSymbol) {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col <= COLS - WIN_SEQUENCE; col++) {
                if (checkWinningSequence(row, col, playerSymbol, 0, 1)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkVerticalWin(final char playerSymbol) {
        for (int row = 0; row <= ROWS - WIN_SEQUENCE; row++) {
            for (int col = 0; col < COLS; col++) {
                if (checkWinningSequence(row, col, playerSymbol, 1, 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkDiagonalWinDescending(final char playerSymbol) {
        for (int row = 0; row <= ROWS - WIN_SEQUENCE; row++) {
            for (int col = 0; col <= COLS - WIN_SEQUENCE; col++) {
                if (checkWinningSequence(row, col, playerSymbol, 1, 1)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkDiagonalWinAscending(final char playerSymbol) {
        for (int row = WIN_SEQUENCE - 1; row < ROWS; row++) {
            for (int col = 0; col <= COLS - WIN_SEQUENCE; col++) {
                if (checkWinningSequence(row, col, playerSymbol, -1, 1)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks for a winning sequence in the specified direction.
     *
     * @param startRow The starting row of the sequence.
     * @param startCol The starting column of the sequence.
     * @param playerSymbol The symbol of the player.
     * @param rowIncrement The increment for the row.
     * @param colIncrement The increment for the column.
     * @return true if a winning sequence is found, false otherwise.
     */
    private boolean checkWinningSequence(final int startRow, final int startCol,
                                         final char playerSymbol,
                                         final int rowIncrement,
                                         final int colIncrement) {
        for (int i = 0; i < WIN_SEQUENCE; i++) {
            int currentRow = startRow + (i * rowIncrement);
            int currentCol = startCol + (i * colIncrement);
            if (board[currentRow][currentCol] != playerSymbol) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the current state of the board for testing.
     *
     * @return a 2D char array representing the board
     */
    public char[][] getBoard() {
        return this.board;
    }

    /**
     * Returns the current state of the board for a specific row.
     *
     * @param i The index of the row to return.
     * @return a char array representing the row.
     * @throws ArrayIndexOutOfBoundsException if the index is out of bounds.
     */
    public char[] getRow(final int i) {
        if (i < 0 || i >= board.length) {
            throw new ArrayIndexOutOfBoundsException("Index " + i + " out of"
                    + " bounds for length " + board.length);
        }
        return board[i];
    }
}
