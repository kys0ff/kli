@file:Suppress("unused")

package off.kys.kli.utils.extensions

import off.kys.kli.io.AnsiColor

/**
 * Extension function for the [State] enum that maps a given state to its corresponding [AnsiColor].
 *
 * This function converts the [State] into an appropriate color for console output using ANSI escape codes.
 * It returns the following color mappings:
 * - [State.SUCCESS]: [AnsiColor.BRIGHT_GREEN]
 * - [State.ERROR]: [AnsiColor.RED]
 * - [State.WARNING]: [AnsiColor.BRIGHT_YELLOW]
 * - [State.INFO]: [AnsiColor.BRIGHT_CYAN]
 *
 * @return The [AnsiColor] corresponding to the state.
 */
private fun State.getColorForState(): AnsiColor = when (this) {
    State.SUCCESS -> AnsiColor.BRIGHT_GREEN
    State.ERROR -> AnsiColor.RED
    State.WARNING -> AnsiColor.BRIGHT_YELLOW
    State.INFO -> AnsiColor.BRIGHT_CYAN
}

/**
 * Prints the given [message] to the console, with the specified [color] applied.
 * This function appends a newline after the message.
 *
 * @param message The message to be printed to the console.
 * @param color The [AnsiColor] that specifies the color to apply to the message.
 */
fun println(message: String, color: AnsiColor): Unit = println(message.color(color))

/**
 * Prints the given [message] (of any type) to the console, with the specified [color] applied.
 * This function appends a newline after the message.
 *
 * @param message The message (of any type) to be printed to the console.
 * @param color The [AnsiColor] that specifies the color to apply to the message.
 */
fun println(message: Any?, color: AnsiColor): Unit = println(message.toString().color(color))

/**
 * Prints the given [message] to the console, with the specified [color] applied.
 * This function does **not** append a newline after the message.
 *
 * @param message The message to be printed to the console.
 * @param color The [AnsiColor] that specifies the color to apply to the message.
 */
fun print(message: String, color: AnsiColor): Unit = print(message.color(color))

/**
 * Prints the given [message] (of any type) to the console, with the specified [color] applied.
 * This function does **not** append a newline after the message.
 *
 * @param message The message (of any type) to be printed to the console.
 * @param color The [AnsiColor] that specifies the color to apply to the message.
 */
fun print(message: Any?, color: AnsiColor): Unit = print(message.toString().color(color))

/**
 * Prints the given [message] to the console, with the color corresponding to the specified [state].
 * The message will be printed with a newline.
 *
 * @param message The message to be printed to the console.
 * @param state The state that determines the color to apply to the message.
 */
fun println(message: String, state: State) = println(message, state.getColorForState())

/**
 * Prints the given [message] (of any type) to the console, with the color corresponding to the specified [state].
 * The message will be printed with a newline.
 *
 * @param message The message (of any type) to be printed to the console.
 * @param state The state that determines the color to apply to the message.
 */
fun println(message: Any?, state: State) = println(message, state.getColorForState())

/**
 * Prints the given [message] to the console, with the color corresponding to the specified [state].
 * The message will be printed without a newline.
 *
 * @param message The message to be printed to the console.
 * @param state The state that determines the color to apply to the message.
 */
fun print(message: String, state: State) = print(message, state.getColorForState())

/**
 * Prints the given [message] (of any type) to the console, with the color corresponding to the specified [state].
 * The message will be printed without a newline.
 *
 * @param message The message (of any type) to be printed to the console.
 * @param state The state that determines the color to apply to the message.
 */
fun print(message: Any?, state: State) = print(message, state.getColorForState())