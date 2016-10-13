/**
 * Nariman Safiulin (woofilee)
 * File: O.kt
 * Created on: Mar 21, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.*

private val PROBLEM_NAME = "umbrella"

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

private data class Pair(var cost: Int, var from: Int)

private fun solve(`in`: Scanner, out: PrintWriter) {
    val n = `in`.nextInt()
    val m = `in`.nextInt()
    val x = Array(n) { `in`.nextInt() }
    val c = Array(m) { `in`.nextInt() }
    val d = Array(n + 1) { Integer.MAX_VALUE }
    d[0] = 0

    Arrays.sort(x)
    var min = Integer.MAX_VALUE

    for (i in m - 1 downTo 0) {
        c[i] = Math.min(c[i], min)
        min = c[i]
    }

    for (i in 1..n) {
        for (j in i - 1 downTo 0) {
            d[i] = Math.min(d[i], d[j] + c[x[i - 1] - x[j]])
        }
    }

    out.println(d[n])
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
