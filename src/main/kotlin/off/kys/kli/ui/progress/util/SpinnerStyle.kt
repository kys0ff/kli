@file:Suppress("unused")

package off.kys.kli.ui.progress.util

enum class SpinnerStyle(val frames: List<String>, val intervalMs: Int = 100) {
    DEFAULT(listOf("|", "/", "-", "\\")),
    DOTS(listOf("⠋","⠙","⠹","⠸","⠼","⠴","⠦","⠧","⠇","⠏")),
    BOUNCING_BAR(listOf("[    ]", "[=   ]", "[==  ]", "[=== ]", "[ ===]", "[  ==]", "[   =]")),
    ARROW(listOf("←", "↖", "↑", "↗", "→", "↘", "↓", "↙")),
    PIPE(listOf("┤", "┘", "┴", "└", "├", "┌", "┬", "┐")),
    GROWING_DOTS(listOf(".  ", ".. ", "...", " ..", "  .", "   ")),
}