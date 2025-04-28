package off.kys.kli.dsl.progress

import off.kys.kli.dsl.markers.ProgressMarker
import off.kys.kli.ui.progress.ProgressBar

@ProgressMarker
fun progressBar(config: ProgressBar.() -> Unit): ProgressBar = ProgressBar().apply(config)