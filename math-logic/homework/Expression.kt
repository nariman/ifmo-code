/*
 * Nariman Safiulin (woofilee)
 * File: Expression.kt
 */

import java.util.*

/**
 * Interface for expression objects
 */
interface Expression {
    /**
     * Checks equality of expressions strictly.
     *
     * @param  other expression to check equality with.
     * @return       true, if expressions are strictly equals, otherwise false.
     */
    infix fun exact(other: Expression): Boolean

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
 * Expression variable
 *
 * @param name variable name
 */
class Variable(val name: String) : Expression {
    override infix fun exact(other: Expression) = other is Variable && name == other.name

    override fun match(other: Expression, map: MutableMap<String, Expression>) =
            map[name]?.exact(other) ?: {
                map.put(name, other)
                true
            }()

    override fun toString() = name
    override fun stringify() = toString()
}

/**
 * Expression constant
 *
 * @param value constant value
 */
class Constant(val value: Int) : Expression {
    override infix fun exact(other: Expression) = other is Constant && value == other.value

    override fun match(other: Expression, map: MutableMap<String, Expression>) = exact(other)

    override fun toString() = value.toString()
    override fun stringify() = toString()
}

/**
 * Expression operators abstract class
 *
 * @param operator a string representation of the operator
 */
abstract class Operator(val operator: String) : Expression

/**
 * Expression quantifiers abstract class
 */
abstract class Quantifier(val quantifier: String, val variable: Variable,
                          val statement: Expression) : Expression {
    override fun exact(other: Expression) =
            other is Quantifier && this.javaClass == other.javaClass &&
                    variable exact other.variable && statement exact other.statement

    override fun match(other: Expression, map: MutableMap<String, Expression>) =
            other is Quantifier && this.javaClass == other.javaClass &&
                    variable.match(other.variable, map) && statement.match(other.statement, map)

    override fun toString() = quantifier + variable.stringify() + statement.stringify()
}

/**
 * Expression abstract unary operator
 *
 *  @param operator a string representation of the operator
 *  @param value    unary operand
 */
abstract class Unary(operator: String, val value: Expression) : Operator(operator) {
    override infix fun exact(other: Expression) =
            other is Unary && this.javaClass == other.javaClass &&
                    value exact other.value

    override fun match(other: Expression, map: MutableMap<String, Expression>) =
            other is Unary && this.javaClass == other.javaClass &&
                    value.match(other.value, map)

    override fun toString() = operator + value.stringify()
    override fun stringify() = toString()
}

/**
 * Expression abstract binary operator
 *
 *  @param operator a string representation of the operator
 *  @param lhs      left-hand side operand
 *  @param rhs      right-hand side operand
 */
abstract class Binary(operator: String, val lhs: Expression,
                      val rhs: Expression) : Operator(operator) {
    override infix fun exact(other: Expression) =
            other is Binary && this.javaClass == other.javaClass &&
                    lhs exact other.lhs && rhs exact other.rhs

    override fun match(other: Expression, map: MutableMap<String, Expression>) =
            other is Binary && this.javaClass == other.javaClass &&
                    lhs.match(other.lhs, map) && rhs.match(other.rhs, map)

    override fun toString(): String = lhs.stringify() + operator + rhs.stringify()
}

/**
 * Function
 *
 * @param name      function name
 * @param arguments function arguments
 */
open class Function(val name: String, vararg val arguments: Expression) : Expression {
    override fun exact(other: Expression) =
            other is Predicate && name == other.name &&
                    arguments.zip(other.arguments).all { it.first exact it.second }

    override fun match(other: Expression, map: MutableMap<String, Expression>) =
            other is Predicate && name == other.name &&
                    arguments.zip(other.arguments).all { it.first.match(it.second, map) }

    override fun toString() =
            name + if (arguments.isEmpty()) "" else "(" + arguments.joinToString(",") + ")"
    override fun stringify() = toString()
}

/**
 * Expression predicate
 *
 * @param name      predicate name
 * @param arguments predicate arguments
 */
open class Predicate(name: String, vararg arguments: Expression) : Function(name, *arguments)

/**
 * Universal quantifier
 */
class Universal(variable: Variable, statement: Expression) : Quantifier("@", variable, statement)

infix fun Variable.all(statement: Expression) = Universal(this, statement)

/**
 * Existential quantifier
 */
class Existential(variable: Variable, statement: Expression) : Quantifier("?", variable, statement)

infix fun Variable.exists(statement: Expression) = Existential(this, statement)

/**
 * Equals predicate
 */
class Equals(lhs: Expression, rhs: Expression) : Predicate("=", lhs, rhs)

infix fun Expression.equals(rhs: Expression) = Equals(this, rhs)

/**
 * Negation unary operator
 */
class Negation(value: Expression) : Unary("!", value)

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

/**
 * Addition binary operator
 */
class Addition(lhs: Expression, rhs: Expression) : Binary("+", lhs, rhs)

operator fun Expression.plus(rhs: Expression) = Addition(this, rhs)

/**
 * Multiplication binary operator
 */
class Multiplication(lhs: Expression, rhs: Expression) : Binary("*", lhs, rhs)

operator fun Expression.times(rhs: Expression) = Multiplication(this, rhs)
