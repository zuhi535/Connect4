package hu.nye.model;

public class Board {
    private final char[][] board;
    private final int rows;
    private final int columns;
    private final char emptySlot = '-';

    // Default constructor with 6 rows and 7 columns
    public Board() {
        this(6, 7);
    }

    // Parameterized constructor that sets up rows, columns, and initializes the board
    public Board(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.board = new char[rows][columns];
        initializeEmptyBoard();  // Initialize all slots as empty
    }

    // Initializes all slots to the empty character
    public void initializeEmptyBoard() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                board[i][j] = emptySlot;
            }
        }
    }

    // Sets a row with the given character array, for use in loading a saved game state
    public void setRow(int rowIndex, char[] rowData) {
        if (rowIndex >= 0 && rowIndex < rows && rowData.length == columns) {
            board[rowIndex] = rowData;
        } else {
            throw new IllegalArgumentException("Invalid row index or row data length");
        }
    }

    // Gets the specified row as a character array, useful for checking if a column is full
    public char[] getRow(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < rows) {
            return board[rowIndex];
        } else {
            throw new IllegalArgumentException("Invalid row index");
        }
    }

    // Returns the entire board for saving and displaying purposes
    public char[][] getBoard() {
        return board;
    }

    // Method to retrieve the symbol at a specific row and column
    public char getSlot(int row, int col) {
        if (row >= 0 && row < rows && col >= 0 && col < columns) {
            return board[row][col];
        } else {
            throw new IllegalArgumentException("Invalid row or column index");
        }
    }

    // Getter for number of rows
    public int getRows() {
        return rows;
    }

    // Getter for number of columns
    public int getCols() {
        return columns;
    }

    // Getter for empty slot character
    public char getEmptySlot() {
        return emptySlot;
    }

    // Checks if the board is full (no empty slots remaining)
    public boolean isFull() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (board[i][j] == emptySlot) {
                    return false;
                }
            }
        }
        return true;
    }

    // Method to place a player's symbol in the specified column
    public boolean makeMove(int column, char player) {
        if (column < 0 || column >= columns) {
            return false;
        }
        for (int i = rows - 1; i >= 0; i--) {
            if (board[i][column] == emptySlot) {
                board[i][column] = player;
                return true;
            }
        }
        return false;  // Column is full
    }

    // Method to check for a win condition (four in a row)
    public boolean checkWin(char player) {
        // Horizontal check
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns - 3; col++) {
                if (board[row][col] == player &&
                        board[row][col + 1] == player &&
                        board[row][col + 2] == player &&
                        board[row][col + 3] == player) {
                    return true;
                }
            }
        }

        // Vertical check
        for (int col = 0; col < columns; col++) {
            for (int row = 0; row < rows - 3; row++) {
                if (board[row][col] == player &&
                        board[row + 1][col] == player &&
                        board[row + 2][col] == player &&
                        board[row + 3][col] == player) {
                    return true;
                }
            }
        }

        // Diagonal check (top-left to bottom-right)
        for (int row = 0; row < rows - 3; row++) {
            for (int col = 0; col < columns - 3; col++) {
                if (board[row][col] == player &&
                        board[row + 1][col + 1] == player &&
                        board[row + 2][col + 2] == player &&
                        board[row + 3][col + 3] == player) {
                    return true;
                }
            }
        }

        // Diagonal check (top-right to bottom-left)
        for (int row = 0; row < rows - 3; row++) {
            for (int col = 3; col < columns; col++) {
                if (board[row][col] == player &&
                        board[row + 1][col - 1] == player &&
                        board[row + 2][col - 2] == player &&
                        board[row + 3][col - 3] == player) {
                    return true;
                }
            }
        }

        return false;  // No four-in-a-row found
    }

    // Display the board with row separators and column indexes
    public void display() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }

        // Display separator line
        for (int j = 0; j < columns; j++) {
            System.out.print("--");
        }
        System.out.println();

        // Display the column numbers below the board
        for (int j = 0; j < columns; j++) {
            System.out.print(j + " ");
        }
        System.out.println();
    }
}
