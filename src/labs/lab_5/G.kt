/**
 * Nariman Safiulin (woofilee)
 * File: G.kt
 * Created on: Apr 08, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.*

private val PROBLEM_NAME = "shortpath"

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

private data class DirectedEdge(val from: Int, val to: Int, val weight: Int)

private fun solve(`in`: Scanner, out: PrintWriter) {
    val n = `in`.nextInt()
    val m = `in`.nextInt()
    val s = `in`.nextInt() - 1
    val t = `in`.nextInt() - 1

    val edges = Array(m) { DirectedEdge(`in`.nextInt() - 1, `in`.nextInt() - 1, `in`.nextInt()) }
    val outEdges = Array(n) { ArrayList<Int>() }
    val inEdges = Array(n) { ArrayList<Int>() }
    val needleStatus = Array(n) { false }
    val needleGraph = ArrayList<Int>()
    val needleInPoints = Array(n) { 0 }
    (0..edges.size - 1).forEach { edgeId -> outEdges[edges[edgeId].from].add(edgeId) }
    (0..edges.size - 1).forEach { edgeId -> inEdges[edges[edgeId].to].add(edgeId) }

    // Space Needle on the mind...
    fun findNeedle(v: Int) {
        needleInPoints[v]++
        if (needleStatus[v]) return
        needleStatus[v] = true
        outEdges[v].forEach { edgeId -> findNeedle(edges[edgeId].to) }
        needleGraph.add(v)
    }
    findNeedle(s)

//    needleGraph.forEach { v -> println(v + 1) }
//    return

    if (!needleGraph.contains(t)) {
        out.println("Unreachable")
        return
    }

    val short = Array(n) { Int.MAX_VALUE }
    val pathStatus = Array(n) { false }
    short[s] = 0
    needleInPoints[s] = 0

    fun findPath(v: Int) {
        if (pathStatus[v]) return
        if (needleInPoints[v] != 0) return
        pathStatus[v] = true
        outEdges[v].forEach { edgeId ->
            if (short[edges[edgeId].to] > short[v] + edges[edgeId].weight) {
                short[edges[edgeId].to] = short[v] + edges[edgeId].weight
            }
            needleInPoints[edges[edgeId].to]-- // once for each v, important!
        }
    }

    // PCMS2 don't have an actual compilator... => don't have an reverse method on arrays...
//    needleGraph.reverse()

    while(!pathStatus[t]) {
        (needleGraph.size - 1 downTo 0).forEach { i -> findPath(needleGraph[i]) }
    }

    out.println(short[t])
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
