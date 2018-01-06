/**
 * Nariman Safiulin (woofilee)
 * File: I.kt
 * Created on: Oct 23, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

private val PROBLEM_NAME = "tree"

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

    fun next(): String = if (hasNext()) st.nextToken() else ""
    fun nextInt(): Int = Integer.parseInt(next())
    fun nextLong(): Long = java.lang.Long.parseLong(next())
    fun nextDouble(): Double = java.lang.Double.parseDouble(next())
    fun close() = br.close()
}

data class Edge(val to: Int, val weight: Int)

private fun solve(`in`:Scanner, out: PrintWriter) {
    val n = `in`.nextInt()
    val logn = (Math.log(n.toDouble()) / Math.log(2.0)).toInt() + 1
    val power = Array(logn + 1) { 1 shl it }
    val graph = Array(n) { LinkedList<Edge>() }
    val p = Array(n) { 0 }
    val d = Array(n) { 0 }
    val dp = Array(n) { Array(logn + 1) { 0 } } // Vertices
    val lengths = Array(n) { 0L }
    val visited = Array(n) { false }

    (1..n - 1).forEach {
        val v = `in`.nextInt()
        val u = `in`.nextInt()
        val w = `in`.nextInt()
        graph[v].add(Edge(u, w))
        graph[u].add(Edge(v, w))
    }

    fun dfs(v: Int, e: Int, length: Long) {
        visited[v] = true
        d[v] = e
        lengths[v] = length
        graph[v].forEach { edge ->
            if (!visited[edge.to]) {
                p[edge.to] = v
                dp[edge.to][0] = v
                dfs(edge.to, e + 1, length + edge.weight)
            }
        }
    }

    dfs(0, 0, 0)

    (1..logn).forEach { i ->
        (1..n - 1).forEach { v ->
            dp[v][i] = dp[dp[v][i - 1]][i - 1]
        }
    }

    fun lca(v: Int, u: Int): Int {
        if (d[v] > d[u]) return lca(u, v)

        var v = v
        var u = u

        (logn downTo 0).forEach {
            if (d[u] - d[v] >= power[it])
                u = dp[u][it]
        }

        if (v == u) return v

        (logn downTo 0).forEach {
            if (dp[v][it] != dp[u][it]) {
                v = dp[v][it]
                u = dp[u][it]
            }
        }

        return p[v]
    }

    (1..`in`.nextInt()).forEach {
        val v = `in`.nextInt()
        val u = `in`.nextInt()
        val l = lca(v, u)
        out.println(lengths[v] + lengths[u] - 2 * lengths[l])
    }
}

private fun middle(`in`: Scanner, out: PrintWriter) {
    val n = `in`.nextInt()
    val logn = (Math.log(n.toDouble()) / Math.log(2.0)).toInt() + 1
    val power = Array(logn + 1) { 1 shl it }
    val graph = Array(n) { LinkedList<Edge>() }
    val visited = Array(n) { false }
    val p = Array(n) { 0 }
    val d = Array(n) { 0 }
    val dpv = Array(n) { Array(logn + 1) { 0 } } // Vertices
    val dpl = Array(n) { Array(logn + 1) { 0 } } // Lengths

    (1..n - 1).forEach {
        val v = `in`.nextInt()
        val u = `in`.nextInt()
        val w = `in`.nextInt()
        graph[v].add(Edge(u, w))
        graph[u].add(Edge(v, w))
    }

    fun dfs(v: Int, e: Int) {
        visited[v] = true
        d[v] = e
        graph[v].forEach { edge ->
            if (!visited[edge.to]) {
                p[edge.to] = v
                dpv[edge.to][0] = v
                dpl[edge.to][0] = edge.weight
                dfs(edge.to, e + 1)
            }
        }
    }

    dfs(0, 0)

    (1..logn).forEach { i ->
        (1..n - 1).forEach { v ->
            dpv[v][i] = dpv[dpv[v][i - 1]][i - 1]
            dpl[v][i] = dpl[v][i - 1] + dpl[dpv[v][i - 1]][i - 1]
        }
    }

    fun path(a: Int, b: Int): Int {
        var l = 0
        var v: Int
        var u: Int

        if (d[a] <= d[b]) {
            v = a
            u = b
        } else {
            v = b
            u = a
        }

        (logn downTo 0).forEach {
            if (d[u] - d[v] >= power[it]) {
                l += dpl[u][it]
                u = dpv[u][it]
            }
        }

        (logn downTo 0).forEach {
            if (dpv[v][it] != dpv[u][it]) {
                l += dpl[v][it] + dpl[u][it]
                v = dpv[v][it]
                u = dpv[u][it]
            }
        }

        return if (v == u) l else l + dpl[v][0] + dpl[u][0]
    }

    (1..`in`.nextInt()).forEach { out.println(path(`in`.nextInt(), `in`.nextInt())) }
}

private fun naive(`in`: Scanner, out: PrintWriter) {
    val n = `in`.nextInt()
    val graph = Array(n) { ArrayList<Edge>() }
    val visited = Array(n) { false }
    val p = Array(n) { 0 }
    val d = Array(n) { 0 }
    val l = Array(n) { 0 }

    (1..n - 1).forEach {
        val v = `in`.nextInt()
        val u = `in`.nextInt()
        val w = `in`.nextInt()
        graph[v].add(Edge(u, w))
        graph[u].add(Edge(v, w))
    }

    fun dfs(v: Int, e: Int) {
        visited[v] = true
        d[v] = e
        graph[v].forEach { edge ->
            if (!visited[edge.to]) {
                p[edge.to] = v
                l[edge.to] = edge.weight
                dfs(edge.to, e + 1)
            }
        }
    }

    dfs(0, 0)

    fun path(v: Int, u: Int): Int {
        var r = 0
        var v = v
        var u = u

        var hv = d[v]
        var hu = d[u]

        while (hv != hu) {
            if (hv > hu) {
                r += l[v]
                v = p[v]
                hv--
            } else {
                r += l[u]
                u = p[u]
                hu--
            }
        }

        while (v != u) {
            r += l[v] + l[u]
            v = p[v]
            u = p[u]
        }

        return r
    }

    (1..`in`.nextInt()).forEach { out.println(path(`in`.nextInt(), `in`.nextInt())) }
}

private fun testgen(out: PrintWriter) {
    val definitelyRandom = Random()
    val n = definitelyRandom.nextInt(150000 + 1)
    val m = definitelyRandom.nextInt(75000 + 1)

    out.println(n)

    val rest = ArrayList<Int>()   // Vertices to append to the graph
    val queue = LinkedList<Int>() // Vertices added to the graph and can be useful for growing graph
    var counter = 0               // Number of vertices added to the graph
    val used = Array(n) { false } // Indicator for vertices added to the graph

    (0..n - 1).forEach { rest.add(it) }
    queue.add(rest.removeAt(definitelyRandom.nextInt(n - counter)))
    var v: Int = queue.remove()
    used[v] = true
    counter++

    while (counter != n) {
        val p = definitelyRandom.nextInt(n - counter)
        if (used[rest[p]]) continue
        val u = rest.removeAt(p)
        used[u] = true
        queue.add(u)

        out.println("$v $u ${definitelyRandom.nextInt(5000)}")

        if (definitelyRandom.nextBoolean() && definitelyRandom.nextBoolean()) // 25% to change the current vertex
            v = queue.remove()

        counter++
    }

    out.println(m)
    (1..m).forEach { out.println("${definitelyRandom.nextInt(n)} ${definitelyRandom.nextInt(n)}") }

    print("N=$n, M=$m")
}

private fun judge() {
    var `in`: Scanner
    var out: PrintWriter
    val tests = 150

    var startTime: Long
    var endTime: Long

    (1..tests).forEach { test ->
        println("Checking test #$test...")

        // Test generation

        print("   - test generation... ")
        startTime = System.nanoTime()

        out = PrintWriter(File(PROBLEM_NAME + ".in"))

        testgen(out)

        out.close()

        endTime = System.nanoTime()
        println(" OK, ${endTime - startTime} ns.")

        // Naive solution

        print("   - naive solution running (answer file)... ")
        startTime = System.nanoTime()

        `in` = Scanner(File(PROBLEM_NAME + ".in"))
        out = PrintWriter(File(PROBLEM_NAME + ".ans"))

        naive(`in`, out)

        `in`.close()
        out.close()

        endTime = System.nanoTime()
        println("OK, ${endTime - startTime} ns.")

        // Main solution

        print("   - main solution running... ")
        startTime = System.nanoTime()

        `in` = Scanner(File(PROBLEM_NAME + ".in"))
        out = PrintWriter(File(PROBLEM_NAME + ".out"))

        solve(`in`, out)

        `in`.close()
        out.close()

        endTime = System.nanoTime()
        println("OK, ${endTime - startTime} ns.")

        // Checking

        print("   - checking... ")

        val outLines = Files.readAllLines(Paths.get(PROBLEM_NAME + ".out"))
        val ansLines = Files.readAllLines(Paths.get(PROBLEM_NAME + ".ans"))

        if (outLines == ansLines) {
            println("OK")
        } else {
            println("WA")
            return
        }

        println()
    }
}

private fun run() {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}

fun main(args: Array<String>) {
//    judge()
    run()
}
