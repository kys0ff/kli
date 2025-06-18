@file:Suppress("MemberVisibilityCanBePrivate", "KDocUnresolvedReference", "unused")

package off.kys.kli.parser

import off.kys.kli.core.Command

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
                val arg = args[i]

                when {
                    arg.startsWith("--") -> {
                        val eqIndex = arg.indexOf('=')
                        if (eqIndex != -1) {
                            // --key=value format
                            val key = arg.substring(2, eqIndex)
                            val value = arg.substring(eqIndex + 1)
                            arguments[key] = value
                        } else {
                            val key = arg.substring(2)
                            if (i + 1 < args.size && !args[i + 1].startsWith("-")) {
                                arguments[key] = args[i + 1]
                                i++
                            } else {
                                flags.add(key)
                            }
                        }
                    }

                    arg.startsWith("-") -> {
                        val eqIndex = arg.indexOf('=')
                        if (eqIndex != -1) {
                            // -k=value format
                            val key = arg.substring(1, eqIndex)
                            val value = arg.substring(eqIndex + 1)
                            arguments[key] = value
                        } else {
                            val key = arg.substring(1)
                            if (i + 1 < args.size && !args[i + 1].startsWith("-")) {
                                arguments[key] = args[i + 1]
                                i++
                            } else {
                                flags.add(key)
                            }
                        }
                    }

                    else -> params.add(arg)
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

    fun isEmpty(): Boolean = params.size == 0

    fun isNotEmpty(): Boolean = isEmpty().not()

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

    /**
     * Retrieves all positional parameters scoped to this [Command].
     *
     * This returns all parameters that come after the command name.
     *
     * Example:
     * ```
     * val parser = ArgParser.from(arrayOf("build", "module1", "module2"))
     * val cmd = Command("build")
     * println(cmd.params()) // ["module1", "module2"]
     * ```
     *
     * @return A list of scoped parameters, or an empty list if none found.
     */
    fun Command.params(): List<String> {
        val index = params.indexOfFirst { it.equals(this.name, ignoreCase = true) }
        return if (index != -1) params.drop(index + 1) else emptyList()
    }

    /**
     * Retrieves the first scoped parameter for this [Command].
     *
     * This is useful when you expect only a single parameter.
     *
     * Example:
     * ```
     * val parser = ArgParser.from(arrayOf("run", "main.kt"))
     * val cmd = Command("run")
     * println(cmd.param()) // "main.kt"
     * ```
     *
     * @return The first parameter, or `null` if none found.
     */
    fun Command.param(): String? = params().firstOrNull()

    /**
     * Retrieves the first scoped parameter for this [Command], or applies a default value if none found.
     *
     * This is useful when you want to return a default value when the parameter is missing or null.
     *
     * Example:
     * ```
     * val parser = ArgParser.from(arrayOf("run"))
     * val cmd = Command("run")
     * println(cmd.param { "default_value" }) // "default_value"
     * ```
     *
     * @param default A lambda function that returns the default value when the parameter is null.
     * @return The first parameter, or the result of the default lambda if none found.
     */
    fun Command.param(default: () -> String): String = params().firstOrNull() ?: default()

    /**
     * Retrieves the scoped parameter at the specified [index] for this [Command].
     *
     * Example:
     * ```
     * val parser = ArgParser.from(arrayOf("copy", "file1.txt", "file2.txt"))
     * val cmd = Command("copy")
     * println(cmd.paramAt(1)) // "file2.txt"
     * ```
     *
     * @param index The zero-based index of the parameter.
     * @return The parameter at the given index, or `null` if out of bounds.
     */
    fun Command.paramAt(index: Int): String? = params().getOrNull(index)

    /**
     * Checks if this [Command] has any scoped parameters.
     *
     * Example:
     * ```
     * val parser = ArgParser.from(arrayOf("deploy"))
     * val cmd = Command("deploy")
     * println(cmd.hasParams()) // false
     * ```
     *
     * @return `true` if there are parameters, `false` otherwise.
     */
    fun Command.hasParams(): Boolean = params().isNotEmpty()

    /**
     * Counts the number of scoped parameters for this [Command].
     *
     * Example:
     * ```
     * val parser = ArgParser.from(arrayOf("move", "file1.txt", "dir/"))
     * val cmd = Command("move")
     * println(cmd.paramCount()) // 2
     * ```
     *
     * @return The number of parameters found.
     */
    fun Command.paramCount(): Int = params().size

}