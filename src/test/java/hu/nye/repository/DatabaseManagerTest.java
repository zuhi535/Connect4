package hu.nye.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.sql.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class DatabaseManagerTest {

    @Mock private Connection mockConnection;
    @Mock private PreparedStatement mockPreparedStatement;
    @Mock private Statement mockStatement;
    @Mock private ResultSet mockResultSet;
    private DatabaseManager databaseManager;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        databaseManager = new DatabaseManager() {
            @Override
            public Connection getConnection() {
                return mockConnection;
            }
        };

        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
    }

    @Test
    void testConstructor_SuccessfulConnection() throws SQLException {

        when(mockConnection.isClosed()).thenReturn(false);

        DatabaseManager localDatabaseManager = new DatabaseManager() {
            @Override
            public Connection getConnection() {
                return mockConnection;
            }
        };

        assertNotNull(localDatabaseManager);
    }

    @Test
    void testCloseConnection_FailedToClose() throws SQLException {

        when(mockConnection.isClosed()).thenReturn(false);
        doThrow(new SQLException("Close failed")).when(mockConnection).close();


        assertDoesNotThrow(() -> databaseManager.close());
    }


    @Test
    void testAddWin_ThrowsSQLException() throws SQLException {

        String playerName = "FaultyPlayer";
        doThrow(new SQLException("Database error")).when(mockConnection).prepareStatement(anyString());


        assertDoesNotThrow(() -> databaseManager.addWin(playerName));
    }


    @Test
    void testConstructor_FailedConnection() {

        DatabaseManager localDatabaseManager = new DatabaseManager() {
            @Override
            public Connection getConnection() {
                return null; 
            }
        };


        assertNotNull(localDatabaseManager);
    }

    @Test
    void testAddWin_PlayerExists() throws SQLException {

        String playerName = "ExistingPlayer";

        // Létrehozunk egy spy-t a DatabaseManager osztályból
        DatabaseManager spyDatabaseManager = Mockito.spy(databaseManager);

        when(spyDatabaseManager.isPlayerInDatabase(playerName)).thenReturn(true);
        doNothing().when(spyDatabaseManager).updatePlayerWins(playerName);

        spyDatabaseManager.addWin(playerName);

        verify(spyDatabaseManager).updatePlayerWins(playerName);
    }

    @Test
    void testAddWin_PlayerDoesNotExist() throws SQLException {

        String playerName = "NewPlayer";

        DatabaseManager spyDatabaseManager = Mockito.spy(databaseManager);

        when(spyDatabaseManager.isPlayerInDatabase(playerName)).thenReturn(false);
        doNothing().when(spyDatabaseManager).insertNewPlayer(playerName);

        spyDatabaseManager.addWin(playerName);

        verify(spyDatabaseManager).insertNewPlayer(playerName);
    }

    @Test
    void testValidatePlayerName_ValidName() {
        assertDoesNotThrow(() -> databaseManager.validatePlayerName("validPlayer123"));
    }

    @Test
    void testValidatePlayerName_InvalidName() {
        assertThrows(IllegalArgumentException.class, () -> databaseManager.validatePlayerName("invalid player"));
        assertThrows(IllegalArgumentException.class, () -> databaseManager.validatePlayerName(""));
        assertThrows(IllegalArgumentException.class, () -> databaseManager.validatePlayerName(null));
    }

    @Test
    void testIsPlayerInDatabase_Found() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);

        boolean exists = databaseManager.isPlayerInDatabase("ExistingPlayer");

        assertTrue(exists);
    }

    @Test
    void testIsPlayerInDatabase_NotFound() throws SQLException {

        String playerName = "TestPlayer";
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        boolean result = databaseManager.isPlayerInDatabase(playerName);

        assertFalse(result);
    }

    @Test
    void testDisplayHighScores_SQLException() throws SQLException {

        when(mockStatement.executeQuery(anyString())).thenThrow(new SQLException("Query failed"));

        assertDoesNotThrow(() -> databaseManager.displayHighScores());
    }

    @Test
    void testAddWin_UpdateFails() throws SQLException {

        String playerName = "ExistingPlayer";

        DatabaseManager spyDatabaseManager = Mockito.spy(databaseManager);
        when(spyDatabaseManager.isPlayerInDatabase(playerName)).thenReturn(true);

        doThrow(new SQLException("Update failed")).when(spyDatabaseManager).updatePlayerWins(playerName);

        assertDoesNotThrow(() -> spyDatabaseManager.addWin(playerName));
    }

    //ellenőrzi ne hívja meg újra az adatbázis kapcsolatot
    @Test
    void testClose_AlreadyClosed() throws SQLException {

        when(mockConnection.isClosed()).thenReturn(true);

        databaseManager.close();

        verify(mockConnection, never()).close();
    }
}
