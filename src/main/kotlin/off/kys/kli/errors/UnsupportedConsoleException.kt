@file:Suppress("unused", "KDocUnresolvedReference")

package off.kys.kli.errors

/**
 * Exception thrown when a CLI tool is run in an unsupported environment.
 *
 * This exception is used to indicate that the tool requires a terminal (or command-line) environment
 * to function properly. Running the tool in an IDE or other non-terminal environments is not supported,
 * and this exception will be thrown if such an attempt is made.
 *
 * @constructor Creates a new `UnsupportedConsoleException` with a default or custom error message.
 * @param message The error message to be displayed (optional). If not provided, a default message will be used.
 */
class UnsupportedConsoleException : Exception {

    /**
     * Default constructor with a pre-defined error message.
     *
     * The default message indicates that the CLI tool must be run in a terminal environment and not in an IDE's run configuration.
     */
    constructor() : super("This CLI tool must be run in a terminal environment. Running from the IDE's default run configuration is not supported.")

    /**
     * Constructor that allows a custom error message.
     *
     * This constructor lets you pass a specific message to be shown when the exception is thrown.
     *
     * @param message The custom error message to be displayed.
     */
    constructor(message: String) : super(message)
}