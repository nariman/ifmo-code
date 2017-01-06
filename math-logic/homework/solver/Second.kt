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

        val assumptions: List<Expression>
        val alpha: Expression?

        val part =
                if (title[0].isNotEmpty())
                    Parser.many(title[0])
                else
                    emptyList()

        if (part.isNotEmpty()) {
            alpha = part.last()
            assumptions = part.dropLast(1)
        } else {
            alpha = null
            assumptions = part
        }

        val unproven: Expression? =
                if (title[1].isNotEmpty())
                    Parser.single(title[1])
                else
                    null

        out.write("${(assumptions + alpha).map { it.toString() }.joinToString(",")}|-$unproven\n")

        val expressions = HashMap<Int, Expression>()
        val annotations = HashMap<Int, Annotation>()
        val messages = HashMap<Int, String>()

        val fulls = HashMap<String, Int>()
        val rights = HashMap<String, ArrayList<Int>>()

        var counter = 0

        fun save(expression: Expression, annotation: Annotation, message: String) {
            if (expression is Implication)
                rights.getOrPut(expression.rhs.toString(), { ArrayList<Int>() }).add(counter)

            fulls.putIfAbsent(expression.toString(), counter)
            expressions.put(counter, expression)
            annotations.put(counter, annotation)
            messages.put(counter, message)
        }

        fun error(message: String? = null) {
            out.write("Вывод некорректен начиная с формулы номер $counter")
            if (message != null) out.write(": $message")
            out.close()
            System.exit(0)
        }

        `in`.forEachLine { line ->
            counter++
            val expression = Parser.single(line)
            out.write("$expression\n")
        }

//        if (alpha == null) {
//            `in`.forEachLine { line ->
//                counter++
//                val expression = Parser.single(line)
//
//                for (i in 0..axioms.size - 1) {
//                    if (axioms[i] exact expression) {
//                        save(expression, Annotation.Axiom(axioms[i]), "Акс. ${i + 1}")
//                        return@forEachLine
//                    }
//                }
//
//                for (i in 0..axiom_schemes.size - 1) {
//                    if (axiom_schemes[i] match expression) {
//                        save(expression, Annotation.Axiom(axiom_schemes[i]), "Сх. акс. ${i + 1}")
//                        return@forEachLine
//                    }
//                }
//
//                // We don't have any assumptions
//                /*
//                for (i in 0..assumptions.size - 1) {
//                    if (assumptions[i] exact expression) {
//                        save(expression, Annotation.Assumption(assumptions[i]), "Предп. ${i + 1}")
//                        return@forEachLine
//                    }
//                }
//                */
//
//                if (expression is Implication && expression.lhs is Conjunction) {
//                    val and = expression.lhs
//
//                    if (and.rhs is Quantifier) {
//                        val q = and.rhs
//                        if (q is Universal && q.statement is Implication) {
//                            val right = expression.rhs
//
//                            if (q.statement.lhs exact right) {
//                                try {
//                                    val zero = right.replace(q.variable, and.lhs)
//                                    if (zero.equals(Variable("0")) && right.replace(q.variable, q.statement.rhs).equals(Increment(q.variable))) {
//                                        save(expression, Annotation.Axiom(induction_axiom_scheme), "Сх. акс.")
//                                        return@forEachLine
//                                    }
//                                } catch (e: IllegalArgumentException) {}
//
//                            }
//                        }
//                    }
//                }
//
//                rights[expression.toString()]?.forEach { j ->
//                    fulls[(expressions[j] as Implication).lhs.toString()]?.let { i ->
//                        save(expression, Annotation.ModusPonens(expressions[i]!!, expressions[j]!!),
//                                "M.P. $i, $j")
//                        return@forEachLine
//                    }
//                }
//
//                if (universal_axiom_scheme match expression) {
//                    save(expression, Annotation.Axiom(universal_axiom_scheme), "Сх акс. 11")
//                    return@forEachLine
//                }
//
//                if (existence_axiom_scheme match expression) {
//                    save(expression, Annotation.Axiom(existence_axiom_scheme), "Сх акс. 12")
//                    return@forEachLine
//                }
//
//                save(expression, Annotation.Mistake(), "Не доказано")
//                error()
//            }
//
//            out.write("|-$unproven\n")
//            for ((c, expression) in expressions) {
//                out.write("$expression\n")
//            }
//        } else {
//            val sb = StringBuilder()
//
//            `in`.forEachLine { line ->
//                counter++
//                val expression = Parser.single(line)
//
//                for (i in 0..axioms.size - 1) {
//                    if (axioms[i] exact expression) {
//                        save(expression, Annotation.Axiom(axioms[i]), "Акс. ${i + 1}")
//                        out.write("$expression\n")
//                        out.write("$expression->$alpha->$expression\n")
//                        out.write("$alpha->$expression\n")
//                        return@forEachLine
//                    }
//                }
//
//                for (i in 0..axiom_schemes.size - 1) {
//                    if (axiom_schemes[i] match expression) {
//                        save(expression, Annotation.Axiom(axiom_schemes[i]), "Сх. акс. ${i + 1}")
//                        out.write("$expression\n")
//                        out.write("$expression->$alpha->$expression\n")
//                        out.write("$alpha->$expression\n")
//                        return@forEachLine
//                    }
//                }
//
//                for (i in 0..assumptions.size - 1) {
//                    if (assumptions[i] exact expression) {
//                        save(expression, Annotation.Assumption(assumptions[i]), "Предп. ${i + 1}")
//                        out.write("$expression\n")
//                        out.write("$expression->$alpha->$expression\n")
//                        out.write("$alpha->$expression\n")
//                        return@forEachLine
//                    }
//                }
//
//                if (alpha exact expression) {
//                    save(expression, Annotation.Assumption(alpha), "Предп.")
//                    out.write("$alpha->$alpha->$alpha\n")
//                    out.write("($alpha->$alpha->$alpha)->($alpha->($alpha->$alpha)->$alpha)->$alpha->$alpha\n")
//                    out.write("($alpha->($alpha->$alpha)->$alpha)->$alpha->$alpha\n")
//                    out.write("($alpha->($alpha->$alpha)->$alpha)\n")
//                    out.write("$alpha->$alpha\n")
//                    return@forEachLine
//                }
//
//                if (expression is Implication && expression.lhs is Conjunction) {
//                    val and = expression.lhs
//
//                    if (and.rhs is Quantifier) {
//                        val q = and.rhs
//                        if (q is Universal && q.statement is Implication) {
//                            val right = expression.rhs
//
//                            if (q.statement.lhs exact right) {
//                                try {
//                                    val zero = right.replace(q.variable, and.lhs)
//                                    if (zero.equals(Constant(0)) && right.replace(q.variable, q.statement.rhs).equals(Increment(q.variable))) {
//                                        save(expression, Annotation.Axiom(induction_axiom_scheme), "Сх. акс.")
//                                        out.write("$expression\n")
//                                        out.write("$expression->$alpha->$expression\n")
//                                        out.write("$alpha->$expression\n")
//                                        return@forEachLine
//                                    }
//                                } catch (e: IllegalArgumentException) {}
//
//                            }
//                        }
//                    }
//                }
//
//                rights[expression.toString()]?.forEach { j ->
//                    fulls[(expressions[j] as Implication).lhs.toString()]?.let { i ->
//                        save(expression, Annotation.ModusPonens(expressions[i]!!, expressions[j]!!),
//                                "M.P. $i, $j")
//                        val f = expressions[j] as Implication
//
//                        out.write("($alpha->${f.lhs})->($alpha->$f)->$alpha->${f.rhs}\n")
//                        out.write("($alpha->$f)->$alpha->${f.rhs}\n")
//                        out.write("$alpha->${f.rhs}\n")
//                        return@forEachLine
//                    }
//                }
//
//                if (universal_axiom_scheme match expression) {
//                    save(expression, Annotation.Axiom(universal_axiom_scheme), "Сх акс. 11")
//                    return@forEachLine
//                }
//
//                if (existence_axiom_scheme match expression) {
//                    save(expression, Annotation.Axiom(existence_axiom_scheme), "Сх акс. 12")
//                    return@forEachLine
//                }
//
//                save(expression, Annotation.Mistake(), "Не доказано")
//                error()
//            }
//
//            out.write("|-$unproven\n")
//            for ((counter, expression) in expressions) {
//                out.write("($counter) $expression (${messages[counter]})\n")
//            }
//        }
    }
}
