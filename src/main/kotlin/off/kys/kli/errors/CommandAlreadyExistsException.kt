package off.kys.kli.errors

class CommandAlreadyExistsException(commandName: String) : Exception("Command '$commandName' is already registered.")
