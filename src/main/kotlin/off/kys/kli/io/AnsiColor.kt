@file:Suppress("unused", "ClassName")

package off.kys.kli.io

sealed class AnsiColor(open val code: String) {
    // Foreground Colors
    data object BLACK : AnsiColor("\u001B[30m")
    data object RED : AnsiColor("\u001B[31m")
    data object GREEN : AnsiColor("\u001B[32m")
    data object YELLOW : AnsiColor("\u001B[33m")
    data object BLUE : AnsiColor("\u001B[34m")
    data object MAGENTA : AnsiColor("\u001B[35m")
    data object CYAN : AnsiColor("\u001B[36m")
    data object WHITE : AnsiColor("\u001B[37m")

    // Default (System Terminal Text Color)
    data object DEFAULT : AnsiColor("\u001B[39m")

    // Bright Foreground Colors
    data object BRIGHT_BLACK : AnsiColor("\u001B[90m")
    data object BRIGHT_RED : AnsiColor("\u001B[91m")
    data object BRIGHT_GREEN : AnsiColor("\u001B[92m")
    data object BRIGHT_YELLOW : AnsiColor("\u001B[93m")
    data object BRIGHT_BLUE : AnsiColor("\u001B[94m")
    data object BRIGHT_MAGENTA : AnsiColor("\u001B[95m")
    data object BRIGHT_CYAN : AnsiColor("\u001B[96m")
    data object BRIGHT_WHITE : AnsiColor("\u001B[97m")

    // Background Colors
    data object BG_BLACK : AnsiColor("\u001B[40m")
    data object BG_RED : AnsiColor("\u001B[41m")
    data object BG_GREEN : AnsiColor("\u001B[42m")
    data object BG_YELLOW : AnsiColor("\u001B[43m")
    data object BG_BLUE : AnsiColor("\u001B[44m")
    data object BG_MAGENTA : AnsiColor("\u001B[45m")
    data object BG_CYAN : AnsiColor("\u001B[46m")
    data object BG_WHITE : AnsiColor("\u001B[47m")

    // Bright Background Colors
    data object BG_BRIGHT_BLACK : AnsiColor("\u001B[100m")
    data object BG_BRIGHT_RED : AnsiColor("\u001B[101m")
    data object BG_BRIGHT_GREEN : AnsiColor("\u001B[102m")
    data object BG_BRIGHT_YELLOW : AnsiColor("\u001B[103m")
    data object BG_BRIGHT_BLUE : AnsiColor("\u001B[104m")
    data object BG_BRIGHT_MAGENTA : AnsiColor("\u001B[105m")
    data object BG_BRIGHT_CYAN : AnsiColor("\u001B[106m")
    data object BG_BRIGHT_WHITE : AnsiColor("\u001B[107m")

    // Styles
    data object RESET : AnsiColor("\u001B[0m")
    data object BOLD : AnsiColor("\u001B[1m")
    data object FAINT : AnsiColor("\u001B[2m")
    data object ITALIC : AnsiColor("\u001B[3m")
    data object UNDERLINE : AnsiColor("\u001B[4m")
    data object SLOW_BLINK : AnsiColor("\u001B[5m")
    data object RAPID_BLINK : AnsiColor("\u001B[6m")
    data object REVERSED : AnsiColor("\u001B[7m")
    data object CONCEAL : AnsiColor("\u001B[8m")
    data object CROSSED_OUT : AnsiColor("\u001B[9m")

    /** Create a custom color easily */
    data class Custom(override val code: String) : AnsiColor(code)

    companion object {
        /** Shorthand for creating custom ANSI codes */
        fun custom(code: String): AnsiColor = Custom(code)

        /** Helper to wrap a text with color */
        fun colorize(text: String, color: AnsiColor): String =
            "${color.code}$text${RESET.code}"
    }
}