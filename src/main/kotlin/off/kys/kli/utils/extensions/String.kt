@file:Suppress("unused")

package off.kys.kli.utils.extensions

import off.kys.kli.io.AnsiColor

/**
 * Extension function to apply a specific color to a string using ANSI escape codes.
 *
 * This function wraps the string with the given `AnsiColor` escape code, allowing text to be displayed
 * in colored format on terminals that support ANSI colors.
 *
 * @param color The [AnsiColor] object representing the color to apply.
 * @return The string wrapped with the ANSI escape codes for the specified color.
 */
fun String.color(color: AnsiColor): String = "${color.code}$this${AnsiColor.RESET.code}"

/**
 * Extension function to make a string bold using ANSI escape codes.
 *
 * This function wraps the string with the ANSI escape code for bold text, making it appear bold
 * in terminals that support ANSI styles.
 *
 * @return The string wrapped with the ANSI escape codes for bold style.
 */
fun String.bold(): String = "${AnsiColor.BOLD.code}$this${AnsiColor.RESET.code}"
