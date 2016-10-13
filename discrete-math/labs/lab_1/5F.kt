/**
 * Nariman Safiulin (woofilee)
 * File: 5F.kt
 * Created on: Apr 25, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.*

private val PROBLEM_NAME = "brackets2num"

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
    var n = s!!.length
    val d = Array(n * 2 + 1) { Array (n + 1) { 0L } }
    d[0][0] = 1L

    (0..n * 2 - 1).forEach { i ->
        (0..n).forEach { j ->
            if (j + 1 <= n)
                d[i + 1][j + 1] += d[i][j]
            if (j > 0)
                d[i + 1][j - 1] += d[i][j]
        }
    }

    var num = 0L
    var depth = 0
    (0..n - 1).forEach { i ->
        if (s[i] == '(')
            depth++
        else {
            num += d[n - i - 1][depth + 1]
            depth--
        }
    }

    out.println(num)
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
    