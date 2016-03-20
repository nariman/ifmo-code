/**
 * Nariman Safiulin (woofilee)
 * File: L.kt
 * Created on: Mar 20, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.StringTokenizer

private val PROBLEM_NAME = "salesman"

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
    val w = Array(n) { Array(n) { Int.MAX_VALUE } }
    val d = Array(1 shl n) { Array(n) { Int.MAX_VALUE } }

    for (i in 0..m - 1) {
        var a = `in`.nextInt() - 1
        var b = `in`.nextInt() - 1
        w[a][b] = `in`.nextInt()
        w[b][a] = w[a][b]
    }
    (0..n - 1).forEach { i -> d[1 shl i][i] = 0 }

    for (i in 0..(1 shl n) - 1) {
        for (j in 0..n - 1) {
            if (d[i][j] == Int.MAX_VALUE) continue
            for (k in 0..n - 1) {
                val off = 1 shl k
                if (i and off == 0 && w[j][k] != Int.MAX_VALUE) {
                    d[i or off][k] = Math.min(d[i or off][k], d[i][j] + w[j][k])
                }
            }
        }
    }

    var min = Int.MAX_VALUE
    val off = d.size - 1
    (0..n - 1).forEach { i -> min = Math.min(min, d[off][i]) }
    out.println(if (min == Int.MAX_VALUE) -1 else min)
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
