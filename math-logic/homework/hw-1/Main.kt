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
    operator fun not() = Negation(this)
    infix fun conj(rhs: Expression) = Conjunction(this, rhs)
    infix fun disj(rhs: Expression) = Disjunction(this, rhs)
    infix fun impl(rhs: Expression) = Implication(this, rhs)

    fun stringify(expression: Expression): String = when (expression) {
        is Variable -> expression.toString()
        is Negation -> expression.toString()
        else -> "(${expression.toString()})"
    }
}

class Variable(val name: String) : Expression {
    override fun toString() = name
    override fun equals(other: Any?) = other is Variable && name == other.name
}

abstract class Operator(val operator: String) : Expression

abstract class Unary(operator: String, val value: Expression) : Operator(operator) {
    override fun toString() = operator + stringify(value)
}

abstract class Binary(operator: String, val rhs: Expression, val lhs: Expression) : Operator(operator) {
    override fun toString(): String = stringify(lhs) + operator + stringify(rhs)
}

class Negation(value: Expression) : Unary("!", value) {
    override fun equals(other: Any?): Boolean {
        return other is Negation && value == other.value
    }
}

class Conjunction(lhs: Expression, rhs: Expression) : Binary("&", rhs, lhs) {
    override fun equals(other: Any?): Boolean {
        return other is Conjunction && lhs == other.lhs && rhs == other.rhs
    }
}

class Disjunction(lhs: Expression, rhs: Expression) : Binary("|", rhs, lhs) {
    override fun equals(other: Any?): Boolean {
        return other is Disjunction && lhs == other.lhs && rhs == other.rhs
    }
}

class Implication(lhs: Expression, rhs: Expression) : Binary("->", rhs, lhs) {
    override fun equals(other: Any?): Boolean {
        return other is Implication && lhs == other.lhs && rhs == other.rhs
    }
}

fun parse(e: String, l: Int, r: Int): Expression {
    var brackets: Int = 0
    fun bracket(i: Int) = if (e[i] == '(') brackets++ else if (e[i] == ')') brackets-- else brackets

    (l..r).forEach { i -> // -> implication
        if (brackets == 0 && e[i] == '>') return parse(e, l, i - 2) impl parse(e, i + 1, r)
        bracket(i)
    }
    (r downTo l).forEach { i -> // | disjunction
        if (brackets == 0 && e[i] == '|') return parse(e, l, i - 1) disj parse(e, i + 1, r)
        bracket(i)
    }
    (r downTo l).forEach { i -> // & conjunction
        if (brackets == 0 && e[i] == '&') return parse(e, l, i - 1) conj parse(e, i + 1, r)
        bracket(i)
    }

    if (e[l] == '!') return !parse(e, l + 1, r)
    if (e[l] == '(') return parse(e, l + 1, r - 1)

    return Variable(e.substring(l..r).trim())
}

fun solve(`in`: BufferedReader, out: BufferedWriter) {
    val regex = Regex("\\s")
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
    ).map { it.replace(regex, "") }.map { parse(it, 0, it.length - 1) }

    val title = `in`.readLine().split("|-")
    val hypotheses: List<Expression> = {
        if (title[0].length > 0)
            title[0].split(",").map { it.replace(regex, "") }.map { parse(it, 0, it.length - 1) }
        else
            emptyList()
    }()
    val expressions = ArrayList<Expression>()
    val unproven = title[1].replace(regex, "").let { parse(it, 0, it.length - 1) }

    var counter = 0
    outer@ for (line in `in`.lines()) {
        counter++
        val expression = line.replace(regex, "").let { parse(it, 0, it.length - 1) }
        println(expression.toString())
        expressions.add(expression)

        out.write("($counter)")
        out.write(" ${expression.toString()} ")

        for (i in 1..axioms.size) {
            if (axioms[i - 1].equals(expression)) {
                out.write("(Сх. акс. $i)\n")
                continue@outer
            }
        }

        for (i in 1..hypotheses.size) {
            if (hypotheses[i - 1].equals(expression)) {
                out.write("(Предп. $i)\n")
                continue@outer
            }
        }

        for (i in expressions.size - 1 downTo 1) {
            if (expressions[i - 1] is Implication) {
                val impl = expressions[i - 1] as Implication
                if (impl.rhs.equals(expression)) {
                    for (j in i - 1 downTo 1) {
                        if (impl.lhs.equals(expressions[j])) {
                            out.write("(M.P. $j, $i)\n")
                            continue@outer
                        }
                    }
                }
            }
        }

        out.write("(Не доказано)\n")
        break
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
