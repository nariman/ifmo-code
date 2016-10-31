/**
 * Nariman Safiulin (woofilee)
 * File: B.kt
 * Created on: Oct 23, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.*

private val PROBLEM_NAME = "lca"

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

    val max = 5 * 100000
    var n = 1

    val p = Array(max) { 0 }
    val d = Array(max) { 0 }
    val dp = Array(max) { Array(log(max) + 1) { 0 } }

    fun lca(v: Int, u: Int): Int {
        if (d[v] > d[u]) return lca(u, v)

        var v = v
        var u = u

        (log(n) downTo 0).forEach {
            if (d[u] - d[v] >= power(it))
                u = dp[u][it]
        }

        if (v == u) return v

        (log(n) downTo 0).forEach {
            if (dp[v][it] != dp[u][it]) {
                v = dp[v][it]
                u = dp[u][it]
            }
        }

        return p[v]
    }

    (1..`in`.nextInt()).forEach {
        when (`in`.next()) {
            "ADD" -> {
                n++

                val a = `in`.nextInt() - 1
                val b = `in`.nextInt() - 1

                p[b] = a
                d[b] = d[a] + 1
                dp[b][0] = a

                (1..log(n)).forEach { i ->
                    dp[b][i] = dp[dp[b][i - 1]][i - 1]
                }
            }
            "GET" -> out.println(lca(`in`.nextInt() - 1, `in`.nextInt() - 1) + 1)
        }
    }
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
