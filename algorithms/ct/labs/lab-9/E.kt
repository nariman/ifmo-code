/*
 * Nariman Safiulin (woofilee)
 * File: E.kt
 * Created on: Dec 3, 2016
 */

import java.io.*
import java.util.*

private val PROBLEM_NAME = "decomposition"


data class Edge(val v: Int, val capacity: Int, val num: Int, var reverse: Edge? = null, var flow: Int = 0)

private fun solve(`in`: Scanner, out: BufferedWriter) {
    val n = `in`.int()
    val m = `in`.int()

    val g = Array(n) { ArrayList<Edge>() }
    var min = Array(n) { 0 }
    var ptr =  Array(n) { 0 }

    for (underscore in 1..m) {
        val u = `in`.int() - 1
        val v = `in`.int() - 1
        val c = `in`.int()

        val a = Edge(v, c, underscore)
        val b = Edge(u, 0, underscore)

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

            for ((v, capacity, num, reverse, flow) in edges) {
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
    var used = Array(n) { false }
    var ans = ArrayList<Edge>()
    var q: Int

    fun res(u: Int, min: Int): Int {
        used[u] = true
        if (u == n - 1) return min

        for (e in g[u]) {
            if (!used[e.v] && e.flow > 0) {
                val delta = res(e.v, Math.min(e.flow, min))

                if (delta != -1) {
                    e.flow -= delta
                    ans.add(e)

                    return delta
                }
            }
        }

        used[u] = false
        return -1
    }

    while (lim >= 1) {
        if (bfs(lim)) {
            ptr = Array(n) { 0 }
            while (dfs(0, lim)) {}
        } else {
            lim = lim shr 1
        }
    }

    q = res(0, Int.MAX_VALUE)
    while (q != -1) {
        sb.append(q).append(" ").append(ans.size).append(" ")
        ans.reversed().forEach { e -> sb.append(e.num).append(" ") }
        sb.append("\n")

        counter++
        used = Array(n) { false }
        ans = ArrayList<Edge>()

        q = res(0, Int.MAX_VALUE)
    }

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
