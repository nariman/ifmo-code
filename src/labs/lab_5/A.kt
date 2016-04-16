/**
 * Nariman Safiulin (woofilee)
 * File: A.kt
 * Created on: Mar 31, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.*

private val PROBLEM_NAME = "spantree"

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

private fun solve(`in`: Scanner, out: PrintWriter) {
    val n = `in`.nextInt()
    val graph = Array(n) { Node(`in`.nextInt(), `in`.nextInt()) }
    val mins = Array(n) { Int.MAX_VALUE }
    val used = Array(n) { false }
    var res = 0.0

    var currentV = 0
    var minV: Int
    var minVlen: Int
    used[0] = true

    (1..n - 1).forEach {
        minV = -1
        minVlen = Int.MAX_VALUE

        (0..n - 1).forEach { otherV ->
            mins[otherV] = Math.min(
                    mins[otherV],
                    (graph[currentV].x - graph[otherV].x) * (graph[currentV].x - graph[otherV].x) +
                            (graph[currentV].y - graph[otherV].y) * (graph[currentV].y - graph[otherV].y)
            )

            if (!used[otherV] && mins[otherV] < minVlen) {
                minV = otherV
                minVlen = mins[otherV]
            }
        }

        res += Math.sqrt(minVlen.toDouble())
        used[minV] = true
        currentV = minV
    }

    out.println(res)
}

private fun generate(`in`: PrintWriter) {
    `in`.println(5000)
    (1..5000).forEach { i -> `in`.println("$i $i") }
    `in`.close()
}

fun main(args: Array<String>) {
    //    generate(PrintWriter(File(PROBLEM_NAME + ".in")))

    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
