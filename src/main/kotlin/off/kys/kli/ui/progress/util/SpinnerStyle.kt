@file:Suppress("unused")

package off.kys.kli.ui.progress.util

/**
 * Enum class representing different spinner animation styles for progress indicators.
 *
 * The `SpinnerStyle` enum defines a variety of spinner animations that can be used
 * to show ongoing progress in your CLI or UI applications. These animations can
 * be customized with different frames and an optional frame interval.
 *
 * Each spinner style consists of:
 * - A list of frames that cycle to create the animation.
 * - An optional interval (in milliseconds) that determines the speed at which the frames cycle.
 *
 * Available Spinner Styles:
 * - [DEFAULT]: A basic spinner using common characters to create a rotating effect.
 * - [DOTS]: A spinner that uses dots and variations to create a spinning effect.
 * - [BOUNCING_BAR]: A bouncing bar that creates the illusion of a bar filling and emptying.
 * - [ARROW]: A spinner using directional arrows that rotate in a circle.
 * - [PIPE]: A spinner using a pipe symbol (`|`, `/`, `-`, `\`) to rotate.
 * - [GROWING_DOTS]: A spinner with growing and shrinking dots that pulse.
 *
 * @param frames A list of frames to animate for the spinner.
 * @param intervalMs The interval (in milliseconds) between each frame update. Defaults to 100ms.
 */
enum class SpinnerStyle(val frames: List<String>, val intervalMs: Int = 100) {
    /** A basic spinner using characters: |, /, -, \ */
    DEFAULT(listOf("|", "/", "-", "\\")),

    /** A spinner using animated dots: ⠋, ⠙, ⠹, etc. */
    DOTS(listOf("⠋", "⠙", "⠹", "⠸", "⠼", "⠴", "⠦", "⠧", "⠇", "⠏")),

    /** A bouncing bar that fills and empties: [    ], [=   ], [==  ], etc. */
    BOUNCING_BAR(listOf("[    ]", "[=   ]", "[==  ]", "[=== ]", "[ ===]", "[  ==]", "[   =]")),

    /** A spinner using rotating directional arrows: ←, ↖, ↑, ↗, etc. */
    ARROW(listOf("←", "↖", "↑", "↗", "→", "↘", "↓", "↙")),

    /** A spinner using pipe symbols: ┤, ┘, ┴, └, ├, ┌, ┬, ┐ */
    PIPE(listOf("┤", "┘", "┴", "└", "├", "┌", "┬", "┐")),

    /** A spinner with growing and shrinking dots: .  , .. , ..., etc. */
    GROWING_DOTS(listOf(".  ", ".. ", "...", " ..", "  .", "   "))
}
