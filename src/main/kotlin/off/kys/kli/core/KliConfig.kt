package off.kys.kli.core

import off.kys.kli.io.AnsiColor

class KliConfig {
    var name: String = "null"
    var version: String = "null"
    var description: String = "null"

    var showInteractiveModeMessage: Boolean = true
    var showGoodbyeMessage: Boolean = true
    var showUsageOnError: Boolean = true

    var showPrompt: Boolean = true
    var promptColor: AnsiColor = AnsiColor.BRIGHT_CYAN
    var greetingColor: AnsiColor = AnsiColor.BRIGHT_GREEN
    var goodbyeColor: AnsiColor = AnsiColor.BRIGHT_GREEN
}