package off.kys.kli.core.config.model

/**
 * Configuration for interactive mode behavior.
 *
 * @property enabled Enables or disables interactive mode.
 * @property greet Message shown when entering interactive mode.
 * @property goodBye Message shown when exiting interactive mode.
 */
data class InteractiveMode(
    val enabled: Boolean = true,
    val greet: Greet? = null,
    val goodBye: GoodBye? = null,
)