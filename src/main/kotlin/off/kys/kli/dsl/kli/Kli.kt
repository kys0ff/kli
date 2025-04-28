package off.kys.kli.dsl.kli

import off.kys.kli.dsl.KliDsl
import off.kys.kli.dsl.markers.KliMarker
import off.kys.kli.utils.KliScope

@KliMarker
fun kli(
    args: Array<String> = emptyArray(),
    config: KliScope.() -> Unit,
) {
    KliDsl().apply { config(); execute(args) }
}