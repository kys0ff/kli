package off.kys.kli.core.config.model

import off.kys.kli.io.AnsiColor

/**
 * Configuration for interactive mode behavior.
 *
 * @property enabled Enables or disables interactive mode.
 * @property showMessage Whether to show a startup message.
 * @property color Color used for interactive status messages.
 * @property greet Message shown when entering interactive mode.
 * @property goodBye Message shown when exiting interactive mode.
 */
data class InteractiveMode(
    val enabled: Boolean = true,
    val showMessage: Boolean = true,
    val color: AnsiColor = AnsiColor.BRIGHT_GREEN,
    val greet: Greet? = null,
    val goodBye: GoodBye? = null,
)