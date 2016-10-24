/**
 * Nariman Safiulin (woofilee)
 * File: Main.kt
 * Created on: 19 Oct, 2016
 */

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.util.*

interface Expression {
    /**
     * Checks equality of expressions explicitly
     */
    infix fun equal(other: Expression): Boolean


    /**
     * Checks equality of expressions with map matching, using this object as pattern, and `other` for pattern matching
     */
    fun match(other: Expression, map: MutableMap<String, Expression>): Boolean

    infix fun match(other: Expression): Boolean = match(other, HashMap<String, Expression>())

    override fun toString(): String
    fun stringify(): String = "$this"
}

class Variable(val name: String) : Expression {
    override fun toString() = name
    override fun stringify() = toString()

    override infix fun equal(other: Expression) = other is Variable && name == other.name

    override fun match(other: Expression, map: MutableMap<String, Expression>): Boolean =
            map[name]?.equal(other) ?: { map.put(name, other); true }()
}

abstract class Operator(val operator: String) : Expression

abstract class Unary(operator: String, val value: Expression) : Operator(operator) {
    override fun toString() = "(" + operator + value.stringify() + ")"

    override infix fun equal(other: Expression) =
            other is Unary && this.javaClass == other.javaClass && value equal other.value

    override fun match(other: Expression, map: MutableMap<String, Expression>) =
            other is Unary && this.javaClass == other.javaClass && value.match(other.value, map)
}

abstract class Binary(operator: String, val lhs: Expression, val rhs: Expression) : Operator(operator) {
    override fun toString(): String = "(" + lhs.stringify() + operator + rhs.stringify() + ")"

    override infix fun equal(other: Expression) =
            other is Binary && this.javaClass == other.javaClass && lhs equal other.lhs && rhs equal other.rhs

    override fun match(other: Expression, map: MutableMap<String, Expression>) =
            other is Binary && this.javaClass == other.javaClass && lhs.match(other.lhs, map) && rhs.match(other.rhs, map)
}

operator fun Expression.not() = Negation(this)
class Negation(value: Expression) : Unary("!", value) {
//    override fun stringify() = toString()
}

infix fun Expression.conj(rhs: Expression) = Conjunction(this, rhs)
class Conjunction(lhs: Expression, rhs: Expression) : Binary("&", lhs, rhs)

infix fun Expression.disj(rhs: Expression) = Disjunction(this, rhs)
class Disjunction(lhs: Expression, rhs: Expression) : Binary("|", lhs, rhs)

infix fun Expression.impl(rhs: Expression) = Implication(this, rhs)
class Implication(lhs: Expression, rhs: Expression) : Binary("->", lhs, rhs)


fun solve(`in`: BufferedReader, out: BufferedWriter) {
    fun parse(e: String, l: Int, r: Int): Expression {
        var b: Int = 0
        fun bracket(i: Int) = if (e[i] == '(') b++ else if (e[i] == ')') b-- else b

        (l..r).forEach { i -> if (b == 0 && e[i] == '>') return parse(e, l, i - 2) impl parse(e, i + 1, r) else bracket(i) }
        (r downTo l).forEach { i -> if (b == 0 && e[i] == '|') return parse(e, l, i - 1) disj parse(e, i + 1, r) else bracket(i) }
        (r downTo l).forEach { i -> if (b == 0 && e[i] == '&') return parse(e, l, i - 1) conj parse(e, i + 1, r) else bracket(i) }

        if (e[l] == '!') return !parse(e, l + 1, r)
        if (e[l] == '(') return parse(e, l + 1, r - 1)

        return Variable(e.substring(l..r).trim())
    }

    val ws = Regex("\\s")
    val axioms = listOf(
            "A -> B -> A",
            "(A -> B) -> (A -> B -> C) -> (A -> C)",
            "A & B -> A",
            "A & B -> B",
            "A -> B -> A & B",
            "A -> A | B",
            "B -> A | B",
            "(A -> Q) -> (B -> Q) -> (A | B -> Q)",
            "(A -> B) -> (A -> !B) -> !A",
            "!!A -> A"
    ).map { it.replace(ws, "") }.map { parse(it, 0, it.length - 1) }

    val title = `in`.readLine().split("|-")
    val hypotheses: List<Expression> =
            if (title[0].length > 0) title[0].split(",").map { it.replace(ws, "") }.map { parse(it, 0, it.length - 1) }
            else emptyList()

    val unproven = title[1].replace(ws, "").let { parse(it, 0, it.length - 1) }

    val expressions = ArrayList<Expression?>()
    var counter = 0

    `in`.forEachLine { line ->
        counter++

        val expression = line.replace(ws, "").let { parse(it, 0, it.length - 1) }

        out.write("($counter)")
        out.write(" $expression ")

        for (i in 1..axioms.size) {
            if (axioms[i - 1] match expression) {
                expressions.add(expression)
                out.write("(Сх. акс. $i)\n")
                return@forEachLine
            }
        }

        for (i in 1..hypotheses.size) {
            if (hypotheses[i - 1] match expression) {
                expressions.add(expression)
                out.write("(Предп. $i)\n")
                return@forEachLine
            }
        }

        for (i in 1..expressions.size) {
            if (expressions[i - 1] is Implication) {
                val impl = expressions[i - 1] as Implication
                if (impl.rhs equal expression) {
                    for (j in 1..expressions.size) {
                        if (expressions[j - 1]?.equal(impl.lhs) ?: false) {
                            expressions.add(expression)
                            out.write("(M. P. $j, $i)\n")
                            return@forEachLine
                        }
                    }
                }
            }
        }

        expressions.add(null)
        out.write("(Не доказано)\n")
    }
}

fun main(args: Array<String>) {
    if (args.size != 2) {
        println("Please, provide a filename with test for input, and filename for output")
        return
    }

    val `in` = File(args[0]).bufferedReader()
    val out = File(args[1]).bufferedWriter()

    solve(`in`, out)

    `in`.close()
    out.close()
}
