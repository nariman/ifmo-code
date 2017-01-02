/*
 * Nariman Safiulin (woofilee)
 * File: Parser.kt
 */

import java.util.*

/**
 * Abstract class for expression parsers
 */
abstract class Parser {
    /**
     * Parses expression from the string
     *
     * @param  expression expression to parse
     * @return            parsed expression
     */
    fun single(expression: String): Expression =
            Utils.clean(expression).let {
                parse(it, 0, it.length - 1)
            }

    /**
     * Parses expressions from the string
     *
     * @param  expressions expressions to parse
     * @return             parsed expressions
     */
    fun many(expressions: String): List<Expression> =
            expressions.let {
                val list = ArrayList<Expression>()
                var last = 0
                var weight: Int = 0

                /**
                 * Checks the brackets balance and corrects this value if necessary
                 */
                fun balance(pos: Int): Int = when (expressions[pos]) {
                    '(' -> weight++
                    ')' -> weight--
                    else -> weight
                }

                for (pos in 0..it.length - 1) {
                    if (weight == 0 && expressions[pos] == ',') {
                        list.add(single(expressions.substring(last..pos - 1)))
                        last = pos + 1
                    }

                    balance(pos)
                }

                list.add(single(expressions.substring(last..it.length - 1)))
                list.toList()
            }

    /**
     * Parses expression from the string
     *
     * @param  expression expression to parse
     * @param  l          left bound of parsing
     * @param  r          right bound of parsing
     * @return            parsed expression
     */
    abstract protected fun parse(expression: String, l: Int, r: Int): Expression
}
