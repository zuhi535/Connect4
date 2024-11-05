package hu.nye.model;

import hu.nye.util.DatabaseManager;
import hu.nye.util.FileManager;
import hu.nye.util.InputHandler;
import hu.nye.util.MoveValidator;

import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public final class Game {
    private final Board gameBoard;
    private final Player humanPlayer;
    private final Player computerPlayer;
    private final InputHandler inputHandler;
    private final MoveValidator moveValidator;
    private final FileManager fileManager;
    private final Random random;
    private final DatabaseManager databaseManager; // DatabaseManager field

    public Game(Player player, Board board, InputHandler inputHandler, MoveValidator moveValidator,
                FileManager fileManager, Random random, DatabaseManager databaseManager) {
        this.gameBoard = board;
        this.humanPlayer = player;
        this.computerPlayer = new Player("Computer", 'Y');
        this.inputHandler = inputHandler;
        this.moveValidator = moveValidator;
        this.fileManager = fileManager;
        this.random = random;
        this.databaseManager = databaseManager;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            showMenu(scanner);
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    playGame();
                    break;
                case 2:
                    viewHighScores();
                    break;
                case 3:
                    running = false; // Exit the loop and terminate the program
                    System.out.println("Exiting the game. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }

    private void showMenu(Scanner scanner) {
        System.out.println("Welcome to Connect 4!");
        System.out.println("1. Start Game");
        System.out.println("2. View High Scores");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
    }

    private void playGame() {
        gameBoard.display();
        System.out.println();

        boolean isPlayerTurn = true;
        while (!gameBoard.isFull()) {
            if (isPlayerTurn) {
                playerTurn();
            } else {
                computerTurn();
            }

            char currentSymbol = isPlayerTurn ? humanPlayer.symbol() : computerPlayer.symbol();
            if (moveValidator.hasFourInARow(gameBoard, currentSymbol)) {
                gameBoard.display();
                System.out.println((isPlayerTurn ? humanPlayer.name() : "Computer") + " wins!");

                if (isPlayerTurn && databaseManager != null) {
                    databaseManager.addResult(humanPlayer.name(), 1);
                }
                saveGame();
                return;
            }

            gameBoard.display();
            System.out.println();

            isPlayerTurn = !isPlayerTurn;
        }

        System.out.println("The game is a draw!");
        saveGame();

        if (databaseManager != null) {
            System.out.println("Final Scores:");
            databaseManager.displayHighScores();
        }
    }

    private void viewHighScores() {
        if (databaseManager != null) {
            System.out.println("High Scores:");
            databaseManager.displayHighScores();
        } else {
            System.out.println("No high scores available.");
        }

        System.out.println("Press Enter to return to the menu.");
        try {
            System.in.read(); // Wait for user input to return to the menu
        } catch (IOException e) {
            System.err.println("Error reading input: " + e.getMessage());
        }
    }

    private void playerTurn() {
        int col;
        do {
            col = inputHandler.getPlayerMove(gameBoard.getCols() - 1);
        } while (moveValidator.isColumnFull(gameBoard, col));

        gameBoard.makeMove(col, humanPlayer.symbol());
    }

    private void computerTurn() {
        int col;
        do {
            col = random.nextInt(gameBoard.getCols());
        } while (moveValidator.isColumnFull(gameBoard, col));

        gameBoard.makeMove(col, computerPlayer.symbol());
    }

    private void saveGame() {
        try {
            fileManager.saveBoard(gameBoard, "saved_game.txt");
        } catch (IOException e) {
            System.out.println("Failed to save game: " + e.getMessage());
        }
    }
}
