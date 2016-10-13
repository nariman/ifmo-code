/**
 * Nariman Safiulin (woofilee)
 * File: D.kt
 * Created on: May 25, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.*

private val PROBLEM_NAME = "z"

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
    val s = `in`.next()
    val z = Array(s.length) { 0 }
    var lb = 0
    var rb = 0
    (1..s.length - 1).forEach { i ->
        z[i] = Math.max(0, Math.min(rb - i, z[i - lb]))
        while (i + z[i] < s.length && s[z[i]] == s[i + z[i]]) z[i]++
        if (i + z[i] >= rb) { lb = i; rb = i + z[i] }
    }
    (1..s.length - 1).forEach { i -> out.print("${z[i]} ") }
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
