package hu.nye.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board();
    }

    @Test
    void testInitialBoardEmpty() {
        char[][] currentBoard = board.getBoard();
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                assertEquals('-', currentBoard[row][col], "Board should be empty initially.");
            }
        }
    }

    @Test
    void testMakeMoveValid() {
        boolean result = board.makeMove(3, 'Y');
        assertFalse(result, "Move should be valid when column is not full.");
        assertEquals('Y', board.getBoard()[5][3], "Move should place 'Y' at the bottom row.");
    }

    @Test
    void testMakeMoveFullColumn() {
        // Fill the column
        for (int i = 0; i < 6; i++) {
            assertFalse(board.makeMove(0, 'R'), "Move should be valid until the column is full.");
        }
        // Attempt to place another piece in the full column
        boolean result = board.makeMove(0, 'Y');
        assertTrue(result, "Move should fail when the column is full.");
    }

    @Test
    void testIsFullInitially() {
        assertFalse(board.isFull(), "Board should not be full initially.");
    }

    @Test
    void testIsFullAfterFillingBoard() {
        // Fill the entire board
        for (int col = 0; col < 7; col++) {
            for (int row = 0; row < 6; row++) {
                assertFalse(board.makeMove(col, 'R'), "Move should be valid until the column is full.");
            }
        }
        assertTrue(board.isFull(), "Board should be full after all positions are filled.");
    }

    @Test
    void testCheckWinHorizontal() {
        // Place 4 'R' horizontally
        board.makeMove(0, 'R');
        board.makeMove(1, 'R');
        board.makeMove(2, 'R');
        board.makeMove(3, 'R');
        assertTrue(board.checkWin('R'), "Player R should win with horizontal line.");
    }

    @Test
    void testCheckWinVertical() {
        // Place 4 'Y' vertically in column 2
        board.makeMove(2, 'Y');
        board.makeMove(2, 'Y');
        board.makeMove(2, 'Y');
        board.makeMove(2, 'Y');
        assertTrue(board.checkWin('Y'), "Player Y should win with vertical line.");
    }

    @Test
    void testCheckWinNoWin() {
        // Place pieces without forming a winning sequence
        board.makeMove(0, 'R');
        board.makeMove(1, 'Y');
        board.makeMove(2, 'R');
        board.makeMove(3, 'Y');
        board.makeMove(4, 'R');
        board.makeMove(5, 'Y');
        board.makeMove(6, 'R');
        assertFalse(board.checkWin('R'), "Player R should not have a winning sequence.");
        assertFalse(board.checkWin('Y'), "Player Y should not have a winning sequence.");
    }

    @Test
    void testCheckWinOverlappingSequences() {
        // Create multiple winning sequences for 'R'
        board.makeMove(0, 'R');
        board.makeMove(1, 'R');
        board.makeMove(2, 'R');
        board.makeMove(3, 'R'); // Horizontal win
        board.makeMove(0, 'R');
        board.makeMove(0, 'R');
        board.makeMove(0, 'R'); // Vertical win
        assertTrue(board.checkWin('R'), "Player R should have a winning sequence.");
    }

    @Test
    void testMakeMoveInvalidColumn() {
        Exception exception = assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            board.makeMove(7, 'R');  // Invalid column
        });
        assertEquals("Index 7 out of bounds for length 7", exception.getMessage(), "Should throw exception for invalid column.");
    }

    @Test
    void testMakeMoveNegativeColumn() {
        Exception exception = assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            board.makeMove(-1, 'Y');  // Invalid negative column
        });
        assertEquals("Index -1 out of bounds for length 7", exception.getMessage(), "Should throw exception for invalid negative column.");
    }

    @Test
    void testSaveFinalGame() throws IOException {
        board.makeMove(0, 'R');
        String filePath = "saved_game.txt";
        board.saveFinalGame(filePath);
        File file = new File(filePath);
        assertTrue(file.exists(), "File should be created.");
        file.delete(); // Clean up after test
    }

    @Test
    void testLoadInitialBoardNoFile() throws IOException {
        board.loadInitialBoard("non_existent_file.txt");
        // Expect the board to remain empty
        char[][] loadedBoard = board.getBoard();
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                assertEquals('-', loadedBoard[row][col], "Board should remain empty when file is not found.");
            }
        }
    }

}
