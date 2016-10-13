/**
 * Nariman Safiulin (woofilee)
 * File: I.kt
 * Created on: Apr 06, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.*

private val PROBLEM_NAME = "game"

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

private fun solve(`in`: Scanner, out: PrintWriter) {
    val n = `in`.nextInt()
    val m = `in`.nextInt()
    val s = `in`.nextInt() - 1
    val edges = Array(n) { ArrayList<Int>() }
    (1..m).forEach { edges[`in`.nextInt() - 1].add(`in`.nextInt() - 1) }

    val whois = Array(n) { -1 }
    val multi = Array(n) { -1 }

    fun dfs(v: Int, d: Int): Int {
        if (whois[v] != -1) {
            if (d % 2 == whois[v]) return multi[v] else return (multi[v] + 1) % 2
        }

        whois[v] = d % 2

        if (edges[v].size == 0) {
            multi[v] = d % 2
        } else {
            var mv = -1

            edges[v].forEach { c ->
                if (mv == -1) {
                    mv = dfs(c, d + 1)
                } else if (mv != dfs(c, d + 1)) {
                    multi[v] = (d + 1) % 2
                    return multi[v]
                }
            }

            multi[v] = mv
        }

        return multi[v]
    }

    if (dfs(s, 0) == 0) out.println("Second player wins") else out.println("First player wins")

//    multi.forEach { i ->
//        println(i)
//    }
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
