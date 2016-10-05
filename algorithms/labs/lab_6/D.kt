/**
 * Nariman Safiulin (woofilee)
 * File: D.kt
 * Created on: May 15, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.*

private val PROBLEM_NAME = "pathbgep"

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

data class DirectedEdge(val to: Int, val weight: Long) : Comparable<DirectedEdge> {
    override fun compareTo(other: DirectedEdge): Int = when {
        this.weight - other.weight < 0 -> -1
        this.weight - other.weight > 0 -> 1
        else -> 0
    }
}

private fun solve(`in`: Scanner, out: PrintWriter) {
    val n = `in`.nextInt()
    val m = `in`.nextInt()
    val distances = Array(n) { Long.MAX_VALUE }
    val edges = Array(n) { ArrayList<DirectedEdge>(n) }

    (0..m - 1).forEach {
        val f = `in`.nextInt() - 1
        val s = `in`.nextInt() - 1
        val w = `in`.nextLong()
        edges[f].add(DirectedEdge(s, w))
        edges[s].add(DirectedEdge(f, w))
    }

    distances[0] = 0;
    val q = PriorityQueue<DirectedEdge>()
    q.add(DirectedEdge(0, 0))

    while (!q.isEmpty()) {
        val v = q.peek().to
        if (q.poll().weight > distances[v]) continue

        edges[v].forEach { edge ->
            val to = edge.to
            val w = edge.weight
            if (distances[v] + w < distances[to]) {
                distances[to] = distances[v] + w
                q.add(DirectedEdge(to, distances[to]))
            }

        }
    }

    (0..n - 1).forEach { i -> out.print("${distances[i]} ") }
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
    