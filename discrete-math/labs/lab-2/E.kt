/**
 * Nariman Safiulin (woofilee)
 * File: E.kt
 * Created on: May 04, 2016
 *
 * May The 4th Be With You...
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.*

private val PROBLEM_NAME = "markchain"

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
    var matrix = Array(n) { Array(n) { `in`.nextDouble() } }

    repeat(10, {
        matrix = Array(n) { i ->
            Array(n) { j ->
                (0..n - 1).map { k ->
                    matrix[i][k] * matrix[k][j]
                }.sum()
            }
        }
    })

    out.println(matrix[0].joinToString("\n"))
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
