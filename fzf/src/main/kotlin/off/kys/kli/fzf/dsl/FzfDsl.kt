@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package off.kys.kli.fzf.dsl

import off.kys.kli.fzf.errors.FzfException
import off.kys.kli.fzf.errors.FzfNotInstalledException
import java.io.BufferedReader
import java.io.InputStreamReader

class FzfDsl {
    private var binds: MutableList<String> = mutableListOf()
    private var items: List<String> = emptyList()

    var title: String = ""
    var prompt: String = "> "
    var preview: String = ""
    var previewWindow: String = "right:50%"
    var layout: String = "default"
    var height: String = "40%"
    var multiSelect: Boolean = false
    var pointer: String = "➤"
    var marker: String = "✓"
    var header: String? = null
    var headerLines: Int = 0

    fun items(vararg items: String) {
        this.items = items.toList()
    }

    fun items(items: Iterable<String>) {
        this.items = items.toList()
    }

    fun bind(key: String, action: String) {
        binds += "$key:$action"
    }

    fun buildCommand(): List<String> {
        val cmd = mutableListOf("fzf")
        cmd += listOf("--prompt", prompt)
        cmd += listOf("--pointer", pointer)
        cmd += listOf("--marker", marker)
        cmd += listOf("--layout", layout)
        cmd += listOf("--height", height)
        if (preview.isNotEmpty()) {
            cmd += listOf("--preview", preview)
        }
        cmd += listOf("--preview-window", previewWindow)

        if (title.isNotEmpty()) cmd += listOf("--header", title)
        header?.let { cmd += listOf("--header", it) }
        if (multiSelect) cmd += "--multi"
        if (headerLines > 0) cmd += listOf("--header-lines", headerLines.toString())

        binds.forEach { cmd += listOf("--bind", it) }

        return cmd
    }

    fun execute(): List<String> {
        checkFzfInstalled()

        val process = ProcessBuilder(buildCommand())
            .redirectErrorStream(true)
            .start()

        process.outputStream.bufferedWriter().use { writer ->
            items.forEach { writer.write("$it\n") }
            writer.flush()
        }

        val reader = BufferedReader(InputStreamReader(process.inputStream))
        val selected = reader.readLines()

        return when (process.waitFor()) {
            0 -> selected
            1 -> emptyList() // No match
            2 -> throw FzfException("FZF error occurred")
            130 -> emptyList() // Interrupted with Ctrl-C
            else -> throw FzfException("Unknown FZF error (code ${process.exitValue()})")
        }
    }

    private fun checkFzfInstalled() {
        val process = ProcessBuilder("fzf", "--version").start()
        val success = process.inputStream.bufferedReader().readText().isNotBlank()
        if (process.waitFor() != 0 || !success)
            throw FzfNotInstalledException()
    }
}