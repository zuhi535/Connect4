package hu.nye.model;

/**
 * Represents a player in the game with a name and a symbol.
 */
public final class Player {
    /** The name of the player. */
    private final String name;

    /** The symbol representing the player (e.g., 'X', 'O'). */
    private final char symbol;

    /**
     * Constructs a Player with the specified name and symbol.
     *
     * @param playerName   the name of the player
     * @param playerSymbol the symbol representing the player
     */
    public Player(final String playerName, final char playerSymbol) {
        this.name = playerName;
        this.symbol = playerSymbol;
    }


    /**
     * Gets the player's name.
     *
     * @return the name of the player
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
     * the player in the format "Name (Symbol)".
     *
     * @return a string representation of the player
     */
    @Override
    public String toString() {
        return name + " (" + symbol + ")";
    }
}
