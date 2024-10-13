package hu.nye.model;

/**
 * Model: a class representing the name and the symbol of a player.
 */
public final class Player {
    /**
     * The name of the player.
     */
    private final String name;

    /**
     * The symbol representing the player (e.g., 'X', 'O').
     */
    private final char symbol;

    /**
     * Creates a Player with the name and symbol given.
     *
     * @param playerName the name of the player
     * @param playerSymbol the symbol to represent the player
     */
    public Player(final String playerName, final char playerSymbol) {
        this.name = playerName;
        this.symbol = playerSymbol;
    }

    /**
     * Returns the name of player.
     *
     * @return the name of player.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the player's symbol.
     *
     * @return the symbol representing the player
     */
    public char getSymbol() {
        return symbol;
    }

    /**
     * Returns a string representation of
     * the player in the form "Name (Symbol)".
     *
     * @return a string representation of the player
     */
    @Override
    public String toString() {
        return name + " (" + symbol + ")";
    }
}
