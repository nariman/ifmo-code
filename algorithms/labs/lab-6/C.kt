/**
 * Nariman Safiulin (woofilee)
 * File: C.kt
 * Created on: May 15, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.*

private val PROBLEM_NAME = "pathsg"

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
    val m = `in`.nextInt()
    val distances = Array(n) { i -> Array(n) { j -> if (i == j) 0L else Int.MAX_VALUE.toLong() } }

    (0..m - 1).forEach {
        val f = `in`.nextInt() - 1
        val s = `in`.nextInt() - 1
        distances[f][s] = Math.min(distances[f][s], `in`.nextLong())
    }

    (0..n - 1).forEach { k -> (0..n - 1).forEach { i -> (0..n - 1).forEach { j ->
        distances[i][j] = Math.min(distances[i][j], distances[i][k] + distances[k][j])
    }}}

    (0..n - 1).forEach { i ->
        (0..n - 1).forEach { j ->
            out.print("${distances[i][j]} ")
        }
        out.println()
    }
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
    