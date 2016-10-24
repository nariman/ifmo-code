/**
 * Nariman Safiulin (woofilee)
 * File: Expression.kt
 * Created on: 24 Oct, 2016
 */

import java.util.*

/**
 * Interface for expression objects
 */
interface Expression {
    /**
     * Checks equality of expressions explicitly.
     *
     * @param  other expression to check equality with.
     * @return       true, if expressions are explicitly equals, otherwise false.
     */
    infix fun equal(other: Expression): Boolean

    /**
     * Checks equality of expressions with pattern matching.
     * Uses this expression as a pattern, and given expression for pattern matching.
     *
     * @param  other expression for pattern matching.
     * @param  map   map, that is needed for matching variables from pattern to variables and
     *               expressions from given expression.
     * @return       true, if given expression matches the pattern.
     */
    fun match(other: Expression, map: MutableMap<String, Expression>): Boolean

    /**
     * Returns a string representation of the expression.
     */
    override fun toString(): String

    /**
     * Checks equality of expressions with pattern matching.
     * Uses this expression as a pattern, and given expression for pattern matching.
     * Default pattern matching implementation with `HashMap`.
     */
    infix fun match(other: Expression): Boolean = match(other, HashMap<String, Expression>())

    /**
     * Returns a string representation of the expression.
     */
    fun stringify(): String = "($this)"
}

/**
 * Expression operators abstract class
 *
 * @param operator a string representation of the operator
 */
abstract class Operator(val operator: String) : Expression

/**
 * Expression abstract unary operator
 *
 *  @param operator a string representation of the operator
 *  @param value    unary operand
 */
abstract class Unary(operator: String, val value: Expression) : Operator(operator) {
    override infix fun equal(other: Expression) =
            other is Unary && this.javaClass == other.javaClass &&
                    value equal other.value

    override fun match(other: Expression, map: MutableMap<String, Expression>) =
            other is Unary && this.javaClass == other.javaClass &&
                    value.match(other.value, map)

    override fun toString() = operator + value.stringify()
}

/**
 * Expression abstract binary operator
 *
 *  @param operator a string representation of the operator
 *  @param lhs      left-hand side operand
 *  @param rhs      right-hand side operand
 */
abstract class Binary(operator: String, val lhs: Expression, val rhs: Expression) : Operator(operator) {
    override infix fun equal(other: Expression) =
            other is Binary && this.javaClass == other.javaClass &&
                    lhs equal other.lhs && rhs equal other.rhs

    override fun match(other: Expression, map: MutableMap<String, Expression>) =
            other is Binary && this.javaClass == other.javaClass &&
                    lhs.match(other.lhs, map) && rhs.match(other.rhs, map)

    override fun toString(): String = lhs.stringify() + operator + rhs.stringify()
}

/**
 * Expression variable
 *
 * @param name variable name
 */
class Variable(val name: String) : Expression {
    override infix fun equal(other: Expression) = other is Variable && name == other.name

    override fun match(other: Expression, map: MutableMap<String, Expression>) =
            map[name]?.equal(other) ?: {
                map.put(name, other)
                true
            }()

    override fun toString() = name
    override fun stringify() = toString()
}

/**
 * Negation unary operator
 */
class Negation(value: Expression) : Unary("!", value) {
    override fun stringify() = toString()
}

operator fun Expression.not() = Negation(this)

/**
 * Conjunction binary operator
 */
class Conjunction(lhs: Expression, rhs: Expression) : Binary("&", lhs, rhs)

infix fun Expression.conj(rhs: Expression) = Conjunction(this, rhs)

/**
 * Disjunction binary operator
 */
class Disjunction(lhs: Expression, rhs: Expression) : Binary("|", lhs, rhs)

infix fun Expression.disj(rhs: Expression) = Disjunction(this, rhs)

/**
 * Implication binary operator
 */
class Implication(lhs: Expression, rhs: Expression) : Binary("->", lhs, rhs)

infix fun Expression.impl(rhs: Expression) = Implication(this, rhs)
