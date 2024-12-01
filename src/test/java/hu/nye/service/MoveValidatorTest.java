package hu.nye.service;

import hu.nye.model.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class MoveValidatorTest {

    private MoveValidator moveValidator;
    private Board board;

    @BeforeEach
    public void setUp() {
        moveValidator = new MoveValidator();
        // Inicializáljuk a táblát 6 sorral és 7 oszloppal (például egy Connect4 játékhoz)
        board = new Board(6, 7);
    }

    //Teszteli a négy egymás melletti azonos karakter keresését vízszintesen.
    @Test
    public void testCheckFourInARowHorizontal() {
        // Beállítjuk a táblát, hogy tartalmazzon egy vízszintes négyest 'X' karakterekkel
        board.setSlot(0, 0, 'X');
        board.setSlot(0, 1, 'X');
        board.setSlot(0, 2, 'X');
        board.setSlot(0, 3, 'X');

        boolean result = moveValidator.checkDirection(board, 'X', 0, 1);  // Vizsgáljuk a vízszintes irányt (dx = 0, dy = 1)

        assertTrue(result, "The horizontal four in a row should be detected.");
    }

    //Teszteli a négy egymás melletti azonos karakter keresését függőlegesen.
    @Test
    public void testCheckFourInARowVertical() {
        // Beállítjuk a táblát, hogy tartalmazzon egy függőleges négyest 'O' karakterekkel
        board.setSlot(0, 0, 'O');
        board.setSlot(1, 0, 'O');
        board.setSlot(2, 0, 'O');
        board.setSlot(3, 0, 'O');

        boolean result = moveValidator.checkDirection(board, 'O', 1, 0);  // Vizsgáljuk a függőleges irányt (dx = 1, dy = 0)

        assertTrue(result, "The vertical four in a row should be detected.");
    }

    //Teszteli a négy egymás melletti azonos karakter keresését átlósan (balról jobbra, felülről lefelé.
    @Test
    public void testCheckFourInARowDiagonal() {
        // Beállítjuk a táblát, hogy tartalmazzon egy átlós négyest '\' irányban 'X' karakterekkel
        board.setSlot(0, 0, 'X');
        board.setSlot(1, 1, 'X');
        board.setSlot(2, 2, 'X');
        board.setSlot(3, 3, 'X');

        boolean result = moveValidator.checkDirection(board, 'X', 1, 1);  // Vizsgáljuk az átlós irányt (dx = 1, dy = 1)

        assertTrue(result, "The diagonal four in a row should be detected.");
    }

    //Teszteli a oszlop teljesen kitöltöttsége ellenőrzését.
    @Test
    public void testIsColumnFull() {
        // Beállítjuk, hogy az első oszlop (index 0) teljesen tele van
        board.setSlot(0, 0, 'X');
        board.setSlot(1, 0, 'X');
        board.setSlot(2, 0, 'X');
        board.setSlot(3, 0, 'X');
        board.setSlot(4, 0, 'X');
        board.setSlot(5, 0, 'X');

        boolean result = moveValidator.isColumnFull(board, 0);

        assertTrue(result, "The column should be full.");
    }

    //Teszteli, hogy az oszlop akkor sem telik meg, ha még van üres hely.
    @Test
    public void testIsColumnNotFull() {
        // Az első oszlop nincs teljesen tele (helyezünk 4 'X' karaktert, de a többi sor üres)
        board.setSlot(0, 0, 'X');
        board.setSlot(1, 0, 'X');
        board.setSlot(2, 0, 'X');
        board.setSlot(3, 0, 'X');

        // Mivel az oszlopban még van hely, az oszlop nem lesz teljes
        boolean result = moveValidator.isColumnFull(board, 0);

        assertFalse(result, "The column should not be full.");
    }

    //metódus helyesen állapítja meg, hogy nem történt meg négy egyforma jel vízszintes elrendezésben
    @Test
    public void testNoFourInARow() {
        // Nem tartalmaz vízszintes négyest
        board.setSlot(0, 0, 'X');
        board.setSlot(0, 1, 'X');
        board.setSlot(0, 2, 'X');
        board.setSlot(0, 4, 'X'); // Eltérés itt

        boolean result = moveValidator.checkDirection(board, 'X', 0, 1);

        assertFalse(result, "No four in a row should be detected.");
    }

    //metódus megfelelően kezeli az érvénytelen irányokat, és nem talál négy egymámellet
    @Test
    public void testInvalidDirection() {
        // Érvénytelen irány (dx = 0, dy = 0)
        board.setSlot(0, 0, 'X');
        board.setSlot(0, 1, 'X');
        board.setSlot(0, 2, 'X');
        board.setSlot(0, 3, 'X');

        boolean result = moveValidator.checkDirection(board, 'X', 0, 0);

        assertFalse(result, "Invalid direction should not detect four in a row.");
    }

    //metódus nem talál egymás után négy játékkövet, ha a tábla üres
    @Test
    public void testCheckDirectionOnEmptyBoard() {
        // Üres tábla esetén semmi nem felel meg
        boolean result = moveValidator.checkDirection(board, 'X', 1, 0);

        assertFalse(result, "No matches should be found on an empty board.");
    }

    //Teszteli, hogy egy oszlop nem lehet tele, ha üres hely marad benne.
    @Test
    public void testIsColumnEmpty() {
        // Az első oszlop üres
        boolean result = moveValidator.isColumnFull(board, 0);

        assertFalse(result, "The column should not be full.");
    }
}
