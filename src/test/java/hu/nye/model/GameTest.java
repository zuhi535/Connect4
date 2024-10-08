package hu.nye.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

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

    @Test
    public void testAiMove() {
        Game game = new Game();
        Board board = game.getBoard();  // Now getBoard() works

        // Simulate AI making a move
        game.aiMove();  // Now aiMove() works

        // Check if AI's disc ('R') is placed
        boolean foundAiDisc = false;
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7; col++) {
                if (board.getGrid()[row][col] == 'R') {  // Now getGrid() works
                    foundAiDisc = true;
                    break;
                }
            }
        }
        assertTrue(foundAiDisc, "AI should have placed a disc on the board.");
    }
}
