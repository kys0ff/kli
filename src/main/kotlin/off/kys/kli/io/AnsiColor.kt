@file:Suppress("unused", "ClassName")

package off.kys.kli.io

/**
 * Represents ANSI color codes used for styling terminal text.
 *
 * The `AnsiColor` class contains a collection of predefined colors and styles that can
 * be used to format text output in a terminal. It supports foreground colors, background colors,
 * and text styles, all of which are represented as ANSI escape codes.
 *
 * You can use these colors and styles to colorize your terminal output.
 *
 * **Types of Colors and Styles**:
 * - Foreground Colors
 * - Background Colors
 * - Bright Foreground and Background Colors
 * - Text Styles (e.g., bold, underline)
 * - Custom colors can also be created using the `Custom` class.
 */
sealed class AnsiColor(open val code: String) {

    // Foreground Colors
    data object BLACK : AnsiColor("\u001B[30m")  // Black color
    data object RED : AnsiColor("\u001B[31m")  // Red color
    data object GREEN : AnsiColor("\u001B[32m")  // Green color
    data object YELLOW : AnsiColor("\u001B[33m")  // Yellow color
    data object BLUE : AnsiColor("\u001B[34m")  // Blue color
    data object MAGENTA : AnsiColor("\u001B[35m")  // Magenta color
    data object CYAN : AnsiColor("\u001B[36m")  // Cyan color
    data object WHITE : AnsiColor("\u001B[37m")  // White color

    // Default (System Terminal Text Color)
    data object DEFAULT : AnsiColor("\u001B[39m")  // Default terminal text color

    // Bright Foreground Colors
    data object BRIGHT_BLACK : AnsiColor("\u001B[90m")  // Bright black color
    data object BRIGHT_RED : AnsiColor("\u001B[91m")  // Bright red color
    data object BRIGHT_GREEN : AnsiColor("\u001B[92m")  // Bright green color
    data object BRIGHT_YELLOW : AnsiColor("\u001B[93m")  // Bright yellow color
    data object BRIGHT_BLUE : AnsiColor("\u001B[94m")  // Bright blue color
    data object BRIGHT_MAGENTA : AnsiColor("\u001B[95m")  // Bright magenta color
    data object BRIGHT_CYAN : AnsiColor("\u001B[96m")  // Bright cyan color
    data object BRIGHT_WHITE : AnsiColor("\u001B[97m")  // Bright white color

    // Background Colors
    data object BG_BLACK : AnsiColor("\u001B[40m")  // Black background
    data object BG_RED : AnsiColor("\u001B[41m")  // Red background
    data object BG_GREEN : AnsiColor("\u001B[42m")  // Green background
    data object BG_YELLOW : AnsiColor("\u001B[43m")  // Yellow background
    data object BG_BLUE : AnsiColor("\u001B[44m")  // Blue background
    data object BG_MAGENTA : AnsiColor("\u001B[45m")  // Magenta background
    data object BG_CYAN : AnsiColor("\u001B[46m")  // Cyan background
    data object BG_WHITE : AnsiColor("\u001B[47m")  // White background

    // Bright Background Colors
    data object BG_BRIGHT_BLACK : AnsiColor("\u001B[100m")  // Bright black background
    data object BG_BRIGHT_RED : AnsiColor("\u001B[101m")  // Bright red background
    data object BG_BRIGHT_GREEN : AnsiColor("\u001B[102m")  // Bright green background
    data object BG_BRIGHT_YELLOW : AnsiColor("\u001B[103m")  // Bright yellow background
    data object BG_BRIGHT_BLUE : AnsiColor("\u001B[104m")  // Bright blue background
    data object BG_BRIGHT_MAGENTA : AnsiColor("\u001B[105m")  // Bright magenta background
    data object BG_BRIGHT_CYAN : AnsiColor("\u001B[106m")  // Bright cyan background
    data object BG_BRIGHT_WHITE : AnsiColor("\u001B[107m")  // Bright white background

    // Styles
    data object RESET : AnsiColor("\u001B[0m")  // Reset style
    data object BOLD : AnsiColor("\u001B[1m")  // Bold style
    data object FAINT : AnsiColor("\u001B[2m")  // Faint (light) style
    data object ITALIC : AnsiColor("\u001B[3m")  // Italic style
    data object UNDERLINE : AnsiColor("\u001B[4m")  // Underline style
    data object SLOW_BLINK : AnsiColor("\u001B[5m")  // Slow blink style
    data object RAPID_BLINK : AnsiColor("\u001B[6m")  // Rapid blink style
    data object REVERSED : AnsiColor("\u001B[7m")  // Reverse colors style
    data object CONCEAL : AnsiColor("\u001B[8m")  // Conceal text style
    data object CROSSED_OUT : AnsiColor("\u001B[9m")  // Strikethrough style

    /**
     * Allows the creation of custom colors using ANSI codes.
     *
     * @param code The custom ANSI code for the color.
     */
    data class Custom(override val code: String) : AnsiColor(code)

    companion object {
        /**
         * Creates a custom `AnsiColor` object using the given ANSI code.
         *
         * @param code The ANSI code for the custom color.
         * @return A custom `AnsiColor` object.
         */
        fun custom(code: String): AnsiColor = Custom(code)

        /**
         * Helper function to wrap a text with a color.
         *
         * @param text The text to colorize.
         * @param color The color to apply.
         * @return The colorized text.
         */
        fun colorize(text: String, color: AnsiColor): String =
            "${color.code}$text${RESET.code}"


        /**
         * Creates a 256-color ANSI code (foreground).
         *
         * @param colorCode The 256-color palette index (0-255).
         * @return An `AnsiColor` object for the given 256-color code.
         */
        fun color256(colorCode: Int): AnsiColor {
            require(colorCode in 0..255) { "256-color code must be in range 0-255." }
            return Custom("\u001B[38;5;${colorCode}m")
        }

        /**
         * Creates a 256-color ANSI code (background).
         *
         * @param colorCode The 256-color palette index (0-255).
         * @return An `AnsiColor` object for the given 256-color background.
         */
        fun bgColor256(colorCode: Int): AnsiColor {
            require(colorCode in 0..255) { "256-color code must be in range 0-255." }
            return Custom("\u001B[48;5;${colorCode}m")
        }

        /**
         * Creates a truecolor (24-bit RGB) ANSI code (foreground).
         *
         * @param r Red component (0-255).
         * @param g Green component (0-255).
         * @param b Blue component (0-255).
         * @return An `AnsiColor` object for the given RGB color.
         */
        @JvmStatic
        fun rgb(r: Int, g: Int, b: Int): AnsiColor {
            require(r in 0..255 && g in 0..255 && b in 0..255) { "RGB values must be in 0-255." }
            return Custom("\u001B[38;2;${r};${g};${b}m")
        }

        /**
         * Creates a truecolor (24-bit RGB) ANSI code (background).
         *
         * @param r Red component (0-255).
         * @param g Green component (0-255).
         * @param b Blue component (0-255).
         * @return An `AnsiColor` object for the given RGB background color.
         */
        @JvmStatic
        fun bgRgb(r: Int, g: Int, b: Int): AnsiColor {
            require(r in 0..255 && g in 0..255 && b in 0..255) { "RGB values must be in 0-255." }
            return Custom("\u001B[48;2;${r};${g};${b}m")
        }

        /**
         * Creates a truecolor (24-bit RGB) ANSI code from a hex color Int (e.g., 0xRRGGBB or 0xAARRGGBB).
         *
         * @param color Int color. If it’s 0xAARRGGBB, alpha will be ignored.
         * @return An `AnsiColor` object for the RGB color.
         */
        fun rgbHex(color: Int): AnsiColor {
            val r = (color shr 16) and 0xFF
            val g = (color shr 8) and 0xFF
            val b = color and 0xFF
            return rgb(r, g, b)
        }

        /**
         * Creates a truecolor (24-bit RGB) background ANSI code from a hex color Int.
         *
         * @param color Int color. If it’s 0xAARRGGBB, alpha will be ignored.
         * @return An `AnsiColor` object for the RGB background color.
         */
        fun bgRgbHex(color: Int): AnsiColor {
            val r = (color shr 16) and 0xFF
            val g = (color shr 8) and 0xFF
            val b = color and 0xFF
            return bgRgb(r, g, b)
        }
    }
}