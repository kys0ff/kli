package off.kys.kli.core.config

import off.kys.kli.core.config.model.ApplicationColors
import off.kys.kli.core.config.model.GoodBye
import off.kys.kli.core.config.model.Greet
import off.kys.kli.core.config.model.InteractiveMode

/**
 * Configuration for the KLI (Kotlin Line Interface).
 *
 * Controls various aspects of CLI behavior including application metadata,
 * interactive mode features, error handling behavior, and output coloring.
 */
class KliConfig {

    /** Name of the CLI application. */
    var name: String = "null"

    /** Version of the CLI application. */
    var version: String = "null"

    /** Short description of the CLI application. */
    var description: String = "null"

    /**
     * Settings related to interactive mode, including greetings and farewell messages.
     */
    var interactiveMode: InteractiveMode = InteractiveMode(
        greet = Greet(message = "Welcome to $name interactive mode!"),
        goodBye = GoodBye(message = "Goodbye!")
    )

    /**
     * If true, displays usage instructions automatically when an error occurs.
     */
    var showUsageOnError: Boolean = true

    /**
     * If true, displays a prompt before reading user input in interactive mode.
     */
    var showPrompt: Boolean = true

    /**
     * Customizable color palette for CLI output elements.
     */
    var colors: ApplicationColors = ApplicationColors()
}