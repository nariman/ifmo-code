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
     * @return       true, if given expression matches the pattern, otherwise false.
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
     *
     * @param  other expression for pattern matching.
     * @return       true, if given expression matches the pattern, otherwise false.
     */
    infix fun match(other: Expression) = match(other, HashMap<String, Expression>())

    /**
     * Returns a string representation of the expression.
     */
    fun stringify() = "($this)"
}

/**
 * Interface for logic expressions
 */
interface Logic : Expression

/**
 * Interface for math expressions
 */
interface Math : Expression

/**
 * Expression variable
 *
 * @param name variable name
 */
class Variable(val name: String) : Math {
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
class Constant(val value: Int) : Math {
    override infix fun exact(other: Expression) = other is Constant && value == other.value

    override fun match(other: Expression, map: MutableMap<String, Expression>) = exact(other)

    override fun toString() = value.toString()
    override fun stringify() = toString()
}

/**
 * Expression operators abstract class
 *
 * @param operator a string representation of the operator
 * @param arguments operands
 */
abstract class Operator(val operator: String, vararg val arguments: Expression) : Expression {
    override fun exact(other: Expression) =
            other is Operator && this.javaClass == other.javaClass &&
                    operator == other.operator && arguments.size == other.arguments.size &&
                    arguments.zip(other.arguments).all { it.first exact it.second }

    override fun match(other: Expression, map: MutableMap<String, Expression>) =
            other is Operator && this.javaClass == other.javaClass &&
                    operator == other.operator && arguments.size == other.arguments.size &&
                    arguments.zip(other.arguments).all { it.first.match(it.second, map) }
}

/**
 * Expression quantifiers abstract class
 */
abstract class Quantifier(val quantifier: String, val variable: Variable,
                          val statement: Expression) : Logic {
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
abstract class Unary(operator: String, val value: Expression) : Operator(operator, value) {
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
                      val rhs: Expression) : Operator(operator, lhs, rhs) {
    override fun toString(): String = lhs.stringify() + operator + rhs.stringify()
}

/**
 * Expression predicate
 *
 * @param name      predicate name
 * @param arguments predicate arguments
 */
open class Predicate(val name: String, vararg val arguments: Expression) : Logic {
    override fun exact(other: Expression) =
            other is Predicate && this.javaClass == other.javaClass &&
                    name == other.name && arguments.size == other.arguments.size &&
                    arguments.zip(other.arguments).all { it.first exact it.second }

    override fun match(other: Expression, map: MutableMap<String, Expression>) =
            other is Predicate && this.javaClass == other.javaClass &&
                    name == other.name && arguments.size == other.arguments.size &&
                    arguments.zip(other.arguments).all { it.first.match(it.second, map) }

    override fun toString() =
            name + if (arguments.isEmpty()) "" else "(" + arguments.joinToString(",") + ")"
    override fun stringify() = toString()
}

/**
 * Expression function
 *
 * @param name      function name
 * @param arguments function arguments
 */
open class Function(val name: String, vararg val arguments: Math) : Math {
    override fun exact(other: Expression) =
            other is Function && this.javaClass == other.javaClass &&
                    name == other.name && arguments.size == other.arguments.size &&
                    arguments.zip(other.arguments).all { it.first exact it.second }

    override fun match(other: Expression, map: MutableMap<String, Expression>) =
            other is Function && this.javaClass == other.javaClass &&
                    name == other.name && arguments.size == other.arguments.size &&
                    arguments.zip(other.arguments).all { it.first.match(it.second, map) }

    override fun toString() =
            name + if (arguments.isEmpty()) "" else "(" + arguments.joinToString(",") + ")"
    override fun stringify() = toString()
}

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
class Equals(val lhs: Math, val rhs: Math) : Predicate("=", lhs, rhs) {
    override fun toString() = lhs.stringify() + "=" + rhs.stringify()
    override fun stringify() = "($this)"
}

infix fun Math.equals(rhs: Math) = Equals(this, rhs)

/**
 * Negation unary operator
 */
class Negation(value: Logic) : Unary("!", value), Logic

operator fun Logic.not() = Negation(this)

/**
 * Increment unary operator
 */
class Increment(value: Math) : Unary("'", value), Math {
    override fun toString() = value.stringify() + operator
}

operator fun Math.inc() = Increment(this)

/**
 * Conjunction binary operator
 */
class Conjunction(lhs: Logic, rhs: Logic) : Binary("&", lhs, rhs), Logic

infix fun Logic.conj(rhs: Logic) = Conjunction(this, rhs)

/**
 * Disjunction binary operator
 */
class Disjunction(lhs: Logic, rhs: Logic) : Binary("|", lhs, rhs), Logic

infix fun Logic.disj(rhs: Logic) = Disjunction(this, rhs)

/**
 * Implication binary operator
 */
class Implication(lhs: Logic, rhs: Logic) : Binary("->", lhs, rhs), Logic

infix fun Logic.impl(rhs: Logic) = Implication(this, rhs)

/**
 * Addition binary operator
 */
class Addition(lhs: Math, rhs: Math) : Binary("+", lhs, rhs), Math

operator fun Math.plus(rhs: Math) = Addition(this, rhs)

/**
 * Multiplication binary operator
 */
class Multiplication(lhs: Math, rhs: Math) : Binary("*", lhs, rhs), Math

operator fun Math.times(rhs: Math) = Multiplication(this, rhs)
