/**
 * Nariman Safiulin (woofilee)
 * File: D.kt
 * Created on: Oct 23, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.*

private val PROBLEM_NAME = "lca3"

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

    (1..n - 1).forEach { graph[`in`.nextInt()].add(it) }

    val parent = Array(n) { 0 }
    val depth = Array(n) { 0 }
    val dp = Array(n) { Array(logn + 1) { 0 } }
    val queue = Array(n + 10) { 0 }

    fun calculate(v: Int) {
        var size = -1

        fun dfs(v: Int, e: Int) {
            depth[v] = e
            queue[++size] = v
            for (it in graph[v]) {
                parent[it] = v
                dp[it][0] = v
                dfs(it, e + 1)
            }
        }

        dfs(v, depth[v])

        for (i in 1..logn) {
            for (j in 0..size) {
                dp[queue[j]][i] = dp[dp[queue[j]][i - 1]][i - 1]
            }
        }
    }

    calculate(0)

    fun lca(v: Int, u: Int): Int {
        if (depth[v] > depth[u]) return lca(u, v)

        var v = v
        var u = u

        for (it in logn downTo 0) {
            if (depth[u] - depth[v] >= power[it])
                u = dp[u][it]
        }

        if (v == u) return v

        for (it in logn downTo 0) {
            if (dp[v][it] != dp[u][it]) {
                v = dp[v][it]
                u = dp[u][it]
            }
        }

        return parent[v]
    }

    var ans = 0
    for (it in 1..`in`.nextInt()) {
        val t = `in`.nextInt()
        val u = (`in`.nextInt() - 1 + ans) % (n - 1) + 1
        val v = (`in`.nextInt() - 1 + ans) % (n - 1) + 1

        when (t) {
            0 -> {
                ans = lca(u, v)
                out.println(ans)
            }
            1 -> {
                graph[v].add(u)
                parent[u] = v
                depth[u] = depth[v] + 1
                dp[u][0] = v
                calculate(u)
            }
        }
    }
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
