package off.kys.kli.dsl.progress

import off.kys.kli.dsl.markers.ProgressMarker
import off.kys.kli.ui.progress.ProgressBar

/**
 * A DSL function that creates and configures a [ProgressBar] instance.
 *
 * This function allows building a progress bar with a specified configuration using a DSL block.
 * The progress bar can be customized and then used to display progress in the command-line interface.
 * The `ProgressMarker` annotation is used to mark the function for progress tracking.
 *
 * @param config A configuration block that applies modifications to the [ProgressBar] instance.
 * @return A [ProgressBar] instance with the specified configuration.
 */
@ProgressMarker
fun progressBar(config: ProgressBar.() -> Unit): ProgressBar = ProgressBar().apply(config)
