/**
 * Nariman Safiulin (woofilee)
 * File: 5E.kt
 * Created on: Apr 25, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.*

private val PROBLEM_NAME = "num2brackets"

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
    var k = `in`.nextLong() + 1

    val d = Array(n * 2 + 1) { Array(n + 1) { 0L } }

    (0..n * 2).forEach { i ->
        (0..n).forEach { j ->
            d[i][j] = 0L;
        }
    }
    d[0][0] = 1L

    (0..n * 2 - 1).forEach { i ->
        (0..n).forEach { j ->
            if (j + 1 <= n)
                d[i + 1][j + 1] += d[i][j]
            if (j > 0)
                d[i + 1][j - 1] += d[i][j]
        }
    }

    var ans = StringBuilder()
    var depth = 0
    (n * 2 - 1 downTo 0).forEach { i ->
        if (depth + 1 <= n && d[i][depth + 1] >= k) {
            ans.append("(");
            ++depth;
        } else {
            ans.append(")");
            if (depth + 1 <= n)
                k -= d[i][depth + 1];
            --depth;
        }
    }

    out.println(ans);
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
    