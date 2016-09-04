/**
 * Nariman Safiulin (woofilee)
 * File: 6B.kt
 * Created on: Apr 25, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.*

private val PROBLEM_NAME = "nextperm"

private class Scanner(file: File) {
    val br = BufferedReader(FileReader(file))
    var st = StringTokenizer("")

    fun hasNext(): Boolean {
        while (!st.hasMoreTokens()) {
            val line = br.readLine() ?: return false
            st = StringTokenizer(line)
        }
        return true
    }

    fun next(): String? = if (hasNext()) st.nextToken() else null
    fun nextInt(): Int = Integer.parseInt(next())
    fun nextLong(): Long = java.lang.Long.parseLong(next())
    fun nextDouble(): Double = java.lang.Double.parseDouble(next())
    fun close() = br.close()
}

private fun solve(`in`: Scanner, out: PrintWriter) {
    val n = `in`.nextInt()
    val e = Array(n) { `in`.nextInt() }
    var a = Array(n) { i -> e[i] }

    fun swap(i: Int, j: Int) {
        val t = a[i]
        a[i] = a[j]
        a[j] = t
    }

    fun reverse(from: Int, to: Int) = (0..(to - from + 1) / 2 - 1).forEach { i -> swap(from + i, to - i) }

    fun prev() {
        (n - 2 downTo 0).forEach { i ->
            if (a[i] > a[i + 1]) {
                var max = i + 1
                (i + 1..n - 1).forEach { j ->
                    if (a[j] > a[max] && a[j] < a[i])
                        max = j
                }
                swap(i, max)
                reverse(i + 1, n - 1)
                a.forEach { r -> out.print("$r ") }
                return
            }
        }
        (1..n).forEach { out.print("0 ") }
        return
    }

    fun next() {
        (n - 2 downTo 0).forEach { i ->
            if (a[i] < a[i + 1]) {
                var min = i + 1
                (i + 1..n - 1).forEach { j ->
                    if (a[j] < a[min] && a[j] > a[i])
                        min = j
                }
                swap(i, min)
                reverse(i + 1, n - 1)
                a.forEach { r -> out.print("$r ") }
                return
            }
        }
        (1..n).forEach { out.print("0 ") }
        return
    }

    prev()
    out.println()
    a = Array(n) { i -> e[i] }
    next()
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
    