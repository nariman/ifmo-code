/**
 * Nariman Safiulin (woofilee)
 * File: 5I.kt
 * Created on: Apr 25, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.*

private val PROBLEM_NAME = "num2part"

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
    val r = `in`.nextLong() + 1
    val d = Array(n + 1) { Array(n + 1) { 0L } }
    val res = StringBuilder()

    (1..n).forEach { i ->
        (1..n).forEach { j ->
            if (i != j) {
                (j..i).forEach { l ->
                    d[j][i] += d[l][i - j]
                }
            } else {
                d[i][j] = 1
            }
        }
    }

    fun next(i: Int, j: Int, r: Long) {
        if (j == i) {
            res.append(i)
            out.print(res)
            return
        }

        if (d[i][j] >= r) {
            res.append(i).append("+")
            next(i, j - i, r)
        } else {
            next(i + 1, j, r - d[i][j])
        }
    }

    next(1, n, r)
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
    