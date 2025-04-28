package off.kys

import off.kys.kli.dsl.kli.kli
import off.kys.kli.dsl.progress.progressBar
import off.kys.kli.io.InputReader
import off.kys.kli.ui.progress.util.ProgressType
import off.kys.kli.ui.progress.util.SpinnerStyle
import off.kys.kli.utils.extensions.State
import off.kys.kli.utils.extensions.println

// Example
fun main(args: Array<String>) = kli(args) {
    configure {
        name = "Robo-cli"
        version = "1.0.0"
        description = "Robo-cli is a tool for file operations like copy, move, and mirror directories."
        showPrompt = true // you can set false if you want silent input
    }

    command("sum") {
        description = "Adds two numbers."

        action { cliArgs ->
            val x = cliArgs["num1"]?.toIntOrNull() ?: InputReader.readInput("Enter first number").toIntOrNull() ?: 0
            val y = cliArgs["num2"]?.toIntOrNull() ?: InputReader.readInput("Enter second number").toIntOrNull() ?: 0
            println("Sum: ${x + y}")
        }
    }

    command("copy") {
        description = "Copies files from source to destination, similar to Robocopy."

        action { cliArgs ->
            val params = cliArgs.getParams()

            if (params.size < 2) {
                println("Error: Missing source and destination paths!", State.ERROR)
                println("Usage: copy <source> <destination> [options]")
                return@action
            }

            params[0].toIntOrNull()
            val source = params[1]
            val destination = params[2]

            val move = cliArgs.getFlag("move")
            val mirror = cliArgs.getFlag("mir")
            val recursive = cliArgs.getFlag("s")

            // Start the progress bar for the operation
            progressBar {
                type = ProgressType.SPINNER
                width = 50 // Set the width of the progress bar
                spinnerStyle = SpinnerStyle.PIPE // Optional: Add a spinner style
                prefix = "Copying"
                suffix = "%"
                start {
                    val totalFiles = 100 // Simulate 100 files to copy
                    for (i in 1..totalFiles) {
                        // Simulating a file copy by incrementing progress
                        update(i)
                        Thread.sleep(50) // Simulate some delay between file operations
                    }
                }
                println("File copy operation completed!", State.SUCCESS)
            }

            // Simulated file copy operation output
            println("Source: $source")
            println("Destination: $destination")
            println("Options:")
            println("- Move files: ${if (move) "Yes" else "No"}")
            println("- Mirror directories: ${if (mirror) "Yes" else "No"}")
            println("- Copy subdirectories: ${if (recursive) "Yes" else "No"}")

            // Here you would add the real file operation logic!
            println("Simulating file operation...", State.SUCCESS)
        }
    }
}