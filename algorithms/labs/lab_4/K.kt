/**
 * Nariman Safiulin (woofilee)
 * File: K.kt
 * Created on: Mar 20, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.StringTokenizer

private val PROBLEM_NAME = "boolean"

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
    val v = `in`.nextInt()
    val d = Array(n + 1) { IntArray(2) }
    val t = Array(n + 1) { Array(2) { 0 } }

    for (i in 1..((n - 1) / 2)) {
        t[i][0] = `in`.nextInt()
        t[i][1] = `in`.nextInt()
    }

    for (i in ((n + 1) / 2)..n) {
        t[i][0] = `in`.nextInt()
        d[i][t[i][0]] = 0
        d[i][t[i][0] xor 1] = 1 shl 16
    }

    for (i in n downTo 1 step 2) {
        val c = t[i / 2][0]

        if (t[i / 2][1] == 1 && d[i][c] + d[i - 1][c] > Math.min(d[i][c], d[i - 1][c]) + 1) {
            d[i / 2][c] = Math.min(d[i][c], d[i - 1][c]) + 1
        } else {
            d[i / 2][c] = d[i][c] + d[i - 1][c]
        }

        d[i / 2][(c + 1) % 2] = Math.min(d[i][(c + 1) % 2], d[i - 1][(c + 1) % 2])
    }

    out.println(if (d[1][v] >= 1 shl 16) "IMPOSSIBLE" else d[1][v])
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
