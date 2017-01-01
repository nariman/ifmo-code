/*
 * Nariman Safiulin (woofilee)
 * File: Parser.kt
 */

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
    fun parse(expression: String): Expression =
            Utils.clean(expression).let {
                parse(it, 0, it.length - 1)
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
