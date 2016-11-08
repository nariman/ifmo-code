/**
 * Nariman Safiulin (woofilee)
 * File: C.kt
 * Created on: Oct 23, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

private val PROBLEM_NAME = "lca_rmq"

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

private fun solve(`in`: Scanner, out: PrintWriter) {
    val n = `in`.nextInt()
    val m = `in`.nextInt()
    val logn = (Math.log((2 * n - 1).toDouble()) / Math.log(2.0)).toInt() + 1
    val graph = Array(n) { ArrayList<Int>() }
    val depth = Array(n) { 0 }
    val euler = Array(2 * n - 1) { Array(logn) { 0 } }
    val first = Array(n) { -1 }
    val part = Array(2 * n) { 0 }

    for (it in 1..n - 1) graph[`in`.nextInt()].add(it)

    fun process() {
        var t = 0

        fun dfs(v: Int, e: Int) {
            euler[t++][0] = v
            depth[v] = e
            for (it in graph[v]) {
                dfs(it, e + 1)
                euler[t++][0] = v
            }
        }

        dfs(0, 0)

        for (i in 0..2 * n - 2) {
            if (first[euler[i][0]] == -1)
                first[euler[i][0]] = i
        }

        for (i in 2..2 * n - 1) part[i] = part[i / 2] + 1

        for (j in 1..logn) {
            var i = 1
            while (i + (1 shl j) < 2 * n) {
                val u = euler[i][j - 1]
                val v = euler[i + (1 shl j - 1)][j - 1]
                euler[i][j] = if (depth[u] < depth[v]) u else v
                i++
            }
        }
    }

    process()

    fun lca(u: Int, v: Int): Int {
        var u: Int = first[u]
        var v: Int = first[v]

        if (u > v) {
            val t = u
            u = v
            v = t
        }

        val c = part[v - u + 1]
        val a = euler[u][c]
        val b = euler[v + 1 - (1 shl c)][c]
        return if (depth[a] < depth[b]) a else b
    }

    var u = `in`.nextInt()
    var v = `in`.nextInt()
    val x = (`in`.nextInt() % n).toLong()
    val y = (`in`.nextInt() % n).toLong()
    val z = (`in`.nextInt() % n).toLong()

    var a = lca(u, v)
    var result = a.toLong()

    for (it in 2..m) {
        u = ((x * u + y * v + z) % n).toInt()
        v = ((x * v + y * u + z) % n).toInt()

        var q: Int = first[(u + a) % n]
        var w: Int = first[v]

        if (q > w) {
            val t = q
            q = w
            w = t
        }

        val c = part[w - q + 1]
        val d = euler[q][c]
        val e = euler[w + 1 - (1 shl c)][c]
        a = if (depth[d] < depth[e]) d else e

        result += a
    }

    out.write(result.toString())
}

private fun middle(`in`: Scanner, out: PrintWriter) {
    val n = `in`.nextInt()
    val m = `in`.nextInt()
    val logn = (Math.log(n.toDouble()) / Math.log(2.0)).toInt() + 1
    val power = Array(logn + 1) { 1 shl it }

    val graph = Array(n) { ArrayList<Int>() }
    (1..n - 1).forEach { graph[`in`.nextInt()].add(it) }

    val p = Array(n) { 0 }
    val d = Array(n) { 0 }
    val dp = Array(n) { Array(logn + 1) { 0 } }

    fun dfs(v: Int, e: Int) {
        d[v] = e
        graph[v].forEach {
            p[it] = v
            dp[it][0] = v
            dfs(it, e + 1)
        }
    }
    dfs(0, 0)

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

    var aF = `in`.nextLong()
    var aS = `in`.nextLong()
    val x = `in`.nextLong()
    val y = `in`.nextLong()
    val z = `in`.nextLong()

    var v = lca(aF.toInt(), aS.toInt())
    var result = v.toLong()

    (2..m).forEach {
        aF = (x * aF + y * aS + z) % n
        aS = (x * aS + y * aF + z) % n
        v = lca(((aF + v) % n).toInt(), aS.toInt())
        result += v
    }

    out.write(result.toString())
}

private fun naive(`in`: Scanner, out: PrintWriter) {
    val n = `in`.nextInt()
    val m = `in`.nextInt()
    val graph = Array(n) { ArrayList<Int>() }
    (1..n - 1).forEach { graph[`in`.nextInt()].add(it) }

    val p = Array(n) { 0 }
    val d = Array(n) { 0 }

    fun dfs(v: Int, e: Int) {
        d[v] = e
        graph[v].forEach {
            p[it] = v
            dfs(it, e + 1)
        }
    }
    dfs(0, 0)

    fun lca(v: Int, u: Int): Int {
        var v = v
        var u = u

        var hv = d[v]
        var hu = d[u]

        while (hv != hu) {
            if (hv > hu) {
                v = p[v]
                hv--
            } else {
                u = p[u]
                hu--
            }
        }

        while (v != u) {
            v = p[v]
            u = p[u]
        }

        return v
    }

    var aF = `in`.nextLong()
    var aS = `in`.nextLong()
    val x = `in`.nextLong()
    val y = `in`.nextLong()
    val z = `in`.nextLong()

    var v = lca(aF.toInt(), aS.toInt())
    var result = v.toLong()

    (2..m).forEach {
        aF = (x * aF + y * aS + z) % n
        aS = (x * aS + y * aF + z) % n
        v = lca(((aF + v) % n).toInt(), aS.toInt())
        result += v
    }

    out.write(result.toString())
}

private fun testgen(out: PrintWriter) {
    val definitelyRandom = Random()
    val n = definitelyRandom.nextInt(100000) + 1
    val m = definitelyRandom.nextInt(10000000) + 1

    out.println("$n $m")
    (1..n - 1).forEach { out.write("${definitelyRandom.nextInt(it)} ") }
    out.println()
    out.println("${definitelyRandom.nextInt(n)} ${definitelyRandom.nextInt(n)}")
    out.println(listOf(
            definitelyRandom.nextInt(1000000000 + 1),
            definitelyRandom.nextInt(1000000000 + 1),
            definitelyRandom.nextInt(1000000000 + 1)
    ).joinToString(" "))

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
