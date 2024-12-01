package hu.nye.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    private Board defaultBoard;
    private Board customBoard;

    @BeforeEach
    void setUp() {
        // Alapértelmezett táblát hozunk létre
        defaultBoard = new Board();

        // Egyedi méretű táblát hozunk létre
        customBoard = new Board(8, 9);
    }

    @Test
    void testDefaultConstructor() {
        assertEquals(6, defaultBoard.getRows());
        assertEquals(7, defaultBoard.getCols());

        // Várható üres tábla létrehozása
        char[][] expectedBoard = new char[6][7];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                expectedBoard[i][j] = '-';
            }
        }

        // Ellenőrizzük, hogy az alapértelmezett tábla megfelelően lett inicializálva
        assertArrayEquals(expectedBoard, defaultBoard.getBoard());
    }


    @Test
    void testCustomConstructor() {
        assertEquals(8, customBoard.getRows());
        assertEquals(9, customBoard.getCols());

        // Várható üres tábla létrehozása
        char[][] expectedBoard = new char[8][9];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 9; j++) {
                expectedBoard[i][j] = '-';
            }
        }

        // Ellenőrizzük, hogy az egyedi méretű tábla megfelelően lett inicializálva
        assertArrayEquals(expectedBoard, customBoard.getBoard());
    }


    @Test
    void testInitializeEmptyBoard() {
        char[][] emptyBoard = new char[6][7];
        for (int i = 0; i < emptyBoard.length; i++) {
            for (int j = 0; j < emptyBoard[i].length; j++) {
                emptyBoard[i][j] = '-';
            }
        }
        defaultBoard.initializeEmptyBoard();

        // Ellenőrizzük, hogy az initializeEmptyBoard metódus megfelelően inicializálja a táblát
        assertArrayEquals(emptyBoard, defaultBoard.getBoard());
    }

    @Test
    void testReset() {
        defaultBoard.setSlot(0, 0, 'R');
        defaultBoard.reset();

        // Ellenőrizzük, hogy a reset metódus után minden mező üres ( '-' )
        for (int i = 0; i < defaultBoard.getRows(); i++) {
            for (int j = 0; j < defaultBoard.getCols(); j++) {
                assertEquals('-', defaultBoard.getSlot(i, j));
            }
        }
    }

    @Test
    void testSetAndGetSlot() {
        defaultBoard.setSlot(2, 3, 'R');

        // Ellenőrizzük, hogy a setSlot és getSlot metódusok megfelelően működnek
        assertEquals('R', defaultBoard.getSlot(2, 3));
    }

    @Test
    void testSetSlotInvalidIndices() {
        // Ellenőrizzük, hogy érvénytelen indexek esetén kivétel dobódik
        assertThrows(IllegalArgumentException.class, () -> defaultBoard.setSlot(-1, 0, 'R'));
        assertThrows(IllegalArgumentException.class, () -> defaultBoard.setSlot(0, 10, 'R'));
    }

    @Test
    void testGetSlotInvalidIndices() {
        // Ellenőrizzük, hogy érvénytelen indexek esetén kivétel dobódik
        assertThrows(IllegalArgumentException.class, () -> defaultBoard.getSlot(-1, 0));
        assertThrows(IllegalArgumentException.class, () -> defaultBoard.getSlot(6, 7));
    }

    @Test
    void testSetAndGetRow() {
        char[] newRow = {'R', 'Y', '-', '-', '-', '-', '-'};
        defaultBoard.setRow(0, newRow);

        // Ellenőrizzük, hogy a setRow és getRow metódusok megfelelően működnek
        assertArrayEquals(newRow, defaultBoard.getRow(0));
    }

    @Test
    void testSetRowInvalidData() {
        char[] invalidRow = {'R', 'Y'}; // Túl rövid sor
        // Ellenőrizzük, hogy ha a sor hossza nem megfelelő, kivétel dobódik
        assertThrows(IllegalArgumentException.class, () -> defaultBoard.setRow(0, invalidRow));
    }

    @Test
    void testSetRowInvalidIndex() {
        char[] newRow = {'R', 'Y', '-', '-', '-', '-', '-'};

        // Ellenőrizzük, hogy ha az index érvénytelen, kivétel dobódik
        assertThrows(IllegalArgumentException.class, () -> defaultBoard.setRow(-1, newRow));
    }

    @Test
    void testGetEmptySlot() {
        // Ellenőrizzük, hogy az üres mező karaktere megfelelően van beállítva
        assertEquals('-', defaultBoard.getEmptySlot());
    }

    @Test
    void testDisplay() {
        // Ez a teszt az output validálását nem végzi, mivel az a konzolra ír.
        // Tesztként lefuttatjuk a metódust, hogy biztosak legyünk, nem dob kivételt.
        assertDoesNotThrow(() -> defaultBoard.display());
    }

    @Test
    void testValidateIndices() {
        assertDoesNotThrow(() -> defaultBoard.setSlot(0, 0, 'R'));

        // Ellenőrizzük, hogy érvénytelen indexek esetén kivétel dobódik
        assertThrows(IllegalArgumentException.class, () -> defaultBoard.setSlot(6, 7, 'R'));
    }
}
