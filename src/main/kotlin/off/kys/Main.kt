package off.kys

import off.kys.kli.core.config.model.InteractiveMode
import off.kys.kli.dsl.kli.kli
import off.kys.kli.dsl.progress.progressBar
import off.kys.kli.io.AnsiColor
import off.kys.kli.io.getTerminalSize
import off.kys.kli.io.readInputOrNull
import off.kys.kli.ui.progress.util.SpinnerStyle
import off.kys.kli.utils.KliScope
import off.kys.kli.utils.extensions.bold
import off.kys.kli.utils.extensions.italic
import off.kys.kli.utils.extensions.print

fun main(args: Array<String>) = kli(args) {
    configure {
        name = "MyCli"
        version = "0.1.3"
        description = "My first CLI app!"

        interactiveMode = InteractiveMode(enabled = false)
    }

    onCrash = {
        println("Error GO!")
    }

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
        else print(greeting, AnsiColor.rgbHex(0xFF8BF5))

        println()

        Thread.sleep(1000)

        progressBar {
            width = getTerminalSize().columns / 3
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