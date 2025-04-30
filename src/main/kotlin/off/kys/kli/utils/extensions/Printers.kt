@file:Suppress("unused")

package off.kys.kli.utils.extensions

import off.kys.kli.io.AnsiColor

/**
 * Prints the given message to the console with the specified color.
 * This function prints the message with a newline.
 *
 * @param message The message to be printed.
 * @param color The color to apply to the message. It uses [AnsiColor] for console color codes.
 */
fun println(message: String, color: AnsiColor): Unit = println(message.color(color))

/**
 * Prints the given message to the console with the specified color.
 * This function prints the message without a newline.
 *
 * @param message The message to be printed.
 * @param color The color to apply to the message. It uses [AnsiColor] for console color codes.
 */
fun print(message: String, color: AnsiColor): Unit = print(message.color(color))

/**
 * Prints the given message to the console with a color corresponding to the specified state.
 * The default state is [State.SUCCESS], which applies a green color.
 *
 * The state can be one of the following:
 * - [State.SUCCESS]: Green color (bright green).
 * - [State.ERROR]: Red color (bright red).
 * - [State.WARNING]: Yellow color (bright yellow).
 * - [State.INFO]: Cyan color (bright cyan).
 *
 * This function prints the message with a newline.
 *
 * @param message The message to be printed.
 * @param state The state that determines the color of the message. The default is [State.SUCCESS].
 */
fun println(message: String, state: State) = println(
    message, when (state) {
        State.SUCCESS -> AnsiColor.BRIGHT_GREEN
        State.ERROR -> AnsiColor.RED
        State.WARNING -> AnsiColor.BRIGHT_YELLOW
        State.INFO -> AnsiColor.BRIGHT_CYAN
    }
)

/**
 * Prints the given message to the console with a color corresponding to the specified state.
 * The default state is [State.SUCCESS], which applies a green color.
 *
 * The state can be one of the following:
 * - [State.SUCCESS]: Green color (bright green).
 * - [State.ERROR]: Red color (bright red).
 * - [State.WARNING]: Yellow color (bright yellow).
 * - [State.INFO]: Cyan color (bright cyan).
 *
 * This function prints the message without a newline.
 *
 * @param message The message to be printed.
 * @param state The state that determines the color of the message. The default is [State.SUCCESS].
 */
fun print(message: String, state: State) = print(
    message, when (state) {
        State.SUCCESS -> AnsiColor.BRIGHT_GREEN
        State.ERROR -> AnsiColor.RED
        State.WARNING -> AnsiColor.BRIGHT_YELLOW
        State.INFO -> AnsiColor.BRIGHT_CYAN
    }
)