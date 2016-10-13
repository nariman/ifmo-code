/**
 * Nariman Safiulin (woofilee)
 * File: 6F.kt
 * Created on: Apr 26, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.*

private val PROBLEM_NAME = "nextmultiperm"

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

    fun swap(i: Int, j: Int) {
        val t = e[i]
        e[i] = e[j]
        e[j] = t
    }

    fun reverse(from: Int, to: Int) = (0..(to - from + 1) / 2 - 1).forEach { i -> swap(from + i, to - i) }

    var i = n - 2
    while (i >= 0 && e[i] >= e[i + 1]) i--

    if (i >= 0) {
        var j = i + 1
        while (j < n - 1 && e[j + 1] > e[i]) j++
        swap(i, j)
        reverse(i + 1, n - 1)
        e.forEach { r -> out.print("$r ") }
    } else
        repeat(n, { out.print("0 ") })
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
    