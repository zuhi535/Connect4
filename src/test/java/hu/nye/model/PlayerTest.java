package hu.nye.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void testPlayerNameAndSymbol() {
        Player player = new Player("Alice", 'X');
        assertEquals("Alice", player.name(), "Player's name should be Alice.");
        assertEquals('X', player.symbol(), "Player's symbol should be 'X'.");
    }

    @Test
    void testToString() {
        Player player = new Player("Bob", 'O');
        String expectedString = "Player{name='Bob', symbol=O}";
        assertEquals(expectedString, player.toString(), "Player's string representation is incorrect.");
    }
}
