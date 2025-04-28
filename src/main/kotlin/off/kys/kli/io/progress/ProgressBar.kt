@file:Suppress("unused")

package off.kys.kli.io.progress

import off.kys.cli.core.progress.util.ProgressStyle
import off.kys.kli.io.AnsiColor
import off.kys.kli.utils.extensions.color
import kotlin.concurrent.thread

// region Progress Bar
class ProgressBar(
    private val total: Int,
    private val width: Int = 50,
    private val style: ProgressStyle = ProgressStyle.BLOCK,
) {
    private var currentProgress = 0
    private var running = false
    private var spinnerState = 0
    private val spinnerChars = listOf("\\", "|", "/")

    private fun render(): String {
        val percent = (currentProgress.toDouble() / total * 100).toInt()
        return when (style) {
            ProgressStyle.BLOCK -> renderBlockStyle(percent)
            ProgressStyle.ARROW -> renderArrowStyle(percent)
            ProgressStyle.SPINNER -> renderSpinnerStyle(percent)
        }
    }

    private fun renderBlockStyle(percent: Int): String {
        val filledWidth = (currentProgress.toDouble() / total * width).toInt()
        val filled = "█".repeat(filledWidth)
        val empty = "░".repeat(width - filledWidth)
        return "${AnsiColor.BRIGHT_GREEN.code}[$filled$empty] ${percent.toString().padStart(3)}%${AnsiColor.RESET.code}"
    }

    private fun renderArrowStyle(percent: Int): String {
        val arrowPos = (currentProgress.toDouble() / total * width).toInt()
        val before = "-".repeat(arrowPos)
        val after = "-".repeat(width - arrowPos - 1)
        return "${AnsiColor.BRIGHT_CYAN.code}[${before}>${after}] ${
            percent.toString().padStart(3)
        }%${AnsiColor.RESET.code}"
    }

    private fun renderSpinnerStyle(percent: Int): String {
        spinnerState = (spinnerState + 1) % spinnerChars.size
        return "${spinnerChars[spinnerState]} ${
            percent.toString().padStart(3)
        }% ${progressMessage()}".color(AnsiColor.BRIGHT_MAGENTA)
    }

    private fun progressMessage(): String {
        val messages = listOf(
            "Calculating...", "Processing...", "Working...",
            "Almost there...", "Finishing up..."
        )
        return messages[(currentProgress % messages.size)]
    }

    fun start() {
        running = true
        thread(isDaemon = true) {
            while (running) {
                print("\r${render()}  ")
                Thread.sleep(if (style == ProgressStyle.SPINNER) 100 else 50)
            }
        }
    }

    fun update(progress: Int) {
        currentProgress = progress.coerceIn(0, total)
    }

    fun stop(message: String = "Operation completed!") {
        running = false
        println("\n${message.color(AnsiColor.BRIGHT_GREEN)}\n")
    }
}