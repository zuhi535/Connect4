package hu.nye.model;

import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

import hu.nye.controller.InputHandler;
import hu.nye.repository.DatabaseManager;
import hu.nye.repository.FileManager;
import hu.nye.service.GameLogic;
import hu.nye.service.MoveValidator;

/**
 * A fő osztály, amely a Connect 4 játékot képviseli.
 * Kezeli a játék folyamatát, a játékosokat, a tábla állapotát és az interakciókat
 * például mentés, betöltés és magas pontszámok megtekintése.
 */
public final class Game {

    /** A játéktábla, ahol a játékot játsszák. */
    private final Board gameBoard;

    /** Az emberi játékos. */
    private final Player humanPlayer;

    /** A számítógépes lejátszó. */
    private final Player computerPlayer;

    /** A felhasználói bevitel kezelője. */
    private final InputHandler inputHandler;

    /** A fájlkezelő a játék állapotának mentéséhez/betöltéséhez. */
    private final FileManager fileManager;

    /** A számítógép mozgatásához használt véletlenszám-generátor. */
    private final Random random;

    /** Az adatbázis-kezelő a magas pontszámok kezelésére. */
    private final DatabaseManager databaseManager;

    /** A játék logikája a játék folyamatának kezeléséhez. */
    private final GameLogic gameLogic;

    public Player getHumanPlayer() {
        return humanPlayer;
    }

    /**
     * Új játékpéldányt hoz létre a megadott játékosokkal,
     * játéktábla és játékelemek.
     *
     * @param player A játékhoz hozzáadandó emberi játékos.
     * @param board A játéktábla, ahol a játékot játszani fogják.
     * @param inputHandlerParam A felhasználói bevitel kezelésére szolgáló kezelő.
     * @param moveValidator A játékos lépéseinek érvényesítésére szolgáló szolgáltatás (itt közvetlenül nem használatos).
     * @param fileManagerParam A játékadatok mentéséért és betöltéséért felelős kezelő.
     * @param randomParam A számítógép mozgatásához használt véletlenszám-generátor.
     * @param databaseManagerParam A magas pontszámú adatbázissal való interakcióért felelős vezető.
     */
    public Game(final Player player,
                final Board board,
                final InputHandler inputHandlerParam,
                final MoveValidator moveValidator,
                final FileManager fileManagerParam,
                final Random randomParam,
                final DatabaseManager databaseManagerParam) {
        this.gameBoard = board;
        this.humanPlayer = new Player(player.name(), 'Y');
        this.computerPlayer = new Player("Computer", 'R');
        this.inputHandler = inputHandlerParam;
        this.fileManager = fileManagerParam;
        this.random = randomParam;
        this.databaseManager = databaseManagerParam;
        this.gameLogic = new GameLogic();
    }

    /**
     * Elindítja a játékot és kezeli a fő játékhurkot.
     * Ez a módszer kéri a felhasználótól a bevitelt és
     * elindítja a játék folyamatát.
     */
    public void start() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            showMenu();
            int choice = scanner.nextInt();


            switch (choice) {
                case 1 -> {
                    resetGame();
                    playGame();
                }
                case 2 -> viewHighScores();
                case 3 -> {
                    running = false;
                    System.out.println("Exiting the game. Goodbye!");
                }
                default -> System.out.println("Invalid choice. "
                        + "Please try again.");
            }
        }
        scanner.close();
    }

    /**
     * Megjeleníti a főmenü opcióit a felhasználó számára.
     */
    private void showMenu() {
        System.out.println("Welcome to Connect 4!");
        System.out.println("1. Start Game");
        System.out.println("2. View High Scores");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
    }

    /**
     * Szabályozza a játék áramlását, váltakozva
     * Fordulatok az emberi játékos között
     * és a számítógépes lejátszó. A játék addig folytatódik, amíg
     * A tábla megtelt, vagy egy játékos nyer.
     * Minden lépés után a tábla frissül és
     * ellenőrizte a győzelmet. Ha az emberi játékos nyer,
     * Nyereményük rögzítésre kerül az adatbázisban, és a játék mentésre kerül.
     * Ha a játék döntetlennel ér véget, megjelenik egy üzenet.
     */
    void playGame() {
        gameBoard.display();
        System.out.println();

        boolean isPlayerTurn = true;

        while (!gameLogic.isFull(gameBoard)) {
            if (isPlayerTurn) {
                playerTurn();
            } else {
                computerTurn();
            }

            char currentSymbol = isPlayerTurn ? humanPlayer.symbol()
                    : computerPlayer.symbol();

            if (gameLogic.checkWin(gameBoard, currentSymbol)) {
                gameBoard.display();
                System.out.println((isPlayerTurn ? humanPlayer.name()
                        : "Computer") + " wins!");

                if (isPlayerTurn && databaseManager != null) {
                    databaseManager.addWin(humanPlayer.name());
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

    /**
     * Kezeli az emberi játékos fordulatát.
     * Folyamatosan kéri a játékost egy
     * érvényes lépés, amíg meg nem történik.
     */
    void playerTurn() {
        int col;
        boolean validMove = false;

        while (!validMove) {
            col = inputHandler.getPlayerMove(gameBoard.getCols() - 1);

            if (gameLogic.makeMove(gameBoard, col, humanPlayer.symbol())) {
                validMove = true;
            } else {

                System.out.println("This column is full. "
                        + "Please choose another column.");
            }
        }
    }

    /**
     * Kezeli a számítógépes lejátszó kikapcsolását.
     * Folyamatosan kéri a játékost egy érvényes lépésre, amíg meg nem történik.
     */
    void computerTurn() {
        int col;
        do {
            col = random.nextInt(gameBoard.getCols());
        } while (!gameLogic.makeMove(gameBoard, col, computerPlayer.symbol()));
    }

    /**
     * Fájlba menti a játéktábla aktuális állapotát.
     * A játék állapota "saved_game.txt" formátumban kerül mentésre.
     */
    void saveGame() {
        try {
            fileManager.saveBoard(gameBoard, "saved_game.txt");
        } catch (IOException e) {
            System.out.println("Failed to save game: " + e.getMessage());
        }
    }

    /**
     * Visszaállítja a játéktáblát és megpróbálja
     * egy korábbi táblaállapot betöltése fájlból.
     * Ha nem található korábbi állapot, üres táblával kezdődik.
     */
    public void resetGame() {
        gameBoard.reset();
        try {
            fileManager.loadBoard(gameBoard, "board_input.txt");
            System.out.println("Board loaded from board_input.txt");
        } catch (IOException e) {
            System.out.println("Starting with an empty board.");
        }
        System.out.println("Game has been reset. "
                + "You can now start a new game.");
    }

    /**
     * Megjeleníti az adatbázis legmagasabb pontszámait.
     * Ha nincsenek magas pontszámok, tájékoztatja a felhasználót.
     */
    void viewHighScores() {
        if (databaseManager != null) {
            System.out.println("High Scores:");
            databaseManager.displayHighScores();
        } else {
            System.out.println("No high scores available.");
        }

        System.out.println("Press Enter to return to the menu.");
        try {
            System.in.read();
        } catch (IOException e) {
            System.err.println("Error reading input: " + e.getMessage());
        }
    }

    /**
     * Bezárja az adatbázis-kapcsolatot
     * ha az adatbázis-kezelő inicializálva van.
     */
    public void closeDatabase() {
        if (databaseManager != null) {
            databaseManager.close();
        }
    }
}
