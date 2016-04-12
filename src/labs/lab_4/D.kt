/**
 * Nariman Safiulin (woofilee)
 * File: D.kt
 * Created on: Mar 14, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.StringTokenizer

private val PROBLEM_NAME = "levenshtein"

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
    val s1: String? = `in`.next()
    val s2: String? = `in`.next()
    val insertCost = 1
    val deleteCost = 1
    val replaceCost = 1

    // Typesafe strings
    if (s1 == null || s2 == null) {
        return
    }

    val d = Array(s1.length + 1) { IntArray(s2.length + 1) }

    d[0][0] = 0
    for (j in 1..s2.length) {
        d[0][j] = d[0][j - 1] + insertCost
        for (i in 1..s1.length) {
            d[i][0] = d[i - 1][0] + deleteCost
            if (s1[i - 1] != s2[j - 1]) {
                d[i][j] = Math.min(
                        Math.min(d[i - 1][j] + deleteCost, d[i][j - 1] + insertCost),
                        d[i - 1][j - 1] + replaceCost
                )
            } else {
                d[i][j] = d[i - 1][j - 1]
            }
        }
    }

    out.println(d[s1.length][s2.length])
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
