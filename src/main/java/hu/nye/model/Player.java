package hu.nye.model;

/**
 * Represents a player in the Connect-4 game.
 * Each player has a name and a symbol that represents them on the game board.
 *
 * @param name   the name of the player
 * @param symbol the symbol representing the player on the board
 */
public record Player(String name, char symbol) {

    /**
     * Returns a string representation of the Player.
     *
     * @return a string containing the player's name and symbol
     */
    @Override
    public String toString() {
        return "Player{name='" + name + "', symbol=" + symbol + '}';
    }

}
