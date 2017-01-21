/*
 * Nariman Safiulin (woofilee)
 * File: C.kt
 * Created on: Dec 9, 2016
 */

import java.io.*
import java.util.*

private val PROBLEM_NAME = "matching"


private fun solve(`in`: Scanner, out: BufferedWriter) {
    val n = `in`.int()
    val w = Array(n) { `in`.int() }
    val e: Array<Array<Int>> = Array(n) { Array(`in`.int()) { `in`.int() - 1 } }

    var u = Array(n) { false }
    val wu = Array(n) { false }
    val m = Array(n) { -1 }
    val rm = Array(n) { -1 }

    fun inner(v: Int): Boolean {
        u[v] = if (!u[v]) true else return false

        for (u in e[v]) if (m[u] == -1 || inner(m[u])) {
            m[u] = v
            return true
        }

        return false
    }

    for (i in 0..n - 1) {
        var bi = -1
        var bw = 0

        for (j in 0..n - 1) if (!wu[j] && w[j] > bw) {
            bw = w[j]
            bi = j
        }

        wu[bi] = true
        u = Array(n) { false }
        inner(bi)
    }

    for ((i, v) in m.withIndex()) if (v != -1) rm[v] = i
    for (v in rm) out.write(if (v == -1) "0 " else "${v + 1} ")
}

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

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = File(PROBLEM_NAME + ".out").bufferedWriter()

    solve(`in`, out)

    `in`.close()
    out.close()
}
