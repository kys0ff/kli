@file:Suppress("MemberVisibilityCanBePrivate")

package off.kys.kli.dsl

import off.kys.kli.core.Command
import off.kys.kli.core.KliConfig
import off.kys.kli.io.AnsiColor
import off.kys.kli.io.readInput
import off.kys.kli.parser.ArgParser
import off.kys.kli.utils.extensions.State
import off.kys.kli.utils.extensions.color
import off.kys.kli.utils.extensions.println
import java.io.Console

/**
 * A Domain-Specific Language (DSL) class for building and managing a Command Line Interface (CLI) tool.
 *
 * This class allows configuring commands, handling arguments, and switching between interactive and argument-based modes.
 *
 * @param config The configuration for the CLI tool. The default configuration is provided via `KliConfig`.
 */
class KliDsl(private val config: KliConfig = KliConfig()) {

    // List of registered commands.
    private val commands = mutableListOf<Command>()

    // A reference to the system's console (if available).
    val console: Console?
        get() = System.console()

    /**
     * Configures the CLI tool using the provided block of code.
     *
     * The block is applied to the current `KliConfig` instance to modify the tool's behavior.
     *
     * @param block The configuration block that modifies the `KliConfig` instance.
     */
    fun configure(block: KliConfig.() -> Unit) = config.apply(block)

    /**
     * Registers a new command with the specified name and configuration.
     *
     * Commands are added to the list of available commands for the CLI tool.
     *
     * @param name The name of the command.
     * @param config The configuration block to modify the `Command` instance.
     */
    fun command(name: String, config: Command.() -> Unit) = commands.add(Command(name).apply(config))

    /**
     * Executes the command-line interface logic based on the provided arguments.
     *
     * If the arguments array is empty, the tool enters interactive mode. Otherwise, the provided arguments
     * are processed to execute commands or flags.
     *
     * @param args The array of arguments passed to the CLI.
     */
    fun execute(args: Array<String>) = if (args.isEmpty()) startInteractiveMode() else processArguments(args)


    // Processes the arguments passed to the CLI, either executing a command or displaying help.
    private fun processArguments(args: Array<String>) {
        val cliArgs = ArgParser.from(args)
        val cmdName = cliArgs.getParams().firstOrNull()
        val cmd = commands.find { it.name == cmdName }

        when {
            cliArgs.getFlag("version") -> showVersion() // Show version if the flag is present.
            cmd != null -> handleCommandExecution(cliArgs) // Execute command if it exists.
            cliArgs.getFlag("help") -> showHelp() // Show help if requested.
            else -> {
                println("Invalid usage", State.ERROR) // Show error message for invalid arguments.
                if (config.showUsageOnError) showHelp() // Optionally show usage on error.
            }
        }
    }

    // Executes the appropriate action for a command based on parsed arguments.
    private fun handleCommandExecution(args: ArgParser) {
        val cmdName = args.getParams().firstOrNull()
        val cmd = commands.find { it.name == cmdName }

        if (cmd != null) {
            if (args.getFlag("help") || args.getFlag("h")) {
                cmd.showHelp() // Show command-specific help.
            } else {
                cmd.execute(args) // Execute the command.
            }
        } else {
            println("Command not found: '${cmdName ?: ""}'", State.ERROR) // Command not found error.
            if (config.showUsageOnError) showHelp() // Optionally show usage on error.
        }
    }

    // Starts the interactive mode of the CLI tool.
    private fun startInteractiveMode() {
        if (config.showInteractiveModeMessage) {
            println("Welcome to ${config.name} interactive mode!", config.greetingColor)
        }
        println("Type 'help' for available commands, 'exit' to quit", AnsiColor.BRIGHT_CYAN)

        // Main loop for reading user input in interactive mode.
        while (true) {
            val input = if (config.showPrompt) {
                readInput(config.name, config.promptColor).trim() // Prompt the user if enabled.
            } else {
                readInput("").trim() // Read input without a prompt if disabled.
            }

            when {
                input.equals("exit", ignoreCase = true) -> break // Exit if user types 'exit'.
                input.equals("help", ignoreCase = true) -> showInteractiveHelp() // Show help if user types 'help'.
                input.isEmpty() -> continue // Skip empty input.
                else -> processInteractiveInput(input) // Process other commands.
            }
        }

        if (config.showGoodbyeMessage) {
            println("Goodbye!", config.goodbyeColor) // Show goodbye message when exiting interactive mode.
        }
    }

    // Processes input provided in interactive mode and passes it as arguments to be processed.
    private fun processInteractiveInput(input: String) {
        try {
            val args = input.split("\\s+".toRegex()).toTypedArray() // Split input into arguments.
            processArguments(args) // Process the arguments as if they were passed via CLI.
        } catch (e: Exception) {
            println("Error executing command: ${e.message}", State.ERROR) // Error handling for invalid commands.
        }
    }

    // Shows the help information for available commands in interactive mode.
    private fun showInteractiveHelp() {
        println("Available commands:", AnsiColor.BRIGHT_CYAN)
        commands.forEach { cmd ->
            println("  ${cmd.name.padEnd(15)} ${cmd.description.color(AnsiColor.BRIGHT_BLACK)}") // List commands with descriptions.
        }
        println()
        println("You can also:")
        println("  - Start commands with parameters directly")
        println("  - Use flags like --help or --version")
    }

    // Displays the version of the CLI tool.
    private fun showVersion() {
        println("${config.name} version ${config.version}", config.greetingColor)
    }

    // Displays the general help message for the CLI tool.
    private fun showHelp() {
        println("${config.name} - ${config.description}", AnsiColor.BRIGHT_WHITE)
        println("Usage:", AnsiColor.BRIGHT_WHITE)
        println("  <command> [arguments] [options]")
        println()
        showInteractiveHelp() // Show interactive help for CLI commands.
    }
}