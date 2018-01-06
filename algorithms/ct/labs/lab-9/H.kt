/*
 * Nariman Safiulin (woofilee)
 * File: H.kt
 * Created on: Dec 3, 2016
 */

import java.io.*
import java.util.*

private val PROBLEM_NAME = "mincost"


data class Edge(val v: Int, val capacity: Long, val cost: Long, var num: Int, var flow: Long = 0)

private fun solve(`in`: Scanner, out: BufferedWriter) {
    val n = `in`.int()
    val m = `in`.int()

    val graph = Array(n) { ArrayList<Edge>() }

    for (i in 1..m) {
        val u = `in`.int() - 1
        val v = `in`.int() - 1
        val cy = `in`.long()
        val ct = `in`.long()

        val a = Edge(v, cy, ct, graph[v].size)
        val b = Edge(u, 0, -ct, graph[u].size)

        graph[u].add(a)
        graph[v].add(b)
    }

    var res = 0L

    while (true) {
        val q = Array(n) { 0 }
        val min = Array(n) { Long.MAX_VALUE }
        val used = Array(n) { 0 }
        val p = Array(n) { 0 }
        val index = Array(n) { 0 }

        min[0] = 0

        var s = 0
        var t = 1

        while (s != t) {
            val x = q[s++]
            if (s == n) s = 0
            used[x] = 2

            for ((i, cur) in graph[x].withIndex()) {
                if (cur.flow != cur.capacity && min[x] + cur.cost < min[cur.v]) {
                    min[cur.v] = min[x] + cur.cost

                    if (used[cur.v] == 0) {
                        q[t++] = cur.v
                        t = if (t == n) 0 else t
                    } else if (used[cur.v] == 2) {
                        s = if (s == 0) n - 1 else s - 1
                        q[s] = cur.v
                    }

                    used[cur.v] = 1
                    p[cur.v] = x
                    index[cur.v] = i
                }
            }
        }

        if (min[n - 1] == Long.MAX_VALUE) break
        var f: Long = Long.MAX_VALUE

        var cur = n - 1
        while (cur != 0) {
            val edge = graph[p[cur]][index[cur]]
            if (edge.capacity - edge.flow < f) f = edge.capacity.toLong() - edge.flow

            cur = p[cur]
        }

        cur = n - 1
        while (cur != 0) {
            graph[p[cur]][index[cur]].flow += f
            graph[cur][graph[p[cur]][index[cur]].num].flow -= f
            res += graph[p[cur]][index[cur]].cost * f

            cur = p[cur]
        }
    }

    out.write(res.toString())
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
