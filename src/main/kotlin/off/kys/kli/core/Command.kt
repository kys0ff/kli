@file:Suppress("MemberVisibilityCanBePrivate")

package off.kys.kli.core

import off.kys.kli.io.AnsiColor
import off.kys.kli.parser.ArgParser
import off.kys.kli.utils.Args
import off.kys.kli.utils.extensions.println

class Command(
    val name: String,
) {
    private var action: (ArgParser) -> Unit = {}

    var description: String = ""

    /**
     * Defines the action to run when this command is executed.
     */
    fun action(block: (Args) -> Unit) {
        action = block
    }

    /**
     * Executes the assigned action with the given parsed arguments.
     */
    fun execute(args: ArgParser) = action(args)

    /**
     * Displays help information for this command.
     */
    fun showHelp() {
        println("Command: $name", AnsiColor.BRIGHT_WHITE)
        if (description.isNotBlank()) {
            println("Description: $description", AnsiColor.BRIGHT_BLACK)
        } else {
            println("No description available.", AnsiColor.BRIGHT_BLACK)
        }
        println()
        println("Usage:", AnsiColor.BRIGHT_CYAN)
        println("  $name [options]")
        println()
        println("Options:", AnsiColor.BRIGHT_CYAN)
        println("  --help, -h       Show command help")
    }
}