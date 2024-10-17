package hu.nye.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

import static org.mockito.Mockito.*;

/**
 * Unit test for the Game class.
 */
class GameTest {

    private Game game;
    private Board mockBoard;
    private Player humanPlayer;
    private Scanner mockScanner;
    private Random mockRandom;

    @BeforeEach
    void setUp() {
        mockBoard = mock(Board.class); // Mocking the Board
        humanPlayer = new Player("Alice", 'X'); // Real Player object
        mockScanner = mock(Scanner.class); // Mocking the Scanner for user input
        mockRandom = mock(Random.class); // Mocking the Random for computer moves

        // Create a new Game instance with the mocks
        game = new Game(humanPlayer, mockBoard, mockScanner, mockRandom);
    }

    @Test // jó
    void testDrawCondition() {
        // Arrange
        when(mockBoard.isFull()).thenReturn(true); // Simulate the board is full

        // Act
        game.start(); // Start the game

        // Assert
        verify(mockBoard).isFull(); // Ensure the game checks for full board
        // Output can be checked indirectly if needed
    }

    @Test
    void testPlayerTurnValidMove() {
        // Arrange
        when(mockScanner.nextInt()).thenReturn(2); // Simulate player choosing column 2
        when(mockBoard.makeMove(2, humanPlayer.symbol())).thenReturn(false); // Simulate valid move
        when(mockBoard.isFull()).thenReturn(false, true); // First not full, then full to end game loop
        when(mockBoard.checkWin(humanPlayer.symbol())).thenReturn(true); // Simulate player winning after move

        // Act
        game.start();

        // Assert
        verify(mockBoard).makeMove(2, humanPlayer.symbol()); // Ensure player move is made
        verify(mockBoard).checkWin(humanPlayer.symbol()); // Ensure game checks for a win
    }


    @Test
    void testPlayerTurnInvalidMove() {
        // Arrange
        when(mockScanner.nextInt()).thenReturn(2); // Simulate player choosing column 2
        when(mockBoard.isFull()).thenReturn(false); // Ensure the game doesn't end early
        when(mockBoard.makeMove(2, humanPlayer.symbol()))
                .thenReturn(true)  // First attempt is invalid (column full)
                .thenReturn(false); // Second attempt is valid
        when(mockBoard.checkWin(humanPlayer.symbol())).thenReturn(true); // Simulate player winning after the move

        // Act
        game.start(); // Start the game

        // Assert
        verify(mockBoard, times(2)).makeMove(2, humanPlayer.symbol()); // Ensure the player retries after invalid move
        verify(mockBoard).checkWin(humanPlayer.symbol()); // Ensure the game checks for a win
    }


    @Test
    void testGetPlayerMoveInvalidInput() {
        // Arrange
        when(mockScanner.nextInt())
                .thenThrow(new RuntimeException()) // Simulate invalid input (e.g., non-integer)
                .thenReturn(2); // Then simulate valid input (column 2)
        when(mockBoard.makeMove(2, humanPlayer.symbol())).thenReturn(false); // Simulate valid move
        when(mockBoard.isFull()).thenReturn(false, true); // First not full, then full to end game loop
        when(mockBoard.checkWin(humanPlayer.symbol())).thenReturn(true); // Simulate player winning after valid move

        // Act
        game.start();

        // Assert
        verify(mockBoard).makeMove(2, humanPlayer.symbol()); // Ensure valid move happens after invalid input
        verify(mockBoard).checkWin(humanPlayer.symbol()); // Ensure game checks for a win
    }





    @Test //jó
    void testSaveFinalGameSuccess() throws IOException {
        // Arrange
        doNothing().when(mockBoard).saveFinalGame(anyString());

        // Act
        game.saveFinalGame();

        // Assert
        verify(mockBoard).saveFinalGame("saved_game.txt"); // Ensure the game is saved
    }

    @Test // jó
    void testSaveFinalGameFailure() throws IOException {
        // Arrange
        doThrow(new IOException("Save failed")).when(mockBoard).saveFinalGame(anyString());

        // Act
        game.saveFinalGame(); // Ensure it does not throw an exception even on failure

        // Assert
        verify(mockBoard).saveFinalGame("saved_game.txt");
    }

    @Test //jó
    void testLoadInitialBoardSuccess() throws IOException {
        // Arrange
        doNothing().when(mockBoard).loadInitialBoard(anyString());

        // Act
        game.loadInitialBoard();

        // Assert
        verify(mockBoard).loadInitialBoard("board_input.txt"); // Ensure the board is loaded
    }

    @Test //jó
    void testLoadInitialBoardFailure() throws IOException {
        // Arrange
        doThrow(new IOException("File not found")).when(mockBoard).loadInitialBoard(anyString());

        // Act
        game.loadInitialBoard(); // Ensure it does not throw an exception during failure

        // Assert
        verify(mockBoard).loadInitialBoard("board_input.txt");
    }



}
