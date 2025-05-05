package off.kys.kli.fzf.dsl.fzf

import off.kys.kli.fzf.dsl.FzfDsl
import off.kys.kli.fzf.dsl.markers.FzfMarker

@FzfMarker
fun fzf(block: FzfDsl.() -> Unit): List<String> = FzfDsl().apply(block).execute()

@FzfMarker
fun List<String>.fzfSelect(block: FzfDsl.() -> Unit = {}): List<String> = fzf {
    items(this@fzfSelect)
    block()
}

@FzfMarker
fun List<String>.fzfSingleSelect(block: FzfDsl.() -> Unit = {}): String? = fzf {
    items(this@fzfSingleSelect)
    block()
    multiSelect = false
}.firstOrNull()