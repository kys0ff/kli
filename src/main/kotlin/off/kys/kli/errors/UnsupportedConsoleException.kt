@file:Suppress("unused")

package off.kys.kli.errors

class UnsupportedConsoleException : Exception {
    constructor() : super("This CLI tool must be run in a terminal environment. Running from the IDE's default run configuration is not supported.")

    constructor(message: String) : super(message)
}