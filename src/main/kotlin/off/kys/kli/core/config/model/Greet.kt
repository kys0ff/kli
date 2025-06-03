package off.kys.kli.core.config.model

import off.kys.kli.io.AnsiColor

/**
 * Greeting message shown when entering interactive mode.
 *
 * @property show Whether to display the greeting.
 * @property color Text color of the greeting message.
 * @property message Content of the greeting message.
 */
data class Greet(
    val show: Boolean = true,
    val color: AnsiColor = AnsiColor.BRIGHT_GREEN,
    val message: String
)