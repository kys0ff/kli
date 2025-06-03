package off.kys.kli.core.config.model

import off.kys.kli.io.AnsiColor

/**
 * Defines the color scheme used throughout the CLI interface.
 *
 * Each color is associated with a specific output use case.
 *
 * @property primaryColor Used for main titles, headers, or feature highlights.
 * @property secondaryColor Used for subtitles, supplementary text, or tips.
 * @property errorColor Used for error messages or critical failures.
 * @property warningColor Used for warnings or deprecations.
 * @property successColor Used for success confirmations or positive feedback.
 * @property infoColor Used for informational messages and logs.
 * @property inputPromptColor Used for user input prompts.
 * @property userInputColor Used for echoing typed input or user choices.
 * @property debugColor Used for internal debug messages or developer logs.
 */
data class ApplicationColors(
    val primaryColor: AnsiColor = AnsiColor.BRIGHT_CYAN,
    val secondaryColor: AnsiColor = AnsiColor.BRIGHT_GREEN,
    val errorColor: AnsiColor = AnsiColor.BRIGHT_RED,
    val warningColor: AnsiColor = AnsiColor.BRIGHT_YELLOW,
    val successColor: AnsiColor = AnsiColor.BRIGHT_GREEN,
    val infoColor: AnsiColor = AnsiColor.BRIGHT_BLUE,
    val inputPromptColor: AnsiColor = AnsiColor.WHITE,
    val userInputColor: AnsiColor = AnsiColor.BRIGHT_WHITE,
    val debugColor: AnsiColor = AnsiColor.MAGENTA,
)
