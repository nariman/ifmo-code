/*
 * Nariman Safiulin (narimansafiulin)
 * File: D.kt
 * Created on: Oct 30, 2017
 */

import java.io.*
import java.util.*
import java.nio.file.*

private val PROBLEM_NAME = "postfix"

private class Scanner(val r: BufferedReader) {
    var t = StringTokenizer("")

    fun close() = r.close()
    fun has(): Boolean {
        while (!t.hasMoreTokens()) t = StringTokenizer(r.readLine() ?: return false)
        return true
    }

    fun string(): String = if (has()) t.nextToken() else ""
    fun int(): Int = string().toInt()
    fun long(): Long = string().toLong()
    fun double(): Double = string().toDouble()
}

private class Stack {
    val elements = ArrayList<Int>()

    fun size(): Int = elements.size
    fun push(c: Int) = elements.add(c)
    fun pop(): Int = elements.removeAt(elements.size - 1)
    fun last(): Int = elements[elements.size - 1]
}

private fun solve(`in`: Scanner, out: BufferedWriter) {
    val stack = Stack()

    stack.push(`in`.int())
    stack.push(`in`.int())

    while (`in`.has()) {
        val smth = `in`.string()
        when (smth) {
            "+" -> stack.push(stack.pop() + stack.pop())
            "*" -> stack.push(stack.pop() * stack.pop())
            "-" -> {
                val f = stack.pop()
                val s = stack.pop()
                stack.push(s - f)
            }
            else -> stack.push(smth.toInt())
        }
    }

    out.write("${stack.pop()}")
}

private fun naive(`in`: Scanner, out: BufferedWriter) {
    // Code naive solution here
}

private fun testgen(`in`: Scanner, out: BufferedWriter) {
    val random = Random()

    // Code test generation here
}

private fun run(name: String, ie: String, oe: String, f: (Scanner, BufferedWriter) -> Unit,
                debug: Boolean = false, files: Boolean = false) {
    if (debug) print("$name running... ")
    val start = System.nanoTime()

    val `in`: Scanner
    val out: BufferedWriter

    if (files) {
        `in` = Scanner(File(PROBLEM_NAME + ".$ie").bufferedReader())
        out = File(PROBLEM_NAME + ".$oe").bufferedWriter()
    } else {
        `in` = Scanner(System.`in`.bufferedReader())
        out = System.out.bufferedWriter()
    }

    f(`in`, out)

    `in`.close()
    out.close()

    val end = System.nanoTime()
    if (debug) println("OK, ${(end - start) / 1e6} ms.")
}

fun main(args: Array<String>) {
    val judge = false
    val naive = false

    val tests = 150

    val debug = false or judge // in judge mode, debug is an important thing!1
    val files = true or judge // we cannot use system i/o, if judge mode is on

    val ie = "in"
    val ae = "ans"
    val oe = "out"

    if (!judge) {
        run("Solution", ie, oe, ::solve, debug, files)
    } else {
        repeat(tests) {
            println("Checking test #${it + 1}...")

            print("  - "); run("Test generation", ie, ie, ::testgen, debug, files)
            if (naive) { print("  - "); run("Naive solution", ie, ae, ::naive, debug, files) }
            print("  - "); run("Main solution", ie, oe, ::solve, debug, files)

            if (naive) {
                print("  - Checking... ")

                if (Files.readAllLines(Paths.get(PROBLEM_NAME + ".$oe")) ==
                        Files.readAllLines(Paths.get(PROBLEM_NAME + ".$ae"))) {
                    println("OK")
                } else {
                    println("WA")
                    return
                }
            }

            println()
        }
    }
}
