package off.kys.kli.dsl.markers

/**
 * A custom DSL marker annotation used to mark functions related to the KLI (Command Line Interface) DSL.
 *
 * This annotation is applied to functions that are part of the KLI DSL (like commands, options, or input parsing).
 * It ensures that functions within the KLI DSL are properly scoped, preventing accidental cross-referencing of functions
 * that belong to different DSLs or unrelated code blocks.
 *
 * The annotation enforces a clean separation between the KLI DSL components and other DSL constructs to avoid
 * ambiguity or misuse of function calls in the DSL.
 *
 * **Target**: The annotation is intended for functions within the DSL.
 * **Retention**: The annotation is retained in the compiled bytecode.
 * **MustBeDocumented**: This annotation is part of the public API and should be included in the documentation.
 */
@DslMarker
@Target(AnnotationTarget.FUNCTION)
@Retention
@MustBeDocumented
internal annotation class KliMarker
