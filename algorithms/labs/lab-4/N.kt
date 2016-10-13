/**
 * Nariman Safiulin (woofilee)
 * File: N.kt
 * Created on: Mar 20, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.StringTokenizer

private val PROBLEM_NAME = "bookshelf"

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
    val l = `in`.nextInt()
    val h = IntArray(n + 1)
    val w = IntArray(n + 1)
    val d = IntArray(n + 1)
    (1..n).forEach { i -> h[i] = `in`.nextInt(); w[i] = `in`.nextInt() }
    d[0] = 0

    for (i in 1..n) {
        var line = 0
        var lineh = 0
        var globh = Int.MAX_VALUE
        for (j in i downTo 1) {
            line += w[j]
            if (line > l) break
            lineh = Math.max(lineh, h[j])
            globh = Math.min(globh, lineh + d[j - 1])
        }
        d[i] = globh
    }

    out.println(d[n])
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
