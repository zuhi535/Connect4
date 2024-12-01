package hu.nye.model;

import hu.nye.util.ColorUtils;

/**
 * A játéktáblát képviseli és biztosítja
 * funkció az állapotának kezelésére.
 * A tábla sorok és oszlopok rácsa,
 * ahol a játékosok elhelyezhetik darabjaikat.
 * Tartalmazza az inicializálás, módosítás,
 * Érvényesítse és jelenítse meg a játéktáblát.
 */
public class Board {
    // ----- Fields -----
    /** A játéktáblát jelképező 2D tömb. */
    private final char[][] board;

    /** A tábla sorainak száma. */
    private final int rows;

    /** A tábla oszlopainak száma. */
    private final int columns;

    /** A táblán lévő üres helyet jelölő karakter. */
    private final char emptySlot = '-';

    /** A tábla sorainak alapértelmezett számában lévő állandó. */
    private static final int DEFAULT_ROWS = 6;

    /** A tábla oszlopainak alapértelmezett számára vonatkozó állandó. */
    private static final int DEFAULT_COLUMNS = 7;

    // ----- Constructors -----
    /**
     * Alapértelmezett konstruktor, amely inicializálja a táblát
     * alapértelmezett méretek (6 sor és 7 oszlop).
     * A tábla inicializálása üres helyekkel történik.
     */
    public Board() {
        this(DEFAULT_ROWS, DEFAULT_COLUMNS);
    }

    /**
     * Paraméterezett konstruktor az inicializáláshoz
     * A tábla egyéni sorokkal és oszlopokkal.
     *
     * @param numColumns A tábla sorainak száma.
     * @param numRows A tábla oszlopainak száma.
     */
    public Board(final int numRows, final int numColumns) {
        this.rows = numRows;
        this.columns = numColumns;
        this.board = new char[numRows][numColumns];
        initializeEmptyBoard();
    }


    // ----- Initialization and Reset -----
    /**
     * Inicializálja a táblát üres helyekkel ("-") minden pozícióhoz.
     * Ezt a módszert hívják meg az építés során
     * a táblán és a tábla visszaállításakor is.
     */
    public void initializeEmptyBoard() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                board[i][j] = emptySlot;
            }
        }
    }

    /**
     * Visszaállítja a táblát a kezdeti üres állapotába a hívással
     * az initializeEmptyBoard metódus.
     */
    public void reset() {
        initializeEmptyBoard();
    }

    /**
     * A teljes táblát 2D tömbként adja vissza.
     *
     * @return A tábla aktuális állapotát ábrázoló 2D karaktertömb.
     */
    public char[][] getBoard() {
        return board;
    }

    /**
     * Egy adott slot értékét kapja a táblán.
     *
     * @param row A nyílás sorindexe.
     * @param col A nyílás oszlopindexe.
     * @return A megadott nyílás értéke (karaktere).
     * @throws IllegalArgumentException Ha a sor- vagy oszlopindex érvénytelen.
     */
    public char getSlot(final int row, final int col) {
        validateIndices(row, col);
        return board[row][col];
    }

    /**
     * Beállítja egy adott nyílás értékét a táblán.
     *
     * @param row A nyílás sorindexe.
     * @param col A nyílás oszlopindexe.
     * @param value A nyílásban beállítandó érték (karakter).
     * @throws IllegalArgumentException Ha a sor- vagy oszlopindex érvénytelen.
     */
    public void setSlot(final int row, final int col, final char value) {
        validateIndices(row, col);
        board[row][col] = value;
    }

    /**
     * A tábla teljes sorát lekéri a megadott sorindexen.
     *
     * @param rowIndex A lekérni kívánt sor indexe.
     * @return A sort jelölő karakteres tömb.
     * @throws IllegalArgumentException Ha a sorindex érvénytelen.
     */
    public char[] getRow(final int rowIndex) {
        if (rowIndex < 0 || rowIndex >= rows) {
            throw new IllegalArgumentException("Invalid row index");
        }
        return board[rowIndex];
    }

    /**
     * A tábla teljes sorát a megadott értékre állítja
     * Sorindex a megadott adatokkal.
     *
     * @param rowIndex A frissítendő sor indexe.
     * @param rowData A beállítandó új soradatok.
     * @throws IllegalArgumentException Ha a sorindex
     */
    public void setRow(final int rowIndex, final char[] rowData) {
        if (rowIndex < 0 || rowIndex >= rows || rowData.length != columns) {
            throw new IllegalArgumentException("Invalid row "
                    + "index or row data length");
        }
        board[rowIndex] = rowData;
    }

    /**
     * A tábla sorainak számát adja eredményül.
     *
     * @return A sorok száma.
     */
    public int getRows() {
        return rows;
    }

    /**
     * A tábla oszlopainak számát adja eredményül.
     *
     * @return Az oszlopok száma.
     */
    public int getCols() {
        return columns;
    }

    /**
     * A tábla üres helyét jelölő karaktert adja eredményül.
     *
     * @return Üres helyet jelölő karakter ("-").
     */
    public char getEmptySlot() {
        return emptySlot;
    }

    /**
     * Érvényesíti a sor- és oszlopindexeket
     * Győződjön meg arról, hogy az érvényes tartományon belül vannak.
     *
     * @param row Az érvényesíteni kívánt sorindex.
     * @param col Az érvényesíteni kívánt oszlopindex.
     * @throws IllegalArgumentException Ha a sor vagy
     */
    private void validateIndices(final int row, final int col) {
        if (row < 0 || row >= rows || col < 0 || col >= columns) {
            throw new IllegalArgumentException("Invalid row or column index");
        }
    }

    /**
     * Megjeleníti a tábla aktuális állapotát a konzolon,
     * ANSI escape kódok használata a színezéshez.
     * A piros darabok piros, a sárga darabok sárga színűek,
     * és az üres helyek alapértelmezett karakterekként kerülnek nyomtatásra.
     */
    public void display() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (board[i][j] == 'R') {
                    System.out.print(ColorUtils.ANSI_RED + board[i][j]
                            + ColorUtils.ANSI_RESET + " ");
                } else if (board[i][j] == 'Y') {
                    System.out.print(ColorUtils.ANSI_YELLOW + board[i][j]
                            + ColorUtils.ANSI_RESET + " ");
                } else {
                    System.out.print(board[i][j] + " ");
                }
            }
            System.out.println();
        }
        printSeparatorLine();
        printColumnNumbers();
    }

    /**
     * Kinyomtat egy elválasztó vonalat a tábla alatt, hogy vizuálisan látható legyen
     * Válassza el a táblát az oszlopszámoktól.
     */
    private void printSeparatorLine() {
        for (int j = 0; j < columns; j++) {
            System.out.print("--");
        }
        System.out.println();
    }

    /**
     * Kinyomtatja a tábla alatti oszlopszámokat a következők jelzésére:
     * azok a pozíciók, ahol a játékosok elhelyezhetik bábuikat.
     */
    private void printColumnNumbers() {
        for (int j = 0; j < columns; j++) {
            System.out.print(j + " ");
        }
        System.out.println();
    }
}
