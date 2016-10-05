/**
 * Nariman Safiulin (woofilee)
 * File: 5B.kt
 * Created on: Apr 25, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.*

private val PROBLEM_NAME = "perm2num"

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
    val e = Array(n + 1) { i -> if (i == 0) 0 else `in`.nextInt() }
    val w = BooleanArray(n + 1)
    val p = LongArray(n + 1)

    fun fact(i: Long): Long = if (i <= 1L) 1L else i * fact(i - 1)
    (1..n).forEach { i -> p[i] = fact(i.toLong()) }

    var num = 0L
    (1..n).forEach { i ->
        (1..e[i] - 1).forEach { j ->
            if (!w[j]) {
                num += p[n - i]
            }
        }
        w[e[i]] = true
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
    