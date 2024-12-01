package hu.nye.util;

/**
 * Hasznossági osztály ANSI színkódokhoz.
 * Módszereket biztosít a szöveg színekkel történő formázásához és az alapértelmezett terminálszín visszaállításához.
 */
public class ColorUtils {

    // ----- ANSI Color Codes -----
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RESET = "\u001B[0m";

    /**
     * A megadott ANSI színnel formázza az üzenetet.
     *
     * @param color Az ANSI színkód (pl. ColorUtils.ANSI_RED).
     * @param message A formázandó üzenet.
     * @return A formázott üzenetsor az alkalmazott színnel.
     */
    public static String colorize(final String color, final String message) {
        return color + message + ANSI_RESET;
    }
}
