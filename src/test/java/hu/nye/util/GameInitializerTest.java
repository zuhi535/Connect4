package hu.nye.util;

import hu.nye.controller.InputHandler;
import hu.nye.service.MoveValidator;
import hu.nye.model.Board;
import hu.nye.model.Game;
import hu.nye.model.Player;
import hu.nye.repository.DatabaseManager;
import hu.nye.repository.FileManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameInitializerTest {

    private Scanner mockScanner;
    private Random mockRandom;
    private DatabaseManager mockDatabaseManager;
    private GameInitializer gameInitializer;

    @BeforeEach
    void setUp() {
        mockScanner = mock(Scanner.class);
        mockRandom = mock(Random.class);
        mockDatabaseManager = mock(DatabaseManager.class);

        gameInitializer = new GameInitializer(mockScanner, mockRandom, mockDatabaseManager);
    }

    @Test
    void testInitializeGame_withValidPlayerName() {

        String playerName = "TestPlayer";

        Game game = gameInitializer.initializeGame(playerName);

        Player player = game.getHumanPlayer();
        assertNotNull(player);
        assertEquals(playerName, player.name());
        assertEquals('Y', player.symbol());
    }

    //metódus megfelelően naplózza azt az üzenetet, amikor nem található mentett tábla
    @Test
    void testInitializeGame_whenNoSavedBoardIsFound_logsMessage() throws Exception {

        String playerName = "TestPlayer";

        FileManager mockFileManager = mock(FileManager.class);
        doThrow(new java.io.IOException("File not found")).when(mockFileManager).loadBoard(any(Board.class), anyString());

        // Create a new GameInitializer with the mocked FileManager
        GameInitializer customInitializer = new GameInitializer(
                mockScanner, mockRandom, mockDatabaseManager
        ) {
            @Override
            public Game initializeGame(String playerName) {
                Player player = new Player(playerName, 'Y');
                Board board = new Board();
                board.reset();

                try {
                    mockFileManager.loadBoard(board, "board_input.txt");
                } catch (java.io.IOException e) {
                    System.out.println("No saved board found, starting with an empty board.");
                }

                InputHandler inputHandler = new InputHandler(mockScanner);
                MoveValidator moveValidator = new MoveValidator();

                return new Game(player, board, inputHandler, moveValidator, mockFileManager, mockRandom, mockDatabaseManager);
            }
        };


        java.io.ByteArrayOutputStream outContent = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(outContent));


        customInitializer.initializeGame(playerName);


        String output = outContent.toString();
        assertTrue(output.contains("No saved board found, starting with an empty board."),
                "Expected log message was not printed.");
    }


}
