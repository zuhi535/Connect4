package hu.nye.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Scanner;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class InputHandlerTest {

    private InputHandler inputHandler;
    private Scanner scannerMock;

    @BeforeEach
    public void setUp() {
        // A Scanner objektum mokkolása a felhasználói bemenet szimulálására
        scannerMock = mock(Scanner.class);
        inputHandler = new InputHandler(scannerMock);
    }

    @Test
    public void testGetPlayerMove_validInput() {
        // Adott
        int validColumn = 3;
        int columnRange = 6; // Feltételezve, hogy a érvényes oszlop tartomány 0-tól 6-ig terjed
        when(scannerMock.hasNextInt()).thenReturn(true); // Szimuláljuk, hogy a felhasználó egész számot ad meg
        when(scannerMock.nextInt()).thenReturn(validColumn); // Szimuláljuk, hogy a felhasználó a 3-as oszlopot adja meg

        // Amikor
        int column = inputHandler.getPlayerMove(columnRange);

        // Akkor
        assertEquals(validColumn, column);
    }

    @Test
    public void testGetPlayerMove_invalidInput_thenValidInput() {
        // Adott
        int invalidColumn = -1;
        int validColumn = 2;
        int columnRange = 5; // Feltételezve, hogy az érvényes oszlop tartomány 0-tól 5-ig terjed

        // Szimuláljuk, hogy először érvénytelen bemenetet ad a felhasználó, majd érvényeset
        when(scannerMock.hasNextInt()).thenReturn(true, true); // Szimuláljunk két próbálkozást, mindkettő egész szám bemenettel
        when(scannerMock.nextInt()).thenReturn(invalidColumn, validColumn); // Először érvénytelen, majd érvényes oszlop

        // Amikor
        int column = inputHandler.getPlayerMove(columnRange);

        // Akkor
        assertEquals(validColumn, column); // Az érvényes oszlopnak kell visszatérnie az érvénytelen bemenet után
    }

    @Test
    public void testGetPlayerMove_inputOutOfRange() {
        // Adott
        int invalidColumn = 7;
        int validColumn = 3;
        int columnRange = 6; // Feltételezve, hogy az érvényes oszlop tartomány 0-tól 6-ig terjed

        // Szimuláljuk, hogy először érvénytelen, tartományon kívüli bemenetet ad a felhasználó, majd érvényeset
        when(scannerMock.hasNextInt()).thenReturn(true, true);
        when(scannerMock.nextInt()).thenReturn(invalidColumn, validColumn); // Először érvénytelen, majd érvényes oszlop

        // Amikor
        int column = inputHandler.getPlayerMove(columnRange);

        // Akkor
        assertEquals(validColumn, column); // Az érvényes oszlopnak kell visszatérnie az érvénytelen tartományon kívüli bemenet után
    }

    @Test
    public void testGetPlayerMove_nonIntegerInput() {
        // Adott
        String invalidInput = "abc";
        int validColumn = 2;
        int columnRange = 5; // Érvényes tartomány 0-tól 5-ig

        // Szimuláljuk, hogy nem egész számot ad a felhasználó, majd érvényes egész számot
        when(scannerMock.hasNextInt()).thenReturn(false, true); // Az első bemenet érvénytelen, a második érvényes
        when(scannerMock.next()).thenReturn(invalidInput); // Szimuláljuk a nem egész szám bemenetet
        when(scannerMock.nextInt()).thenReturn(validColumn); // Az érvényes bemenet a második próbálkozásnál

        // Amikor
        int column = inputHandler.getPlayerMove(columnRange);

        // Akkor
        assertEquals(validColumn, column); // Az érvényes oszlopnak kell visszatérnie az érvénytelen bemenet után
    }
}
