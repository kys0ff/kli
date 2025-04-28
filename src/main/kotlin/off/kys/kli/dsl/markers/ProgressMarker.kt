package off.kys.kli.dsl.markers

/**
 * A custom DSL marker annotation used to mark functions related to progress tracking.
 *
 * This annotation is used to ensure that DSL functions dealing with progress (such as
 * progress bars) are properly scoped and prevent accidental cross-referencing of functions
 * that may otherwise lead to ambiguous or incorrect DSL usage.
 *
 * The annotation applies to functions and prevents the accidental mixing of unrelated DSL
 * constructs by providing a clear boundary for progress-related functionality.
 *
 * **Target**: The annotation can be applied to functions.
 * **Retention**: It is retained in the compiled bytecode.
 * **MustBeDocumented**: This annotation is part of the public API and is intended to be documented.
 */
@DslMarker
@Target(AnnotationTarget.FUNCTION)
@Retention
@MustBeDocumented
internal annotation class ProgressMarker
