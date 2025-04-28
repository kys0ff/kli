package off.kys.kli.core

import off.kys.kli.io.AnsiColor

/**
 * Represents the configuration for the KLI (Command Line Interface).
 *
 * The `KliConfig` class allows customization of the CLI tool's behavior, appearance,
 * and interactive mode. This includes options like displaying messages, customizing
 * colors, and controlling whether certain UI elements are shown.
 *
 * **Properties**:
 * - `name`: The name of the CLI tool. Default value is `"null"`.
 * - `version`: The version of the CLI tool. Default value is `"null"`.
 * - `description`: A description of the CLI tool. Default value is `"null"`.
 * - `showInteractiveModeMessage`: A flag that controls whether a message is displayed when entering interactive mode. Default is `true`.
 * - `showGoodbyeMessage`: A flag that controls whether a goodbye message is shown when exiting the CLI tool. Default is `true`.
 * - `showUsageOnError`: A flag that controls whether usage instructions are displayed when an error occurs. Default is `true`.
 * - `showPrompt`: A flag that controls whether a prompt is shown before user input in interactive mode. Default is `true`.
 * - `promptColor`: The color used for the prompt text. Default is `AnsiColor.BRIGHT_CYAN`.
 * - `greetingColor`: The color used for the greeting message. Default is `AnsiColor.BRIGHT_GREEN`.
 * - `goodbyeColor`: The color used for the goodbye message. Default is `AnsiColor.BRIGHT_GREEN`.
 */
class KliConfig {
    var name: String = "null" // The name of the CLI tool
    var version: String = "null" // The version of the CLI tool
    var description: String = "null" // A description of the CLI tool

    var showInteractiveModeMessage: Boolean = true // Flag to show message when entering interactive mode
    var showGoodbyeMessage: Boolean = true // Flag to show goodbye message when exiting
    var showUsageOnError: Boolean = true // Flag to show usage instructions on error

    var showPrompt: Boolean = true // Flag to show a prompt before input in interactive mode
    var promptColor: AnsiColor = AnsiColor.BRIGHT_CYAN // The color for the prompt
    var greetingColor: AnsiColor = AnsiColor.BRIGHT_GREEN // The color for the greeting message
    var goodbyeColor: AnsiColor = AnsiColor.BRIGHT_GREEN // The color for the goodbye message
}
