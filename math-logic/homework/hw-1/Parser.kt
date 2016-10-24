/**
 * Nariman Safiulin (woofilee)
 * File: Parser.kt
 * Created on: 24 Oct, 2016
 */

/**
 * Singleton for expression parsing
 */
object Parser {
    val whitespace = Regex("\\s")

    /**
     * Parses expression from the string
     *
     * @param expression expression to parse
     * @return           parsed expression
     */
    fun parse(expression: String): Expression =
            expression.replace(whitespace, "").let {
                parse(it, 0, it.length - 1)
            }

    /**
     * Parses expression from the string
     *
     * @param expression expression to parse
     * @param l          left bound of parsing
     * @param r          right bound of parsing
     * @return           parsed expression
     */
    private fun parse(expression: String, l: Int, r: Int): Expression {
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
