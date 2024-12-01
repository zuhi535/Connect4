package hu.nye.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * Kezeli a Connect 4 játék adatbázis-interakcióit.
 */
public abstract class DatabaseManager {

    /** Az SQLite szolgáltatással való interakcióhoz használt adatbázis-kapcsolati objektum. */
    private Connection connection;

    // ----- Constructor -----
    /**
     * Adatbáziskezelőt hoz létre és
     * inicializálja a kapcsolatot az SQLite adatbázissal.
     * Azt is biztosítja, hogy az adatbázisséma megfelelően legyen beállítva.
     */
    public DatabaseManager() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:game.db");
            if (connection == null) {
                System.err.println("Failed to create connection object.");
            } else {
                System.out.println("Connected to database successfully.");
                initializeDatabase();
            }
        } catch (SQLException e) {
            System.err.println("Failed to connect "
                    + "to database: " + e.getMessage());
            connection = null;
        }
    }

    // ----- Database Initialization -----
    /** Az SQLite szolgáltatással való interakcióhoz használt adatbázis-kapcsolati objektum. */
    private void initializeDatabase() {
        String createTableSql = "CREATE TABLE IF NOT EXISTS high_scores ("
                + "player_name TEXT PRIMARY KEY, "
                + "wins INTEGER DEFAULT 0)";


        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(createTableSql);
        } catch (SQLException e) {
            System.err.println("Failed to "
                    + "initialize database: " + e.getMessage());
        }
    }

    // ----- Public Methods -----

    /**
     * Nyereményt ad az adott játékosnak.
     * Ha a játékos nem létezik,
     * hozzáadódnak az adatbázishoz.
     *
     * @param playerName a játékos nevét
     * @throws IllegalArgumentException ha a játékosnév érvénytelen
     */
    public void addWin(final String playerName) {
        validatePlayerName(playerName);

        try {
            if (isPlayerInDatabase(playerName)) {
                updatePlayerWins(playerName);
            } else {
                insertNewPlayer(playerName);
            }
        } catch (SQLException e) {
            System.err.println("Failed to add win "
                    + "for player " + playerName + ": " + e.getMessage());
        }
    }

    /**
     * A legmagasabb pontszámokat győzelmek szerint csökkenő sorrendben jeleníti meg.
     */
    public void displayHighScores() {
        String sql = "SELECT player_name, wins "
                + "FROM high_scores ORDER BY wins DESC";

        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            System.out.printf("%-20s %s%n", "Name", "Wins");
            System.out.println("-------------------- ----");
            while (rs.next()) {
                String player = rs.getString("player_name");
                int wins = rs.getInt("wins");
                System.out.printf("%-20s %d%n", player, wins);
            }
        } catch (SQLException e) {
            System.err.println("Failed to display "
                    + "high scores: " + e.getMessage());
        }
    }

    /**
     * Bezárja az adatbázis-kapcsolatot.
     */
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Failed to close "
                    + "database connection: " + e.getMessage());
        }
    }

    // ----- Private Helper Methods -----

    /**
     * Ellenőrzi a játékos nevét, hogy megbizonyosodjon arról, hogy az megfelel a kívánt formátumnak.
     *
     * @param playerName a játékos nevét
     * @throws IllegalArgumentException név, ha a név érvénytelen
     */
    void validatePlayerName(final String playerName) {
        if (playerName == null || playerName.trim().isEmpty()
                || !playerName.matches("[a-zA-Z0-9_]+")) {
            throw new IllegalArgumentException("Invalid player name.");
        }
    }

    /**
     * Ellenőrzi, hogy egy játékos létezik-e az adatbázisban.
     *
     * @param playerName a játékos nevét
     * @return igaz, ha a játékos létezik, egyébként hamis
     * @throws SQLException adatbázishiba esetén
     */
    boolean isPlayerInDatabase(final String playerName)
            throws SQLException {
        validatePlayerName(playerName);

        String checkSql = "SELECT 1 FROM high_scores WHERE player_name = ?";
        try (PreparedStatement checkStatement
                     = connection.prepareStatement(checkSql)) {
            checkStatement.setString(1, playerName);
            ResultSet rs = checkStatement.executeQuery();
            return rs.next(); // True if a row is found, otherwise false
        }
    }

    /**
     * Frissíti egy meglévő játékos győzelmi számát az adatbázisban.
     *
     * @param playerName annak a játékosnak a neve, akinek a nyereményét frissítik
     * @throws SQLException ha hiba történik az adatbázis elérése közben
     */
    void updatePlayerWins(final String playerName) throws SQLException {
        String updateSql = "UPDATE high_scores "
                + "SET wins = wins + 1 WHERE player_name = ?";
        try (PreparedStatement updateStatement
                     = connection.prepareStatement(updateSql)) {
            updateStatement.setString(1, playerName);
            updateStatement.executeUpdate();
        }
    }

    /**
     * Új játékost szúr be az adatbázisba egy kezdeti győzelemmel.
     *
     * @param playerName a játékos nevét
     * @throws SQLException adatbázishiba esetén
     */
    void insertNewPlayer(final String playerName) throws SQLException {
        String insertSql = "INSERT INTO high_scores "
                + "(player_name, wins) VALUES (?, 1)";
        try (PreparedStatement insertStatement
                     = connection.prepareStatement(insertSql)) {
            insertStatement.setString(1, playerName);
            insertStatement.executeUpdate();
        }
    }

    public abstract Connection getConnection();
}