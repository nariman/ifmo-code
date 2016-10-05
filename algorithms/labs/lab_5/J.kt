/**
 * Nariman Safiulin (woofilee)
 * File: J.kt
 * Created on: Apr 24, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.*

private val PROBLEM_NAME = "bridges"

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

private data class Edge(val number: Int, val v1: Int, val v2: Int) {
    fun from(to: Int): Int  = if (to == v1) v2 else v1
    fun to(from: Int): Int  = if (from == v1) v2 else v1
}

private fun solve(`in`: Scanner, out: PrintWriter) {
    val n = `in`.nextInt()
    val m = `in`.nextInt()

    val vertex = Array(n) { ArrayList<Int>() }
    val edges = ArrayList<Edge>(m)
    val res = ArrayList<Int>()
    val used = Array(n) { false }
    val inTime = Array(n) { Int.MAX_VALUE }
    val oTime = Array(n) { Int.MAX_VALUE }
    var currTime = 0;

    (0..m - 1).forEach { i ->
        val f = `in`.nextInt() - 1
        val s = `in`.nextInt() - 1
        edges.add(Edge(i, f, s))
        vertex[f].add(i)
        vertex[s].add(i)
    }

    fun dfs(v: Int, fromEdgeID: Int) {
        used[v] = true;
        inTime[v] = currTime;
        oTime[v] = currTime++;

        vertex[v].forEach { edgeID ->
            if (fromEdgeID == edgeID) return@forEach
            val to = edges[edgeID].to(v)
            if (used[to]) {
                oTime[v] = Math.min(oTime[v], inTime[to])
            } else {
                dfs(to, edgeID)
                oTime[v] = Math.min(oTime[v], oTime[to])
                if (oTime[to] > inTime[v])
                    res.add(edges[edgeID].number)
            }
        }
    }

    (0..n - 1).forEach { v -> if (!used[v]) dfs(v, -1) }
    res.sort { f, s -> f - s }

    out.println(res.size)
    res.forEach { e ->
        out.print("${e + 1} ")
    }
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
    