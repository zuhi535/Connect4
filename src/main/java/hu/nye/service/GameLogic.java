package hu.nye.service;

import hu.nye.model.Board;

/**
 * Kezeli a játékszabályokat és a logikát, beleértve a mozdulatokat és a nyerési feltételeket.
 */
public class GameLogic {

    /**
     * A játékos szimbólumát a tábla megadott oszlopába helyezi.
     *
     * @param board A játéktábla.
     * @param column Az az oszlop, ahová a játékos el szeretné helyezni a szimbólumát.
     * @param player A játékos szimbóluma (pl. 'R' vagy 'Y').
     * @return Igaz, ha az áthelyezés sikeres volt, hamis egyébként (pl. tele van oszlop).
     */
    public boolean makeMove(final Board board, final int column, final char player) {
        if (column < 0 || column >= board.getCols()) {
            return false;
        }
        for (int i = board.getRows() - 1; i >= 0; i--) {
            if (board.getSlot(i, column) == board.getEmptySlot()) {
                board.setSlot(i, column, player);
                return true;
            }
        }
        return false;
    }

    /**
     * Ellenőrzi, hogy egy játékos megnyerte-e a játékot.
     *
     * @param board A játéktábla.
     * @param player A játékos szimbóluma a győzelem ellenőrzéséhez.
     * @return Igaz, ha a játékos nyert, hamis egyébként.
     */
    public boolean checkWin(final Board board, final char player) {
        return checkHorizontal(board, player) ||
                checkVertical(board, player) ||
                checkDiagonalTLBR(board, player) ||
                checkDiagonalTRBL(board, player);
    }

    /**
     * Ellenőrzi, hogy a tábla megtelt-e.
     *
     * @param board A játéktábla.
     * @return Igaz, ha a tábla megtelt, hamis egyébként.
     */
    public boolean isFull(final Board board) {
        for (int row = 0; row < board.getRows(); row++) {
            for (int col = 0; col < board.getCols(); col++) {
                if (board.getSlot(row, col) == board.getEmptySlot()) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkHorizontal(final Board board, final char player) {
        for (int row = 0; row < board.getRows(); row++) {
            for (int col = 0; col < board.getCols() - 3; col++) {
                if (board.getSlot(row, col) == player &&
                        board.getSlot(row, col + 1) == player &&
                        board.getSlot(row, col + 2) == player &&
                        board.getSlot(row, col + 3) == player) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkVertical(final Board board, final char player) {
        for (int col = 0; col < board.getCols(); col++) {
            for (int row = 0; row < board.getRows() - 3; row++) {
                if (board.getSlot(row, col) == player &&
                        board.getSlot(row + 1, col) == player &&
                        board.getSlot(row + 2, col) == player &&
                        board.getSlot(row + 3, col) == player) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkDiagonalTLBR(final Board board, final char player) {
        for (int row = 0; row < board.getRows() - 3; row++) {
            for (int col = 0; col < board.getCols() - 3; col++) {
                if (board.getSlot(row, col) == player &&
                        board.getSlot(row + 1, col + 1) == player &&
                        board.getSlot(row + 2, col + 2) == player &&
                        board.getSlot(row + 3, col + 3) == player) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkDiagonalTRBL(final Board board, final char player) {
        for (int row = 0; row < board.getRows() - 3; row++) {
            for (int col = 3; col < board.getCols(); col++) {
                if (board.getSlot(row, col) == player &&
                        board.getSlot(row + 1, col - 1) == player &&
                        board.getSlot(row + 2, col - 2) == player &&
                        board.getSlot(row + 3, col - 3) == player) {
                    return true;
                }
            }
        }
        return false;
    }
}
