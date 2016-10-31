/**
 * Nariman Safiulin (woofilee)
 * File: F.kt
 * Created on: Oct 23, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

private val PROBLEM_NAME = "minonpath"

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
    fun power(e: Int) = 1 shl e
    fun log(v: Int): Int = (Math.log(v.toDouble()) / Math.log(2.0)).toInt() + 1

    val n = `in`.nextInt()

    val graph = Array(n) { ArrayList<Int>() }
    val weight = Array(n) { Int.MAX_VALUE }

    (1..n - 1).forEach {
        graph[`in`.nextInt() - 1].add(it)
        weight[it] = `in`.nextInt()
    }

    val p = Array(n) { 0 }
    val d = Array(n) { 0 }
    val dp = Array(n) { Array(log(n) + 1) { 0 } }
    val dpWeight = Array(n) { Array(log(n) + 1) { Int.MAX_VALUE } }

    fun dfs(v: Int, e: Int) {
        d[v] = e
        graph[v].forEach {
            p[it] = v
            dfs(it, e + 1)
        }
    }
    dfs(0, 0)

    p.forEachIndexed { i, v ->
        dp[i][0] = v
        dpWeight[i][0] = weight[i]
    }

    (1..log(n)).forEach { i ->
        (1..n - 1).forEach { v ->
            dp[v][i] = dp[dp[v][i - 1]][i - 1]
            dpWeight[v][i] = Math.min(dpWeight[v][i - 1], dpWeight[dp[v][i - 1]][i - 1])
        }
    }

    fun minonpath(v: Int, u: Int): Int {
        if (d[v] > d[u]) return minonpath(u, v)

        var w = Int.MAX_VALUE
        var v = v
        var u = u

        (log(n) downTo 0).forEach {
            if (d[u] - d[v] >= power(it)) {
                w = Math.min(w, dpWeight[u][it])
                u = dp[u][it]
            }
        }

        (log(n) downTo 0).forEach {
            if (dp[v][it] != dp[u][it]) {
                w = Math.min(w, Math.min(dpWeight[v][it], dpWeight[u][it]))
                v = dp[v][it]
                u = dp[u][it]
            }
        }

        return if (v == u) w else Math.min(w, Math.min(weight[v], weight[u]))
    }

    (1..`in`.nextInt()).forEach { out.println(minonpath(`in`.nextInt() - 1, `in`.nextInt() - 1)) }
}

private fun naive(`in`: Scanner, out: PrintWriter) {
    val n = `in`.nextInt()
    val graph = Array(n) { ArrayList<Int>() }
    val weight = Array(n) { Int.MAX_VALUE }

    (1..n - 1).forEach {
        graph[`in`.nextInt() - 1].add(it)
        weight[it] = `in`.nextInt()
    }

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

    fun minonpath(v: Int, u: Int): Int {
        var w = Int.MAX_VALUE
        var v = v
        var u = u

        var hv = d[v]
        var hu = d[u]

        while (hv != hu) {
            if (hv > hu) {
                w = Math.min(w, weight[v])
                v = p[v]
                hv--
            } else {
                w = Math.min(w, weight[u])
                u = p[u]
                hu--
            }
        }

        while (v != u) {
            w = Math.min(w, Math.min(weight[v], weight[u]))
            v = p[v]
            u = p[u]
        }

        return w
    }

    (1..`in`.nextInt()).forEach { out.println(minonpath(`in`.nextInt() - 1, `in`.nextInt() - 1)) }
}

private fun testgen(out: PrintWriter) {
    val definitelyRandom = Random()
    val n = definitelyRandom.nextInt(5 * 10000 - 1) + 2
    val m = definitelyRandom.nextInt(5 * 10000 + 1)

    out.println(n)
    (2..n).forEach {
        out.println("${definitelyRandom.nextInt(it - 1) + 1} ${definitelyRandom.nextInt(2 * 1000000 + 1) - 1000000}")
    }

    out.println(m)
    (1..m).forEach {
        val v = definitelyRandom.nextInt(n) + 1
        var u = definitelyRandom.nextInt(n) + 1
        while (v == u) {
            u = definitelyRandom.nextInt(n) + 1
        }

        out.println("$v $u")
    }
}

private fun judge() {
    var `in`: Scanner
    var out: PrintWriter
    val tests = 150

    (1..tests).forEach { test ->
        print("Checking test #$test... ")

        out = PrintWriter(File(PROBLEM_NAME + ".in"))

        testgen(out)

        out.close()

        `in` = Scanner(File(PROBLEM_NAME + ".in"))
        out = PrintWriter(File(PROBLEM_NAME + ".ans"))

        naive(`in`, out)

        `in`.close()
        out.close()

        `in` = Scanner(File(PROBLEM_NAME + ".in"))
        out = PrintWriter(File(PROBLEM_NAME + ".out"))

        solve(`in`, out)

        `in`.close()
        out.close()

        val outLines = Files.readAllLines(Paths.get(PROBLEM_NAME + ".out"))
        val ansLines = Files.readAllLines(Paths.get(PROBLEM_NAME + ".ans"))

        if (outLines == ansLines) {
            println("OK")
        } else {
            println("WA")
            return
        }
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
