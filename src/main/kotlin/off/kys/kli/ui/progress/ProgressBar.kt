@file:Suppress("MemberVisibilityCanBePrivate")

package off.kys.kli.ui.progress

import kotlinx.coroutines.*
import off.kys.kli.ui.progress.util.ProgressType
import off.kys.kli.ui.progress.util.SpinnerStyle

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

    fun start(total: Int = 100, block: ProgressBar.() -> Unit) {
        this.total = total
        this.current = 0

        if (type == ProgressType.SPINNER)
            startSpinner()

        block()

        if (type == ProgressType.SPINNER)
            stopSpinner()

        println()
    }

    fun update(value: Int) {
        current = value.coerceIn(0, total)
        if (type != ProgressType.SPINNER)
            draw()
    }

    fun increment(step: Int = 1) = update(current + step)

    private fun draw() = when (type) {
        ProgressType.BAR -> drawBar()
        ProgressType.SPINNER -> Unit // spinner draws separately
        ProgressType.DOTS -> drawDots()
        ProgressType.BLOCKS -> drawBlocks()
        ProgressType.PULSE -> drawPulse()
    }

    private fun startSpinner() {
        spinnerJob = CoroutineScope(Dispatchers.IO).launch {
            val frames = spinnerStyle.frames
            while (isActive) {
                val frame = frames[spinnerIndex % frames.size]
                print("\r$prefix $frame")
                spinnerIndex++
                delay(spinnerStyle.intervalMs.toLong())
            }
        }
    }

    private fun stopSpinner() = spinnerJob?.cancel()

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
        print("\r$bar")
    }

    private fun drawDots() {
        val dots = ".".repeat((current / 5) % width)
        print("\r$prefix$dots")
    }

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

    private fun drawPulse() {
        val pulse = if ((current / 5) % 2 == 0) "←" else "→"
        print("\r$prefix $pulse")
    }
}