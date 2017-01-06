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
            deduction.add("$expression")
            deduction.add("$expression->$alpha->$expression")
            deduction.add("$alpha->$expression")
        }
        fun deduct_axiom_proof() {
            deduction.add("$alpha->$alpha->$alpha")
            deduction.add("($alpha->$alpha->$alpha)->($alpha->($alpha->$alpha)->$alpha)->$alpha->$alpha")
            deduction.add("($alpha->($alpha->$alpha)->$alpha)->$alpha->$alpha")
            deduction.add("($alpha->($alpha->$alpha)->$alpha)")
            deduction.add("$alpha->$alpha")
        }
        fun deduct_mp(expression: Expression) {
            expression as Implication
            deduction.add("($alpha->${expression.lhs})->($alpha->$expression)->$alpha->${expression.rhs}")
            deduction.add("($alpha->$expression)->$alpha->${expression.rhs}")
            deduction.add("$alpha->${expression.rhs}")
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
            } else if (e1 is Variable && e1 == x) {
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
                    if (alpha != null) deduct_axiom(expression)
                    continue@loop
                }
            }

            for (i in 0..axiom_schemes.size - 1) {
                if (axiom_schemes[i] match expression) {
                    save(expression, Annotation.Axiom(axiom_schemes[i]), "Сх. акс. ${i + 1}")
                    if (alpha != null) deduct_axiom(expression)
                    continue@loop
                }
            }

            for (i in 0..assumptions.size - 1) {
                if (assumptions[i] exact expression) {
                    save(expression, Annotation.Assumption(assumptions[i]), "Предп. ${i + 1}")
                    if (alpha != null) deduct_axiom(expression)
                    continue@loop
                }
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
                    deduct_mp(expressions[j]!!)
                    continue@loop
                }

            if (induction_axiom_scheme match expression) {
                expression as Implication
                expression.lhs as Conjunction
                expression.lhs.rhs as Universal
                expression.lhs.rhs.statement as Implication

                val phi = expression.rhs
                val x = expression.lhs.rhs.variable

                try {
                    if (phi.replace(x, Constant(0)) exact expression.lhs.lhs &&
                            phi.replace(x, Increment(x)) exact expression.lhs.rhs.statement.rhs) {
                        save(expression, Annotation.Axiom(induction_axiom_scheme), "Сх. акс.")
                        if (alpha != null) deduct_axiom(expression)
                        continue@loop
                    }
                } catch (e: IllegalArgumentException) {} // Possible next variants
            }

            if (alpha != null && alpha exact expression) {
                save(expression, Annotation.Assumption(alpha), "Предп.")
                deduct_axiom_proof()
                continue@loop
            }

            if (universal_axiom_scheme match expression) {
                expression as Implication
                expression.lhs as Universal

                val phi = expression.lhs.statement
                val x = expression.lhs.variable
                val term = mismatch(phi, expression.rhs, x)!! as Math

                try {
                    if (phi.replace(x, term) exact expression.rhs) {
                        save(expression, Annotation.Axiom(universal_axiom_scheme), "Сх акс. 11")
                        if (alpha != null) deduct_axiom(expression)
                        continue@loop
                    }
                } catch (e: IllegalArgumentException) {
                    save(
                            expression,
                            Annotation.Mistake(),
                            "Терм $term не свободен для подстановки в формулу $phi вместо $x"
                    )
                    break@loop
                }
            }

            if (existence_axiom_scheme match expression) {
                expression as Implication
                expression.rhs as Existential

                val phi = expression.rhs.statement
                val x = expression.rhs.variable
                val term = mismatch(phi, expression.lhs, x)!! as Math

                try {
                    if (phi.replace(x, term) exact expression.lhs) {
                        save(expression, Annotation.Axiom(existence_axiom_scheme), "Сх акс. 12")
                        if (alpha != null) deduct_axiom(expression)
                        continue@loop
                    }
                } catch (e: IllegalArgumentException) {
                    save(
                            expression,
                            Annotation.Mistake(),
                            "Терм $term не свободен для подстановки в формулу $phi вместо $x"
                    )
                    break@loop
                }
            }

            save(expression, Annotation.Mistake(), "Не доказано")
            break@loop
        }

        if (annotations[counter] is Annotation.Mistake) {
            out.write("Вывод неверен начиная с формулы номер $counter: ")
            out.write(messages[counter])
        } else {
            if (alpha != null) {
                out.write("${assumptions.joinToString(",")}|-${Implication(alpha, unproven)}\n")
                for (expression in deduction) out.write("$expression\n")
            }
            else {
                out.write("|-$unproven\n") // If alpha is null, then assumptions list is empty
                for ((c, expression) in expressions) out.write("$expression\n")
            }
        }
    }
}
