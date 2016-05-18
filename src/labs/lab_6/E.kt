/**
 * Nariman Safiulin (woofilee)
 * File: E.kt
 * Created on: May 15, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.*

private val PROBLEM_NAME = "path"

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

data class DirectedEdge(val from: Int, val to: Int, val weight: Long)

private fun solve(`in`: Scanner, out: PrintWriter) {
    val INF = Long.MAX_VALUE

    val n = `in`.nextInt()
    val m = `in`.nextInt()
    val s = `in`.nextInt() - 1
    val matrix = Array(n) { Array<DirectedEdge?>(n) { null } }
    val edges = ArrayList<DirectedEdge>()
    val graph = Array(n) { ArrayList<DirectedEdge>() }
    val distances = Array(n) { INF }
    val last = Array(n) { false }
    val from = Array(n) { -1 }

    repeat(m, {
        val f = `in`.nextInt() - 1
        val t = `in`.nextInt() - 1
        val w = `in`.nextLong()

        if (matrix[f][t] == null || w < matrix[f][t]!!.weight) {
            matrix[f][t] = DirectedEdge(f, t, w)
        }
    })

    (0..n - 1).forEach { f ->
        (0..n - 1).forEach { t ->
            if (matrix[f][t] != null) {
                edges.add(matrix[f][t]!!)
                graph[f].add(matrix[f][t]!!)
            }
        }
    }

    distances[s] = 0

    var end = -1
    (0..n - 1).forEach { i ->
        end = -1
        edges.forEach { edge ->
            if (distances[edge.from] != INF && distances[edge.to] > distances[edge.from] + edge.weight) {
                distances[edge.to] = Math.max(-INF, distances[edge.from] + edge.weight)
                from[edge.to] = edge.from
                end = edge.to
            }
        }
    }

    if (end != -1) {
        var start = end     
        val used = Array(n) { false }
        fun dfs(v: Int) {
            last[v] = true
            used[v] = true
            graph[v].forEach { edge -> if (!used[edge.to]) dfs(edge.to) }
        }

        repeat(n, { start = from[start] })
        dfs(start)
    }

    (0..n - 1).forEach { i ->
        when {
            distances[i] == INF -> out.println("*")
            last[i] -> out.println("-")
            else -> out.println(distances[i])
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
    