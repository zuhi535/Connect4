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
        
        int validColumn = 3;
        int columnRange = 6;
        when(scannerMock.hasNextInt()).thenReturn(true); 
        when(scannerMock.nextInt()).thenReturn(validColumn);

        int column = inputHandler.getPlayerMove(columnRange);

        assertEquals(validColumn, column);
    }

    @Test
    public void testGetPlayerMove_invalidInput_thenValidInput() {
   
        int invalidColumn = -1;
        int validColumn = 2;
        int columnRange = 5; 

        // Szimuláljuk, hogy először érvénytelen bemenetet ad a felhasználó, majd érvényeset
        when(scannerMock.hasNextInt()).thenReturn(true, true); 
        when(scannerMock.nextInt()).thenReturn(invalidColumn, validColumn); 

        int column = inputHandler.getPlayerMove(columnRange);

        assertEquals(validColumn, column);
    }

    @Test
    public void testGetPlayerMove_inputOutOfRange() {

        int invalidColumn = 7;
        int validColumn = 3;
        int columnRange = 6; 

        // Szimuláljuk, hogy először érvénytelen, tartományon kívüli bemenetet ad a felhasználó, majd érvényeset
        when(scannerMock.hasNextInt()).thenReturn(true, true);
        when(scannerMock.nextInt()).thenReturn(invalidColumn, validColumn); 

        int column = inputHandler.getPlayerMove(columnRange);

        assertEquals(validColumn, column);
    }

    @Test
    public void testGetPlayerMove_nonIntegerInput() {
        
        String invalidInput = "abc";
        int validColumn = 2;
        int columnRange = 5; 

        // Szimuláljuk, hogy nem egész számot ad a felhasználó, majd érvényes egész számot
        when(scannerMock.hasNextInt()).thenReturn(false, true); 
        when(scannerMock.next()).thenReturn(invalidInput); 
        when(scannerMock.nextInt()).thenReturn(validColumn); 

        int column = inputHandler.getPlayerMove(columnRange);

        assertEquals(validColumn, column); 
    }
}
