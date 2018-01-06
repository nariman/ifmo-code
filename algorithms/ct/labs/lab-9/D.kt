/*
 * Nariman Safiulin (woofilee)
 * File: D.kt
 * Created on: Dec 3, 2016
 */

import java.io.*
import java.util.*

private val PROBLEM_NAME = "paths"


private fun solve(`in`: Scanner, out: BufferedWriter) {
    val n = `in`.int()
    val m = `in`.int()

    val graph = Array(n) { ArrayList<Int>() }
    val matching = Array(n) { -1 }
    var visited = Array(n) { false }
    val visitedInGraph = Array(n) { false }

    repeat(m, { graph[`in`.int() - 1].add(`in`.int() - 1) })

    fun dfs(v: Int): Boolean {
        visited[v] = if (!visited[v]) true else return false

        for (i in 0..graph[v].size - 1) {
            val u = graph[v][i]

            if (matching[u] == -1 || dfs(matching[u])) {
                matching[u] = v
                return true
            }
        }

        return false
    }

    loop@ for (v in 0..n - 1) {
        for (i in 0..graph[v].size - 1) {
            val u = graph[v][i]

            if (matching[u] == -1) {
                matching[u] = v
                visitedInGraph[v] = true

                continue@loop
            }
        }
    }

    for (v in 0..n - 1) {
        if (visitedInGraph[v]) continue
        dfs(v)
        visited = Array(n) { false }
    }

    var counter = 0

    loop@ for (v in 0..n - 1) {
        visited[v] = if (!visited[v]) true else continue

        if (matching[v] != v) {
            var next = matching[v]
            while (next != -1) {
                visited[next] = if (!visited[next]) true else continue@loop
                next = matching[next]
            }
        }

        counter++
    }

    out.write(counter.toString())
}

private class Scanner(f: File) {
    val r = f.bufferedReader()
    var t = StringTokenizer("")

    fun hasNext(): Boolean {
        while (!t.hasMoreTokens()) t = StringTokenizer(r.readLine() ?: return false)
        return true
    }

    fun next(): String = if (hasNext()) t.nextToken() else ""
    fun int(): Int = next().toInt()
    fun long(): Long = next().toLong()
    fun double(): Double = next().toDouble()
    fun close() = r.close()
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = File(PROBLEM_NAME + ".out").bufferedWriter()

    solve(`in`, out)

    `in`.close()
    out.close()
}
