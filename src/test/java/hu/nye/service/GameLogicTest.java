package hu.nye.service;

import hu.nye.model.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the GameLogic class.
 */
class GameLogicTest {

    private GameLogic gameLogic;
    private Board board;

    @BeforeEach
    void setUp() {
        gameLogic = new GameLogic();
        board = mock(Board.class); // Mocking the Board to simplify behavior
    }

    // Ellenőrzi, hogy a hely (slot) frissítve lett
    @Test
    void testMakeMoveSuccessful() {
        when(board.getCols()).thenReturn(7);
        when(board.getRows()).thenReturn(6);
        when(board.getEmptySlot()).thenReturn(' ');
        when(board.getSlot(5, 0)).thenReturn(' ');

        boolean result = gameLogic.makeMove(board, 0, 'R');

        assertTrue(result);
        verify(board).setSlot(5, 0, 'R');
    }

    // Nem történhet hely (slot) frissítés
    @Test
    void testMakeMoveInvalidColumn() {
        when(board.getCols()).thenReturn(7);

        boolean result = gameLogic.makeMove(board, -1, 'R');

        assertFalse(result);
        verify(board, never()).setSlot(anyInt(), anyInt(), anyChar());
    }

    @Test
    void testMakeMoveColumnFull() {
        when(board.getCols()).thenReturn(7);
        when(board.getRows()).thenReturn(6);
        when(board.getEmptySlot()).thenReturn(' ');

        // Simulate a full column
        for (int row = 0; row < 6; row++) {
            when(board.getSlot(row, 0)).thenReturn('R');
        }

        boolean result = gameLogic.makeMove(board, 0, 'Y');

        assertFalse(result);
    }

    @Test
    void testCheckWinHorizontal() {
        when(board.getRows()).thenReturn(6);
        when(board.getCols()).thenReturn(7);
        when(board.getSlot(0, 0)).thenReturn('R');
        when(board.getSlot(0, 1)).thenReturn('R');
        when(board.getSlot(0, 2)).thenReturn('R');
        when(board.getSlot(0, 3)).thenReturn('R');

        boolean result = gameLogic.checkWin(board, 'R');

        assertTrue(result);
    }

    @Test
    void testCheckWinVertical() {
        when(board.getRows()).thenReturn(6);
        when(board.getCols()).thenReturn(7);
        when(board.getSlot(0, 0)).thenReturn('R');
        when(board.getSlot(1, 0)).thenReturn('R');
        when(board.getSlot(2, 0)).thenReturn('R');
        when(board.getSlot(3, 0)).thenReturn('R');

        boolean result = gameLogic.checkWin(board, 'R');

        assertTrue(result);
    }

    @Test
    void testCheckWinDiagonalTLBR() {
        when(board.getRows()).thenReturn(6);
        when(board.getCols()).thenReturn(7);
        when(board.getSlot(0, 0)).thenReturn('R');
        when(board.getSlot(1, 1)).thenReturn('R');
        when(board.getSlot(2, 2)).thenReturn('R');
        when(board.getSlot(3, 3)).thenReturn('R');

        boolean result = gameLogic.checkWin(board, 'R');

        assertTrue(result);
    }

    @Test
    void testCheckWinDiagonalTRBL() {
        when(board.getRows()).thenReturn(6);
        when(board.getCols()).thenReturn(7);
        when(board.getSlot(0, 3)).thenReturn('R');
        when(board.getSlot(1, 2)).thenReturn('R');
        when(board.getSlot(2, 1)).thenReturn('R');
        when(board.getSlot(3, 0)).thenReturn('R');

        boolean result = gameLogic.checkWin(board, 'R');

        assertTrue(result);
    }

    @Test
    void testIsFullTrue() {
        when(board.getRows()).thenReturn(6);
        when(board.getCols()).thenReturn(7);

        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                when(board.getSlot(row, col)).thenReturn('R');
            }
        }

        boolean result = gameLogic.isFull(board);

        assertTrue(result);
    }

    @Test
    void testIsFullFalse() {
        when(board.getRows()).thenReturn(6);
        when(board.getCols()).thenReturn(7);
        when(board.getEmptySlot()).thenReturn(' ');

        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                if (row == 5 && col == 6) {
                    when(board.getSlot(row, col)).thenReturn(' ');
                } else {
                    when(board.getSlot(row, col)).thenReturn('R');
                }
            }
        }

        boolean result = gameLogic.isFull(board);

        assertFalse(result);
    }


}
