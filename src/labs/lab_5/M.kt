/**
 * Nariman Safiulin (woofilee)
 * File: M.kt
 * Created on: Apr 26, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.*

private val PROBLEM_NAME = "cond"

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

private data class DirectedEdge(val from: Int, val to: Int)

private fun solve(`in`: Scanner, out: PrintWriter) {
    val n = `in`.nextInt()
    val m = `in`.nextInt()
    val edges = Array(m) { DirectedEdge(`in`.nextInt() - 1, `in`.nextInt() - 1) }
    val outEdges = Array(n) { ArrayList<Int>() }
    val inEdges = Array(n) { ArrayList<Int>() }
    var used: Array<Boolean>
    (0..edges.size - 1).forEach { edgeID -> outEdges[edges[edgeID].from].add(edgeID) }
    (0..edges.size - 1).forEach { edgeID -> inEdges[edges[edgeID].to].add(edgeID) }

    val firstGraph = ArrayList<Int>()
    val secondGraph = ArrayList<Int>()
    val graph = IntArray(n)
    val components = ArrayList<ArrayList<Int>>()

    used = Array(n) { false }

    fun first(v: Int) {
        used[v] = true
        outEdges[v].forEach { edgeID ->
            if (!used[edges[edgeID].to])
                first(edges[edgeID].to)
        }
        firstGraph.add(v)
    }

    (0..n - 1).forEach { v -> if (!used[v]) first(v) }
    used = Array(n) { false }

    fun second(v: Int) {
        used[v] = true
        secondGraph.add(v)
        inEdges[v].forEach { edgeID ->
            if (!used[edges[edgeID].from])
                second(edges[edgeID].from)
        }
    }

    var graphCounter = 0
    (0..n - 1).forEach { i ->
        val v = firstGraph[n - i - 1]
        if (!used[v]) {
            second(v)
            components.add(ArrayList<Int>())
            secondGraph.forEach { v ->
                graph[v] = graphCounter
                components[graphCounter].add(v)
            }
            secondGraph.clear()
            graphCounter++
        }
    }

    var edgesCounter = 0
    val table =  Array(graphCounter) { ArrayList<Int>(graphCounter) }

    components.forEach { component ->
        component.forEach { from ->
            outEdges[from].forEach { edgeID ->
                if (graph[from] != graph[edges[edgeID].to] && !table[graph[from]].contains(graph[edges[edgeID].to])) {
                    table[graph[from]].add(graph[edges[edgeID].to])
                    table[graph[edges[edgeID].to]].add(graph[from]) // safety from fucking mysterious
                    edgesCounter++
                }
            }
        }
    }

    out.println("$graphCounter $edgesCounter")
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
    