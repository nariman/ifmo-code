/**
 * Nariman Safiulin (woofilee)
 * File: A.kt
 * Created on: May 15, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.*

private val PROBLEM_NAME = "pathbge1"

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
    val edges = Array(n) { HashSet<Int>(n) }
    val distances = Array(n) { Int.MAX_VALUE }
    val used = Array(n) { false }

    (0..m - 1).forEach { i ->
        val f = `in`.nextInt() - 1
        val s = `in`.nextInt() - 1
        edges[f].add(s)
        edges[s].add(f)
    }

    val q = ArrayDeque<Int>()
    q.addLast(0)
    distances[0] = 0

    while (!q.isEmpty()) {
        val v = q.removeFirst()
        if (used[v]) continue
        used[v] = true
        val distance = distances[v] + 1
        edges[v].forEach { i ->
            distances[i] = Math.min(distances[i], distance)
            q.addLast(i)
        }
    }

    distances.forEach { d -> out.print("$d ") }
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
    