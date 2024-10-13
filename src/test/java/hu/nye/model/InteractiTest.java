package hu.nye.model;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;

/**
 * Test class for simulating interactive input in the Connect Four game.
 */
public class InteractiTest {

    /**
     * Tests the game functionality with mock input.
     * It simulates user inputs to test the game's response.
     */
    @Test
    public void testWithMockInput() {
        // Mock bemenetek
        String input = "3\n4\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        Game game = new Game();
        game.startGame();
    }
}
