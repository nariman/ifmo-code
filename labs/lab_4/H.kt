/**
 * Nariman Safiulin (woofilee)
 * File: H.kt
 * Created on: Mar 20, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.StringTokenizer

private val PROBLEM_NAME = "tapcoder"

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
    val d = Array(n) { `in`.nextInt() }
    val x = `in`.nextInt()
    val brownMinBorder = 2200
    var max = -1
    val dp = Array(n + 1) { Array(2200) { -1 } }
    dp[0][x] = 0

    for (i in 1..n) {
        for (j in 0..(2200 - 1)) {
            if (dp[i - 1][j] == -1) continue

            var increase = j + d[i - 1]
            val decrease = Math.max(0, j - d[i - 1])
            dp[i][decrease] = Math.max(dp[i][decrease], dp[i - 1][j])

            if (increase >= brownMinBorder) {
                if (i == n) continue
                increase = Math.max(0, increase - d[i])
                if (Math.max(0, increase) < brownMinBorder) {
                    dp[i + 1][increase] = Math.max(dp[i + 1][increase], dp[i - 1][j] + 2)
                }
            } else {
                dp[i][increase] = Math.max(dp[i][increase], dp[i - 1][j]);
            }
        }
    }

    (0..(2200 - 1)).forEach { i -> max = Math.max(max, dp[n][i]) }
    for (i in 0..(2200 - 1)) {
        if(dp[n - 1][i] == -1) continue
        val t = i + d[n - 1];
        if(t >= 2200)
            max = Math.max(max, dp[n - 1][i] + 1);
    }

    out.println(max)
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
