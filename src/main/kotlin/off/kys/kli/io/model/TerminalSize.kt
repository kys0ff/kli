package off.kys.kli.io.model

/**
 * Represents the size of a terminal with its dimensions defined by the number of columns and rows.
 *
 * @property columns The number of columns in the terminal.
 * @property rows The number of rows in the terminal.
 */
data class TerminalSize(val columns: Int, val rows: Int)