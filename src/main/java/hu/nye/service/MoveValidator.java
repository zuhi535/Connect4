package hu.nye.service;

import hu.nye.model.Board;

/**
 * Módszereket biztosít a lépések érvényesítésére és a nyerési feltételek ellenőrzésére a Connect 4 játékban.
 */
public class MoveValidator {

    /**
     * Ellenőrzi, hogy egy adott irányban négy egymást követő slot ugyanazt a szimbólumot tartalmazza-e.
     *
     * @param board a játéktábla érvényesítése
     * @param symbol a játékos ellenőrizendő szimbóluma
     * @param dx a vízszintes irányú növekmény
     * @param dy a függőleges irányú növekmény
     * @Return true, ha négy egymást követő slot ugyanazt a szimbólumot tartalmazza, ellenkező esetben false
     */
    protected boolean checkDirection(final Board board, final char symbol, final int dx, final int dy) {
        if (dx == 0 && dy == 0) {
            return false;
        }

        int rows = board.getRows();
        int cols = board.getCols();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (isValidStartPosition(board, row, col, dx, dy)) {
                    if (checkFourInARow(board, symbol, row, col, dx, dy)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    /**
     * Érvényes, ha egy kezdőpozíció érvényes négy egymást követő slot ellenőrzésére.
     *
     * @param board a játéktáblára
     * @param row a kezdő sor pozíciója
     * @param col a kezdő oszlop pozíciója
     * @param dx a vízszintes irányú növekmény
     * @param dy a függőleges irányú növekmény
     * @return true, ha a kiindulási pozíció négy egymást követő nyílást tesz lehetővé, ellenkező esetben false
     */
    private boolean isValidStartPosition(final Board board, final int row, final int col, final int dx, final int dy) {
        int rows = board.getRows();
        int cols = board.getCols();

        return row + 3 * dx >= 0 && row + 3 * dx < rows &&
                col + 3 * dy >= 0 && col + 3 * dy < cols;
    }

    /**
     * Ellenőrzi, hogy egy adott irányban négy egymást követő slot megegyezik-e a játékos szimbólumával.
     *
     * @param board a játéktáblára
     * @param symbol a játékos ellenőrizendő szimbóluma
     * @param row a kezdő sor pozíciója
     * @param col a kezdő oszlop pozíciója
     * @param dx a vízszintes irányú növekmény
     * @param dy a függőleges irányú növekmény
     * @Return true, ha négy egymást követő hely egyezik a szimbólummal, hamis ellenkező esetben
     */
    private boolean checkFourInARow(final Board board, final char symbol, final int row, final int col, final int dx, final int dy) {
        for (int i = 0; i < 4; i++) {
            if (board.getSlot(row + i * dx, col + i * dy) != symbol) {
                return false;
            }
        }
        return true;
    }

    /**
     * Meghatározza, hogy egy megadott oszlop megtelt-e.
     *
     * @param board a játéktáblára
     * @param col az ellenőrizendő oszlopot
     * @return true, ha az oszlop megtelt, hamis egyébként
     */
    public boolean isColumnFull(final Board board, final int col) {
        int rows = board.getRows();
        for (int row = 0; row < rows; row++) {
            if (board.getSlot(row, col) == board.getEmptySlot()) {
                return false;
            }
        }
        return true;
    }

}
