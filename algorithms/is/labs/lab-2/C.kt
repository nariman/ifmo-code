/*
 * Nariman Safiulin (narimansafiulin)
 * File: C.kt
 * Created on: Oct 5, 2017
 */

import java.io.*
import java.util.*
import java.nio.file.*

private const val PROBLEM_NAME = "inversions"

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
    val array = IntArray(n) { `in`.int() }
    var inversions: Long = 0

    fun mergesort(array: IntArray, startIndex: Int, middleIndex: Int, endIndex: Int) {
        val left = array.copyOfRange(startIndex, middleIndex + 1)
        val right = array.copyOfRange(middleIndex + 1, endIndex + 1)
        var i = 0
        var j = 0

        for (k in startIndex..endIndex) {
            if (i < left.size && (j >= right.size || left[i] <= right[j])) {
                array[k] = left[i]
                i++
            } else {
                array[k] = right[j]
                j++
                if (i < left.size) inversions += middleIndex - startIndex + 1 - i;
            }
        }
    }

    fun mergesort(array: IntArray, startIndex: Int, endIndex: Int) {
        if (endIndex - startIndex <= 0) return
        val middleIndex = Math.floor((startIndex + endIndex) / 2.0).toInt()
 
        mergesort(array, startIndex, middleIndex)
        mergesort(array, middleIndex + 1, endIndex)
        mergesort(array, startIndex, middleIndex, endIndex)
    }

    fun mergesort(array: IntArray) = mergesort(array, 0, array.size - 1)

    mergesort(array)
    out.write("$inversions")
}

private fun naive(`in`: Scanner, out: BufferedWriter) {
    val n = `in`.int()
    val array = IntArray(n) { `in`.int() }
    var inversions: Long = 0

    for (i in 0..n - 1) for (j in i + 1..n - 1)
        if (array[i] > array[j]) inversions++

    out.write("$inversions")
}

private fun testgen(`in`: Scanner, out: BufferedWriter) {
    val random = Random()

    val n = random.nextInt(100000) + 1
    print("N=$n ")

    val array = IntArray(n) { random.nextInt(2 * 1000000000) - 1000000000 }
    out.write("$n\n")
    out.write("${array.joinToString(" ")}")
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
