package hu.nye.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class PlayerTest {

    //ellenőrzi, hogy a Player osztály konstruktora megfelelően inicializálja a játékost a megadott névvel és szimbólummal.
    @Test
    public void testPlayerConstructor_shouldInitializeWithGivenNameAndSymbol() {

        String expectedName = "Player1";
        char expectedSymbol = 'Y';

        Player player = new Player(expectedName, expectedSymbol);

        assertEquals(expectedName, player.name());
        assertEquals(expectedSymbol, player.symbol());
    }

    //metódusa helyesen adja vissza a játékos string reprezentációját.
    @Test
    public void testToString_shouldReturnCorrectStringRepresentation() {
        
        String expectedName = "Player1";
        char expectedSymbol = 'X';
        Player player = new Player(expectedName, expectedSymbol);

        String result = player.toString();

        String expectedString = "Player{name='" + expectedName + "', symbol=" + expectedSymbol + "}";
        assertEquals(expectedString, result);
    }
}
