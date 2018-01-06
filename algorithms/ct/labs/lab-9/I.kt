/*
 * Nariman Safiulin (woofilee)
 * File: I.kt
 * Created on: Dec 3, 2016
 */

import java.io.*
import java.util.*

private val PROBLEM_NAME = "multiassignment"


data class Edge(val u: Int, val v: Int, val capacity: Long, val cost: Long, var num: Int, var flow: Long = 0)

private fun solve(`in`: Scanner, out: BufferedWriter) {
    val MAX_VAL = Long.MAX_VALUE - Int.MAX_VALUE

    val n = `in`.int()
    val k = `in`.int()

    val graph = Array(n * 2 + 1) { ArrayList<Edge>() }

    fun edge(u: Int, v: Int, cy: Long, ct: Long) {
        val a = Edge(u, v, cy, ct, graph[v].size)
        val b = Edge(v, u, 0, -ct, graph[u].size)

        graph[u].add(a)
        graph[v].add(b)
    }

    for (i in 0..n - 1) for (j in 0..n - 1) edge(i + 1, n + j + 1, 1, `in`.long())

    for (i in 0..n - 1) {
        edge(0, i + 1, k.toLong(), 0)
        edge(n + i + 1, n - 1, k.toLong(), 0)
    }

    var res = 0L

    while (true) {
        val q = Array(n * 2 + 1) { 0 }
        val min = Array(n * 2 + 1) { MAX_VAL }
        val used = Array(n * 2 + 1) { 0 }
        val p = Array(n * 2 + 1) { 0 }
        val index = Array(n * 2 + 1) { 0 }

        min[0] = 0

        var s = 0
        var t = 1

        while (s != t) {
            val x = q[s++]
            if (s == n * 2 + 1) s = 0
            used[x] = 2

            for ((i, cur) in graph[x].withIndex()) {
                if (cur.flow != cur.capacity && min[x] + cur.cost < min[cur.v]) {
                    min[cur.v] = min[x] + cur.cost

                    if (used[cur.v] == 0) {
                        q[t++] = cur.v
                        t = if (t == n * 2 + 1) 0 else t
                    } else if (used[cur.v] == 2) {
                        s = if (s == 0) n * 2 else s - 1
                        q[s] = cur.v
                    }

                    used[cur.v] = 1
                    p[cur.v] = x
                    index[cur.v] = i
                }
            }
        }

        if (min[n * 2] == MAX_VAL) break
        var f: Long = MAX_VAL

        var cur = n * 2
        while (cur != 0) {
            val edge = graph[p[cur]][index[cur]]
            if (edge.capacity - edge.flow < f) f = edge.capacity.toLong() - edge.flow

            cur = p[cur]
        }

        cur = n * 2
        while (cur != 0) {
            graph[p[cur]][index[cur]].flow += f
            graph[cur][graph[p[cur]][index[cur]].num].flow -= f
            res += graph[p[cur]][index[cur]].cost * f

            cur = p[cur]
        }
    }

    out.write(res.toString())

    val left = Array(n) { HashSet<Int>() }
    var visited = Array(n) { false }

    for (i in 1..n) for (e in graph[i]) if (e.flow == 1L && e.capacity == 1L) left[i - 1].add(e.v - n - 1)

    for (underscore in 0..k - 1) {
        val matching = Array(n) { -1 }
        val visitedInGraph = Array(n) { false }

        for (i in 0..n - 1) {
            for (u in left[i]) {
                if (matching[u] == -1) {
                    matching[u] = i
                    visitedInGraph[i] = true
                    break
                }
            }
        }

        fun dfs(v: Int): Boolean {
            visited[v] = if (!visited[v]) true else return false

            for (u in left[v]) {
                if (matching[u] == -1 || dfs(matching[u])) {
                    matching[u] = v
                    return true
                }
            }

            return false
        }

        for (i in 0..n - 1) {
            if (visitedInGraph[i]) continue
            dfs(i)
            visited = Array(n) { false }
        }

        val rematching = Array(n) { 0 }

        for (i in rematching.indices) {
            rematching[matching[i]] = i
        }

        for (i in rematching.indices) {
            out.write((rematching[i] + 1).toString() + " ")
            left[i].remove(rematching[i])
        }

        out.write("\n")
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
