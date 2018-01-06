/*
 * Nariman Safiulin (woofilee)
 * File: B.kt
 * Created on: Dec 3, 2016
 */

import java.io.*
import java.util.*

private val PROBLEM_NAME = "maxflow"


private fun solve(`in`: Scanner, out: BufferedWriter) {
    val n = `in`.int()
    val m = `in`.int()

    val capacity = Array(n) { Array(n) { 0 } }
    val flow = Array(n) { Array(n) { 0 } }
    var min: Array<Int> = Array(n) { Int.MAX_VALUE }
    var pushed: Array<Int> = Array(n) { 0 }

    repeat(m, {
        val a = `in`.int() - 1
        val b = `in`.int() - 1
        val c = `in`.int()

        capacity[a][b] = c
    })

    fun bfs(): Boolean {
        min = Array(n) { Int.MAX_VALUE }
        min[0] = 0

        val queue = LinkedList<Int>()
        queue.add(0)

        while (!queue.isEmpty()) {
            val u = queue.pop()
            (0..n - 1).forEach { v ->
                if (min[v] == Int.MAX_VALUE && flow[u][v] < capacity[u][v]) {
                    min[v] = min[u] + 1
                    queue.add(v)
                }
            }
        }

        return min[n - 1] != Int.MAX_VALUE
    }

    fun dfs(v: Int, c: Int): Int {
        if (v == n - 1 || c == 0) return c

        (pushed[v]..n - 1).forEach { u ->
            if (min[u] == min[v] + 1) {
                val delta = dfs(u, Math.min(c, capacity[v][u] - flow[v][u]))
                if (delta != 0) {
                    flow[v][u] += delta
                    flow[u][v] -= delta
                    return delta
                }
            }
        }

        return 0
    }

    var max = 0
    while (bfs()) {
        pushed = Array(n) { 0 }

        do {
            val delta = dfs(0, Int.MAX_VALUE)
            max += delta
        } while (delta != 0)
    }

    out.write(max.toString())
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
