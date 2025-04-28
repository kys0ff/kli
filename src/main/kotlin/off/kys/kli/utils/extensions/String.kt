@file:Suppress("unused")

package off.kys.kli.utils.extensions

import off.kys.kli.io.AnsiColor

fun String.color(color: AnsiColor): String = "${color.code}$this${AnsiColor.RESET.code}"

fun String.bold(): String = "${AnsiColor.BOLD.code}$this${AnsiColor.RESET.code}"