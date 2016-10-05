/**
 * Nariman Safiulin (woofilee)
 * File: С.kt
 * Created on: May 23, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.*

private val PROBLEM_NAME = "problem3"

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

private data class State(val from: Int, val to: Int, var isAcceptState = false)

private fun solve(`in`: Scanner, out: PrintWriter) {
    val MOD = 1000000000 + 7
    val alpha = 'z' - 'a' + 1

    val n = `in`.nextInt()
    val m = `in`.nextInt()
    val k = `in`.nextInt()
    val states = Array(n) { Array(alpha) { -1 } }
    val backStates = Array(n) { ArrayList<Int>() }
    val acceptStates = Array(n) { false }

    (1..k).forEach { acceptStates[`in`.nextInt() - 1] = true }
    (1..m).forEach {
        val f = `in`.nextInt() - 1
        val t = `in`.nextInt() - 1
        val sym = `in`.next()[0] - 'a'
        states[f][sym] = t
        backStates[t].add(f)
    }

    val useful = Array(n) { false }

    fun useful(): Array<Boolean> {
        fun recursive(f: Int) {
            if (useful[f]) return
            useful[f] = true
            backStates[f].forEach { t -> if (!useful[t]) recursive(t) }
        }

        (0..n - 1).forEach { v -> if (acceptStates[v]) recursive(v) }
        return useful
    }

    fun cycles(): Boolean {
        val parent = Array(n) { -1 }
        val used = Array(n) { 0 }

        fun recursive(f: Int, p: Int): Boolean {
            if (used[f] == 2) return false
            if (used[f] == 1) {
                var curr = p
                while (curr != parent[f]) {
                    if (useful[curr]) return true else curr = parent[curr]
                }
                return false
            }

            parent[f] = p
            used[f] = 1
            states[f].forEach { t -> 
                if (t == -1) return@forEach 
                if (recursive(t, f)) return true
            }
            used[f] = 2

            return false
        }

        return recursive(0, -1)
    }

    fun topsort(): ArrayList<Int> {
        val used = Array(n) { false }
        val result = ArrayList<Int>()

        fun recursive(f: Int) {
            used[f] = true
            states[f].forEach { t -> if (t != -1 && !used[t]) recursive(t) }
            result.add(f)
        }

        (0..n - 1).forEach { v -> if (!used[v]) recursive(v) }
        return result
    }

    useful()
    if (cycles()) {
        out.println(-1)
        return
    }

    val sorted = topsort()
    sorted.reverse()
    val result = Array(n) { 0 }
    result[0] = 1

    sorted.forEach { v ->
        backStates[v].forEach { p -> result[v] += result[p]; result[v] %= MOD }
    }

    var total = 0
    (0..n - 1).forEach { v ->
        if (acceptStates[v]) {
            total += result[v]
            total %= MOD
        }
    }

    out.println(total)
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
