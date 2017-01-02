/*
 * Nariman Safiulin (woofilee)
 * File: Second.kt
 */

import java.io.BufferedReader
import java.io.BufferedWriter

/**
 * Solver for the second problem in a homework
 */
object Second : Solver {
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
            for (pos in l..r) {
                if (weight == 0 && expression[pos] == '>')
                    return parse(expression, l, pos - 2) impl parse(expression, pos + 1, r)
                balance(pos)
            }

            // Disjunction
            for (pos in r downTo l) {
                if (weight == 0 && expression[pos] == '|')
                    return parse(expression, l, pos - 1) disj parse(expression, pos + 1, r)
                balance(pos)
            }

            // Conjunction
            for (pos in r downTo l) {
                if (weight == 0 && expression[pos] == '&')
                    return parse(expression, l, pos - 1) conj parse(expression, pos + 1, r)
                balance(pos)
            }

            // Negation
            if (expression[l] == '!')
                return !parse(expression, l + 1, r)

            // Universal or Existential quantifier
            if (expression[l] == '@' || expression[l] == '?') {
                var m = l + 2
                while (Utils.isDigit(expression[m])) m++

                // Universal quantifier
                if (expression[l] == '@')
                    return Variable(expression.substring(l + 1..m - 1)) all parse(expression, m, r)

                // Existential quantifier
                return Variable(expression.substring(l + 1..m - 1)) exists parse(expression, m, r)
            }

            // Predicate
            if ('A' <= expression[l] && expression[l] <= 'Z') {
                var m = l + 1
                while (m <= r && Utils.isDigit(expression[m])) m++

                if (m < r)
                    return Predicate(expression.substring(l..m - 1), *(expression
                            .substring(m + 1..r - 1)
                            .split(",")
                            .map { single(it) }
                            .toTypedArray()))
                else
                    return Predicate(expression.substring(l..m - 1))
            }

            // Equals predicate
            for (pos in r downTo l) {
                if (weight == 0 && expression[pos] == '=')
                    return parse(expression, l, pos - 1) equals parse(expression, pos + 1, r)
                balance(pos)
            }

            // Addition binary operator
            for (pos in r downTo l) {
                if (weight == 0 && expression[pos] == '+')
                    return parse(expression, l, pos - 1) + parse(expression, pos + 1, r)
                balance(pos)
            }

            // Multiplication binary operator
            for (pos in r downTo l) {
                if (weight == 0 && expression[pos] == '*')
                    return parse(expression, l, pos - 1) * parse(expression, pos + 1, r)
                balance(pos)
            }

            // Function
            if ('a' <= expression[l] && expression[l] <= 'z') {
                var m = l + 1
                while (m <= r && Utils.isDigit(expression[m])) m++

                if (m < r)
                    return Function(expression.substring(l..m - 1), *(expression
                            .substring(m + 1..r - 1)
                            .split(",")
                            .map { single(it) }
                            .toTypedArray()))
                else
                    return Function(expression.substring(l..m - 1))
            }

            // Constant
            if (expression[r] == '\'')
                return parse(expression, l, r - 1) + Constant(1)

            // Bracket expression
            if (expression[l] == '(')
                return parse(expression, l + 1, r - 1)

            // Constant
            if (expression[l] == '0')
                return Constant(0)

            // Variable // Anyway
            return Variable(expression.substring(l..r))
        }
    }

    override fun solve(`in`: BufferedReader, out: BufferedWriter) {
        val title: List<String> = Utils.clean(`in`.readLine()).split("|-")

        val hypotheses =
                if (title[0].isNotEmpty())
                    P.many(title[0])
                else
                    emptyList()

        val unproven: Expression? =
                if (title[1].isNotEmpty())
                    P.single(title[1])
                else
                    null

        out.write("${hypotheses.map { it.toString() }.joinToString(",")}|-$unproven\n")

        var counter = 0

        `in`.forEachLine { line ->
            counter++
            val expression = P.single(line)

//            out.write("($counter)")
            out.write("$expression\n")
        }
    }
}
