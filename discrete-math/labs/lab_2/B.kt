/**
 * Nariman Safiulin (woofilee)
 * File: B.kt
 * Created on: May 02, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.*

private val PROBLEM_NAME = "shooter"

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
    val m = `in`.nextDouble()
    val k = `in`.nextInt()
    var kthProbability = 0.0
    var totalProbability = 0.0

    (0..n - 1).forEach { i ->
        val pi = `in`.nextDouble()
        totalProbability += Math.pow((1 - pi), m)
        if (i == k - 1) kthProbability = Math.pow((1 - pi), m)
    }

    out.println(kthProbability / totalProbability)
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
    