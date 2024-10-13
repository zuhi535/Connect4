package hu.nye;

import hu.nye.model.Game;

/**
 * The Connect Four game application entry point.
 */
public final class App {

    /**
     * Private constructor to prevent instantiation of the utility class.
     */
    private App() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Main entry point to the Connect Four game.
     *
     * @param args The command line arguments
     *             which are not used in this application.
     */
    public static void main(final String[] args) {
        Game game = new Game();
        game.start();
    }
}