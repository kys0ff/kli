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
fun String.color(color: AnsiColor): String = "${color()}$this${AnsiColor.RESET()}"

/**
 * Make the string bold.
 */
fun String.bold(): String = "${AnsiColor.BOLD()}$this${AnsiColor.RESET()}"

/**
 * Make the string faint (light).
 */
fun String.faint(): String = "${AnsiColor.FAINT()}$this${AnsiColor.RESET()}"

/**
 * Make the string italic.
 */
fun String.italic(): String = "${AnsiColor.ITALIC()}$this${AnsiColor.RESET()}"

/**
 * Underline the string.
 */
fun String.underline(): String = "${AnsiColor.UNDERLINE()}$this${AnsiColor.RESET()}"

/**
 * Apply slow blink style to the string.
 */
fun String.slowBlink(): String = "${AnsiColor.SLOW_BLINK()}$this${AnsiColor.RESET()}"

/**
 * Apply rapid blink style to the string.
 */
fun String.rapidBlink(): String = "${AnsiColor.RAPID_BLINK()}$this${AnsiColor.RESET()}"

/**
 * Reverse the colors of the string.
 */
fun String.reversed(): String = "${AnsiColor.REVERSED()}$this${AnsiColor.RESET()}"

/**
 * Conceal the string (make it hidden).
 */
fun String.conceal(): String = "${AnsiColor.CONCEAL()}$this${AnsiColor.RESET()}"

/**
 * Cross out (strike through) the string.
 */
fun String.crossedOut(): String = "${AnsiColor.CROSSED_OUT()}$this${AnsiColor.RESET()}"
