/**
 * Nariman Safiulin (woofilee)
 * File: D.kt
 * Created on: Mar 31, 2016
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
    val graph = Array(n) { ArrayList<Int>() }
    val status = Array(n) { false }
    val res = Array(n) { 0 }

    (1..m).forEach { i ->
        val v1 = `in`.nextInt() - 1
        val v2 = `in`.nextInt() - 1
        graph[v1].add(v2)
        graph[v2].add(v1)
    }

    fun bfs() {
        var current = ArrayList<Int>()
        var next = ArrayList<Int>()
        var lvl = 1
        current.add(0)
        res[0] = 0
        status[0] = true

        while(current.size > 0) {
            current.forEach { v ->
                graph[v].forEach { to ->
                    if (!status[to]) {
                        status[to] = true
                        next.add(to)
                        res[to] = lvl
                    }
                }
            }

            lvl++
            current = next
            next = ArrayList<Int>()
        }
    }

    bfs()
    (0..n - 1).forEach { i -> out.print("${res[i]} ") }
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
