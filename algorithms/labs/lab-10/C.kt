/*
 * Nariman Safiulin (woofilee)
 * File: C.kt
 * Created on: Jan 11, 2017
 */

import java.io.*
import java.util.*
import java.nio.file.*

private val PROBLEM_NAME = "decomposition"

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

private fun solve(`in`: Scanner, out: BufferedWriter) {
    val n = `in`.int()
    val graph = Array(n) { ArrayList<Int>() }
    val parent = Array(n) { 0 }
    val centroids = Array(n) { false }
    val sizes = Array(n) { 0 }

    for (i in 1..n - 1) {
        val u = `in`.int() - 1
        val v = `in`.int() - 1

        graph[u].add(v)
        graph[v].add(u)
    }

    fun dfs(u: Int, p: Int) { // u is current node, p is parent node
        sizes[u] = 1

        for (v in graph[u]) if (v != p && !centroids[v]) {
            dfs(v, u)
            sizes[u] += sizes[v] + 1
        }
    }

    fun decompose(u: Int): Int {
        dfs(u, -1)
        val nodes: Int = sizes[u] / 2 // (number of nodes in current subtree excludes u) / 2
        var c = u
        var p = -1

        loop@ while (true) {
            for (v in graph[c]) if (v != p && !centroids[v] && sizes[v] > nodes) {
                p = c
                c = v
                continue@loop
            }

            break@loop
        }

        centroids[c] = true

        for (v in graph[c]) if (!centroids[v])
            parent[decompose(v)] = c + 1

        return c
    }

    decompose(0)
    out.write(parent.joinToString(" "))
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
