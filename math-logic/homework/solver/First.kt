/*
 * Nariman Safiulin (woofilee)
 * File: First.kt
 */

import java.io.BufferedReader
import java.io.BufferedWriter
import java.util.*

/**
 * Solver for the first problem in a homework
 */
object First : Solver {
    /**
     * Singleton for expression parsing
     */
    private object P : Parser() {
        /**
         * Parses expression from the string
         *
         * @param  expression expression to parse
         * @param  l          left bound of parsing
         * @param  r          right bound of parsing
         * @return            parsed expression
         */
        override fun parse(expression: String, l: Int, r: Int): Expression {
            var weight: Int = 0

            /**
             * Checks the brackets balance and corrects this value if necessary
             */
            fun balance(pos: Int): Int = when (expression[pos]) {
                '(' -> weight++
                ')' -> weight--
                else -> weight
            }

            // Implication
            (l..r).forEach { pos ->
                if (weight == 0 && expression[pos] == '>')
                    return parse(expression, l, pos - 2) impl parse(expression, pos + 1, r)
                balance(pos)
            }

            // Disjunction
            (r downTo l).forEach { pos ->
                if (weight == 0 && expression[pos] == '|')
                    return parse(expression, l, pos - 1) disj parse(expression, pos + 1, r)
                balance(pos)
            }

            // Conjunction
            (r downTo l).forEach { pos ->
                if (weight == 0 && expression[pos] == '&')
                    return parse(expression, l, pos - 1) conj parse(expression, pos + 1, r)
                balance(pos)
            }

            // Negation
            if (expression[l] == '!')
                return !parse(expression, l + 1, r)

            // Bracket expression
            if (expression[l] == '(')
                return parse(expression, l + 1, r - 1)

            // Variable
            return Variable(expression.substring(l..r))
        }
    }

    val axioms = listOf(
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
    ).map { P.parse(it) }

    override fun solve(`in`: BufferedReader, out: BufferedWriter) {
        val title: List<String> = Utils.clean(`in`.readLine()).split("|-")

        val hypotheses = (
                if (title[0].isNotEmpty())
                    title[0].split(",")
                else
                    emptyList()
                ).map { P.parse(it) }

        val unproven: Expression? =
                if (title[1].isNotEmpty())
                    P.parse(title[1])
                else
                    null

        out.write("${hypotheses.map { it.toString() }.joinToString(",")}|-$unproven\n")

        val expressions = HashMap<Int, Expression>()
        val fulls = HashMap<String, Int>()
        val rights = HashMap<String, ArrayList<Int>>()
        var counter = 0

        fun save(message: String, expression: Expression) {
            out.write("($message)\n")

            if (expression is Implication)
                rights.getOrPut(expression.rhs.toString(), { ArrayList<Int>() }).add(counter)

            fulls.putIfAbsent(expression.toString(), counter)
            expressions.put(counter, expression)
        }

        `in`.forEachLine { line ->
            counter++
            val expression = P.parse(line)

            out.write("($counter)")
            out.write(" $expression ")

            for (i in 1..axioms.size) {
                if (axioms[i - 1] match expression) {
                    save("Сх. акс. $i", expression)
                    return@forEachLine
                }
            }

            for (i in 1..hypotheses.size) {
                if (hypotheses[i - 1] equal expression) {
                    save("Предп. $i", expression)
                    return@forEachLine
                }
            }

            rights[expression.toString()]?.forEach { j ->
                fulls[(expressions[j] as Implication).lhs.toString()]?.let { i ->
                    save("M.P. $i, $j", expression)
                    return@forEachLine
                }
            }

            save("Не доказано", expression)
        }
    }
}
