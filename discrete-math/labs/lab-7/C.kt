/*
 * Nariman Safiulin (woofilee)
 * File: C.kt
 * Created on: Jan 4, 2017
 */

import java.io.*
import java.util.*
import java.nio.file.*
import java.lang.Math.abs
import java.lang.Math.atan2
import java.lang.Math.toDegrees

/*
 * WA40
 */

private val PROBLEM_NAME = "areas"

private class Scanner(val r: BufferedReader) {
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

const val EPS = 0.00000001

private data class Point(val x: Double, val y: Double) : Comparable<Point> {
    fun angleTo(other: Point): Double {
        val theta = toDegrees(atan2(other.y - y, other.x - x))
        return if (theta < 0) theta + 360 else theta
    }

    override fun compareTo(other: Point): Int {
        if (x == other.x && y == other.y) return 0
        else if (x < other.x || x == other.x && y < other.y) return -1
        else return 1
    }

    override fun equals(other: Any?): Boolean = other is Point && compareTo(other) == 0
    override fun hashCode(): Int = 1299709 * x.hashCode() + 1159523 * y.hashCode()
    override fun toString(): String = "($x, $y)"
}

private data class Line(val p1: Point, val p2: Point) {
    fun intersect(o: Line): Point? {
        val x1 = p1.x
        val x2 = p2.x
        val x3 = o.p1.x
        val x4 = o.p2.x

        val y1 = p1.y
        val y2 = p2.y
        val y3 = o.p1.y
        val y4 = o.p2.y

        val denominator = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4)

        if (denominator == 0.0)
            return null

        return Point(
                ((x1 * y2 - y1 * x2) * (x3 - x4) - (x1 - x2) * (x3 * y4 - y3 * x4)) / denominator,
                ((x1 * y2 - y1 * x2) * (y3 - y4) - (y1 - y2) * (x3 * y4 - y3 * x4)) / denominator
        )
    }

    override fun toString(): String = "($p1, $p2)"
}

private fun solve(`in`: Scanner, out: BufferedWriter) {
    val n = `in`.int()
    val lines = Array(n) {
        Line(
                Point(`in`.double() + 100, `in`.double() + 100),
                Point(`in`.double() + 100, `in`.double() + 100)
        )
    }

    val graph = HashMap<Point, ArrayList<Point>>()
    var mapCounter = 0
    val mapPoint = HashMap<Point, Int>()
    val mapIndex = HashMap<Int, Point>()

    fun id(p: Point): Int = mapPoint.getOrPut(p, {
        val index = mapCounter++
        mapIndex[index] = p
        index
    })

    fun point(i: Int) = mapIndex[i]

    for (i in 0..n - 1) {
        val iss = ArrayList<Point>()

        for (j in 0..n - 1) {
            if (i == j || lines[i].p1 == lines[i].p2 || lines[j].p1 == lines[j].p2)
                continue

            lines[i].intersect(lines[j])?.let { iss.add(it) }
        }

        iss.sort()

//        println()
//        println("${lines[i]}: ${iss.joinToString(", ")}")

        for (j in 0..iss.size - 2) if (iss[j] != iss[j + 1]) {
            graph.getOrPut(iss[j], { ArrayList<Point>() }).add(iss[j + 1])
            graph.getOrPut(iss[j + 1], { ArrayList<Point>() }).add(iss[j])
        }
    }

    for ((p, list) in graph) graph.put(p, arrayListOf(*list.distinct().toTypedArray()))
    for ((p, list) in graph) list.sort { p1, p2 ->
        val a1 = p.angleTo(p1)
        val a2 = p.angleTo(p2)

        if (a1 < a2)
            return@sort -1
        else if (a1 > a2)
            return@sort 1
        else
            return@sort 0
    }

    val indexed = Array(graph.size) { ArrayList<Int>() }
    for ((p, list) in graph) indexed[id(p)].addAll(list.map(::id))

//    println()
//
//    graph.forEach { p, list ->
//        println("$p (${id(p)}): ${list.joinToString { "$it (${id(it)})" }}")
//    }
//
//    println()
//
//    for ((index, list) in indexed.withIndex()) {
//        println("$index: ${list.joinToString(", ")}")
//    }
//
//    println()

    val used = Array(indexed.size) { Array(indexed[it].size) { false } }
    val areas = ArrayList<Double>()

    for (i in 0..indexed.size - 1) {
        for (j in 0..indexed[i].size - 1) {
            if (used[i][j]) continue
            used[i][j] = true

            var a = i
            var b = indexed[i][j]
            val facet = ArrayList<Int>()
            var area = 0.0

            while (true) {
                facet.add(b)

//                println("${point(a)} -> ${point(b)}")

                area += point(a)!!.x * point(b)!!.y - point(a)!!.y * point(b)!!.x

                var e = indexed[b].indexOf(a)
                if (++e == indexed[b].size) e = 0

                if (used[b][e]) break
                used[b][e] = true

                a = b
                b = indexed[b][e]
            }

            area /= 2

//            println("$area")

            if (area < 0 && abs(area) >= EPS) areas.add(abs(area))
        }
    }

    out.write("${areas.size}\n${areas.sorted().joinToString("\n")}")
}

private fun naive(`in`: Scanner, out: BufferedWriter) {
    // Code naive solution here
}

private fun testgen(`in`: Scanner, out: BufferedWriter) {
    val random = Random()

    // Code test generation here
}

private fun run(name: String, ie: String, oe: String, f: (Scanner, BufferedWriter) -> Unit,
                files: Boolean = false) {
    if (files) print("$name running... ")
    val start = System.nanoTime()

    val `in`: Scanner
    val out: BufferedWriter

    if (files) {
        `in` = Scanner(File(PROBLEM_NAME + ".$ie").bufferedReader())
        out = File(PROBLEM_NAME + ".$oe").bufferedWriter()
    } else {
        `in` = Scanner(System.`in`.bufferedReader())
        out = System.out.bufferedWriter()
    }

    f(`in`, out)

    `in`.close()
    out.close()

    val end = System.nanoTime()
    if (files) println("OK, ${(end - start) / 1e6} ms.")
}

fun main(args: Array<String>) {
    val judge = false
    val naive = false

    val tests = 150

    val files = false or judge // we cannot use system i/o, if judge mode is on

    val ie = "in"
    val ae = "ans"
    val oe = "out"

    if (!judge) {
        run("Solution", ie, oe, ::solve, files)
    } else {
        repeat(tests) {
            println("Checking test #${it + 1}...")

            print("  - "); run("Test generation", ie, ie, ::testgen, files)
            if (naive) { print("  - "); run("Naive solution", ie, ae, ::naive, files) }
            print("  - "); run("Main solution", ie, oe, ::solve, files)

            if (naive) {
                print("  - Checking... ")

                if (Files.readAllLines(Paths.get(PROBLEM_NAME + ".out")) ==
                        Files.readAllLines(Paths.get(PROBLEM_NAME + ".ans"))) {
                    println("OK")
                } else {
                    println("WA")
                    return
                }
            }

            println()
        }
    }
}
