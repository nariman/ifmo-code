/**
 * Nariman Safiulin (woofilee)
 * File: I.kt
 * Created on: Mar 20, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.StringTokenizer

private val PROBLEM_NAME = "monsters"

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
    val n: Int = `in`.nextInt()
    val d = Array(n) { `in`.nextLong() }
    val c = Array(n) { `in`.nextLong() }

    val dp = Array(n + 1) { Array(2 * n + 2) { -1L } }
    dp[0][0] = 0L
    for (i in 0..(n - 1)) {
        for (j in 0..(2 * i)) {
            if (dp[i][j] == -1L) continue
            if (dp[i][j] >= d[i]) dp[i + 1][j] = Math.max(dp[i + 1][j], dp[i][j])
            if (c[i] == 1L) {
                dp[i + 1][j + 1] = Math.max(dp[i + 1][j + 1], dp[i][j] + d[i])
            } else {
                dp[i + 1][j + 2] = Math.max(dp[i + 1][j + 2], dp[i][j] + d[i])
            }
        }
    }

    var i = -1
    while (dp[n][++i] <= 0);
    out.println(i)
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
