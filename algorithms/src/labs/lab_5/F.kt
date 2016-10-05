/**
 * Nariman Safiulin (woofilee)
 * File: F.kt
 * Created on: Mar 31, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.*

private val PROBLEM_NAME = "topsort"

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
    val graph = Array(n) { ArrayList<Int>() }
    val status = Array(n) { 0 }
    val res = ArrayList<Int>()

    (1..m).forEach { i -> graph[`in`.nextInt() - 1].add(`in`.nextInt() - 1) }

    fun dfs(v: Int): Boolean {
        if (status[v] == 1) return true
        if (status[v] == 2) return false
        status[v] = 1
        (0..graph[v].size - 1).forEach { i -> if (dfs(graph[v][i])) return true }
        res.add(v)
        status[v] = 2
        return false
    }

    for (i in 0..n - 1) {
        if (dfs(i)) {
            out.print("-1")
            return
        }
    }

    (res.size - 1 downTo 0).forEach { i -> out.print("${res[i] + 1} ") }
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
