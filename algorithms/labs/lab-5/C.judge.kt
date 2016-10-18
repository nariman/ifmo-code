/**
 * Nariman Safiulin (woofilee)
 * File: C.judge.kt
 * Created on: Apr 13, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

private val PROBLEM_NAME = "transport"

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
data class Node(var x: Double, var y: Double)
data class Edge(val v1: Int, val v2: Int, val len: Double): Comparable<Edge> {
    override fun compareTo(other: Edge): Int =
            if (this.len - other.len < 0) -1 else if (this.len - other.len > 0) 1 else 0
}
data class MinEdge(var from: Int, var len: Double)

private fun generateAns(`in`: Scanner, out: PrintWriter) {
    val n = `in`.nextInt()
    val graph = Array(n) { Node(0.0, 0.0) }
    val edges = ArrayList<Edge>()
    var lengths = 0.0

    if (n == 1) {
        out.println(0)
        return
    }

    (0..n - 1).forEach { v -> graph[v].x = `in`.nextDouble() }
    (0..n - 1).forEach { v -> graph[v].y = `in`.nextDouble() }

    val r = `in`.nextDouble()
    val a = `in`.nextDouble()

    fun span() {
        val mins = Array(n) { MinEdge(-1, Double.MAX_VALUE) }
        val used = Array(n) { false }
        var current = 0
        var min: Int
        var minLen: Double
        used[0] = true

        fun len(x1: Double, y1: Double, x2: Double, y2: Double): Double =
                Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2))

        (1..n - 1).forEach {
            min = -1
            minLen = Double.MAX_VALUE

            (0..n - 1).forEach { other ->
                val calculated: Double = len(graph[current].x, graph[current].y, graph[other].x, graph[other].y)
                if (mins[other].len > calculated) {
                    mins[other].from = current
                    mins[other].len = calculated
                }

                if (!used[other] && mins[other].len < minLen) {
                    min = other
                    minLen = mins[min].len
                }
            }

            lengths += mins[min].len
            edges.add(Edge(mins[min].from, min, mins[min].len))
            used[min] = true
            current = min
        }
    }
    span()

    fun total(): Double {
        var cost = lengths * r
        var calculated = cost
        edges.sort { f, s ->
            if (f.len - s.len < 0) 1 else if (f.len - s.len > 0) -1 else 0
        }

        var cumulativeLength = 0.0
        var airports = 0
        edges.forEach { edge ->
            cumulativeLength += edge.len
            if (airports == 0) {
                airports += 2
            } else {
                airports++
            }

            if (airports * a + cost - cumulativeLength * r < calculated) {
                calculated = airports * a + cost - cumulativeLength * r
            }
        }

        return calculated
    }

    out.println(total())
}

private fun generateTest(out: PrintWriter) {
    val random = Random()

    val n = random.nextInt(150) + 1
    val r = random.nextInt(50) + java.lang.Double.parseDouble(random.nextDouble().toString().subSequence(0, 7).toString())
    val a = random.nextInt(50) + java.lang.Double.parseDouble(random.nextDouble().toString().subSequence(0, 7).toString())

    out.println(n)

    (1..n).forEach { out.print("" + random.nextInt(1000000) + " ") }
    out.println()
    (1..n).forEach { out.print("" + random.nextInt(1000000) + " ") }
    out.println()

    out.println(r)
    out.println(a)
}

private fun solve() {
    TaskC.main(null)
}

private fun judge() {
    var tested = 0
    var correct = 0

    (1..150).forEach { test ->
        tested++
        println("=== Checking test #${test} ===")
        print("Generating test... ")
        val testIn = PrintWriter(File(PROBLEM_NAME + ".in"))
        generateTest(testIn)
        testIn.close()
        println("OK")
        print("Generating correct solution... ")
        val ansIn = Scanner(File(PROBLEM_NAME + ".in"))
        val ansOut = PrintWriter(File(PROBLEM_NAME + ".ans"))
        generateAns(ansIn, ansOut)
        ansIn.close()
        ansOut.close()
        println("OK")
        print("Starting program... ")
        solve()
        println("OK - Program execution complete")

        print("Checking answer... ")
        val outString = Files.readAllLines(Paths.get(PROBLEM_NAME + ".out"))[0]
        val ansString = Files.readAllLines(Paths.get(PROBLEM_NAME + ".ans"))[0]

        println("Correct (judge) solution: ${ansString}")
        println("Solution for matching: ${outString}")
        println("Matching... ")

        if (Math.abs(java.lang.Double.parseDouble(outString) - java.lang.Double.parseDouble(ansString)) < 0.000001) {
            println("Test #${test} passed")
            correct++
        } else {
            println("Test #${test} failed !!!")
        }

        println()
    }

    println("=== Checking complete ===")
    println("$tested tested, $correct passed, ${tested - correct} failed")
}

fun main(args: Array<String>) {
    judge()
}
