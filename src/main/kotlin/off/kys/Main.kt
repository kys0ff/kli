package off.kys

import off.kys.kli.dsl.kli.kli
import off.kys.kli.io.readInputOrEmpty

fun main(args: Array<String>) = kli(args) {
    configure {
        name = "MyCli"
        version = "0.1.0"
        description = "My first CLI app!"
    }

    command("greet") {
        description = "Greets a user."

        action { cliArgs ->
            val name = cliArgs["name"] ?: readInputOrEmpty("Enter your name")
            println("Hello, $name!")
        }
    }
}