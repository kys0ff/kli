@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package off.kys.kli.ui.progress

import off.kys.kli.ui.progress.util.ProgressType
import off.kys.kli.ui.progress.util.SpinnerStyle

/**
 * A terminal-based progress bar/spinner/dots/etc. visualizer.
 *
 * Supports multiple visual styles like:
 * - Traditional bar
 * - Dots
 * - Block-style bar
 * - Pulse animation
 * - Spinner animation (in a separate thread)
 */
class ProgressBar(
    var width: Int = 30,                        // Width of the progress bar (number of symbols)
    var symbol: Char = '=',                     // Character to represent filled progress
    var emptySymbol: Char = ' ',                // Character to represent unfilled progress
    var prefix: String = "",                    // Optional prefix text before the bar
    var suffix: String = "%",                   // Optional suffix text after the percentage
    var type: ProgressType = ProgressType.BAR,  // The type of progress visual to use
    var spinnerStyle: SpinnerStyle = SpinnerStyle.DEFAULT, // Spinner style (frames and interval)
) {
    private var current: Int = 0          // Current progress value
    private var total: Int = 100          // Total value to reach 100%
    private var spinnerIndex: Int = 0     // Index of the current spinner frame
    private var spinnerThread: Thread? = null // Background thread for spinner animation
    @Volatile
    private var spinning = false    // Flag to control spinner thread execution

    /**
     * Starts the progress and executes the block. Optionally clears output after completion.
     *
     * @param total The total value to reach 100%.
     * @param clearOnFinish Whether to remove the progress display after finish.
     * @param block The operation to execute while showing progress.
     */
    fun start(total: Int = 100, clearOnFinish: Boolean = false, block: ProgressBar.() -> Unit) {
        this.total = total
        this.current = 0

        if (type == ProgressType.SPINNER) startSpinner()

        block()

        if (type == ProgressType.SPINNER) stopSpinner()

        if (clearOnFinish) clearLine() else println()
    }

    /**
     * Updates the progress to a specific value.
     * Triggers a redrawing if it's not a spinner type.
     */
    fun update(value: Int) {
        current = value.coerceIn(0, total)
        if (type != ProgressType.SPINNER)
            draw()
    }

    /**
     * Increments the current progress by a given step (default is 1).
     */
    fun increment(step: Int = 1) = update(current + step)

    /**
     * Clears the current terminal line.
     */
    private fun clearLine() {
        print("\r")                   // Return carriage
        print(" ".repeat(100))       // Overwrite with spaces
        print("\r")                   // Return to line start again
    }

    /**
     * Selects the appropriate drawing method based on the progress type.
     */
    private fun draw() = when (type) {
        ProgressType.BAR -> drawBar()
        ProgressType.SPINNER -> Unit // Spinner is handled by a background thread
        ProgressType.DOTS -> drawDots()
        ProgressType.BLOCKS -> drawBlocks()
        ProgressType.PULSE -> drawPulse()
    }

    /**
     * Starts the spinner in a separate thread, looping through the animation frames.
     */
    private fun startSpinner() {
        spinning = true
        val frames = spinnerStyle.frames
        spinnerThread = Thread {
            while (spinning) {
                val frame = frames[spinnerIndex % frames.size]
                print("\r$prefix $frame") // Print current frame
                spinnerIndex++
                try {
                    Thread.sleep(spinnerStyle.intervalMs.toLong())
                } catch (_: InterruptedException) {
                    break
                }
            }
        }.apply {
            isDaemon = true
            start()
        }
    }

    /**
     * Stops the spinner thread by interrupting it.
     */
    private fun stopSpinner() {
        spinning = false
        spinnerThread?.interrupt()
        spinnerThread = null
    }

    /**
     * Draws a traditional progress bar with filled and empty sections.
     */
    private fun drawBar() {
        val percent = current * 100 / total
        val filled = width * current / total
        val empty = width - filled

        val bar = buildString {
            append(prefix)
            append(" [")
            repeat(filled) { append(symbol) }
            repeat(empty) { append(emptySymbol) }
            append("] ")
            append(percent)
            append(suffix)
        }
        print("\r$bar") // Overwrite the same line
    }

    /**
     * Draws a minimalistic progress style using dots.
     */
    private fun drawDots() {
        val dots = ".".repeat((current / 5) % width)
        print("\r$prefix$dots")
    }

    /**
     * Draws bar-style progress using block characters for a stronger visual.
     */
    private fun drawBlocks() {
        val percent = current * 100 / total
        val filled = width * current / total
        val empty = width - filled

        val bar = buildString {
            append(prefix)
            append(" [")
            repeat(filled) { append('▓') }
            repeat(empty) { append('░') }
            append("] ")
            append(percent)
            append(suffix)
        }
        print("\r$bar")
    }

    /**
     * Draws a pulse animation that switches between left and right arrows.
     */
    private fun drawPulse() {
        val pulse = if ((current / 5) % 2 == 0) "←" else "→"
        print("\r$prefix $pulse")
    }
}
