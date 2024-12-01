package hu.nye.model;

/**
 * Egy játékost képvisel a Connect-4 játékban.
 * Minden játékosnak van egy neve és egy szimbóluma, amely képviseli őket a játéktáblán.
 *
 * @param name a játékos nevét
 * @param symbol a táblán lévő játékost jelképező szimbólum
 */
public record Player(String name, char symbol) {

    /**
     * A lejátszó karakterlánc-ábrázolását adja vissza.
     *
     * @return egy karakterláncot, amely tartalmazza a játékos nevét és szimbólumát
     */
    @Override
    public String toString() {
        return "Player{name='" + name + "', symbol=" + symbol + '}';
    }

}
