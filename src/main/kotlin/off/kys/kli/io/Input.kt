@file:Suppress("unused")

package off.kys.kli.io

import off.kys.kli.errors.UnsupportedConsoleException
import off.kys.kli.io.model.TerminalSize
import off.kys.kli.utils.KliScope
import off.kys.kli.utils.extensions.State
import off.kys.kli.utils.extensions.color
import off.kys.kli.utils.extensions.println
import java.io.IOException

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
 * Returns null if input is unavailable or an error happens.
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
 * Returns an empty string if input is unavailable or an error happens.
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
 * @throws UnsupportedConsoleException if the console is not available (no safe fallback for password)
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
 * Asks the user to confirm an action (yes/no).
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

/**
 * Retrieves the size of the terminal by determining the number of rows and columns.
 * It makes use of platform-specific commands to extract terminal dimensions:
 * - On Windows, it uses `mode con`.
 * - On Unix-like systems, it uses `stty size`.
 * If an error occurs or the terminal size cannot be determined, it returns null.
 *
 * @return The terminal size as a [TerminalSize] object, or null if the size cannot be determined.
 */
fun KliScope.getTerminalSize(): TerminalSize? = try {
    if (isWindows()) {
        getTerminalSizeWindows()
    } else {
        getTerminalSizeUnix()
    }
} catch (_: Exception) {
    null
}

/**
 * Gets the terminal size on Unix-like systems by executing `stty size`.
 * Parses the output in the form of "rows cols".
 */
private fun getTerminalSizeUnix(): TerminalSize {
    val process = ProcessBuilder("sh", "-c", "stty size < /dev/tty")
        .redirectErrorStream(true)
        .start()
    val output = process.inputStream.bufferedReader().readText().trim()
    val (rows, cols) = output.split(" ").map { it.toInt() }
    return TerminalSize(cols, rows) // Columns come second in stty output
}

/**
 * Gets the terminal size on Windows by executing `mode con`.
 * Parses the output for "Columns" and "Lines".
 */
private fun getTerminalSizeWindows(): TerminalSize {
    val process = ProcessBuilder("cmd", "/c", "mode con")
        .redirectErrorStream(true)
        .start()
    val output = process.inputStream.bufferedReader().readLines()
    val cols = output.find { it.contains("Columns") }
        ?.split(":")?.get(1)?.trim()?.toIntOrNull() ?: 80
    val rows = output.find { it.contains("Lines") }
        ?.split(":")?.get(1)?.trim()?.toIntOrNull() ?: 24
    return TerminalSize(cols, rows)
}

/**
 * Clears the terminal screen.
 * Uses the appropriate command for the host OS.
 */
fun KliScope.clearScreen() = if (isWindows()) {
    ProcessBuilder("cmd", "/c", "cls")
        .inheritIO()
        .start()
        .waitFor()
} else {
    print("\u001b[H\u001b[2J") // ANSI escape sequence to clear and reset
    printStream?.flush()
}

/**
 * Moves the terminal cursor to the specified (x, y) position.
 * Top-left is (1, 1). ANSI escape sequence based.
 */
fun KliScope.moveCursor(x: Int, y: Int) {
    print("\u001B[${y};${x}H")
    printStream?.flush()
}

/**
 * Checks whether the terminal supports ANSI escape codes.
 * Usually true on Unix, but also on some modern Windows terminals.
 */
fun KliScope.supportsAnsi(): Boolean =
    !isWindows() || System.getenv("TERM")?.contains("xterm") == true

/**
 * Reads a single key input from the user. The behavior depends on the operating system.
 * - On non-Windows systems, it uses raw mode to read a single character from the terminal.
 * - On Windows systems, it falls back to reading a character without a raw mode.
 *
 * @return The character read from the input or null if an error occurs during the operation.
 */
fun KliScope.readSingleKey(): Char? = try {
    if (!isWindows()) {
        ProcessBuilder(
            "sh", "-c",
            "stty raw -echo </dev/tty && dd bs=1 count=1 2>/dev/null </dev/tty && stty sane </dev/tty"
        )
            .redirectErrorStream(true)
            .start()
            .inputStream.read().toChar()
    } else {
        // Basic fallback for Windows, no raw mode
        inputStream?.read()?.toChar()
    }
} catch (_: IOException) {
    null
}