/*
 * Nariman Safiulin (narimansafiulin)
 * File: E.kt
 * Created on: Oct 8, 2017
 */

import java.io.*
import java.util.*
import java.nio.file.*

private const val PROBLEM_NAME = "radixsort"

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

private fun solve(`in`: Scanner, out: BufferedWriter) {
    val n = `in`.int()
    val m = `in`.int()
    val k = `in`.int()

    val a = Array(n) { `in`.string() }
    val b = Array(n) { "" }

    fun radixSort(l: Int, r: Int) {
        if (r - l == 0) return

        radixSort(l, (l + r) / 2)
        radixSort((l + r) / 2 + 1, r)
    
        var lt = l
        var rt = (l + r) / 2 + 1
        var i = l

        while (lt <= (l + r) / 2 && rt <= r) {
            var e = 0

            for (j in m - k..m - 1)
                if      (a[lt][j] < a[rt][j]) { e = -1; break }
                else if (a[lt][j] > a[rt][j]) { e = 1;  break }

            if (e == -1 || e == 0) { b[i] = a[lt]; lt++ }
            else                   { b[i] = a[rt]; rt++ }
            i++
        }

        while (lt <= (l + r) / 2) { b[i] = a[lt]; lt++; i++ }
        while (rt <= r)           { b[i] = a[rt]; rt++; i++ }
    
        for (i in l..r) a[i] = b[i]
    }

    radixSort(0, n - 1)
    out.write("${a.joinToString("\n")}")
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
