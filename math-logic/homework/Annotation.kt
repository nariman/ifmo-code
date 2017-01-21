/*
 * Nariman Safiulin (woofilee)
 * File: Annotation.kt
 */

/**
 * Expression annotations
 */
sealed class Annotation {
    /**
     * Axiom annotation
     *
     * @param axiom axiom expression
     */
    class Axiom(val axiom: Expression) : Annotation()

    /**
     * Modus Ponens annotation, where (lhs) → (annotated expression) ≡ (implication)
     *
     * @param lhs         lhs expression of the implication
     * @param implication implication expression
     */
    class ModusPonens(val lhs: Expression, val implication: Expression) : Annotation()

    /**
     * Assumption annotation
     *
     * @param expression assuming expression
     */
    class Assumption(val expression: Expression) : Annotation()

    class Universal : Annotation()

    class Existential : Annotation()

    /**
     * Mistake annotation
     */
    class Mistake : Annotation()
}
