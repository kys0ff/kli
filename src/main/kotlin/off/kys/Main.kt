package off.kys

import off.kys.kli.dsl.kli.kli
import off.kys.kli.io.AnsiColor
import off.kys.kli.io.readInputOrNull
import off.kys.kli.utils.KliScope

fun main(args: Array<String>) = kli(args) {
    configure {
        name = "MyCli"
        version = "0.1.1"
        description = "My first CLI app!"
    }

    greetCommand()
}

fun KliScope.greetCommand() = command("greet") {
    description = "Greets the user"

    argument("name", "The name to greet")
    flag("shout", "Shout the greeting")

    action { args ->
        val name = args.getParams().getOrNull(1) ?: readInputOrNull("Enter your name", AnsiColor.CYAN) ?: "stranger"
        val greeting = "Hello, $name"
        if (args.getFlag("shout"))
            println(greeting.uppercase())
        else println(greeting)
    }
}