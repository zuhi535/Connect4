package hu.nye.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the Game functionality in the Connect Four game.
 */
public class GameTest {

    /**
     * Tests the win condition for the human player.
     * It simulates a scenario where the human player
     * fills up a column to achieve victory.
     */
    @Test
    public void testHumanWinCondition() {
        Game game = new Game();
        Board board = game.getBoard();  // Now getBoard() works

        // Human player fills up a column to win
        board.placeDisc(0, 'Y');
        board.placeDisc(0, 'Y');
        board.placeDisc(0, 'Y');
        board.placeDisc(0, 'Y');

        boolean hasWon = board.checkWin('Y');
        assertTrue(hasWon, "Human player should win with four discs in a column.");
    }

    /**
     * Tests the AI's move functionality.
     * It simulates an AI making a move and verifies
     * that a disc is placed on the board.
     */
    @Test
    public void testAiMove() {
        Game game = new Game();
        Board board = game.getBoard();

        // Simulate AI making a move
        game.aiMove();  // AI mozgás végrehajtása

        // Check if AI's disc ('R') is placed
        boolean foundAiDisc = false;
        char[][] grid = board.getGrid();  // Tábla gridjének lekérése
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                if (grid[row][col] == 'R') {
                    foundAiDisc = true;
                    break;
                }
            }
            if (foundAiDisc) break;  // Ha megtaláltuk, kilépünk a külső ciklusból is
        }
        assertTrue(foundAiDisc, "AI should have placed a disc on the board.");
    }

}
