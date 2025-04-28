package off.kys.kli.ui.progress.util

/**
 * Enum class representing different types of progress indicators.
 *
 * The `ProgressType` enum defines the available styles of progress indicators
 * that can be used in your CLI or UI applications. These indicators are commonly
 * used to show progress during long-running operations or tasks.
 *
 * Available Progress Types:
 * - [BAR]: A traditional progress bar that fills as the task progresses.
 * - [SPINNER]: A spinning animation that indicates the task is ongoing.
 * - [DOTS]: A sequence of dots that animate to indicate progress.
 * - [BLOCKS]: A series of blocks that fill in as progress advances.
 * - [PULSE]: A pulsing animation that signifies activity or progress.
 */
enum class ProgressType {
    /** A traditional progress bar. */
    BAR,

    /** A spinning animation. */
    SPINNER,

    /** A sequence of animated dots. */
    DOTS,

    /** A series of animated blocks. */
    BLOCKS,

    /** A pulsing animation to indicate progress. */
    PULSE
}
