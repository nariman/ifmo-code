/**
 * Nariman Safiulin (woofilee)
 * File: Main.kt
 * Created on: 19 Oct, 2016
 */

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.util.*

fun solve(`in`: BufferedReader, out: BufferedWriter) {
    val title: List<String> = `in`.readLine().split("|-")
    out.write("${title[0]}|-${title[1]}\n")

    val unproven: Expression? =
            if (title[1].length > 0)
                Parser.parse(title[1])
            else
                null

    val hypotheses = object : Chain(
            if (title[0].length > 0)
                title[0].split(",")
            else
                emptyList()
    ) {}

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
            if (hypotheses.list[i - 1] match expression) {
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

        out.write("(Не доказано)\n")
    }
}

fun main(args: Array<String>) {
    if (args.size != 2) {
        println("Please, provide a two file names for input and output respectively.")
        return
    }

    val `in` = File(args[0]).bufferedReader()
    val out = File(args[1]).bufferedWriter()

    val startTime = System.nanoTime()
    solve(`in`, out)
    val endTime = System.nanoTime()

    println("Time of execution: ${endTime - startTime} ns.")
    `in`.close()
    out.close()
}
