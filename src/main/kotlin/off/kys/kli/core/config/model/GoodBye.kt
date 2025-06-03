package off.kys.kli.core.config.model

import off.kys.kli.io.AnsiColor

/**
 * Farewell message shown when exiting interactive mode.
 *
 * @property show Whether to display the farewell.
 * @property color Text color of the farewell message.
 * @property message Content of the farewell message.
 */
data class GoodBye(
    val show: Boolean = true,
    val color: AnsiColor = AnsiColor.BRIGHT_GREEN,
    val message: String
)