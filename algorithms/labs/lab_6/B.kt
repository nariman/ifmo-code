/**
 * Nariman Safiulin (woofilee)
 * File: B.kt
 * Created on: May 15, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.*

private val PROBLEM_NAME = "pathmgep"

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
    val s = `in`.nextInt() - 1
    val f = `in`.nextInt() - 1

    if (s == f) {
        out.print(0)
        return
    }

    val matrix = Array(n) { Array(n) { `in`.nextInt() } }
    val distances = Array(n) { Long.MAX_VALUE }
    val used = Array(n) { false }
    distances[s] = 0

    (0..n - 1).forEach { i ->
        var v = -1

        (0..n - 1).forEach { j ->
            if (!used[j] && (v == -1 || distances[j] < distances[v]))
                v = j
        }

        if (distances[v] == Long.MAX_VALUE)
            return@forEach

        used[v] = true
        (0..n - 1).forEach { j ->
            if (v != j && matrix[v][j] != -1 && distances[v] + matrix[v][j] < distances[j]) {
                distances[j] = distances[v] + matrix[v][j]
            }
        }
    }

    if (distances[f] == Long.MAX_VALUE) out.println(-1) else out.println(distances[f])
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
    