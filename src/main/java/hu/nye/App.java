package hu.nye;

import java.sql.Connection;
import java.util.Random;
import java.util.Scanner;

import hu.nye.model.Game;
import hu.nye.repository.DatabaseManager;
import hu.nye.util.GameInitializer;

/**
 * A Connect4 játékalkalmazás fő belépési pontja.
 * Ez az osztály kezeli a játék inicializálását, elindítását,
 * és a felhasználóval való interakció kezelését.
 * Biztosítja továbbá, hogy az erőforrások, például az adatbázis-
 * kapcsolat és a scanner, használat után megfelelően lezárásra kerüljenek.
 */
public final class App {

    /**
     * A Connect4 játékot futtató fő metódus.
     * Inicializálja a játékot a felhasználói bemenet bekérésével,
     * beállítja a játék paramétereit,
     * és elindítja a játék ciklust. Kezeli továbbá az
     * esetlegesen előforduló kivételeket a végrehajtás során.
     *
     * @param args Parancssori argumentumok, ebben az alkalmazásban nem használatosak.
     */
    public static void main(final String[] args) {

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Welcome to Connect4!");

            System.out.print("Enter player name: ");
            String playerName = scanner.nextLine();

            Random random = new Random();
            DatabaseManager databaseManager = new DatabaseManager() {
                @Override
                public Connection getConnection() {
                    return null;
                }
            };
            GameInitializer initializer = new GameInitializer(scanner, random, databaseManager);
            Game game = initializer.initializeGame(playerName);

            game.start();

            game.closeDatabase();
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }

}
