package off.kys.kli.utils

import off.kys.kli.parser.ArgParser

/**
 * Type alias for [ArgParser], used to represent the parsed arguments scope.
 *
 * This alias simplifies references to [ArgParser] as `ArgsScope`, improving readability
 * when working with argument parsing throughout the project.
 *
 * @see ArgParser
 */
typealias ArgsScope = ArgParser

/**
 * **Deprecated** type alias for [ArgParser], used to represent the parsed arguments scope.
 *
 * This alias simplifies references to [ArgParser] as `ArgsScope`, improving readability
 * when working with argument parsing throughout the project. However, this alias is now deprecated
 * in favor of using [ArgsScope] for better clarity and consistency in naming.
 *
 * @see ArgParser
 * @see ArgsScope
 */
@Deprecated(
    message = "Use ArgsScope instead for better clarity and consistency.",
    replaceWith = ReplaceWith("ArgsScope"),
)
typealias Args = ArgParser
