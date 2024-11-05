package hu.nye;

import hu.nye.model.Game;
import hu.nye.model.Player;
import hu.nye.model.Board;
import hu.nye.util.DatabaseManager;  // Importáld a DatabaseManager osztályt
import hu.nye.util.FileManager;
import hu.nye.util.InputHandler;
import hu.nye.util.MoveValidator;

import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public final class App {

    public static void main(final String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        DatabaseManager databaseManager = new DatabaseManager();  // Inicializáld itt

        try {
            System.out.println("Welcome to Connect4!");
            System.out.print("Enter player name: ");
            String playerName = scanner.nextLine();

            Player player = new Player(playerName, 'R');
            Board board = new Board();
            FileManager fileManager = new FileManager();

            try {
                fileManager.loadBoard(board, "board_input.txt");
                System.out.println("Board loaded from board_input.txt");
            } catch (IOException e) {
                System.out.println("No saved board found, starting with an empty board.");
            }

            InputHandler inputHandler = new InputHandler(scanner);
            MoveValidator moveValidator = new MoveValidator();

            // Inicializáld a játékot a DatabaseManager-rel
            Game game = new Game(player, board, inputHandler, moveValidator, fileManager, random, databaseManager);
            game.start();
        } finally {
            // Zárd le a database kapcsolatot
            databaseManager.close();
        }
    }
}