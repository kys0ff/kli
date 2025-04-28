package off.kys.kli.utils

import off.kys.kli.dsl.KliDsl

/**
 * Represents the scope for KLI DSL operations.
 *
 * `KliScope` is simply an alias for [KliDsl],
 * allowing easier and cleaner naming across the internal API
 * when writing extension functions and interactive commands.
 *
 * @see off.kys.kli.dsl.KliDsl
 */
typealias KliScope = KliDsl
