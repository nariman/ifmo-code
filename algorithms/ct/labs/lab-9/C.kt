/*
 * Nariman Safiulin (woofilee)
 * File: C.kt
 * Created on: Dec 3, 2016
 */

import java.io.*
import java.util.*

private val PROBLEM_NAME = "cut"


data class Edge(val v: Int, val capacity: Int, var reverse: Edge? = null, var flow: Int = 0)

private fun solve(`in`: Scanner, out: BufferedWriter) {
    val n = `in`.int()
    val m = `in`.int()

    val g = Array(n) { ArrayList<Edge>() }
    val used = Array(n) { false }
    var min = Array(n) { 0 }
    var ptr =  Array(n) { 0 }

    for (i in 0..m - 1) {
        val u = `in`.int() - 1
        val v = `in`.int() - 1
        val c = `in`.int()

        val a = Edge(v, c)
        val b = Edge(u, c)

        a.reverse = b
        b.reverse = a

        g[u].add(a)
        g[v].add(b)
    }

    fun bfs(lim: Int): Boolean {
        min = Array(n) { -1 }
        min[0] = 0

        val queue = LinkedList<Int>()
        queue.add(0)

        while (!queue.isEmpty()) {
            val u = queue.pop()
            val edges = g[u]

            for ((v, capacity, reverse, flow) in edges) {
                if (min[v] == -1 && capacity - flow >= lim) {
                    min[v] = min[u] + 1
                    queue.add(v)
                }
            }
        }

        return min[n - 1] != -1
    }

    fun dfs(v: Int, f: Int): Boolean {
        if (v == n - 1) return true
        if (f == 0) return false

        val edges = g[v]

        while (ptr[v] < edges.size) {
            val e = edges[ptr[v]]

            if (min[e.v] == min[v] + 1 && e.capacity - e.flow >= f) {
                if (dfs(e.v, f)) {
                    e.flow += f
                    e.reverse!!.flow -= f
                    return true
                }
            }

            ptr[v]++
        }

        return false
    }

    var lim = 1 shl 30
    var counter = 0
    val sb = StringBuilder()

    fun res(u: Int) {
        used[u] = true
        sb.append(u + 1).append(" ")
        counter++
        for ((v, capacity, reverse, flow) in g[u])
            if (!used[v] && capacity > 0 && capacity > flow)
                res(v)
    }

    while (lim >= 1) {
        if (bfs(lim)) {
            ptr = Array(n) { 0 }
            while (dfs(0, lim)) {}
        } else {
            lim = lim shr 1
        }
    }

    res(0)
    out.write(counter.toString() + "\n" + sb)
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
