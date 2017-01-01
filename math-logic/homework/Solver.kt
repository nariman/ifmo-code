/*
 * Nariman Safiulin (woofilee)
 * File: Solver.kt
 */

import java.io.BufferedReader
import java.io.BufferedWriter
import java.util.*

/**
 * Interface for the problem solvers
 */
interface Solver {
    fun solve(`in`: BufferedReader, out: BufferedWriter)
}

/**
 * Solver for the first problem in a homework
 */
object First : Solver {
    override fun solve(`in`: BufferedReader, out: BufferedWriter) {
        val title: List<String> = Utils.clean(`in`.readLine()).split("|-")

        val hypotheses = object : Chain(
                if (title[0].isNotEmpty())
                    title[0].split(",")
                else
                    emptyList()
        ) {}

        val unproven: Expression? =
                if (title[1].isNotEmpty())
                    Parser.parse(title[1])
                else
                    null

        out.write("${hypotheses.list.map { it.toString() }.joinToString(",")}|-$unproven\n")

        val expressions = HashMap<Int, Expression>()
        val fulls = HashMap<String, Int>()
        val rights = HashMap<String, ArrayList<Int>>()
        var counter = 0

        fun save(message: String, expression: Expression) {
            out.write("($message)\n")

            if (expression is Implication)
                rights.getOrPut(expression.rhs.toString(), { ArrayList<Int>() }).add(counter)

            fulls.putIfAbsent(expression.toString(), counter)
            expressions.put(counter, expression)
        }

        `in`.forEachLine { line ->
            counter++
            val expression = Parser.parse(line)

            out.write("($counter)")
            out.write(" $expression ")

            for (i in 1..axioms.list.size) {
                if (axioms.list[i - 1] match expression) {
                    save("Сх. акс. $i", expression)
                    return@forEachLine
                }
            }

            for (i in 1..hypotheses.list.size) {
                if (hypotheses.list[i - 1] equal expression) {
                    save("Предп. $i", expression)
                    return@forEachLine
                }
            }

            rights[expression.toString()]?.forEach { j ->
                fulls[(expressions[j] as Implication).lhs.toString()]?.let { i ->
                    save("M.P. $i, $j", expression)
                    return@forEachLine
                }
            }

            save("Не доказано", expression)
        }
    }
}

/**
 * Solver for the second problem in a homework
 */
object Second : Solver {
    override fun solve(`in`: BufferedReader, out: BufferedWriter) {
        throw UnsupportedOperationException("Not implemented")
    }
}

/**
 * Solver for the third problem in a homework
 */
object Third : Solver {
    override fun solve(`in`: BufferedReader, out: BufferedWriter) {
        throw UnsupportedOperationException("Not implemented")
    }
}
