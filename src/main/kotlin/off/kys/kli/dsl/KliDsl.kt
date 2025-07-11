@file:Suppress("MemberVisibilityCanBePrivate")

package off.kys.kli.dsl

import off.kys.kli.core.Command
import off.kys.kli.core.config.KliConfig
import off.kys.kli.errors.CommandAlreadyExistsException
import off.kys.kli.io.AnsiColor
import off.kys.kli.io.readInput
import off.kys.kli.parser.ArgParser
import off.kys.kli.utils.extensions.State
import off.kys.kli.utils.extensions.color
import off.kys.kli.utils.extensions.println
import java.io.Console
import java.io.InputStream
import java.io.PrintStream

/**
 * A Domain-Specific Language (DSL) class for building and managing an off.kys.kli.core.Command Line Interface (CLI) tool.
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

    val inputStream: InputStream?
        get() = System.`in`

    val printStream: PrintStream?
        get() = System.out

    /**
     * A lambda for globally customizing crash handling.
     * This lambda is invoked whenever an exception is thrown during command registration or execution.
     *
     * @param onCrash A function that receives the thrown exception.
     */
    var onCrash: ((Throwable) -> Unit)? = null

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
     * @param config The configuration block to modify the `off.kys.kli.core.Command` instance.
     */
    fun command(name: String, config: Command.() -> Unit) {
        // Check for duplicate command name.
        if (commands.any { it.name == name })
            throw CommandAlreadyExistsException(name)

        commands.add(Command(name, this.config).apply(config))
    }

    /**
     * Executes the command-line interface logic based on the provided arguments.
     *
     * If the arguments array is empty, the tool enters interactive mode. Otherwise, the provided arguments
     * are processed to execute commands or flags.
     *
     * @param args The array of arguments passed to the CLI.
     */
    fun execute(args: Array<String>) {
        try {
            if (args.isEmpty() && config.interactiveMode.enabled) startInteractiveMode() else processArguments(args)
        } catch (e: Exception) {
            handleCrash(e)
        }
    }

    fun isWindows(): Boolean = System.getProperty("os.name").lowercase().contains("win")

    // Processes the arguments passed to the CLI, either executing a command or displaying help.
    private fun processArguments(args: Array<String>) {
        val cliArgs = ArgParser.from(args)
        val cmdName = cliArgs.getParams().firstOrNull()

        // If first argument is "help" (or --help / -h)
        when (args.firstOrNull()?.lowercase()) {
            "help", "--help", "-h" -> {
                showHelp()
                return
            }
        }

        val cmd = commands.find { it.name == cmdName }

        when {
            cliArgs.getFlag("version") -> showVersion() // Show version if the flag is present.
            cmd != null -> handleCommandExecution(cliArgs) // Execute command if it exists.
            cliArgs.isEmpty() -> showHelp()
            else -> {
                println("Invalid usage", State.ERROR) // Show error message for invalid arguments.
                if (config.showUsageOnError) { // Optionally show usage on error.
                    println()
                    showHelp()
                }
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
            println(
                "off.kys.kli.core.Command not found: '${cmdName ?: ""}'",
                State.ERROR
            ) // off.kys.kli.core.Command not found error.
            if (config.showUsageOnError) showHelp() // Optionally show usage on error.
        }
    }

    // Starts the interactive mode of the CLI tool.
    private fun startInteractiveMode() {
        if (config.interactiveMode.greet?.show == true)
            println(config.interactiveMode.greet!!.message, config.interactiveMode.greet!!.color)
        println("Type 'help' for available commands, 'exit' to quit", config.colors.primaryColor)

        // Main loop for reading user input in interactive mode.
        while (true) {
            val input = if (config.showPrompt) {
                readInput(config.name, config.colors.primaryColor).trim() // Prompt the user if enabled.
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

        if (config.interactiveMode.goodBye?.show == true)
            println(
                config.interactiveMode.goodBye!!.message,
                config.interactiveMode.goodBye!!.color
            ) // Show goodbye message when exiting interactive mode.
    }

    /**
     * Processes input provided in interactive mode by splitting it into arguments and passing it to the command processor.
     *
     * @param input The input string from the user.
     */
    private fun processInteractiveInput(input: String) {
        try {
            val args = input.split("\\s+".toRegex()).toTypedArray()
            processArguments(args)
        } catch (e: Exception) {
            handleCrash(e)
        }
    }

    /**
     * Handles any exception thrown during execution by calling the global `onCrash` lambda if defined.
     * Otherwise, it prints the error message and stack trace.
     *
     * @param e The thrown exception.
     */
    private fun handleCrash(e: Throwable) {
        onCrash?.invoke(e) ?: run {
            println("Unhandled error: ${e.message}", State.ERROR)
            e.printStackTrace() // Optional: for debugging
        }
    }

    // Shows the help information for available commands in interactive mode.
    private fun showInteractiveHelp() {
        println("Available commands:", AnsiColor.BRIGHT_CYAN)
        commands.forEach { cmd ->
            println("  ${cmd.name.padEnd(15)} ${cmd.description.color(config.colors.infoColor)}") // List commands with descriptions.
        }
        println()
        println("You can also:")
        println("  - Start commands with parameters directly")
        println("  - Use flags like --help or --version")
    }

    // Displays the version of the CLI tool.
    private fun showVersion() {
        println("${config.name} version ${config.version}", config.colors.secondaryColor)
    }

    // Displays the general help message for the CLI tool.
    // Displays the general help message for the CLI tool.
    private fun showHelp() {
        // Header
        println("${config.name.color(config.colors.primaryColor)} - ${config.description.color(config.colors.infoColor)}")
        println()

        // Usage
        println("Usage:".color(config.colors.warningColor))
        println("  <command> [arguments] [options]".color(config.colors.debugColor))
        println()

        // Available Commands
        println("Commands:".color(config.colors.warningColor))
        commands.forEach { cmd ->
            println(
                "  ${
                    cmd.name.color(AnsiColor.BRIGHT_MAGENTA).padEnd(15)
                } ${cmd.description.color(AnsiColor.BRIGHT_BLACK)}"
            )
        }
        println()

        // Flags
        println("Global Options:".color(config.colors.warningColor))
        println("  --help, -h        ".color(AnsiColor.BRIGHT_MAGENTA) + "Show this help message")
        println("  --version         ".color(AnsiColor.BRIGHT_MAGENTA) + "Show version info")
        println()

        // Tips
        println("Tips:".color(config.colors.infoColor))
        println("  Type '${"help".color(AnsiColor.BRIGHT_YELLOW)}' in interactive mode to list commands.")
        println("  Use '${"--help".color(AnsiColor.BRIGHT_YELLOW)}' after any command to get command-specific help.")
        println()
    }
}
