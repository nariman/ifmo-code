/*
 * Nariman Safiulin (woofilee)
 * File: Chain.kt
 */

/**
 * Abstract expressions immutable list
 */
abstract class Chain(list: List<String>) {
    val list = list.map { Parser.parse(it) }
}

/**
 * Singleton with list of axioms
 */
object Axioms : Chain(listOf(
        "A -> B -> A",
        "(A -> B) -> (A -> B -> C) -> (A -> C)",
        "A -> B -> A & B",
        "A & B -> A",
        "A & B -> B",
        "A -> A | B",
        "B -> A | B",
        "(A -> Q) -> (B -> Q) -> (A | B -> Q)",
        "(A -> B) -> (A -> !B) -> !A",
        "!!A -> A"
))

val axioms = Axioms
