package hu.nye.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ColorUtilsTest {

    @Test
    void testColorizeWithRed() {
        
        String color = ColorUtils.ANSI_RED;
        String message = "Error Message";

        String result = ColorUtils.colorize(color, message);

        assertEquals(ColorUtils.ANSI_RED + message + ColorUtils.ANSI_RESET, result);
    }

    @Test
    void testColorizeWithYellow() {

        String color = ColorUtils.ANSI_YELLOW;
        String message = "Warning Message";

        String result = ColorUtils.colorize(color, message);

        assertEquals(ColorUtils.ANSI_YELLOW + message + ColorUtils.ANSI_RESET, result);
    }

    @Test
    void testColorizeWithEmptyMessage() {

        String color = ColorUtils.ANSI_RED;
        String message = "";

        String result = ColorUtils.colorize(color, message);

        assertEquals(ColorUtils.ANSI_RED + message + ColorUtils.ANSI_RESET, result);
    }

    @Test
    void testColorizeWithNullMessage() {

        String color = ColorUtils.ANSI_RED;
        String message = null;
        
        String result = ColorUtils.colorize(color, message);

        assertEquals(ColorUtils.ANSI_RED + "null" + ColorUtils.ANSI_RESET, result);
    }
}
