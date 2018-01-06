/*
 * Nariman Safiulin (woofilee)
 * File: A.kt
 * Created on: Dec 3, 2016
 */

import java.io.*
import java.util.*

private val PROBLEM_NAME = "matching"


private fun solve(`in`: Scanner, out: BufferedWriter) {
    val n = `in`.int()
    val m = `in`.int()
    val k = `in`.int()

    val graph = Array(n + m) { Array(n + m) { false } }
    val matching = Array(n + m) { -1 }
    var visited: Array<Boolean>

    repeat(k, {
        val a = `in`.int() - 1
        val b = n + `in`.int() - 1

        graph[a][b] = true
        graph[b][a] = true
    })

    0.until(n).map { v ->
        visited = Array(n + m) { false }

        fun dfs(v: Int): Boolean {
            visited[v] = if (!visited[v]) true else return false

            0.until(n + m).filter { u -> graph[v][u] }.forEach { u ->
                if (matching[u] == -1 || dfs(matching[u])) {
                    matching[u] = v
                    return true
                }
            }

            return false
        }

        dfs(v)
    }.count { it }.let { out.write(it.toString()) }
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
