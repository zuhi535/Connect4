package hu.nye;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Test class for the Connect Four game application.
 */
public class AppTest {

    /**
     * Tests the main method of the application.
     * Simulates a series of player inputs to verify game behavior.
     */
    @Test
    public void testMain() {
        // Mock input stream for simulating player moves
        // This simulates the human player always dropping discs in column 1
        String mockInput = "1\n".repeat(10); // Simulating 10 moves into column 1

        // Creating an InputStream from the mock input string
        InputStream in = new ByteArrayInputStream(mockInput.getBytes());
        System.setIn(in);  // Set the standard input to the mock input

        // Call the main method to start the game
        App.main(new String[]{});
    }
}
