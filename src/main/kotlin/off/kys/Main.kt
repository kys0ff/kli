package off.kys

import off.kys.kli.dsl.kli.kli
import off.kys.kli.io.AnsiColor
import off.kys.kli.io.readInputOrNull
import off.kys.kli.utils.KliScope
import off.kys.kli.utils.extensions.bold
import off.kys.kli.utils.extensions.italic
import off.kys.kli.utils.extensions.print

fun main(args: Array<String>) = kli(args) {
    configure {
        name = "MyCli"
        version = "0.1.3"
        description = "My first CLI app!"
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
    }
}