@file:Suppress("unused")

package off.kys.kli.io

import off.kys.kli.errors.UnsupportedConsoleException
import off.kys.kli.utils.KliScope
import off.kys.kli.utils.extensions.State
import off.kys.kli.utils.extensions.color
import off.kys.kli.utils.extensions.println

// Internal helper: detect if running in IDE environment (IntelliJ/Eclipse)
private fun isRunningInIDE(): Boolean {
    val idea = System.getProperty("idea.active") != null
    val eclipse = System.getProperty("eclipse.startTime") != null
    return idea || eclipse
}

// Internal helper: warn once if falling back
private var warnedAboutConsole = false
private fun warnIfNoConsole() {
    if (!warnedAboutConsole && isRunningInIDE()) {
        println("[Warning] No terminal detected. Falling back to standard input.", AnsiColor.BRIGHT_YELLOW)
        warnedAboutConsole = true
    }
}

/**
 * Reads a line of input from the user.
 * Falls back to [readln] if no console is available.
 * @throws UnsupportedConsoleException if input fails
 */
fun KliScope.readInput(prompt: String, color: AnsiColor = AnsiColor.DEFAULT): String {
    print(prompt.color(color))
    print("> ")
    return try {
        console?.readLine() ?: run {
            warnIfNoConsole()
            readln()
        }
    } catch (e: Exception) {
        throw UnsupportedConsoleException(e.message ?: "Failed to read input")
    }
}

/**
 * Reads a line of input from the user.
 * Returns null if input is unavailable or error happens.
 */
fun KliScope.readInputOrNull(prompt: String, color: AnsiColor = AnsiColor.DEFAULT): String? {
    print(prompt.color(color))
    print("> ")
    return try {
        console?.readLine() ?: run {
            warnIfNoConsole()
            readlnOrNull()
        }
    } catch (_: Exception) {
        null
    }
}

/**
 * Reads a line of input from the user.
 * Returns empty string if input is unavailable or error happens.
 */
fun KliScope.readInputOrEmpty(prompt: String, color: AnsiColor = AnsiColor.DEFAULT): String {
    print(prompt.color(color))
    print("> ")
    return try {
        console?.readLine() ?: run {
            warnIfNoConsole()
            readln()
        }
    } catch (_: Exception) {
        ""
    }
}

/**
 * Reads a password input from the user, hiding characters.
 * @throws UnsupportedConsoleException if console is not available (no safe fallback for password)
 */
fun KliScope.readPassword(prompt: String): String {
    print(prompt.color(AnsiColor.BRIGHT_MAGENTA))
    print("> ")
    return try {
        console?.readPassword()?.concatToString()
            ?: throw UnsupportedConsoleException("Password input is not supported in this environment.")
    } catch (e: Exception) {
        throw UnsupportedConsoleException(e.message ?: "Failed to read password")
    }
}

/**
 * Asks user to confirm an action (yes/no).
 * Accepts "y" or "yes" as positive responses.
 */
fun KliScope.confirm(prompt: String): Boolean {
    val input = readInput("$prompt [y/N]").lowercase()
    return input == "y" || input == "yes"
}

/**
 * Presents a selection menu and asks user to choose one item.
 * Keeps retrying until a valid selection is made.
 */
fun <T> KliScope.select(
    options: List<T>,
    displayConverter: (T) -> String = { it.toString() },
): T {
    println("Select an option:".color(AnsiColor.BRIGHT_CYAN))
    options.forEachIndexed { index, item ->
        println("${(index + 1).toString().color(AnsiColor.BRIGHT_BLUE)}) ${displayConverter(item)}")
    }

    while (true) {
        val input = readInput("Enter choice (1-${options.size})").toIntOrNull()
        if (input != null && input in 1..options.size) {
            return options[input - 1]
        }
        println("Invalid selection", State.ERROR)
    }
}

/**
 * Presents a titled menu and asks user to choose one item.
 * Display options with title, retry on invalid input.
 */
fun <T> KliScope.selectMenu(
    title: String,
    options: List<T>,
    displayConverter: (T) -> String = { it.toString() },
): T {
    println("\n$title", AnsiColor.BRIGHT_BLUE)
    options.forEachIndexed { index, item ->
        println("${(index + 1).toString().color(AnsiColor.BRIGHT_YELLOW)}) ${displayConverter(item)}")
    }

    while (true) {
        val input = readInput("Enter choice (1-${options.size})").toIntOrNull()
        if (input != null && input in 1..options.size)
            return options[input - 1]

        println("Invalid selection", State.ERROR)
    }
}