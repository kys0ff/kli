@file:Suppress("unused")

package off.kys.kli.io

import off.kys.kli.utils.extensions.State
import off.kys.kli.utils.extensions.color
import off.kys.kli.utils.extensions.println
import java.io.Console

object InputReader {
    private val console: Console? = System.console()

    @JvmStatic
    fun readInput(prompt: String, color: AnsiColor = AnsiColor.DEFAULT): String {
        print(prompt.color(color))
        print("> ")
        return readlnOrNull()!!
    }

    @JvmStatic
    fun readInputOrNull(prompt: String, color: AnsiColor = AnsiColor.DEFAULT): String? {
        print(prompt.color(color))
        print("> ")
        return readlnOrNull()
    }

    @JvmStatic
    fun readInputOrEmpty(prompt: String, color: AnsiColor = AnsiColor.DEFAULT): String {
        print(prompt.color(color))
        print("> ")
        return readlnOrNull() ?: ""
    }

    fun readPassword(prompt: String): String =
        console?.readPassword("${prompt.color(AnsiColor.BRIGHT_MAGENTA)} ")?.joinToString("")
            ?: throw IllegalStateException("Console not available")

    fun confirm(prompt: String): Boolean {
        val input = readInput("$prompt [y/N]").lowercase()
        return input == "y" || input == "yes"
    }

    fun <T> select(options: List<T>, displayConverter: (T) -> String = { it.toString() }): T {
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

    fun <T> selectMenu(
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
}
