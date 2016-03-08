/**
 * Nariman Safiulin (woofilee)
 * File: C.kt
 * Created on: Mar 09, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.*

private val PROBLEM_NAME = "knapsack"

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
    val ma = IntArray(n + 1)
    val c = IntArray(n + 1)
    val dp = Array(n + 1) { IntArray(m + 1) }
    val st = Stack<Int>()

    (1..n).forEach { i -> ma[i] = `in`.nextInt() }
    (1..n).forEach { i -> c[i] = `in`.nextInt() }
    (1..n).forEach { k -> (1..m).map { s ->
        dp[k][s] = if (s < ma[k]) dp[k - 1][s] else Math.max(dp[k - 1][s], dp[k - 1][s - ma[k]] + c[k])
    } }

    fun lam(k: Int, s: Int) {
        if (dp[k][s] == 0) return
        if (dp[k - 1][s] == dp[k][s]) {
            lam(k - 1, s)
        } else  {
            lam(k - 1, s - ma[k])
            st.push(k)
        }
    }

    lam(n, m)
    out.println(st.size)
    st.forEach { i -> out.print("$i ") }
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
