/*
 * Nariman Safiulin (narimansafiulin)
 * File: F.kt
 * Created on: Mar 1, 2018
 */

import java.io.*
import java.util.*
import java.nio.file.*

private val PROBLEM_NAME = "f"
private val INPUT_NAME = "input"
private val OUTPUT_NAME = "output"

/**
 * Говнокод и только
 */

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

private data class Quad(
    public val first: Int,
    public val second: Int,
    public val third: Char,
    public val fourth: Quad?
)

private fun solve(`in`: Scanner, out: BufferedWriter) {
    val n = `in`.int()
    val m = `in`.int()

    val matrix = Array(n + 2) { Array(m + 2) { false } }

    var s: Pair<Int, Int> = Pair(1, 1)
    var t: Pair<Int, Int> = Pair(1, 1)

    for (i in 1..n) {
        val line = `in`.string()

        for (j in 1..m) {
            when (line[j - 1]) {
                'S'  -> { s = Pair(i, j); matrix[i][j] = true }
                'T'  -> { t = Pair(i, j); matrix[i][j] = true }
                '.'  -> matrix[i][j] = true
                else -> false
            }
        }
    }

    val queue = LinkedList<Quad>()

    queue.add(Quad(s.first, s.second, 'X', null))
    matrix[s.first][s.second] = false

    while (!queue.isEmpty()) {
        val v = queue.poll()

        if (v.first == t.first && v.second == t.second) {
            val sb = StringBuilder()
            var u = v

            while (u.fourth != null) {
                sb.append(u.third)
                u = u.fourth
            }

            val path = sb.reverse().toString()

            out.write("${path.length}\n$path")
            return
        }

        for (st in listOf(
            Quad(v.first - 1, v.second, 'U', v),
            Quad(v.first, v.second - 1, 'L', v),
            Quad(v.first + 1, v.second, 'D', v),
            Quad(v.first, v.second + 1, 'R', v)
        )) {
            if (matrix[st.first][st.second]) {
                matrix[st.first][st.second] = false
                queue.add(st)
            }
        }
    }

    out.write("-1")
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
        `in` = Scanner(File(INPUT_NAME + ".$ie").bufferedReader())
        out = File(OUTPUT_NAME + ".$oe").bufferedWriter()
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

    val ie = "txt"
    val ae = "ans"
    val oe = "txt"

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
