/*
 * Nariman Safiulin (woofilee)
 * File: Second.kt
 */

import java.io.BufferedReader
import java.io.BufferedWriter
import java.util.*

/**
 * Solver for the second problem in a homework
 */
object Second : Solver {
    const val NOT_PROVED = "Не доказано"

    override fun solve(`in`: BufferedReader, out: BufferedWriter) {
        val title: List<String> = Utils.clean(`in`.readLine()).split("|-")

        val assumptions: List<Logic>
        val alpha: Logic?

        val _part: List<Logic> =
                if (title[0].isNotEmpty())
                    Parser.many(title[0]).map { it as Logic }
                else
                    emptyList<Logic>()

        if (_part.isNotEmpty()) {
            alpha = _part.last()
            assumptions = _part.dropLast(1)
        } else {
            alpha = null
            assumptions = _part
        }

        val unproven = Parser.single(title[1]) as Logic

        val expressions = HashMap<Int, Expression>()
        val annotations = HashMap<Int, Annotation>()
        val messages = HashMap<Int, String>()

        val fulls = HashMap<String, Int>()
        val rights = HashMap<String, ArrayList<Int>>()

        val deduction = ArrayList<String>()

        var counter = 0

        fun save(expression: Expression, annotation: Annotation, message: String) {
            if (expression is Implication)
                rights.getOrPut(expression.rhs.toString(), { ArrayList<Int>() }).add(counter)

            fulls.putIfAbsent(expression.toString(), counter)
            expressions.put(counter, expression)
            annotations.put(counter, annotation)
            messages.put(counter, message)
        }

        fun deduct_axiom(expression: Expression) {
            if (alpha != null) {
                val map = mapOf<Expression, Expression>(
                        Predicate("A") to expression,
                        Predicate("B") to alpha
                )

                AXIOM_EXPRESSIONS.forEach {
                    deduction.add(it.replace(map).toString())
                }
            }
        }
        
        fun deduct_axiom_proof() {
            if (alpha != null) {
                val map = mapOf<Expression, Expression>(
                        Predicate("A") to alpha
                )

                AXIOM_PROOF_EXPRESSIONS.forEach {
                    deduction.add(it.replace(map).toString())
                }
            }
        }
        
        fun deduct_mp(expression: Implication) {
            if (alpha != null) {
                val map = mapOf<Expression, Expression>(
                        Predicate("A") to alpha,
                        Predicate("B") to expression,
                        Predicate("C") to expression.lhs,
                        Predicate("D") to expression.rhs
                )

                MP_EXPRESSIONS.forEach {
                    deduction.add(it.replace(map).toString())
                }
            }
        }

        fun deduct_universal(b: Expression, c: Expression, x: Expression) {
            if (alpha != null) {
                val map = mapOf(
                        Predicate("A") to alpha,
                        Predicate("B") to b,
                        Predicate("C") to c,
                        Variable("x") to x
                )

                UNIVERSAL_RULE.forEach {
                    deduction.add(it.replace(map).toString())
                }
            }
        }

        fun deduct_existential(b: Expression, c: Expression, x: Expression) {
            if (alpha != null) {
                val map = mapOf(
                        Predicate("A") to alpha,
                        Predicate("B") to b,
                        Predicate("C") to c,
                        Variable("x") to x
                )

                EXISTENTIAL_RULE.forEach {
                    deduction.add(it.replace(map).toString())
                }
            }
        }

        fun mismatch(e1: Expression, e2: Expression, x: Variable): Expression? {
            if (e1 is Parameterized && e1.javaClass == e2.javaClass) {
                e2 as Parameterized
                var m: Expression? = null

                for ((index, argument) in e1.arguments.withIndex()) {
                    if (m != null) break
                    m = mismatch(argument, e2.arguments[index], x)
                }

                return m
            } else if (e1 is Quantifier && e1.javaClass == e2.javaClass) {
                return mismatch(e1.statement, (e2 as Quantifier).statement, x)
            } else if (e1 is Variable && e1 exact x) {
                return e2
            }

            return null
        }

        loop@ for (line in `in`.lines()) {
            counter++
            val expression = Parser.single(line)

            for (i in 0..axioms.size - 1) {
                if (axioms[i] exact expression) {
                    save(expression, Annotation.Axiom(axioms[i]), "Акс. ${i + 1}")
                    deduct_axiom(expression)

                    continue@loop
                }
            }

            for (i in 0..axiom_schemes.size - 1) {
                if (axiom_schemes[i] match expression) {
                    save(expression, Annotation.Axiom(axiom_schemes[i]), "Сх. акс. ${i + 1}")
                    deduct_axiom(expression)

                    continue@loop
                }
            }

            for (i in 0..assumptions.size - 1) {
                if (assumptions[i] exact expression) {
                    save(expression, Annotation.Assumption(assumptions[i]), "Предп. ${i + 1}")
                    deduct_axiom(expression)

                    continue@loop
                }
            }

            if (alpha != null && alpha exact expression) {
                save(expression, Annotation.Assumption(alpha), "Предп.")
                deduct_axiom_proof()

                continue@loop
            }

            for (j in rights[expression.toString()] ?: emptyList<ArrayList<Int>>())
                if (fulls[(expressions[j] as Implication).lhs.toString()] != null) {
                    save(
                            expression,
                            Annotation.ModusPonens(
                                    expressions[fulls[(expressions[j] as Implication).lhs.toString()]]!!,
                                    expressions[j]!!
                            ),
                            "M.P. ${fulls[(expressions[j] as Implication).lhs.toString()]}, $j"
                    )

                    deduct_mp(expressions[j] as Implication)

                    continue@loop
                }

            if (induction_axiom_scheme match expression) {
                expression as Implication
                expression.lhs as Conjunction
                expression.lhs.rhs as Universal
                expression.lhs.rhs.statement as Implication

                val f = expression.rhs
                val x = expression.lhs.rhs.variable

                try {
                    if (f.substitute(x, Constant(0)) exact expression.lhs.lhs &&
                            f.substitute(x, Increment(x)) exact expression.lhs.rhs.statement.rhs) {
                        save(expression, Annotation.Axiom(induction_axiom_scheme), "Сх. акс.")
                        deduct_axiom(expression)

                        continue@loop
                    }
                } catch (e: IllegalArgumentException) {} // Possible next variants
            }

            if (universal_axiom_scheme match expression) {
                expression as Implication
                expression.lhs as Universal

                val f = expression.lhs.statement
                val x = expression.lhs.variable
                val t = mismatch(f, expression.rhs, x) as? Math

                try {
                    if ((if (t == null) f else f.substitute(x, t)) exact expression.rhs) {
                        save(expression, Annotation.Axiom(universal_axiom_scheme), "Сх. акс. 11")
                        deduct_axiom(expression)

                        continue@loop
                    }
                } catch (e: IllegalArgumentException) {
                    save(
                            expression,
                            Annotation.Mistake(),
                            "Терм $t не свободен для подстановки в формулу $f вместо $x"
                    )

                    break@loop
                }
            }

            if (existential_axiom_scheme match expression) {
                expression as Implication
                expression.rhs as Existential

                val f = expression.rhs.statement
                val x = expression.rhs.variable
                val t = mismatch(f, expression.lhs, x) as? Math

                try {
                    if ((if (t == null) f else f.substitute(x, t)) exact expression.lhs) {
                        save(expression, Annotation.Axiom(existential_axiom_scheme), "Сх. акс. 12")
                        deduct_axiom(expression)

                        continue@loop
                    }
                } catch (e: IllegalArgumentException) {
                    save(
                            expression,
                            Annotation.Mistake(),
                            "Терм $t не свободен для подстановки в формулу $f вместо $x"
                    )

                    break@loop
                }
            }

            if (expression is Implication && expression.rhs is Universal) {
                val x = expression.rhs.variable
                var free: Boolean

                try {
                    free = !(expression.lhs.substitute(x, Constant(0)) exact expression.lhs)
                } catch (e: IllegalArgumentException) {
                    free = false
                }

                if (fulls[(expression.lhs impl expression.rhs.statement).toString()] != null &&
                        !free) {
                    if (alpha != null) {
                        if (alpha.substitute(x, Constant(0)) exact alpha) {
                            save(expression, Annotation.Universal(), "Universal rule")
                            deduct_universal(expression.lhs, expression.rhs.statement, x)

                            continue@loop
                        } else {
                            save(
                                    expression,
                                    Annotation.Mistake(),
                                    "Используется правило с квантором по переменной $x,\n" +
                                            "входящей свободно в допущение $alpha"
                            )

                            break@loop
                        }
                    } else {
                        save(expression, Annotation.Universal(), "Universal rule")
                        continue@loop
                    }
                } else if (free) {
                    save(
                            expression,
                            Annotation.Mistake(),
                            "Переменная $x входит свободно в формулу ${expression.lhs}"
                    )
                    break@loop
                }
            }

            if (expression is Implication && expression.lhs is Existential) {
                val x = expression.lhs.variable
                var free: Boolean

                try {
                    free = !(expression.rhs.substitute(x, Constant(0)) exact expression.rhs)
                } catch (e: IllegalArgumentException) {
                    free = false
                }

                if (fulls[(expression.lhs.statement impl expression.rhs).toString()] != null &&
                        !free) {
                    if (alpha != null) {
                        if (alpha.substitute(x, Constant(0)) exact alpha) {
                            save(expression, Annotation.Existential(), "Existential rule")
                            deduct_existential(expression.lhs.statement, expression.rhs, x)

                            continue@loop
                        } else {
                            save(
                                    expression,
                                    Annotation.Mistake(),
                                    "Используется правило с квантором по переменной $x,\n" +
                                            "входящей свободно в допущение $alpha"
                            )

                            break@loop
                        }
                    } else {
                        save(expression, Annotation.Existential(), "Existential rule")
                        continue@loop
                    }
                } else if (free) {
                    save(
                            expression,
                            Annotation.Mistake(),
                            "Переменная $x входит свободно в формулу ${expression.rhs}"
                    )

                    break@loop
                }
            }

            save(expression, Annotation.Mistake(), NOT_PROVED)
            break@loop
        }

        if (annotations[counter] is Annotation.Mistake) {
            out.write("Вывод неверен начиная с формулы номер $counter")

            if (messages[counter] != NOT_PROVED)
                out.write(": ${messages[counter]}")
        } else {
            if (alpha != null) {
                out.write(assumptions.map { it.stringify() }.joinToString(","))
                out.write("|-${alpha impl unproven}\n")

                for (expression in deduction)
                    out.write("$expression\n")
            }
            else {
                out.write("|-$unproven\n") // If alpha is null, then assumptions list is empty

                for (expression in expressions.values)
                    out.write("$expression\n")
            }
        }
    }

    override fun check(ans: BufferedReader, out: BufferedReader) {
        throw UnsupportedOperationException("Not implemented")
    }

    val AXIOM_EXPRESSIONS = listOf(
            "A",
            "A -> B -> A",
            "B -> A"
    ).map { Parser.single(it) }

    val AXIOM_PROOF_EXPRESSIONS = listOf(
            "A -> A -> A",
            "(A -> A -> A) -> (A -> (A -> A) -> A) -> A -> A",
            "(A -> (A -> A) -> A) -> A -> A",
            "(A -> (A -> A) -> A)",
            "A -> A"
    ).map { Parser.single(it) }

    val MP_EXPRESSIONS = listOf(
            "(A -> C) -> (A -> B) -> (A -> D)",
            "(A -> B) -> (A -> D)",
            "A -> D"
    ).map { Parser.single(it) }

    val UNIVERSAL_RULE = listOf(
            "A->B->C",
            "A&B->A",
            "A&B->B",
            "A->B->C",
            "(A->B->C)->A&B->A->B->C",
            "A&B->A->B->C",
            "(A&B->A)->(A&B->A->B->C)->A&B->B->C",
            "(A&B->A->B->C)->A&B->B->C",
            "A&B->B->C",
            "(A&B->B)->(A&B->B->C)->(A&B->C)",
            "(A&B->B->C)->(A&B->C)",
            "A&B->C",
            "A&B->@xC",
            "A->B->A&B",
            "A&B->@xC",
            "(A&B->@xC)->A->A&B->@xC",
            "A->A&B->@xC",
            "(A&B->@xC)->B->A&B->@xC",
            "((A&B->@xC)->B->A&B->@xC)->A->(A&B->@xC)->B->A&B->@xC",
            "A->(A&B->@xC)->B->A&B->@xC",
            "(A->A&B->@xC)->(A->((A&B->@xC)->B->A&B->@xC))->A->B->A&B->@xC",
            "(A->((A&B->@xC)->B->A&B->@xC))->A->B->A&B->@xC",
            "A->B->A&B->@xC",
            "(B->A&B)->(B->A&B->@xC)->B->@xC",
            "((B->A&B)->(B->A&B->@xC)->B->@xC)->A->(B->A&B)->(B->A&B->@xC)->B->@xC",
            "A->(B->A&B)->(B->A&B->@xC)->B->@xC",
            "(A->B->A&B)->(A->(B->A&B)->(B->A&B->@xC)->B->@xC)->A->(B->A&B->@xC)->B->@xC",
            "(A->(B->A&B)->(B->A&B->@xC)->B->@xC)->A->(B->A&B->@xC)->B->@xC",
            "A->(B->A&B->@xC)->B->@xC",
            "(A->B->A&B->@xC)->(A->(B->A&B->@xC)->B->@xC)->A->B->@xC",
            "(A->(B->A&B->@xC)->B->@xC)->A->B->@xC",
            "A->B->@xC"
    ).map { Parser.single(it) }

    val EXISTENTIAL_RULE = listOf(
            "B->A->B",
            "A->B->C",
            "(A->B->C)->B->A->B->C",
            "B->A->B->C",
            "(A->B)->(A->B->C)->A->C",
            "((A->B)->(A->B->C)->A->C)->B->(A->B)->(A->B->C)->A->C",
            "B->(A->B)->(A->B->C)->A->C",
            "(B->A->B)->(B->(A->B)->(A->B->C)->A->C)->B->(A->B->C)->A->C",
            "(B->(A->B)->(A->B->C)->A->C)->B->(A->B->C)->A->C",
            "B->(A->B->C)->A->C",
            "(B->A->B->C)->(B->(A->B->C)->A->C)->B->A->C",
            "(B->(A->B->C)->A->C)->B->A->C",
            "B->A->C",
            "?xB->A->C",
            "A->?xB->A",
            "?xB->A->C",
            "(?xB->A->C)->A->?xB->A->C",
            "A->?xB->A->C",
            "(?xB->A)->(?xB->A->C)->?xB->C",
            "((?xB->A)->(?xB->A->C)->?xB->C)->A->(?xB->A)->(?xB->A->C)->?xB->C",
            "A->(?xB->A)->(?xB->A->C)->?xB->C",
            "(A->?xB->A)->(A->(?xB->A)->(?xB->A->C)->?xB->C)->A->(?xB->A->C)->?xB->C",
            "(A->(?xB->A)->(?xB->A->C)->?xB->C)->A->(?xB->A->C)->?xB->C",
            "A->(?xB->A->C)->?xB->C",
            "(A->?xB->A->C)->(A->(?xB->A->C)->?xB->C)->A->?xB->C",
            "(A->(?xB->A->C)->?xB->C)->A->?xB->C",
            "A->?xB->C"
    ).map { Parser.single(it) }
}
