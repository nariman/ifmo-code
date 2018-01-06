/**
 * Nariman Safiulin (woofilee)
 * File: J.kt
 * Created on: Oct 23, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.*

private val PROBLEM_NAME = "turtles"

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
    var n = 0
    val logn: Int
    val power: Array<Int>
    val depth: Array<Int>
    val dp: Array<Array<Int>>
    val cost: Array<Array<Int>>

    val h = `in`.nextInt()
    val w = `in`.nextInt()
    val matrix = Array(h) { Array(w) { false } }

    var indicator: Boolean
    val horizontal = Array(h) { Array(w) { 0 } }
    val vertical = Array(h) { Array(w) { 0 } }

    (0..h - 1).forEach { i -> `in`.next().forEachIndexed { j, c -> if (c == '#') matrix[i][j] = true } }

    (0..h - 1).forEach { i ->
        indicator = false

        (0..w - 1).forEach { j ->
            if (matrix[i][j]) {
                indicator = true
                horizontal[i][j] = n
            } else if (indicator) {
                n++
                indicator = false
            }
        }

        if (indicator) n++
    }

    (0..w - 1).forEach { i ->
        indicator = false

        (0..h - 1).forEach { j ->
            if (matrix[j][i]) {
                indicator = true
                vertical[j][i] = n
            } else if (indicator) {
                n++
                indicator = false
            }
        }

        if (indicator) n++
    }

    logn = (Math.log(n.toDouble()) / Math.log(2.0)).toInt() + 1
    power = Array(logn + 1) { 1 shl it }
    depth = Array(n) { 0 }
    dp = Array(n) { Array(logn + 1) { 0 } }
    cost = Array(n) { Array(logn + 1) { 0 } }

    val graph = Array(n) { HashSet<Int>() }

    (0..h - 1).forEach { i ->
        (0..w - 1).forEach { j ->
            if (matrix[i][j]) {
                val u = horizontal[i][j]
                val v = vertical[i][j]
                if (!graph[u].contains(v)) {
                    graph[u].add(v)
                    graph[v].add(u)
                }
            }
        }
    }

    val visited = Array(n) { false }

    fun dfs(v: Int, e: Int) {
        visited[v] = true
        depth[v] = e
        cost[v][0] = 1
        graph[v].forEach {
            if (!visited[it]) {
                dp[it][0] = v
                dfs(it, e + 1)
            }
        }
    }

    dfs(0, 0)

    (1..logn).forEach { i ->
        (1..n - 1).forEach { v ->
            dp[v][i] = dp[dp[v][i - 1]][i - 1]
            cost[v][i] = cost[v][i - 1] + cost[dp[v][i - 1]][i - 1]
        }
    }

    fun lca(u: Int, v: Int): Int {
        if (depth[u] > depth[v]) return lca(v, u)

        var u = u
        var v = v
        var w = 0

        (logn downTo 0).forEach {
            if (depth[v] - depth[u] >= power[it]) {
                w += cost[v][it]
                v = dp[v][it]
            }
        }

        if (u == v) return w

        (logn downTo 0).forEach {
            if (dp[u][it] != dp[v][it]) {
                w += cost[u][it] + cost[v][it]
                u = dp[u][it]
                v = dp[v][it]
            }
        }

        return w + cost[u][0] + cost[v][0]
    }

    (1..`in`.nextInt()).forEach {
        val x1 = `in`.nextInt() - 1
        val y1 = `in`.nextInt() - 1
        val x2 = `in`.nextInt() - 1
        val y2 = `in`.nextInt() - 1

        out.println(
                Math.min(
                        Math.min(
                                lca(vertical[x1][y1], vertical[x2][y2]),
                                lca(vertical[x1][y1], horizontal[x2][y2])
                        ),
                        Math.min(
                                lca(horizontal[x1][y1], vertical[x2][y2]),
                                lca(horizontal[x1][y1], horizontal[x2][y2])
                        )
                )
        )
    }
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
