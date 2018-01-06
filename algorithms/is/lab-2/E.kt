/*
 * Nariman Safiulin (narimansafiulin)
 * File: E.kt
 * Created on: Oct 8, 2017
 */

import java.io.*
import java.util.*
import java.nio.file.*

private val PROBLEM_NAME = "kth"

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
    val k = `in`.int()
    val a = `in`.int()
    val b = `in`.int()
    val c = `in`.int()
    val array = IntArray(n) { 0 }

    array[0] = `in`.int()

    if (n == 1) {
        out.write("${array[0]}")
        return
    }

    array[1] = `in`.int()

    if (n == 2) {
        if (k == 1) out.write("${Math.min(array[0], array[1])}")
        else out.write("${Math.max(array[0], array[1])}")
        return
    }

    fun kitten(l: Int, r: Int, k: Int): Int {
        var i = l
        var j = r
        val pivot = array[(l + r) / 2]
        var t = 0

        while (i <= j) {
            while (array[i] < pivot) i++
            while (array[j] > pivot) j--
            if (i <= j) {
                t = array[i]; array[i] = array[j]; array[j] = t
                i++; j--
            }
        }

        when {
            l <= k && k <= j -> return kitten(l, j, k)
            i <= k && k <= r -> return kitten(i, r, k)
            else -> return array[k]
        }
    }

    for (i in 2..n - 1) array[i] = a * array[i - 2] + b * array[i - 1] + c
    out.write("${kitten(0, n - 1, k - 1)}")
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