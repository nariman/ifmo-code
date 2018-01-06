/**
 * Nariman Safiulin (woofilee)
 * File: E.kt
 * Created on: Oct 23, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.*

private val PROBLEM_NAME = "dynamic"

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
    while (true) {
        val n = `in`.nextInt()
        if (n == 0) break

        val logn = (Math.log(n.toDouble()) / Math.log(2.0)).toInt() + 1
        val power = Array(logn + 1) { 1 shl it }
        val graph = Array(n) { LinkedList<Int>() }

        for (i in 1..n - 1) {
            val u = `in`.nextInt() - 1
            val v = `in`.nextInt() - 1
            graph[u].add(v)
            graph[v].add(u)
        }

        val parent = Array(n) { 0 }
        val depth = Array(n) { 0 }
        val dp = Array(n) { Array(logn + 1) { 0 } }
        val visited = Array(n) { false }

        fun dfs(v: Int, e: Int) {
            visited[v] = true
            depth[v] = e

            for (u in graph[v]) {
                if (!visited[u]) {
                    parent[u] = v
                    dp[u][0] = v
                    dfs(u, e + 1)
                }
            }
        }

        dfs(0, 0)

        for (i in 1..logn) {
            for (v in 1..n - 1) {
                dp[v][i] = dp[dp[v][i - 1]][i - 1]
            }
        }

        fun lca(v: Int, u: Int): Int {
            if (depth[v] > depth[u]) return lca(u, v)

            var v = v
            var u = u

            for (i in logn downTo 0) {
                if (depth[u] - depth[v] >= power[i])
                    u = dp[u][i]
            }

            if (v == u) return v

            for (i in logn downTo 0) {
                if (dp[v][i] != dp[u][i]) {
                    v = dp[v][i]
                    u = dp[u][i]
                }
            }

            return parent[v]
        }


        val m = `in`.nextInt()
        var root = 0

        for (i in 1..m) {
            when (`in`.next()) {
                "?" -> {
                    val u = `in`.nextInt() - 1
                    val v = `in`.nextInt() - 1

                    val a = lca(u, v)
                    val b = lca(root, u)
                    val c = lca(root, v)

                    var r = a
                    if (depth[b] > depth[r]) r = b
                    if (depth[c] > depth[r]) r = c

                    out.println(r + 1)
                }
                "!" -> root = `in`.nextInt() - 1
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
