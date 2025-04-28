package off.kys.kli.dsl.kli

import off.kys.kli.dsl.KliDsl
import off.kys.kli.dsl.markers.KliMarker
import off.kys.kli.utils.KliScope

/**
 * A function to initialize and execute a KLI (Command Line Interface) application.
 *
 * This function sets up a new instance of the KLI DSL (`KliDsl`), applies a configuration block
 * to it, and then executes the KLI with the provided command-line arguments (`args`).
 * It allows easy setup and configuration of the KLI environment, enabling command execution
 * based on the provided arguments.
 *
 * **Parameters**:
 * - `args`: An array of strings representing command-line arguments passed to the KLI. Defaults to an empty array.
 * - `config`: A configuration block that applies custom settings and configurations to the KLI instance.
 *
 * The `KliDsl` instance is created, configured with the provided `config` block, and then executed with
 * the given arguments (`args`).
 *
 * **Usage Example**:
 * ```kotlin
 * kli(args) {
 *     configure {
 *         // Custom KLI configuration
 *     }
 * }
 * ```
 *
 * **Annotation**:
 * - The function is marked with the `@KliMarker` annotation to indicate it is part of the KLI DSL.
 *
 * @param args The command-line arguments to pass to the KLI.
 * @param config The configuration block to configure the KliDsl instance.
 */
@KliMarker
fun kli(
    args: Array<String> = emptyArray(),
    config: KliScope.() -> Unit,
) {
    KliDsl().apply { config(); execute(args) }
}
