package hu.nye;

import hu.nye.model.Game;

/**
 * The entry point for the Connect Four game application.
 */
public final class App {

    /**
     * Private constructor to prevent instantiation of the utility class.
     */
    private App() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * The main method that starts the Connect Four game.
     *
     * @param args Command-line arguments (not used in this application).
     */
    public static void main(final String[] args) {
        Game game = new Game();
        game.start();
    }
}
