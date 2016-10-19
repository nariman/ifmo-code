/**
 * Nariman Safiulin (woofilee)
 * File: C.kt
 * Created on: May 02, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.*

private val PROBLEM_NAME = "lottery"

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
    val n = `in`.nextDouble()
    val m = `in`.nextDouble()
    var totalProbability = 1.0
    var totalSum = 0.0
    val winSum = Array(m.toInt()) { 0.0 }
    val winValues = Array(m.toInt()) { 0.0 }

    (0..m.toInt() - 1).forEach { i -> winValues[i] = `in`.nextDouble(); winSum[i] = `in`.nextDouble(); }

    (0..m.toInt() - 2).forEach { i ->
        totalProbability /= winValues[i]
        totalSum += winSum[i] * totalProbability * (winValues[i + 1] - 1) / winValues[i + 1]
    }
    totalSum += winSum[m.toInt() - 1] * totalProbability / winValues[m.toInt() - 1]

    out.println(n - totalSum)
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
    