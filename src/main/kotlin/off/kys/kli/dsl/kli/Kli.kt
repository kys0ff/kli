package off.kys.kli.dsl.kli

import off.kys.kli.dsl.KliDsl
import off.kys.kli.dsl.markers.KliMarker
import off.kys.kli.utils.extensions.State
import off.kys.kli.utils.extensions.println

/**
 * A function to initialize and execute a KLI (Command Line Interface) application using the `KliDsl` DSL.
 *
 * This function simplifies the setup of the KLI environment by creating a `KliDsl` instance,
 * configuring it with a block of code, and then running the CLI with the provided command-line arguments (`args`).
 * The function facilitates the configuration of commands, handling arguments, and setting up global options for the CLI.
 * It also handles errors gracefully, with optional custom crash handling.
 *
 * **Parameters**:
 * - `args`: An array of strings representing the command-line arguments passed to the KLI.
 *   It defaults to an empty array, meaning that the function can also be invoked without arguments to start in interactive mode.
 * - `block`: A configuration block of type `KliDsl.() -> Unit`, which allows for customizing the behavior of the CLI.
 *   This block is applied to the `KliDsl` instance to register commands and set global configurations like greeting messages and error handling.
 *
 * The `KliDsl` instance is configured using the provided `block`, which can include commands and global settings.
 * Once configured, the CLI is executed with the given arguments (`args`). If no arguments are provided, the CLI enters interactive mode.
 *
 * **Usage Example**:
 * ```kotlin
 * kli(args) {
 *     configure {
 *         // Custom KLI configuration goes here
 *     }
 * }
 * ```
 *
 * **Annotation**:
 * - This function is annotated with `@KliMarker` to indicate that it is part of the KLI DSL and restricts usage to DSL contexts.
 *
 * **Error Handling**:
 * - Any exceptions or errors encountered during the execution are caught and passed to the `onCrash` handler if defined.
 *   If no handler is defined, the error message is printed to `stderr`, and the exception's stack trace is displayed for debugging.
 *
 * **Example Flow**:
 * 1. A new `KliDsl` instance is created.
 * 2. The provided configuration block is applied to the instance to set up commands and options.
 * 3. The arguments (`args`) are processed, either executing a command or switching to interactive mode if no arguments are provided.
 * 4. If an error occurs, the crash handler (if defined) is invoked, or a default error message is printed.
 *
 * **Parameters**:
 * - `args`: Command-line arguments for the KLI. Default to an empty array.
 * - `block`: The configuration block used to set up commands and options within the KLI.
 *
 */
@KliMarker
fun kli(args: Array<String>, block: KliDsl.() -> Unit) {
    val dsl = KliDsl()
    try {
        dsl.block() // Configures the KLI with the provided DSL block (command registration, configurations)
        dsl.execute(args) // Executes the KLI with the given arguments, either in interactive or command mode.
    } catch (e: Throwable) {
        // Error handling: catches all exceptions
        dsl.onCrash?.invoke(e) ?: run {
            println("Unhandled fatal error: ${e.message}", State.ERROR) // Prints a fatal error message to stderr.
            e.printStackTrace() // Optionally prints the stack trace for debugging purposes.
        }
    }
}