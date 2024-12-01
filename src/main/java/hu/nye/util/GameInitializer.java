package hu.nye.util;

import java.util.Random;
import java.util.Scanner;

import hu.nye.controller.InputHandler;
import hu.nye.model.Board;
import hu.nye.model.Game;
import hu.nye.model.Player;
import hu.nye.repository.DatabaseManager;
import hu.nye.repository.FileManager;
import hu.nye.service.MoveValidator;

/**
 * Felelős egy új játék inicializálásáért az összes szükséges függőséggel.
 */
public class GameInitializer {
    private final Scanner scanner;
    private final Random random;
    private final DatabaseManager databaseManager;

    /**
     * GameInitializer konstruktora.
     * Lehetővé teszi a függőségi befecskendezést a könnyebb tesztelés és a rugalmasság érdekében.
     *
     * @param scanner Szkenner a felhasználói bevitelhez.
     * @param random Véletlenszerű generátor AI lépésekhez.
     * @param databaseManager DatabaseManager példány a magas pontszámok követéséhez.
     */
    public GameInitializer(final Scanner scanner, final Random random, final DatabaseManager databaseManager) {
        this.scanner = scanner;
        this.random = random;
        this.databaseManager = databaseManager;
    }

    /**
     * Új játék inicializálása a lejátszó, a tábla és az összes szükséges segédprogram beállításával.
     *
     * @param playerName A játékos neve.
     * @return Teljesen konfigurált játékpéldány.
     */
    public Game initializeGame(final String playerName) {
        Player player = new Player(playerName, 'R');
        Board board = new Board();
        board.reset(); // Tábla alaphelyzetbe állítása

        FileManager fileManager = new FileManager();
        try {
            fileManager.loadBoard(board, "board_input.txt");
            System.out.println("Board loaded from board_input.txt");
        } catch (Exception e) {
            System.out.println("No saved board found, starting with an empty board.");
        }

        InputHandler inputHandler = new InputHandler(scanner);
        MoveValidator moveValidator = new MoveValidator();

        return new Game(player, board, inputHandler, moveValidator, fileManager, random, databaseManager);
    }
}
