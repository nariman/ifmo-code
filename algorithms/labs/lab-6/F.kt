/**
 * Nariman Safiulin (woofilee)
 * File: F.kt
 * Created on: May 15, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.*

private val PROBLEM_NAME = "negcycle"

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

data class DirectedEdge(val from: Int, val to: Int, val weight: Int)

private fun solve(`in`: Scanner, out: PrintWriter) {
    val NON_EXIST_EDGE = 1000000000

    val n = `in`.nextInt()
    val edges = ArrayList<DirectedEdge>()
    val distances = Array(n) { 0 }
    val from = Array(n) { -1 }

    (0..n - 1).forEach { i ->
        (0..n - 1).forEach { j ->
            val w = `in`.nextInt()
            if (w != NON_EXIST_EDGE) {
                edges.add(DirectedEdge(i, j, w))
            }
        }
    }

    var end = -1
    (0..n - 1).forEach { i ->
        end = -1
        edges.forEach { edge ->
            if (distances[edge.to] > distances[edge.from] + edge.weight) {
                distances[edge.to] = Math.max(-NON_EXIST_EDGE, distances[edge.from] + edge.weight)
                from[edge.to] = edge.from
                end = edge.to
            }
        }
    }

    if (end == -1) {
        out.println("NO")
        return
    }

    var start = end
    repeat(n, { start = from[start] })
    val path = ArrayList<Int>()

    var current = start
    while (true) {
        path.add(current)
        if (current == start && path.size > 1)
            break
        current = from[current]
    }

    out.println("YES\n${path.size}")
    (path.size - 1 downTo 0).forEach { i -> out.print("${path[i] + 1} ") }
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
    