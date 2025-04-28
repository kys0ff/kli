@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package off.kys.kli.ui.progress

import kotlinx.coroutines.*
import off.kys.kli.ui.progress.util.ProgressType
import off.kys.kli.ui.progress.util.SpinnerStyle

/**
 * A class representing a progress bar or other types of progress indicators (e.g., spinner, dots, blocks, pulse).
 *
 * The `ProgressBar` class provides functionality to display different types of progress indicators in the terminal.
 * It can show a simple progress bar, spinner, animated dots, blocks, or pulse effect.
 * This is particularly useful for CLI applications where visual feedback of an ongoing process is needed.
 *
 * @property width The width of the progress bar (only applies when the progress type is `BAR`, `BLOCKS`, or `DOTS`).
 * @property symbol The symbol used to represent the filled part of the progress bar (default is '=').
 * @property emptySymbol The symbol used to represent the unfilled part of the progress bar (default is a space).
 * @property prefix A string to display before the progress bar or spinner (default is an empty string).
 * @property suffix A string to display after the progress value (default is "%").
 * @property type The type of progress indicator to use (e.g., `BAR`, `SPINNER`, `DOTS`, `BLOCKS`, `PULSE`).
 * @property spinnerStyle The style of the spinner (applies only if `type` is `SPINNER`).
 */
class ProgressBar(
    var width: Int = 30,
    var symbol: Char = '=',
    var emptySymbol: Char = ' ',
    var prefix: String = "",
    var suffix: String = "%",
    var type: ProgressType = ProgressType.BAR,
    var spinnerStyle: SpinnerStyle = SpinnerStyle.DEFAULT,
) {
    private var current: Int = 0
    private var total: Int = 100
    private var spinnerIndex: Int = 0
    private var spinnerJob: Job? = null

    /**
     * Starts the progress indicator with the specified total value and an optional block of code for updates.
     *
     * @param total The total value of the progress (default is 100).
     * @param block The block of code that updates the progress (can call `update` or `increment` inside it).
     */
    fun start(total: Int = 100, block: ProgressBar.() -> Unit) {
        this.total = total
        this.current = 0

        // Start spinner animation if the selected type is spinner
        if (type == ProgressType.SPINNER)
            startSpinner()

        block()

        // Stop spinner animation after progress block is complete
        if (type == ProgressType.SPINNER)
            stopSpinner()

        println() // Move to the next line after the progress display
    }

    /**
     * Updates the progress to the specified value.
     *
     * @param value The new value of the progress (should be between 0 and `total`).
     */
    fun update(value: Int) {
        current = value.coerceIn(0, total) // Ensures the value stays between 0 and total
        if (type != ProgressType.SPINNER)
            draw()
    }

    /**
     * Increments the current progress by the specified step.
     *
     * @param step The step to increment the progress by (default is 1).
     */
    fun increment(step: Int = 1) = update(current + step)

    // Private function to draw the correct type of progress indicator
    private fun draw() = when (type) {
        ProgressType.BAR -> drawBar()
        ProgressType.SPINNER -> Unit // Spinner draws separately in the `startSpinner` coroutine
        ProgressType.DOTS -> drawDots()
        ProgressType.BLOCKS -> drawBlocks()
        ProgressType.PULSE -> drawPulse()
    }

    // Private function to start the spinner animation in a separate coroutine
    private fun startSpinner() {
        spinnerJob = CoroutineScope(Dispatchers.IO).launch {
            val frames = spinnerStyle.frames
            while (isActive) {
                val frame = frames[spinnerIndex % frames.size]
                print("\r$prefix $frame") // Update spinner frame
                spinnerIndex++
                delay(spinnerStyle.intervalMs.toLong()) // Delay based on intervalMs
            }
        }
    }

    // Private function to stop the spinner animation
    private fun stopSpinner() = spinnerJob?.cancel()

    // Private function to draw a standard progress bar
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
        print("\r$bar") // Print the progress bar
    }

    // Private function to draw a progress indicator using dots
    private fun drawDots() {
        val dots = ".".repeat((current / 5) % width) // Draw a number of dots based on progress
        print("\r$prefix$dots")
    }

    // Private function to draw a progress indicator using blocks
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
        print("\r$bar") // Print the block progress indicator
    }

    // Private function to draw a progress indicator with a pulse effect
    private fun drawPulse() {
        val pulse = if ((current / 5) % 2 == 0) "←" else "→" // Alternating pulse directions
        print("\r$prefix $pulse")
    }
}