@file:Suppress("MemberVisibilityCanBePrivate")

package off.kys.kli.dsl

import off.kys.kli.core.Command
import off.kys.kli.core.KliConfig
import off.kys.kli.io.AnsiColor
import off.kys.kli.io.InputReader
import off.kys.kli.parser.ArgParser
import off.kys.kli.utils.extensions.State
import off.kys.kli.utils.extensions.color
import off.kys.kli.utils.extensions.println

class KliDsl(private val config: KliConfig = KliConfig()) {
    private val commands = mutableListOf<Command>()

    fun configure(block: KliConfig.() -> Unit) = config.apply(block)

    fun command(name: String, config: Command.() -> Unit) = commands.add(Command(name).apply(config))

    fun execute(args: Array<String>) = if (args.isEmpty()) startInteractiveMode() else processArguments(args)


    private fun processArguments(args: Array<String>) {
        val cliArgs = ArgParser.from(args)
        val cmdName = cliArgs.getParams().firstOrNull()
        val cmd = commands.find { it.name == cmdName }

        when {
            cliArgs.getFlag("version") -> showVersion()
            cmd != null -> handleCommandExecution(cliArgs)
            cliArgs.getFlag("help") -> showHelp()
            else -> {
                println("Invalid usage", State.ERROR)
                if (config.showUsageOnError) showHelp()
            }
        }
    }

    private fun handleCommandExecution(args: ArgParser) {
        val cmdName = args.getParams().firstOrNull()
        val cmd = commands.find { it.name == cmdName }

        if (cmd != null) {
            if (args.getFlag("help") || args.getFlag("h")) {
                cmd.showHelp()
            } else {
                cmd.execute(args)
            }
        } else {
            println("Command not found: '${cmdName ?: ""}'", State.ERROR)
            if (config.showUsageOnError) showHelp()
        }
    }

    private fun startInteractiveMode() {
        if (config.showInteractiveModeMessage) {
            println("Welcome to ${config.name} interactive mode!", config.greetingColor)
        }
        println("Type 'help' for available commands, 'exit' to quit", AnsiColor.BRIGHT_CYAN)

        while (true) {
            val input = if (config.showPrompt) {
                InputReader.readInput(config.name, config.promptColor).trim()
            } else {
                InputReader.readInput("").trim()
            }

            when {
                input.equals("exit", ignoreCase = true) -> break
                input.equals("help", ignoreCase = true) -> showInteractiveHelp()
                input.isEmpty() -> continue
                else -> processInteractiveInput(input)
            }
        }

        if (config.showGoodbyeMessage) {
            println("Goodbye!", config.goodbyeColor)
        }
    }

    private fun processInteractiveInput(input: String) {
        try {
            val args = input.split("\\s+".toRegex()).toTypedArray()
            processArguments(args)
        } catch (e: Exception) {
            println("Error executing command: ${e.message}", State.ERROR)
        }
    }

    private fun showInteractiveHelp() {
        println("Available commands:", AnsiColor.BRIGHT_CYAN)
        commands.forEach { cmd ->
            println("  ${cmd.name.padEnd(15)} ${cmd.description.color(AnsiColor.BRIGHT_BLACK)}")
        }
        println()
        println("You can also:")
        println("  - Start commands with parameters directly")
        println("  - Use flags like --help or --version")
    }

    private fun showVersion() {
        println("${config.name} version ${config.version}", config.greetingColor)
    }

    private fun showHelp() {
        println("${config.name} - ${config.description}", AnsiColor.BRIGHT_WHITE)
        println("Usage:", AnsiColor.BRIGHT_WHITE)
        println("  <command> [arguments] [options]")
        println()
        showInteractiveHelp()
    }
}