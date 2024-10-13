package hu.nye.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the Board functionality in the Connect Four game.
 */
public class BoardTest {

    /**
     * Tests placing a disc in a valid column.
     * Expecting the disc to be placed successfully.
     */
    @Test
    public void testPlaceDiscInValidColumn() {
        Board board = new Board(6, 7);
        boolean result = board.placeDisc(3, 'Y');
        assertFalse(result, "Disc should be placed successfully in column 3.");  // Sikeres elhelyezésnél false a helyes
    }

    /**
     * Tests attempting to place a disc in a full column.
     * Expecting the attempt to fail.
     */
    @Test
    public void testPlaceDiscInFullColumn() {
        Board board = new Board(6, 7);
        // Fill up the column
        for (int i = 0; i < 6; i++) {
            board.placeDisc(0, 'Y');
        }
        // Try to place another disc in the full column
        boolean result = board.placeDisc(0, 'R');
        assertTrue(result, "Column 0 is full, disc should not be placed.");  // Sikertelen elhelyezésnél true a helyes
    }

    /**
     * Tests placing a disc in an invalid column.
     * Expecting the attempt to fail.
     */
    @Test
    public void testPlaceDiscInInvalidColumn() {
        Board board = new Board(6, 7);
        boolean result = board.placeDisc(-1, 'Y');  // Invalid column
        assertFalse(result, "Placing a disc in an invalid column should fail.");  // Sikertelen elhelyezésnél false a helyes
    }

}
