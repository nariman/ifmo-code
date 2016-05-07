/**
 * Nariman Safiulin (woofilee)
 * File: G.kt
 * Created on: Mar 20, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.StringTokenizer

private val PROBLEM_NAME = "concert"

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
    val d = Array(n) { `in`.nextInt() }
    val b = `in`.nextInt()
    val m = `in`.nextInt()
    var max = -1
    val ex = Array(n) { Array<Boolean>(m * (n + 2)) { false } }

    fun r(curr: Int, vol: Int) {
        if (curr == n) {
            max = Math.max(max, vol)
            return
        }

        val increased = vol + d[curr]
        val decrease = vol - d[curr]

        if (increased <= m && !ex[curr][increased]) {
            ex[curr][increased] = true
            r(curr + 1, vol + d[curr])
        }
        if (decrease >= 0 && !ex[curr][decrease]) {
            ex[curr][decrease] = false
            r(curr + 1, vol - d[curr])
        }
    }
    r(0, b)
    out.println(max)
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
