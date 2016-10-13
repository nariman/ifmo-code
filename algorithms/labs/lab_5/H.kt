/**
 * Nariman Safiulin (woofilee)
 * File: H.kt
 * Created on: Apr 1, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.*

private val PROBLEM_NAME = "hamiltonian"

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
    val status = Array(n) { false }
    val res = ArrayList<Int>()

    (1..m).forEach { i -> graph[`in`.nextInt() - 1].add(`in`.nextInt() - 1) }

    fun dfs(v: Int) {
        if (status[v]) return
        status[v] = true
        (0..graph[v].size - 1).forEach { i -> dfs(graph[v][i]) }
        res.add(v)
    }

    (0..n - 1).forEach { i -> dfs(i) }
    (res.size - 1 downTo 1).forEach { i ->
        if (!graph[res[i]].contains(res[i - 1])) {
            out.print("NO")
            return
        }
    }
    out.print("YES")
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
