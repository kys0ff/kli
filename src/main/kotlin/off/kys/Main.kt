package off.kys

import off.kys.kli.core.config.model.ApplicationColors
import off.kys.kli.core.config.model.InteractiveMode
import off.kys.kli.dsl.kli.kli
import off.kys.kli.dsl.progress.progressBar
import off.kys.kli.io.*
import off.kys.kli.ui.progress.util.SpinnerStyle
import off.kys.kli.utils.KliScope
import off.kys.kli.utils.extensions.*

fun main(args: Array<String>) = kli(args) {
    configure {
        name = "MyCli"
        version = "0.1.6"
        description = "My first CLI app!"

        colors = ApplicationColors(
            primaryColor = AnsiColor.CYAN
        )
        interactiveMode = InteractiveMode(enabled = true)
    }

    onCrash = { println(it.localizedMessage, State.ERROR) }

    greetCommand()
}

fun KliScope.greetCommand() = command("greet") {
    description = "Greets the user"

    argument("name", "The name to greet")
    flag("shout", "Shout the greeting")

    action {
        val name = param { get("name") ?: readInputOrNull("Enter your name", AnsiColor.CYAN) ?: "stranger" }
        val greeting = "Hello, $name".italic().bold()

        if (getFlag("shout"))
            print(greeting.uppercase(), AnsiColor.rgbHex(0xFF8BF5))
        else
            print(greeting, AnsiColor.rgbHex(0xFF8BF5))

        println()

        Thread.sleep(1000)

        val read = readSingleKey()

        if (read == 'c') {
            clearScreen()
        }
        progressBar {
            width = getTerminalSize()?.columns?.div(3) ?: 7
            prefix = "Download in progress"
            //type = ProgressType.BLOCKS
            spinnerStyle = SpinnerStyle.GROWING_DOTS

            start(clearOnFinish = true) {
                (0..100).toList().forEach { progress ->
                    increment()
                    Thread.sleep(20)
                }
            }
        }
    }
}