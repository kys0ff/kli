@file:Suppress("unused")

package off.kys.kli.core

import off.kys.kli.core.config.KliConfig
import off.kys.kli.io.AnsiColor
import off.kys.kli.parser.ArgParser
import off.kys.kli.utils.ArgsScope
import off.kys.kli.utils.extensions.State
import off.kys.kli.utils.extensions.color
import off.kys.kli.utils.extensions.println

/**
 * Represents a single CLI command within the KLI framework.
 *
 * A command encapsulates a named operation, such as `init` or `build`, with
 * configurable parameters like positional arguments and flags. It supports
 * execution blocks with access to parsed arguments.
 *
 * @property name The unique name of this command (used for invocation).
 * @property config CLI configuration options (e.g., color schemes).
 */
class Command(
    val name: String,
    val config: KliConfig
) {
    /** Optional help text describing what the command does. */
    var description: String = ""

    private val colors = config.colors
    private val arguments = mutableListOf<Argument>()
    private val flags = mutableListOf<Flag>()
    private var action: (ArgParser) -> Unit = {}

    /**
     * Registers a positional argument required by this command.
     *
     * @param name Name of the argument.
     * @param description Optional description shown in help output.
     */
    fun argument(name: String, description: String = "") {
        arguments.add(Argument(name, description))
    }

    /**
     * Registers a flag (e.g., `--verbose`) for this command.
     *
     * @param name Name of the flag without dashes.
     * @param description Optional description shown in help output.
     */
    fun flag(name: String, description: String = "") {
        flags.add(Flag(name, description))
    }

    /**
     * Checks whether a given name matches a known argument or flag.
     *
     * @param name The name to check (can be null).
     * @return `true` if the name is a registered argument or flag.
     */
    fun isRegisteredParamOrArg(name: String?): Boolean {
        return arguments.any { it.name == name } || flags.any { it.name == name }
    }

    /**
     * Sets the command's logic to be executed when invoked.
     *
     * @param block Code block with DSL-like access to parsed arguments.
     */
    fun action(block: ArgsScope.() -> Unit) {
        action = block
    }

    /**
     * Executes the command with parsed CLI arguments.
     *
     * @param args The parsed arguments for this run.
     */
    fun execute(args: ArgParser) {
        action(args)
    }

    /**
     * Displays help output describing this command's usage,
     * arguments, flags, and built-in options.
     */
    fun showHelp() {
        // Usage
        println("Usage: ${name.color(colors.primaryColor)} [options]", State.INFO)
        println()

        // Description
        if (description.isNotEmpty()) {
            println(description, colors.infoColor)
        }

        // Arguments
        if (arguments.isNotEmpty()) {
            println("\nArguments:", colors.warningColor)
            arguments.forEach {
                val argName = it.name.color(colors.primaryColor)
                val desc = it.description.color(colors.infoColor)
                println("  $argName  $desc")
            }
        }

        // Flags
        if (flags.isNotEmpty()) {
            println("\nFlags:", colors.warningColor)
            flags.forEach {
                val flagName = "--${it.name}".color(colors.secondaryColor)
                val desc = it.description.color(colors.infoColor)
                println("  $flagName  $desc")
            }
        }

        // Built-in Flags
        println("\nBuiltin Flags:".color(colors.warningColor))

        val helpFlag = "--help, -h".color(colors.secondaryColor)
        val helpDesc = "Show this help message and exit.".color(AnsiColor.BRIGHT_BLACK)
        println("  $helpFlag  $helpDesc")
    }

    // Internal data holders for arguments and flags
    private data class Argument(val name: String, val description: String)
    private data class Flag(val name: String, val description: String)
}
