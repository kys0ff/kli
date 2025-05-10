package off.kys.kli.core

import off.kys.kli.io.AnsiColor
import off.kys.kli.parser.ArgParser
import off.kys.kli.utils.Args
import off.kys.kli.utils.ArgsScope
import off.kys.kli.utils.extensions.State
import off.kys.kli.utils.extensions.color
import off.kys.kli.utils.extensions.println

/**
 * Represents a CLI command in the KLI framework.
 *
 * Each command has:
 * - a name (e.g., "init", "build")
 * - optional description
 * - configurable arguments and flags
 * - an executable action block
 */
class Command(val name: String) {
    /** Description of the command shown in help text */
    var description: String = ""

    /** List of registered positional arguments for the command */
    private val arguments = mutableListOf<Argument>()

    /** List of registered flags (options starting with --) */
    private val flags = mutableListOf<Flag>()

    /** The main logic to execute when this command is run */
    private var action: (ArgParser) -> Unit = {}

    /**
     * Adds a positional argument to this command.
     *
     * @param name The name of the argument (e.g., "filename")
     * @param description Optional description for help display
     */
    fun argument(name: String, description: String = "") = arguments.add(Argument(name, description))

    /**
     * Adds a flag (option) to this command.
     *
     * @param name The name of the flag (without "--")
     * @param description Optional description for help display
     */
    fun flag(name: String, description: String = "") = flags.add(Flag(name, description))

    /**
     * Checks if the provided name matches a registered argument or flag.
     *
     * @param name The name to check (without -- for flags)
     * @return True if it's a registered argument or flag, false otherwise
     */
    fun isRegisteredParamOrArg(name: String?): Boolean =
        arguments.any { it.name == name } || flags.any { it.name == name }

    /**
     * Sets the action block to execute when this command runs.
     *
     * @param block The logic, receiving parsed args.
     *
     * @deprecated This method is deprecated in favor of the new `action` method that uses `ArgsScope`.
     * Use the new method for better scope handling and flexibility.
     * This method will be removed in a future version.
     *
     * Expect that your app will be buggy and may crash at any point when using this deprecated method.
     */
    @Deprecated(
        message = "Use the new action method with ArgsScope for improved flexibility. This method will be removed soon. Expect that your app will be buggy and may crash at any point when using this deprecated method.",
        replaceWith = ReplaceWith(expression = "action(block: ArgsScope.() -> Unit)"),
        level = DeprecationLevel.WARNING
    )
    fun action(deprecated:Any? = null, block: (Args) -> Unit) {
        action = block
    }

    /**
     * Sets the action block to execute when this command runs.
     *
     * @param block The logic, receiving parsed args within the ArgsScope.
     */
    fun action(block: ArgsScope.() -> Unit) {
        action = block
    }

    /**
     * Executes the command with provided parsed arguments.
     *
     * @param args The parsed arguments and flags
     */
    fun execute(args: ArgParser) = action(args)

    /**
     * Prints formatted help text for this command, showing usage,
     * arguments, flags, and built-in flags like --help and --version.
     */
    fun showHelp() {
        // Usage line
        println("Usage: ${name.color(AnsiColor.BRIGHT_CYAN)} [options]", State.INFO)
        println()

        // Description block
        if (description.isNotEmpty()) {
            println(description, AnsiColor.BRIGHT_WHITE)
        }

        // Arguments block
        if (arguments.isNotEmpty()) {
            println("\nArguments:", AnsiColor.BRIGHT_YELLOW)
            arguments.forEach {
                val argName = it.name.color(AnsiColor.BRIGHT_CYAN)
                val desc = it.description.color(AnsiColor.BRIGHT_BLACK)
                println("  $argName  $desc")
            }
        }

        // Flags block
        if (flags.isNotEmpty()) {
            println("\nFlags:", AnsiColor.BRIGHT_YELLOW)
            flags.forEach {
                val flagName = "--${it.name}".color(AnsiColor.BRIGHT_GREEN)
                val desc = it.description.color(AnsiColor.BRIGHT_BLACK)
                println("  $flagName  $desc")
            }
        }

        // Built-in flags (always present)
        println("\nBuiltin Flags:".color(AnsiColor.BRIGHT_YELLOW))

        val helpFlag = "--help".color(AnsiColor.BRIGHT_GREEN)
        val helpDesc = "Show this help message and exit.".color(AnsiColor.BRIGHT_BLACK)
        println("  $helpFlag  $helpDesc")

        val versionFlag = "--version".color(AnsiColor.BRIGHT_GREEN)
        val versionDesc = "Show version information and exit.".color(AnsiColor.BRIGHT_BLACK)
        println("  $versionFlag  $versionDesc")
    }

    /** Represents a positional argument */
    private data class Argument(val name: String, val description: String)

    /** Represents a flag/option (e.g., --verbose) */
    private data class Flag(val name: String, val description: String)
}