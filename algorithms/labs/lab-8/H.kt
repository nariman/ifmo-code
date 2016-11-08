/**
 * Nariman Safiulin (woofilee)
 * File: H.kt
 * Created on: Oct 23, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.*

private val PROBLEM_NAME = "ancestor"

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

    fun next(): String = if (hasNext()) st.nextToken() else ""
    fun nextInt(): Int = Integer.parseInt(next())
    fun nextLong(): Long = java.lang.Long.parseLong(next())
    fun nextDouble(): Double = java.lang.Double.parseDouble(next())
    fun close() = br.close()
}

private fun solve(`in`: Scanner, out: PrintWriter) {
    val n = `in`.nextInt() + 1
    val logn = (Math.log(n.toDouble()) / Math.log(2.0)).toInt() + 1
    val power = Array(logn + 1) { 1 shl it }
    val graph = Array(n) { ArrayList<Int>() }
    val p = Array(n) { 0 }
    val d = Array(n) { 0 }
    val dp = Array(n) { Array(logn + 1) { 0 } }

    (1..n - 1).forEach { graph[`in`.nextInt()].add(it) }

    fun dfs(v: Int, e: Int) {
        d[v] = e
        graph[v].forEach {
            p[it] = v
            dp[it][0] = v
            dfs(it, e + 1)
        }
    }

    dfs(0, 0)

    (1..logn).forEach { i ->
        (1..n - 1).forEach { v ->
            dp[v][i] = dp[dp[v][i - 1]][i - 1]
        }
    }

    fun parent(v: Int, u: Int): Int {
        if (d[v] >= d[u]) return 0

        var u = u

        (logn downTo 0).forEach {
            if (d[u] - d[v] >= power[it])
                u = dp[u][it]
        }

        return if (v == u) 1 else 0
    }

    (1..`in`.nextInt()).forEach { out.println(parent(`in`.nextInt(), `in`.nextInt())) }
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
