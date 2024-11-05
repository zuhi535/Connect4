package hu.nye.util;

import hu.nye.model.Board;

public class MoveValidator {

    public boolean isColumnFull(Board board, int col) {
        return board.getRow(0)[col] != board.getEmptySlot();
    }

    // Új metódus a négy egymás melletti jel ellenőrzéséhez
    public boolean hasFourInARow(Board board, char symbol) {
        int rows = board.getRows();
        int cols = board.getCols();

        // Vízszintes ellenőrzés
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols - 3; col++) {
                if (board.getSlot(row, col) == symbol &&
                        board.getSlot(row, col + 1) == symbol &&
                        board.getSlot(row, col + 2) == symbol &&
                        board.getSlot(row, col + 3) == symbol) {
                    return true;
                }
            }
        }

        // Függőleges ellenőrzés
        for (int col = 0; col < cols; col++) {
            for (int row = 0; row < rows - 3; row++) {
                if (board.getSlot(row, col) == symbol &&
                        board.getSlot(row + 1, col) == symbol &&
                        board.getSlot(row + 2, col) == symbol &&
                        board.getSlot(row + 3, col) == symbol) {
                    return true;
                }
            }
        }

        // Átlós ellenőrzés (bal felső - jobb alsó irány)
        for (int row = 0; row < rows - 3; row++) {
            for (int col = 0; col < cols - 3; col++) {
                if (board.getSlot(row, col) == symbol &&
                        board.getSlot(row + 1, col + 1) == symbol &&
                        board.getSlot(row + 2, col + 2) == symbol &&
                        board.getSlot(row + 3, col + 3) == symbol) {
                    return true;
                }
            }
        }

        // Átlós ellenőrzés (jobb felső - bal alsó irány)
        for (int row = 0; row < rows - 3; row++) {
            for (int col = 3; col < cols; col++) {
                if (board.getSlot(row, col) == symbol &&
                        board.getSlot(row + 1, col - 1) == symbol &&
                        board.getSlot(row + 2, col - 2) == symbol &&
                        board.getSlot(row + 3, col - 3) == symbol) {
                    return true;
                }
            }
        }

        return false;  // Ha nincs négyes sorozat
    }
}
