@file:Suppress("MemberVisibilityCanBePrivate")

package off.kys.kli.core

import off.kys.kli.io.AnsiColor
import off.kys.kli.parser.ArgParser
import off.kys.kli.utils.Args
import off.kys.kli.utils.extensions.println

/**
 * Represents a command within the KLI (Command Line Interface).
 *
 * A `Command` consists of a name, an optional description, and an action to execute
 * when the command is triggered. The command can be executed with arguments, and it
 * provides a method to display help information to guide users on its usage.
 *
 * **Properties**:
 * - `name`: The name of the command (e.g., `help`, `version`).
 * - `description`: A description of what the command does. By default, it's an empty string.
 * - `action`: A function that defines what happens when the command is executed. It takes parsed arguments as input.
 *
 * **Methods**:
 * - `action`: Defines the action to be performed when this command is executed. The block passed to it receives the parsed arguments.
 * - `execute`: Executes the command's action with the provided parsed arguments.
 * - `showHelp`: Displays the help information for the command, including its name, description, usage, and available options.
 */
class Command(
    val name: String, // Name of the command
) {
    private var action: (ArgParser) -> Unit = {} // The action to be executed when this command is called

    var description: String = "" // Description of what this command does

    /**
     * Defines the action to run when this command is executed.
     *
     * @param block The block of code that specifies what should happen when this command is triggered.
     */
    fun action(block: (Args) -> Unit) {
        action = block // Assign the block as the action for this command
    }

    /**
     * Executes the assigned action with the given parsed arguments.
     *
     * @param args The arguments that were parsed for this command.
     */
    fun execute(args: ArgParser) = action(args) // Run the action with the arguments

    /**
     * Displays help information for this command.
     *
     * This will show the command's name, description, and usage details including available options.
     */
    fun showHelp() {
        println("Command: $name", AnsiColor.BRIGHT_WHITE) // Print command name in white
        if (description.isNotBlank()) {
            println("Description: $description", AnsiColor.BRIGHT_BLACK) // Print description if available
        } else {
            println("No description available.", AnsiColor.BRIGHT_BLACK) // Default message when no description is set
        }
        println()
        println("Usage:", AnsiColor.BRIGHT_CYAN) // Usage info in cyan color
        println("  $name [options]") // Command usage format
        println()
        println("Options:", AnsiColor.BRIGHT_CYAN) // Available options header
        println("  --help, -h       Show command help") // Help flag option
    }
}
