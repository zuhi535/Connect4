package hu.nye.model;

import hu.nye.controller.InputHandler;
import hu.nye.repository.DatabaseManager;
import hu.nye.repository.FileManager;
import hu.nye.service.GameLogic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;

import java.io.*;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class GameTest {

    private Board mockBoard;
    private FileManager mockFileManager;
    private DatabaseManager mockDatabaseManager;
    private InputHandler mockInputHandler;
    private Random mockRandom;
    private Game game;
    private GameLogic mockGameLogic;

    @BeforeEach
    void setUp() {
        // Mock dependencies
        mockBoard = mock(Board.class);
        mockInputHandler = mock(InputHandler.class);
        mockFileManager = mock(FileManager.class);
        mockRandom = mock(Random.class);
        mockDatabaseManager = mock(DatabaseManager.class);
        mockGameLogic = mock(GameLogic.class);

        // Létrehozunk egy mock játékost
        Player humanPlayer = new Player("TestPlayer", 'R');

        // Inicializáljuk a Játék osztályt
        game = new Game(humanPlayer, mockBoard, mockInputHandler, null,
                mockFileManager, mockRandom, mockDatabaseManager);
    }

    @Test
    void testSaveGame_Success() throws IOException {

        doNothing().when(mockFileManager).saveBoard(mockBoard, "saved_game.txt");
        game.saveGame();

        verify(mockFileManager).saveBoard(mockBoard, "saved_game.txt");
    }

    @Test
    void testSaveGame_Failure() throws IOException {

        doThrow(new IOException("Test Exception")).when(mockFileManager).saveBoard(mockBoard, "saved_game.txt");
        game.saveGame();

    // Ellenőrizzük a kivételkezelést (nem várható összeomlás)
        verify(mockFileManager).saveBoard(mockBoard, "saved_game.txt");
    }

    @Test
    void testResetGame_LoadFromFile() throws IOException {

        doNothing().when(mockFileManager).loadBoard(mockBoard, "board_input.txt");

        game.resetGame();

        // Ellenőrizzük, hogy a tábla vissza lett állítva és betöltve
        verify(mockBoard).reset();
        verify(mockFileManager).loadBoard(mockBoard, "board_input.txt");
    }

    @Test
    void testResetGame_StartWithEmptyBoard() throws IOException {
        doThrow(new IOException("Test Exception")).when(mockFileManager).loadBoard(mockBoard, "board_input.txt");

        game.resetGame();

        verify(mockBoard).reset();
        verify(mockFileManager).loadBoard(mockBoard, "board_input.txt");
    }

    @Test
    void testViewHighScores() {
        // A legmagasabb pontszámok megjelenítésének szimulálása
        doNothing().when(mockDatabaseManager).displayHighScores();

        // Átirányítjuk a System.in-t, hogy szimuláljuk az "Enter" billentyű lenyomását
        InputStream originalIn = System.in;
        ByteArrayInputStream testInput = new ByteArrayInputStream("\n".getBytes());
        System.setIn(testInput);

        try {
            game.viewHighScores();
            verify(mockDatabaseManager).displayHighScores();
        } finally {
            // Az eredeti System.in visszaállítása
            System.setIn(originalIn);
        }
    }

    @Test
    void testStart_InvalidMenuChoice() {
        InputStream originalIn = System.in;
        ByteArrayInputStream testInput = new ByteArrayInputStream("4\n3\n".getBytes());
        System.setIn(testInput);

        // Rögzítjük a System.out kimenetet
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Meghívjuk a start metódust, hogy szimuláljuk a felhasználói bemenetet és rögzítsük a kimenetet
        game.start();

        assertTrue(outContent.toString().contains("Invalid choice. Please try again."));

        assertTrue(outContent.toString().contains("Exiting the game. Goodbye!"));

        // Visszaállítjuk az eredeti System.in és System.out értékeket
        System.setIn(originalIn);
        System.setOut(originalOut);
    }


    @Test
    void testPlayGame_HumanWins() throws Exception {
        when(mockGameLogic.isFull(mockBoard)).thenReturn(false); // Board not full
        when(mockGameLogic.checkWin(mockBoard, 'Y')).thenReturn(true); // Human wins

        // Sikeres lépés szimulálása
        when(mockGameLogic.makeMove(eq(mockBoard), anyInt(), anyChar())).thenReturn(true);

        Field gameLogicField = Game.class.getDeclaredField("gameLogic");
        gameLogicField.setAccessible(true);
        gameLogicField.set(game, mockGameLogic);

        game.playGame();

        // Ellenőrizzük, hogy a győzelem rögzítve lett az adatbázisban
        verify(mockDatabaseManager).addWin("TestPlayer");
    }

    @Test
    void testStart_ExitDirectly() {
        InputStream originalIn = System.in;
        ByteArrayInputStream testInput = new ByteArrayInputStream("3\n".getBytes());
        System.setIn(testInput);

        game.start();

        // Ellenőrizzük, hogy a játék nem indult el, és nem lett alaphelyzetbe állítva
        verify(mockBoard, never()).reset();

        System.setIn(originalIn);
    }

    @Test
    void testCloseDatabase() {
        game.closeDatabase();

        // Ellenőrizzük, hogy az adatbázis kapcsolat lezárult
        verify(mockDatabaseManager).close();
    }

    @Test
    void testShowMenu_WithoutModification() throws InterruptedException {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Thread testThread = new Thread(() -> game.start());
        testThread.start();

        Thread.sleep(100);
        testThread.interrupt();

        // Ellenőrizzük, hogy a menü kimenete tartalmazza a várt szöveget
        String menuOutput = outContent.toString();
        assert(menuOutput.contains("Welcome to Connect 4!"));
        assert(menuOutput.contains("1. Start Game"));
        assert(menuOutput.contains("2. View High Scores"));
        assert(menuOutput.contains("3. Exit"));

        System.setOut(originalOut);
    }

    @Test
    void testPlayerTurn_ValidMove() throws Exception {
        when(mockInputHandler.getPlayerMove(anyInt())).thenReturn(1);
        when(mockGameLogic.makeMove(mockBoard, 1, 'Y')).thenReturn(true);

        Field gameLogicField = Game.class.getDeclaredField("gameLogic");
        gameLogicField.setAccessible(true);
        gameLogicField.set(game, mockGameLogic);

        game.playerTurn();

        verify(mockInputHandler).getPlayerMove(anyInt());
        verify(mockGameLogic).makeMove(mockBoard, 1, 'Y');
    }

    @Test
    void testPlayerTurn_InvalidMoveThenValid() throws Exception {
        when(mockInputHandler.getPlayerMove(anyInt()))
                .thenReturn(1, 2);
        when(mockGameLogic.makeMove(mockBoard, 1, 'Y')).thenReturn(false);
        when(mockGameLogic.makeMove(mockBoard, 2, 'Y')).thenReturn(true);

        Field gameLogicField = Game.class.getDeclaredField("gameLogic");
        gameLogicField.setAccessible(true);
        gameLogicField.set(game, mockGameLogic);

        game.playerTurn();

        verify(mockInputHandler, times(2)).getPlayerMove(anyInt());
        verify(mockGameLogic).makeMove(mockBoard, 1, 'Y');
        verify(mockGameLogic).makeMove(mockBoard, 2, 'Y');
    }

    @Test
    void testComputerTurn() throws Exception {
        when(mockRandom.nextInt(anyInt())).thenReturn(1);
        when(mockGameLogic.makeMove(mockBoard, 1, 'R')).thenReturn(true);

        Field gameLogicField = Game.class.getDeclaredField("gameLogic");
        gameLogicField.setAccessible(true);
        gameLogicField.set(game, mockGameLogic);

        game.computerTurn();

        verify(mockRandom).nextInt(anyInt());
        verify(mockGameLogic).makeMove(mockBoard, 1, 'R');
    }

    @Test
    void testPlayGame_Draw() throws Exception {

        when(mockGameLogic.isFull(mockBoard)).thenReturn(false, true);
        when(mockGameLogic.checkWin(any(Board.class), anyChar())).thenReturn(false);

        // A játékmenet során a játékosok tudnak lépni, de a tábla tele lesz döntetlenre.
        when(mockGameLogic.makeMove(eq(mockBoard), anyInt(), anyChar())).thenReturn(true);

        Field gameLogicField = Game.class.getDeclaredField("gameLogic");
        gameLogicField.setAccessible(true);
        gameLogicField.set(game, mockGameLogic);

        game.playGame();

        verify(mockDatabaseManager, never()).addWin(anyString());

        verify(mockBoard, atLeastOnce()).display();
    }


    @Test
    void testSaveGame_ErrorMessageDisplayed() throws IOException {
        doThrow(new IOException("Disk Full")).when(mockFileManager).saveBoard(mockBoard, "saved_game.txt");

        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        game.saveGame();

        assertTrue(outContent.toString().contains("Failed to save game: Disk Full"));

        System.setOut(originalOut);
    }

    @Test
    void testViewHighScores_Available() throws IOException {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        InputStream originalIn = System.in;
        ByteArrayInputStream testInput = new ByteArrayInputStream("\n".getBytes());
        System.setIn(testInput);

        try {
            doNothing().when(mockDatabaseManager).displayHighScores();

            game.viewHighScores();

            verify(mockDatabaseManager).displayHighScores();
            assertTrue(outContent.toString().contains("High Scores:"));

        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
    }

    @Test
    void testViewHighScores_NotAvailable() throws IOException {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        InputStream originalIn = System.in;
        ByteArrayInputStream testInput = new ByteArrayInputStream("\n".getBytes());
        System.setIn(testInput);

        try {
            Game gameWithoutDatabase = new Game(
                    new Player("TestPlayer", 'Y'),
                    mockBoard,
                    mockInputHandler,
                    null,
                    mockFileManager,
                    mockRandom,
                    null
            );

            gameWithoutDatabase.viewHighScores();

            assertTrue(outContent.toString().contains("No high scores available."));
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
    }

    @Test
    void testStartGameAndPlay() {
        InputStream originalIn = System.in;
        ByteArrayInputStream testInput = new ByteArrayInputStream("3\n".getBytes());
        System.setIn(testInput);

        game.start();

        verify(mockBoard, never()).display();

        System.setIn(originalIn);
    }
}
