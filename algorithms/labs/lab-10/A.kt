/*
 * Nariman Safiulin (woofilee)
 * File: A.kt
 * Created on: Jan 10, 2017
 */

/*
 * Here is Kotlin code from Mark Gordon's C++ code
 * http://usaco.org/current/data/sol_grassplant.html
 */

import java.io.*
import java.util.*
import java.nio.file.*

private val PROBLEM_NAME = "grassplant"

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

private data class Pair(var first: Int, var second: Int)
private data class Part(var chain: Int, var l: Int, var r: Int)

private fun solve(`in`: Scanner, out: BufferedWriter) {
    val n = `in`.int()
    val m = `in`.int()

    val graph = Array(n) { ArrayList<Int>() }
    val chain = Array(n) { ArrayList<Int>() }
    val parent = Array(n) { 0 }
    val size = Array(n) { 0 }
    val index = Array(n) { 0 }
    val position = Array(n) { 0 }

    fun build() {
        fun inner(u: Int, p: Int): Pair {
            var nodes = 1
            var result = Pair(0, -1)

            for (i in 0..graph[u].size - 1) {
                val v = graph[u][i]
                if (v == p) continue

                val res = inner(v, u)
                nodes += res.first
                result = if (result.first > res.first) result else res
            }

            if (result.second == -1) result.second = u
            chain[result.second].add(u)
            parent[result.second] = p

            result.first = nodes
            return result
        }

        inner(0, -1)

        for (i in 0..n - 1) {
            size[i] = chain[i].size

            for (j in 0..chain[i].size - 1) {
                index[chain[i][j]] = i
                position[chain[i][j]] = j
            }
        }
    }

    fun chains(u: Int, v: Int): LinkedList<Part> {
        var cu = LinkedList<Pair>()
        var cv = LinkedList<Pair>()

        var u = u
        var v = v

        while (u != -1) {
            cu.add(Pair(index[u], position[u]))
            u = parent[index[u]]
        }

        while (v != -1) {
            cv.add(Pair(index[v], position[v]))
            v = parent[index[v]]
        }

        cu.reverse()
        cv.reverse()

        if (cu.size > cv.size) {
            val t = cu
            cu = cv
            cv = t
        }

        var i = 0
        while (i < cu.size && cu[i] == cv[i]) i++

        val dirty = LinkedList<Part>()
        if (i == cu.size) {
            dirty.add(Part(cu.last.first, cu.last.second, cu.last.second))
        } else if (cu[i].first == cv[i].first) {
            dirty.add(Part(cu[i].first,
                    Math.min(cu[i].second, cv[i].second),
                    Math.max(cu[i].second, cv[i].second)))
            i++
        }

        for (j in i..cu.size - 1) dirty.add(Part(cu[j].first, cu[j].second, size[cu[j].first]))
        for (j in i..cv.size - 1) dirty.add(Part(cv[j].first, cv[j].second, size[cv[j].first]))

        val clean = LinkedList<Part>()
        for (j in 0..dirty.size - 1) if (dirty[j].l !== dirty[j].r) clean.add(dirty[j])

        return clean
    }

    for (i in 1..n - 1) {
        val u = `in`.int() - 1
        val v = `in`.int() - 1

        graph[u].add(v)
        graph[v].add(u)
    }

    build()
    val bits = Array(n) { Array(1 shl (32 - Integer.numberOfLeadingZeros(size[it] - 1))) { 0 } }

    for (i in 1..m) {
        val type = `in`.next()
        val u = `in`.int() - 1
        val v = `in`.int() - 1
        val chains = chains(u, v)

        when (type) {
            "P" -> { // Planting the bomb!.. The bomb has been planted!
                fun add(vector: Array<Int>, x: Int, v: Int) {
                    var j = x or vector.size

                    while (j < vector.size shl 1) {
                        vector[j xor vector.size] += v
                        j += j and -j
                    }
                }

                for (j in 0..chains.size - 1) {
                    add(bits[chains[j].chain], chains[j].l, 1)
                    if (chains[j].r < size[chains[j].chain]) {
                        add(bits[chains[j].chain], chains[j].r, -1)
                    }
                }
            }
            "Q" -> { // Count bombs... NOW!
                val vector = bits[chains[0].chain]
                var ret = vector[0]

                var j = chains[0].l

                while (j != 0) {
                    ret += vector[j]
                    j = j and j - 1
                }

                out.write("$ret\n")
            }
        }
    }
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
