package off.kys.kli.utils.extensions

/**
 * Enum class representing different states for printing messages.
 * Each state corresponds to a specific color for output messages.
 */
enum class State {
    /**
     * Represents a successful state. The corresponding color is green.
     */
    SUCCESS,

    /**
     * Represents an error state. The corresponding color is red.
     */
    ERROR,

    /**
     * Represents a warning state. The corresponding color is yellow.
     */
    WARNING,

    /**
     * Represents an informational state. The corresponding color is cyan.
     */
    INFO
}