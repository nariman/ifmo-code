/**
 * Nariman Safiulin (woofilee)
 * File: 6A.kt
 * Created on: Apr 25, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.*

private val PROBLEM_NAME = "nextvector"

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
    val s = `in`.next()
    s!!
    val e = Array(s.length) { i -> Integer.parseInt(s[i].toString()) }

    var n = e.size - 1

    while (n >= 0 && e[n] != 1) {
        e[n] = 1
        n--
    }

    if (n == -1)
        out.print("-")
    else {
        e[n] = 0
        e.forEach { b -> out.print("$b") }
    }

    out.println()

    (0..s.length - 1).forEach { i -> e[i] = Integer.parseInt(s[i].toString()) }
    n = e.size - 1

    while (n >= 0 && e[n] != 0) {
        e[n] = 0
        n--
    }

    if (n == -1)
        out.print("-")
    else {
        e[n] = 1
        e.forEach { b -> out.print("$b") }
    }
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
    