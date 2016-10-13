/**
 * Nariman Safiulin (woofilee)
 * File: A.kt
 * Created on: Apr 1, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.*

private val PROBLEM_NAME = "spantree2"

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

data class Node(val x: Int, val y: Int)
data class Edge(val v1: Int, val v2: Int, val weight: Int): Comparable<Edge> {
    override fun compareTo(other: Edge): Int = this.weight - other.weight
}

private class DisjointSetForest(var size: Int) {
    var random = Random()
    var data: Array<Int> = Array(size) { 0 }

    init {
        while (--size > 0) data[size] = size
    }

    fun makeSet(v: Int) = { data[v] = v }
    fun find(v: Int): Int = if (data[v] == v) v else { data[v] = find(data[v]); data[v] }
    fun unite(x: Int, y: Int) = if (random.nextInt() % 2 == 0) data[find(x)] = find(y) else data[find(y)] = find(x)
}

private fun solve(`in`: Scanner, out: PrintWriter) {
    val n = `in`.nextInt()
    val m = `in`.nextInt()
    val forest = DisjointSetForest(n)
    val edges = Array(m) { Edge(`in`.nextInt() - 1, `in`.nextInt() - 1, `in`.nextInt()) }
    var res = 0

    Arrays.sort(edges)

    (0..edges.size - 1).forEach { i ->
        if (forest.find(edges[i].v1) != forest.find(edges[i].v2)) {
            res += edges[i].weight
            forest.unite(edges[i].v1, edges[i].v2)
        }
    }

    out.println(res)
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
