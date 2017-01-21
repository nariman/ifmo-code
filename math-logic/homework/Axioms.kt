/*
 * Nariman Safiulin (woofilee)
 * File: Axiom.kt
 */

val axiom_schemes = listOf(
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
).map { Parser.single(it) }

val axioms = listOf(
        "a = b -> a' = b'",
        "a = b -> a = c -> b = c",
        "a' = b' -> a = b",
        "!a' = 0",
        "a + b' = (a + b)'",
        "a + 0 = a",
        "a * 0 = a",
        "a * b' = a * b + a"
).map { Parser.single(it) }

val universal_axiom_scheme = Parser.single("@xF(x)->F(0)")
val existential_axiom_scheme = Parser.single("F(0)->?xF(x)")
val induction_axiom_scheme = Parser.single("F(0)&@x(F(x)->F(x'))->F(x)")
