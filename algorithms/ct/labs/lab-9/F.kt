/*
 * Nariman Safiulin (woofilee)
 * File: F.kt
 * Created on: Dec 3, 2016
 */

import java.io.*
import java.util.*

private val PROBLEM_NAME = "circulation"


data class Edge(val v: Int, val capacityMin: Int, val capacityMax: Int, var reverse: Edge? = null, var flow: Int = 0)

private fun solve(`in`: Scanner, out: BufferedWriter) {
    val n = `in`.int() + 2
    val m = `in`.int()

    val g = Array(n) { ArrayList<Edge>() }
    val r = ArrayList<Edge>()
    var min = Array(n) { 0 }
    var ptr =  Array(n) { 0 }

    for (underscore in 1..m) {
        val u = `in`.int()
        val v = `in`.int()
        val l = `in`.int()
        val c = `in`.int()

        val a = Edge(v, c - l, l)
        val b = Edge(u, 0, l)
        val e = Edge(n - 1, l, Int.MIN_VALUE)
        val f = Edge(u, 0, Int.MIN_VALUE)
        val i = Edge(v, l, Int.MIN_VALUE)
        val j = Edge(0, 0, Int.MIN_VALUE)

        a.reverse = b
        b.reverse = a
        e.reverse = f
        f.reverse = e
        i.reverse = j
        j.reverse = i

        g[u].add(a)
        g[v].add(b)
        g[u].add(e)
        g[n - 1].add(f)
        g[0].add(i)
        g[v].add(j)

        r.add(a)
    }

    fun bfs(lim: Int): Boolean {
        min = Array(n) { -1 }
        min[0] = 0

        val queue = LinkedList<Int>()
        queue.add(0)

        while (!queue.isEmpty()) {
            val u = queue.pop()
            val edges = g[u]

            for ((v, capacityMin, capacityMax, reverse, flow) in edges) {
                if (min[v] == -1 && capacityMin - flow >= lim) {
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

            if (min[e.v] == min[v] + 1 && e.capacityMin - e.flow >= f) {
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

    while (lim >= 1) {
        if (bfs(lim)) {
            ptr = Array(n) { 0 }
            while (dfs(0, lim)) {}
        } else {
            lim = lim shr 1
        }
    }

    if (g[0].any { e -> e.flow < e.capacityMin }) {
        out.write("NO\n")
    } else {
        out.write("YES\n")
        r.forEach { e -> out.write("${e.flow + e.capacityMax}\n") }
    }
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
