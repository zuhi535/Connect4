package hu.nye.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class DatabaseManager {
    private Connection connection;

    public DatabaseManager() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:game.db");
            if (connection == null) {
                System.err.println("Failed to create connection object.");
            } else {
                System.out.println("Connected to database successfully.");
                initializeDatabase(); // Initialize the database
            }
        } catch (SQLException e) {
            System.err.println("Failed to connect to database: " + e.getMessage());
            connection = null; // Ensure null value if an error occurs
        }
    }

    // Checks if the high_scores table exists, and creates it if not
    private void initializeDatabase() {
        try (Statement statement = connection.createStatement()) {
            String createTableSql = "CREATE TABLE IF NOT EXISTS high_scores (" +
                    "player_name TEXT PRIMARY KEY, " +
                    "score REAL DEFAULT 0)";
            statement.executeUpdate(createTableSql);
        } catch (SQLException e) {
            System.err.println("Failed to initialize database: " + e.getMessage());
        }
    }

    // Method to add results (1 point for a win, 0.5 for a draw, 0 for a loss)
    public void addResult(String playerName, double points) {
        try {
            if (isPlayerInDatabase(playerName)) {
                String updateSql = "UPDATE high_scores SET score = score + ? WHERE player_name = ?";
                try (PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {
                    updateStatement.setDouble(1, points);
                    updateStatement.setString(2, playerName);
                    updateStatement.executeUpdate();
                }
            } else {
                String insertSql = "INSERT INTO high_scores (player_name, score) VALUES (?, ?)";
                try (PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {
                    insertStatement.setString(1, playerName);
                    insertStatement.setDouble(2, points);
                    insertStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.err.println("Failed to add result for player " + playerName + ": " + e.getMessage());
        }
    }

    // Checks if the player is already in the database
    private boolean isPlayerInDatabase(String playerName) {
        String checkSql = "SELECT 1 FROM high_scores WHERE player_name = ?";
        try (PreparedStatement checkStatement = connection.prepareStatement(checkSql)) {
            checkStatement.setString(1, playerName);
            ResultSet rs = checkStatement.executeQuery();
            return rs.next(); // True if already exists, otherwise false
        } catch (SQLException e) {
            System.err.println("Failed to check player in database: " + e.getMessage());
            return false;
        }
    }

    // Displays the high scores (in descending order of score)
    public void displayHighScores() {
        try (Statement statement = connection.createStatement()) {
            String sql = "SELECT player_name, score FROM high_scores ORDER BY score DESC";
            ResultSet rs = statement.executeQuery(sql);

            System.out.printf("%-20s %s%n", "Name", "Score"); // Header formatting
            System.out.println("-------------------- -----"); // Divider line
            while (rs.next()) {
                String player = rs.getString("player_name");
                double score = rs.getDouble("score");
                System.out.printf("%-20s %.1f%n", player, score); // Formatted output
            }
        } catch (SQLException e) {
            System.err.println("Failed to display high scores: " + e.getMessage());
        }
    }

    // Close the connection when no longer needed
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Failed to close database connection: " + e.getMessage());
        }
    }
}
