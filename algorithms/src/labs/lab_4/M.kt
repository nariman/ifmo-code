/**
 * Nariman Safiulin (woofilee)
 * File: M.kt
 * Created on: Mar 20, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.StringTokenizer

private val PROBLEM_NAME = "free"

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
    val a = `in`.nextInt()
    val b = `in`.nextInt()
    val c = `in`.nextInt()
    val k = `in`.nextInt()

    val t = Array(c + 1) { Array(b + 1) { IntArray(a + 1) } }
    val ans = Array(c + 1) { Array(b + 1) { IntArray(a + 1) } }

    (0..c).forEach { i -> (0..b).forEach { j -> (0..a).forEach { l ->
        var curr = 0
        var left = 0

        if (l != 0) {
            t[i][j][l] = t[i][j][l - 1] + 1
            if (t[i][j][l] >= k) {
                t[i][j][l] = 0
                ans[i][j][l] = ans[i][j][l - 1] + 1
            } else {
                ans[i][j][l] = ans[i][j][l - 1]
            }
        }

        if (j != 0) {
            left = t[i][j - 1][l] + 2
            if (left >= k) {
                left = 0
                curr = ans[i][j - 1][l] + 1
            } else {
                curr = ans[i][j - 1][l]
            }
        }

        if (curr > ans[i][j][l] || curr == ans[i][j][l] && left > t[i][j][l]) {
            ans[i][j][l] = curr
            t[i][j][l] = left
        }

        if (i != 0) {
            left = t[i - 1][j][l] + 3
            if (left >= k) {
                left = 0
                curr = ans[i - 1][j][l] + 1
            } else {
                curr = ans[i - 1][j][l]
            }
        }

        if (curr > ans[i][j][l] || curr == ans[i][j][l] && left > t[i][j][l]) {
            ans[i][j][l] = curr
            t[i][j][l] = left
        }
    } } }

    out.println(ans[c][b][a])
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
