@file:Suppress("MemberVisibilityCanBePrivate", "KDocUnresolvedReference")

package off.kys.kli.parser

/**
 * A utility class for parsing command-line arguments.
 *
 * This class helps to process a list of arguments passed to a program.
 * It distinguishes between flags (like `-f` or `--flag`), key-value pairs (like `--key value`),
 * and positional parameters (just values like `file.txt`).
 *
 * The class provides methods to retrieve flags, arguments, and parameters.
 *
 * @property arguments A map of argument names to their values (e.g., `--key value`).
 * @property flags A set of flags (e.g., `-f`, `--flag`) that don't have values.
 * @property params A list of positional parameters that don't have flags or keys (e.g., files or other values).
 */
class ArgParser(
    private val arguments: Map<String, String>, // Stores key-value arguments
    private val flags: Set<String>,            // Stores flags with no values
    private val params: List<String>,          // Stores positional parameters
) {

    /**
     * Companion object that allows creation of an `ArgParser` from a raw argument array.
     *
     * The `from` method takes an array of strings (e.g., `args` from `main(args: Array<String>)`)
     * and parses them into a structured `ArgParser` object. It processes flags, arguments,
     * and positional parameters accordingly.
     *
     * Example:
     * ```
     * val parser = ArgParser.from(arrayOf("--flag", "-v", "--file", "myfile.txt", "param1"))
     * ```
     *
     * @param args An array of strings representing the raw command-line arguments.
     * @return A populated `ArgParser` instance.
     */
    companion object {
        fun from(args: Array<String>): ArgParser {
            val arguments = mutableMapOf<String, String>()
            val flags = mutableSetOf<String>()
            val params = mutableListOf<String>()

            var i = 0
            while (i < args.size) {
                when {
                    args[i].startsWith("--") -> { // Long-form arguments (e.g., --key value)
                        val key = args[i].substring(2)
                        if (i + 1 < args.size && !args[i + 1].startsWith("-")) {
                            arguments[key] = args[i + 1] // Capture key-value argument
                            i++
                        } else flags.add(key) // Capture flag
                    }

                    args[i].startsWith("-") -> { // Short-form arguments (e.g., -k value)
                        val key = args[i].substring(1)
                        if (i + 1 < args.size && !args[i + 1].startsWith("-")) {
                            arguments[key] = args[i + 1] // Capture key-value argument
                            i++
                        } else flags.add(key) // Capture flag
                    }

                    else -> params.add(args[i]) // Capture positional parameter
                }
                i++
            }

            return ArgParser(arguments, flags, params)
        }
    }

    /**
     * Checks if a flag is present.
     *
     * This method returns `true` if the specified flag is present in the list of flags.
     *
     * Example:
     * ```
     * val parser = ArgParser.from(arrayOf("-f", "--verbose"))
     * println(parser.getFlag("f")) // true
     * ```
     *
     * @param flag The name of the flag to check.
     * @return `true` if the flag is present, `false` otherwise.
     */
    fun getFlag(flag: String): Boolean = flags.contains(flag)

    /**
     * Retrieves the value of a specified argument.
     *
     * This method returns the value associated with a named argument (e.g., `--key value`).
     * If the argument is not found, it returns `null`.
     *
     * Example:
     * ```
     * val parser = ArgParser.from(arrayOf("--file", "document.txt"))
     * println(parser.getArgument("file")) // "document.txt"
     * ```
     *
     * @param arg The name of the argument to retrieve.
     * @return The value of the argument, or `null` if not found.
     */
    fun getArgument(arg: String): String? = arguments[arg]

    /**
     * Retrieves the value of a specified argument using the bracket notation.
     *
     * This method is an alias for [getArgument].
     *
     * Example:
     * ```
     * val parser = ArgParser.from(arrayOf("--path", "/home/user"))
     * println(parser["path"]) // "/home/user"
     * ```
     *
     * @param arg The name of the argument to retrieve.
     * @return The value of the argument, or `null` if not found.
     */
    operator fun get(arg: String): String? = getArgument(arg)

    /**
     * Retrieves a list of all positional parameters.
     *
     * This method returns all values that were passed as positional parameters (i.e., without flags or keys).
     *
     * Example:
     * ```
     * val parser = ArgParser.from(arrayOf("file1.txt", "file2.txt"))
     * println(parser.getParams()) // ["file1.txt", "file2.txt"]
     * ```
     *
     * @return A list of all positional parameters.
     */
    fun getParams(): List<String> = params
}