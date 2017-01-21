/*
 * Nariman Safiulin (woofilee)
 * File: Expression.kt
 */

import java.util.*

/**
 * Interface for expression objects
 */
interface Expression {
    val variables: HashSet<String>

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
     * Substitutes given variable with other expression.
     *
     * @param  variable                 variable to substitute.
     * @param  replacement              replacement expression.
     * @param  set                      set, that is needed for checking for bound variables while
     *                                  replacing.
     * @return                          expression with replaced variable.
     * @throws IllegalArgumentException if variable cannot be replaced
     */
    fun substitute(variable: Variable, replacement: Math, set: MultiSet<String>): Expression = this

    /**
     * Substitutes given variable with other expression.
     *
     * @param  variable                 variable to substitute.
     * @param  replacement              replacement expression.
     * @return                          expression with replaced variable.
     * @throws IllegalArgumentException if variable cannot be replaced
     */
    fun substitute(variable: Variable, replacement: Math) =
            substitute(variable, replacement, MultiSet<String>())

    /**
     * Replaces given variables/predicates with other expression.
     *
     * @param  map map with pairs (expression to replace to replacement
     *             expression)
     * @return     expression with replaced variables/predicates.
     */
    fun replace(map: Map<Expression, Expression>): Expression = this

    /**
     * Returns a string representation of the expression.
     */
    fun stringify() = "($this)"
}

/**
 * Interface for logic expressions
 */
interface Logic : Expression {
    override fun substitute(variable: Variable, replacement: Math, set: MultiSet<String>): Logic =
            this
    override fun replace(map: Map<Expression, Expression>): Logic = this
}

/**
 * Interface for math expressions
 */
interface Math : Expression {
    override fun match(other: Expression, map: MutableMap<String, Expression>) = false
    override fun substitute(variable: Variable, replacement: Math, set: MultiSet<String>): Math =
            this
    override fun replace(map: Map<Expression, Expression>): Math = this
}

/**
 * Expression variable
 *
 * @param name variable name
 */
class Variable(val name: String) : Math {
    override val variables = HashSet<String>()

    init {
        variables.add(name)
    }

    override infix fun exact(other: Expression) = other is Variable && name == other.name

    override fun substitute(variable: Variable, replacement: Math,
                            set: MultiSet<String>): Math {
        if (name == variable.name && name !in set) {
            if (replacement.variables.any { it in set && it != variable.name })
                throw IllegalArgumentException()

            return replacement
        }

        return this
    }

    override fun replace(map: Map<Expression, Expression>): Math {
        map.forEach {
            if (it.key is Variable && (it.key as Variable).name == name && it.value is Math)
                return it.value as Math
        }

        return this
    }

    override fun toString() = name
    override fun stringify() = toString()
}

/**
 * Expression constant
 *
 * @param value constant value
 */
class Constant(val value: Int) : Math {
    override val variables = HashSet<String>()

    override infix fun exact(other: Expression) = other is Constant && value == other.value

    override fun toString() = value.toString()
    override fun stringify() = toString()
}

/**
 * Abstract class for parameterized expressions
 *
 * @param arguments operands
 */
abstract class Parameterized(vararg val arguments: Expression) : Expression {
    final override val variables = HashSet<String>()

    init {
        arguments.forEach { variables.addAll(it.variables) }
    }

    override fun exact(other: Expression) =
            other is Parameterized && this.javaClass == other.javaClass &&
                    arguments.size == other.arguments.size &&
                    arguments.zip(other.arguments).all { it.first exact it.second }

    override fun match(other: Expression, map: MutableMap<String, Expression>) =
            other is Parameterized && this.javaClass == other.javaClass &&
                    /* arguments.size == other.arguments.size && */
                    arguments.zip(other.arguments).all { it.first.match(it.second, map) }

    abstract override fun replace(map: Map<Expression, Expression>): Expression
}

/**
 * Expression operators abstract class
 *
 * @param operator a string representation of the operator
 * @param arguments operands
 */
abstract class Operator(val operator: String,
                        vararg arguments: Expression) : Parameterized(*arguments) {
    override fun exact(other: Expression) =
            other is Operator && operator == other.operator && super.exact(other)

    override fun match(other: Expression, map: MutableMap<String, Expression>) =
            other is Operator && operator == other.operator && super.match(other, map)
}

/**
 * Expression quantifiers abstract class
 */
abstract class Quantifier(val quantifier: String, val variable: Variable,
                          val statement: Logic) : Logic {
    final override val variables = HashSet<String>()

    init {
        variables.addAll(statement.variables)
    }

    override fun exact(other: Expression) =
            other is Quantifier && this.javaClass == other.javaClass &&
                    variable exact other.variable && statement exact other.statement

    override fun match(other: Expression, map: MutableMap<String, Expression>) =
            other is Quantifier && this.javaClass == other.javaClass &&
                    /* variable.match(other.variable, map) && */
                    statement.match(other.statement, map)

    override fun toString() = quantifier + variable.stringify() + statement.stringify()
}

/**
 * Expression abstract unary operator
 *
 *  @param operator a string representation of the operator
 *  @param value    unary operand
 */
abstract class Unary(operator: String, open val value: Expression) : Operator(operator, value) {
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
abstract class Binary(operator: String, open val lhs: Expression,
                      open val rhs: Expression) : Operator(operator, lhs, rhs) {
    override fun toString(): String = lhs.stringify() + operator + rhs.stringify()
}

/**
 * Expression predicate
 *
 * @param name      predicate name
 * @param arguments predicate arguments
 */
open class Predicate(val name: String,
                     vararg arguments: Expression) : Parameterized(*arguments), Logic {
    override fun exact(other: Expression) =
            other is Predicate && name == other.name && super.exact(other)

    override fun match(other: Expression, map: MutableMap<String, Expression>) =
            map[toString()]?.exact(other) ?: {
                map.put(toString(), other)
                true
            }()

    override fun substitute(variable: Variable, replacement: Math, set: MultiSet<String>) =
            Predicate(name, *(arguments.map {
                it.substitute(variable, replacement, set)
            }.toTypedArray()))

    override fun replace(map: Map<Expression, Expression>): Logic {
        map.forEach {
            if (it.key is Predicate && (it.key as Predicate).name == name && it.value is Logic)
                return it.value as Logic
        }

        return Predicate(name, *(arguments.map {
            it.replace(map)
        }.toTypedArray()))
    }

    override fun toString() =
            name +
                    if (arguments.isEmpty()) ""
                    else "(" + arguments.map { it.stringify() }.joinToString(",") + ")"
    override fun stringify() = toString()
}

/**
 * Expression function
 *
 * @param name      function name
 * @param arguments function arguments
 */
open class Function(val name: String,
                    vararg arguments: Math) : Parameterized(*arguments), Math {
    override fun exact(other: Expression) =
            other is Function && name == other.name && super.exact(other)

    override fun match(other: Expression, map: MutableMap<String, Expression>) =
            other is Function && name == other.name && super<Math>.match(other, map)

    override fun substitute(variable: Variable, replacement: Math, set: MultiSet<String>) =
            Function(name, *(arguments.map {
                (it as Math).substitute(variable, replacement, set)
            }.toTypedArray()))

    override fun replace(map: Map<Expression, Expression>): Math =
            Function(name, *(arguments.map {
                (it as Math).replace(map)
            }.toTypedArray()))

    override fun toString() =
            name +
                    if (arguments.isEmpty()) ""
                    else "(" + arguments.map { it.stringify() }.joinToString(",") + ")"
    override fun stringify() = toString()
}

/**
 * Universal quantifier
 */
class Universal(variable: Variable, statement: Logic) : Quantifier("@", variable, statement) {
    override fun substitute(variable: Variable, replacement: Math,
                            set: MultiSet<String>): Universal {
        set.add(this.variable.name)
        val q = Universal(this.variable, statement.substitute(variable, replacement, set))
        set.remove(this.variable.name)
        return q
    }

    override fun replace(map: Map<Expression, Expression>): Logic {
        map.forEach {
            if (it.key is Variable && (it.key as Variable).name == variable.name && it.value is Variable)
                return Universal(it.value as Variable, statement.replace(map))
        }

        return Universal(variable, statement.replace(map))
    }
}

infix fun Variable.all(statement: Logic) = Universal(this, statement)

/**
 * Existential quantifier
 */
class Existential(variable: Variable,
                  statement: Logic) : Quantifier("?", variable, statement) {
    override fun substitute(variable: Variable, replacement: Math,
                            set: MultiSet<String>): Existential {
        set.add(this.variable.name)
        val q = Existential(this.variable, statement.substitute(variable, replacement, set))
        set.remove(this.variable.name)
        return q
    }

    override fun replace(map: Map<Expression, Expression>): Logic {
        map.forEach {
            if (it.key is Variable && (it.key as Variable).name == variable.name && it.value is Variable)
                return Existential(it.value as Variable, statement.replace(map))
        }

        return Existential(variable, statement.replace(map))
    }
}

infix fun Variable.exists(statement: Logic) = Existential(this, statement)

/**
 * Equals predicate
 */
class Equals(val lhs: Math, val rhs: Math) : Predicate("=", lhs, rhs) {
    override fun substitute(variable: Variable, replacement: Math, set: MultiSet<String>) =
            Equals(lhs.substitute(variable, replacement, set),
                    rhs.substitute(variable, replacement, set))

    override fun replace(map: Map<Expression, Expression>): Logic =
            Equals(lhs.replace(map), rhs.replace(map))

    override fun toString() = lhs.stringify() + "=" + rhs.stringify()
    override fun stringify() = "($this)"
}

infix fun Math.equals(rhs: Math) = Equals(this, rhs)

/**
 * Negation unary operator
 */
class Negation(override val value: Logic) : Unary("!", value), Logic {
    override fun substitute(variable: Variable, replacement: Math, set: MultiSet<String>) =
            Negation(value.substitute(variable, replacement, set))

    override fun replace(map: Map<Expression, Expression>): Logic =
            Negation(value.replace(map))
}

operator fun Logic.not() = Negation(this)

/**
 * Increment unary operator
 */
class Increment(override val value: Math) : Unary("'", value), Math {
    override fun match(other: Expression, map: MutableMap<String, Expression>) =
            super<Math>.match(other, map)

    override fun substitute(variable: Variable, replacement: Math, set: MultiSet<String>) =
            Increment(value.substitute(variable, replacement, set))

    override fun replace(map: Map<Expression, Expression>): Math =
            Increment(value.replace(map))

    override fun toString() = value.stringify() + operator
}

fun Math.inc() = Increment(this)

/**
 * Conjunction binary operator
 */
class Conjunction(override val lhs: Logic,
                  override val rhs: Logic) : Binary("&", lhs, rhs), Logic {
    override fun substitute(variable: Variable, replacement: Math, set: MultiSet<String>) =
            Conjunction(lhs.substitute(variable, replacement, set),
                    rhs.substitute(variable, replacement, set))

    override fun replace(map: Map<Expression, Expression>) =
            Conjunction(lhs.replace(map), rhs.replace(map))
}

infix fun Logic.conj(rhs: Logic) = Conjunction(this, rhs)

/**
 * Disjunction binary operator
 */
class Disjunction(override val lhs: Logic,
                  override val rhs: Logic) : Binary("|", lhs, rhs), Logic {
    override fun substitute(variable: Variable, replacement: Math, set: MultiSet<String>) =
            Disjunction(lhs.substitute(variable, replacement, set),
                    rhs.substitute(variable, replacement, set))

    override fun replace(map: Map<Expression, Expression>) =
            Disjunction(lhs.replace(map), rhs.replace(map))
}

infix fun Logic.disj(rhs: Logic) = Disjunction(this, rhs)

/**
 * Implication binary operator
 */
class Implication(override val lhs: Logic,
                  override val rhs: Logic) : Binary("->", lhs, rhs), Logic {
    override fun substitute(variable: Variable, replacement: Math, set: MultiSet<String>) =
            Implication(lhs.substitute(variable, replacement, set),
                    rhs.substitute(variable, replacement, set))

    override fun replace(map: Map<Expression, Expression>) =
            Implication(lhs.replace(map), rhs.replace(map))
}

infix fun Logic.impl(rhs: Logic) = Implication(this, rhs)

/**
 * Addition binary operator
 */
class Addition(override val lhs: Math, override val rhs: Math) : Binary("+", lhs, rhs), Math {
    override fun match(other: Expression, map: MutableMap<String, Expression>) =
            super<Math>.match(other, map)

    override fun substitute(variable: Variable, replacement: Math, set: MultiSet<String>) =
            Addition(lhs.substitute(variable, replacement, set),
                    rhs.substitute(variable, replacement, set))

    override fun replace(map: Map<Expression, Expression>) =
            Addition(lhs.replace(map), rhs.replace(map))
}

operator fun Math.plus(rhs: Math) = Addition(this, rhs)

/**
 * Multiplication binary operator
 */
class Multiplication(override val lhs: Math,
                     override val rhs: Math) : Binary("*", lhs, rhs), Math {
    override fun match(other: Expression, map: MutableMap<String, Expression>) =
            super<Math>.match(other, map)

    override fun substitute(variable: Variable, replacement: Math, set: MultiSet<String>) =
            Multiplication(lhs.substitute(variable, replacement, set),
                    rhs.substitute(variable, replacement, set))

    override fun replace(map: Map<Expression, Expression>) =
            Multiplication(lhs.replace(map), rhs.replace(map))
}

operator fun Math.times(rhs: Math) = Multiplication(this, rhs)
