package hu.nye.repository;

import hu.nye.model.Board;
import org.junit.jupiter.api.*;
import java.io.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class FileManagerTest {

    private FileManager fileManager;
    private Board mockBoard;

    @BeforeEach
    void setUp() {
        fileManager = new FileManager();
        mockBoard = mock(Board.class);
    }

    @Test
    void testLoadBoardWithValidFile() throws IOException {

        String filePath = "test_load_board.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("RR....\n");
            writer.write("YY....\n");
        }

        when(mockBoard.getRows()).thenReturn(6);
        when(mockBoard.getCols()).thenReturn(6);

        doNothing().when(mockBoard).initializeEmptyBoard();
        doNothing().when(mockBoard).setRow(anyInt(), any(char[].class));

        fileManager.loadBoard(mockBoard, filePath);

        verify(mockBoard).initializeEmptyBoard();
        verify(mockBoard, times(2)).setRow(anyInt(), any(char[].class));

        new File(filePath).delete();
    }

    @Test
    void testLoadBoardWithNonexistentFile() throws IOException {

        fileManager.loadBoard(mockBoard, "nonexistent_file.txt");

        verify(mockBoard, never()).initializeEmptyBoard();
        verify(mockBoard, never()).setRow(anyInt(), any(char[].class));
    }

    @Test
    void testSaveBoardWithNewFile() throws IOException {

        String filePath = "test_save_board.txt";
        char[][] boardData = {
                {'R', 'R', '.', '.', '.', '.'},
                {'Y', 'Y', '.', '.', '.', '.'}
        };

        when(mockBoard.getBoard()).thenReturn(boardData);

        fileManager.saveBoard(mockBoard, filePath);

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            assertEquals("RR....", reader.readLine());
            assertEquals("YY....", reader.readLine());
            assertEquals("====", reader.readLine());
        }

        new File(filePath).delete();
    }

    @Test
    void testSaveBoardWithExistingFile() throws IOException {
        // Setup
        String filePath = "test_existing_board.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Initial Data\n");
        }

        char[][] boardData = {
                {'R', 'R', '.', '.', '.', '.'},
                {'Y', 'Y', '.', '.', '.', '.'}
        };

        when(mockBoard.getBoard()).thenReturn(boardData);

        fileManager.saveBoard(mockBoard, filePath);

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            assertEquals("Initial Data", reader.readLine());
            assertEquals("RR....", reader.readLine());
            assertEquals("YY....", reader.readLine());
            assertEquals("====", reader.readLine());
        }

        new File(filePath).delete();
    }

    @Test
    void testIsFileEndingWith() throws IOException {

        String filePath = "test_file_ending.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Some data\n====");
        }

        boolean result = fileManager.isFileEndingWith(filePath, "====");

        assertTrue(result);

        new File(filePath).delete();
    }

    @Test
    void testIsFileNotEndingWith() throws IOException {

        String filePath = "test_file_not_ending.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Some data\n");
        }

        boolean result = fileManager.isFileEndingWith(filePath, "====");

        assertFalse(result);

        new File(filePath).delete();
    }
}
