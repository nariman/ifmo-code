/*
 * Nariman Safiulin (woofilee)
 * File: A.kt
 * Created on: Dec 24, 2016
 */

import java.io.*
import java.util.*
import java.nio.file.*

private val PROBLEM_NAME = "rainbow"

// Aarrgghh... This solution is highly optimized, but runs to Time Limit on test 7 :(

private class Scanner(f: File) {
    val r = f.bufferedReader()
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

data class Edge(val u: Int, val v: Int, val color: Int)

private fun solve(`in`: Scanner, out: BufferedWriter) {
    val n = `in`.int()
    val m = `in`.int()

    val edges = Array(m) { Edge(`in`.int() - 1, `in`.int() - 1, `in`.int() - 1) }
    val graph = Array(m) { Array(m) { 0 } }
    val graphSize = Array(m) { 0 }

    val right = HashSet<Int>()
    val result = HashSet<Int>()

    for (i in 0..m - 1) right.add(i)

    val color = Array(100) { false }
    val used = Array(m) { false }
    val x1 = Array(m) { false }
    val x2 = Array(m) { false }

    val parent = Array(m) { 0 }

    val msu = Array(n) { it }
    val rank = Array(n) { 0 }

    val queue = Array(m) { 0 }

    fun get(v: Int): Int {
        if (v != msu[v]) msu[v] = get(msu[v])
        return msu[v]
    }

    fun unite(u: Int, v: Int) {
        var a = get(u)
        var b = get(v)

        if (a == b) return

        if (rank[a] < rank[b]) {
            val t = a
            a = b
            b = t
        }

        if (rank[a] == rank[b]) rank[a]++

        msu[b] = a
    }

    out@ while (true) {
        for (i in 0..m - 1) graphSize[i] = 0

        val re: Array<Int> = result.toTypedArray()
        val ri: Array<Int> = right.toTypedArray()

        for (ai in 0..re.size - 1) {
            val a = re[ai]

            for (i in 0..n - 1) msu[i] = i
            for (bi in 0..re.size - 1)  if (bi == ai) continue else unite(edges[re[bi]].u, edges[a].v)

            for (ci in 0..ri.size - 1) {
                val c = ri[ci]
                if (get(edges[c].u) != get(edges[c].v)) graph[a][graphSize[a]++] = c
                if (edges[c].color == edges[a].color || !color[edges[c].color]) graph[c][graphSize[c]++] = a
            }
        }

        for (i in 0..n - 1) msu[i] = i
        for (i in 0..m - 1) {
            x1[i] = false
            x2[i] = false
        }

        for (li in 0..re.size - 1) unite(edges[re[li]].u, edges[re[li]].v)
        for (li in 0..ri.size - 1) {
            val l = ri[li]
            if (get(edges[l].u) != get(edges[l].v)) x1[l] = true
            if (!color[edges[l].color]) x2[l] = true
        }

        var queueStart = 0
        var queueEnd = 0

        for (i in 0..m - 1) {
            used[i] = false

            if (x1[i]) {
                parent[i] = -1
                used[i] = true
                queue[queueEnd++] = i
            }
        }

        while (queueStart != queueEnd) {
            var l = queue[queueStart++]

            if (x2[l]) {
                while (l != -1) {
                    if (right.contains(l)) {
                        result.add(l)
                        right.remove(l)
                        color[edges[l].color] = true
                    } else {
                        result.remove(l)
                        right.add(l)
                        color[edges[l].color] = false
                    }

                    l = parent[l]
                }

                continue@out
            }

            for (i in 0..graphSize[l] - 1) {
                val k = graph[l][i]

                if (!used[k]) {
                    parent[k] = l
                    used[k] = true
                    queue[queueEnd++] = k
                }
            }
        }

        break
    }

    out.write("${result.size}\n")
    for (it in result) out.write("${it + 1} ")
}

private fun naive(`in`: Scanner, out: BufferedWriter) {
    Runtime.getRuntime().exec("cmd /c A.exe").waitFor()
}

private fun testgen(`in`: Scanner, out: BufferedWriter) {
    val random = Random()
    val n = random.nextInt(1) + 100
    val m = random.nextInt(1) + 5000

    out.write("$n $m\n")

    for (i in 1..m) {
        val a = random.nextInt(n) + 1
        var b = random.nextInt(n) + 1
        val c = random.nextInt(100) + 1

        while (a == b) b = random.nextInt(n) + 1

        out.write("$a $b $c\n")
    }

    print("N=$n, M=$m... ")
}

private fun run(name: String, ie: String, oe: String, f: (Scanner, BufferedWriter) -> Unit) {
    print("$name running... ")
    val start = System.nanoTime()

    val `in` = Scanner(File(PROBLEM_NAME + ".$ie"))
    val out = File(PROBLEM_NAME + ".$oe").bufferedWriter()

    f(`in`, out)

    `in`.close()
    out.close()

    val end = System.nanoTime()
    println("OK, ${(end - start) / 1e6} ms.")
}

fun main(args: Array<String>) {
    val judge = false
    val naive = false
    val tests = 150

    val ie = "in"
    val ae = "ans"
    val oe = "out"

    if (!judge) {
        run("Solution", ie, oe, ::solve)
    } else {
        repeat(tests) {
            println("Checking test #${it + 1}...")

            print("  - "); run("Test generation", oe, ie, ::testgen)
            if (naive) { print("  - "); run("Naive solution", ie, ae, ::naive) }
            print("  - "); run("Main solution", ie, oe, ::solve)

            if (naive) {
                print("  - Checking... ")

                if (Files.readAllLines(Paths.get(PROBLEM_NAME + ".out"))[0] ==
                        Files.readAllLines(Paths.get(PROBLEM_NAME + ".ans"))[0]) {
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
