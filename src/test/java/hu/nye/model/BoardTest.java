package hu.nye.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    @Test
    public void testPlaceDiscInValidColumn() {
        Board board = new Board(6, 7);
        boolean result = board.placeDisc(3, 'Y');
        assertTrue(result, "Disc should be placed successfully in column 3.");
    }

    @Test
    public void testPlaceDiscInFullColumn() {
        Board board = new Board(6, 7);
        // Fill up the column
        for (int i = 0; i < 6; i++) {
            board.placeDisc(0, 'Y');
        }
        // Try to place another disc in the full column
        boolean result = board.placeDisc(0, 'R');
        assertFalse(result, "Column 0 is full, disc should not be placed.");
    }

    @Test
    public void testPlaceDiscInInvalidColumn() {
        Board board = new Board(6, 7);
        boolean result = board.placeDisc(-1, 'Y');  // Invalid column
        assertFalse(result, "Placing a disc in an invalid column should fail.");
    }
}
