package off.kys.kli.errors

/**
 * Exception thrown when attempting to register a command with a name that is already in use.
 *
 * This exception is used to prevent the registration of duplicate commands in the CLI tool.
 * Each command must have a unique name to avoid conflicts during execution.
 *
 * @constructor Creates a new `CommandAlreadyExistsException` with the name of the duplicate command.
 * @param commandName The name of the command that is already registered.
 */
class CommandAlreadyExistsException(commandName: String) : Exception(
    "Command '$commandName' is already registered."
)
