/**
 * Nariman Safiulin (woofilee)
 * File: B.kt
 * Created on: Mar 08, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.StringTokenizer

private val PROBLEM_NAME = "lcs"

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
    val arrN = IntArray(n + 1)
    for (i in 1..n) {
        arrN[i] = `in`.nextInt()
    }

    val m = `in`.nextInt()
    val arrM = IntArray(m + 1)
    for (i in 1..m) {
        arrM[i] = `in`.nextInt()
    }

    val lcs = Array(n + 1) { IntArray(m + 1) }
    val path = Array(n + 1) { arrayOfNulls<Pair<Int, Int>>(m + 1) }

    for (i in 1..n) {
        for (j in 1..m) {
            when {
                arrN[i] == arrM[j] -> {
                    lcs[i][j] = lcs[i - 1][j - 1] + 1
                    path[i][j] = Pair(i - 1, j - 1)
                }
                lcs[i - 1][j] >= lcs[i][j - 1] -> {
                    lcs[i][j] = lcs[i - 1][j]
                    path[i][j] = Pair(i - 1, j)
                }
                else -> {
                    lcs[i][j] = lcs[i][j - 1]
                    path[i][j] = Pair(i, j - 1)
                }
            }
        }
    }

    out.println(lcs[n][m])

    fun lam(i: Int, j: Int) {
        when {
            i == 0 || j == 0 -> return
            path[i][j]!!.first == i - 1 && path[i][j]!!.second == j -> lam(i - 1, j)
            path[i][j]!!.first == i && path[i][j]!!.second == j - 1 -> lam(i, j - 1)
            else -> {
                lam(i - 1, j - 1)
                out.print("${arrN[i]} ")
            }
        }
    }
    lam(n, m)
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
