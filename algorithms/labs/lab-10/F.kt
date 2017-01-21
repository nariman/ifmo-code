/*
 * Nariman Safiulin (woofilee)
 * File: F.kt
 * Created on: Jan 14, 2017
 */

import java.io.*
import java.util.*
import java.nio.file.*

private const val PROBLEM_NAME = "close-vertices"

private class Scanner(val r: BufferedReader) {
    var t = StringTokenizer("")

    fun close() = r.close()
    fun has(): Boolean {
        while (!t.hasMoreTokens()) t = StringTokenizer(r.readLine() ?: return false)
        return true
    }

    fun string(): String = if (has()) t.nextToken() else ""
    fun int(): Int = string().toInt()
    fun long(): Long = string().toLong()
    fun double(): Double = string().toDouble()
}

private data class Edge(val to: Int, val weight: Int)

private const val MAX_DEPTH = 20
private const val P = 1 shl 17

private fun solve(`in`: Scanner, out: BufferedWriter) {
    val n = `in`.int()
    val l = `in`.int()
    val w = `in`.int()

    val graph = Array(n) { ArrayList<Edge>() }
    val parent = Array(n) { 0 }
    val depths = Array(n) { 0 }
    val sizes = Array(n) { 0 }

    val t = Array(P * 2 + 10) { 0 }
    var process = ArrayList<Pair<Int, Int>>()
    var result = 0L

    for (i in 1..n - 1) {
        val u = `in`.int() - 1
        val w = `in`.int()

        graph[u].add(Edge(i, w))
        graph[i].add(Edge(u, w))
    }

    fun dfs(u: Int, p: Int) { // u is current node, p is parent node
        sizes[u] = 1

        for ((v, w) in graph[u]) if (v != p && depths[v] == 0) {
            dfs(v, u)
            sizes[u] += sizes[v] + 1
        }
    }

    fun decompose(u: Int, depth: Int): Int {
        dfs(u, -1)
        val nodes: Int = sizes[u] / 2 // (number of nodes in current subtree excludes u) / 2
        var c = u
        var p = -1

        loop@ while (true) {
            for ((v, w) in graph[c]) if (v != p && depths[v] == 0 && sizes[v] > nodes) {
                p = c
                c = v
                continue@loop
            }

            break@loop
        }

        depths[c] = depth

        for ((v, w) in graph[c]) if (depths[v] == 0)
            parent[decompose(v, depth + 1)] = c + 1

        return c
    }

    fun add(position: Int, value: Int) {
        var position = position + P
        t[position] += value

        while (position > 1) {
            position = position shr 1
            t[position] += value
        }
    }

    fun sum(k: Int): Int {
        var res = 0
        var p = 1 + P
        var k = k + P

        while (p < k) {
            if (p and 1 != 0) res += t[p++]
            if (k and 1 == 0) res += t[k--]
            p = p shr 1
            k = k shr 1
        }

        return if (p == k) res + t[p] else res
    }

    fun dfsCalc(u: Int, p: Int, depth: Int, edges: Int, weight: Int, c: Boolean) {
        process.add(Pair(weight, edges))
        if (c && edges <= l && weight <= w)
            result += 2

        for ((v, w) in graph[u]) if (v != p && depths[v] > depth)
            dfsCalc(v, u, depth, edges + 1, weight + w, c)
    }

    fun hCalc(c: Int) {
        process.sort { f, s -> 
            if (f.first != s.first) f.first.compareTo(s.first) else f.second.compareTo(s.second)
        }

        val s = process.size
        var p = s - 1

        for (i in 0..s - 1) add(process[i].second, 1)
        for (i in 0..s - 1) {
            while (p >= 0 && process[p].first + process[i].first > w) {
                add(process[p].second, -1)
                p--
            }

            result += c * sum(l - process[i].second)
        }

        while (p >=0) add(process[p--].second, -1)
        process = ArrayList()
    }

    fun calc(u: Int) {
        val depth = depths[u]
        process = ArrayList()

        for ((v, w) in graph[u]) if (depths[v] > depth)
            dfsCalc(v, -1, depth, 1, w, true)

        hCalc(1)

        for ((v, w) in graph[u]) if (depths[v] > depth) {
            dfsCalc(v, -1, depth, 1, w, false)
            hCalc(-1)
        }
    }

    decompose(0, 1)
    for (u in 0..n - 1) calc(u)

    print("Result: ${result / 2} ")
    out.write("${result / 2}")
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

    val files = true or judge // we cannot use system i/o, if judge mode is on

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

                if (Files.readAllLines(Paths.get(PROBLEM_NAME + ".$oe")) ==
                        Files.readAllLines(Paths.get(PROBLEM_NAME + ".$ae"))) {
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
