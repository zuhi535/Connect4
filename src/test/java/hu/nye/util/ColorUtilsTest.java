package hu.nye.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ColorUtilsTest {

    @Test
    void testColorizeWithRed() {
        // Given
        String color = ColorUtils.ANSI_RED;
        String message = "Error Message";

        // When
        String result = ColorUtils.colorize(color, message);

        // Then
        assertEquals(ColorUtils.ANSI_RED + message + ColorUtils.ANSI_RESET, result);
    }

    @Test
    void testColorizeWithYellow() {
        // Given
        String color = ColorUtils.ANSI_YELLOW;
        String message = "Warning Message";

        // When
        String result = ColorUtils.colorize(color, message);

        // Then
        assertEquals(ColorUtils.ANSI_YELLOW + message + ColorUtils.ANSI_RESET, result);
    }

    @Test
    void testColorizeWithEmptyMessage() {
        // Given
        String color = ColorUtils.ANSI_RED;
        String message = "";

        // When
        String result = ColorUtils.colorize(color, message);

        // Then
        assertEquals(ColorUtils.ANSI_RED + message + ColorUtils.ANSI_RESET, result);
    }

    @Test
    void testColorizeWithNullMessage() {
        // Given
        String color = ColorUtils.ANSI_RED;
        String message = null;

        // When
        String result = ColorUtils.colorize(color, message);

        // Then
        assertEquals(ColorUtils.ANSI_RED + "null" + ColorUtils.ANSI_RESET, result);
    }
}
