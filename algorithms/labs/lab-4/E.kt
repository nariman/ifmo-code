/**
 * Nariman Safiulin (woofilee)
 * File: E.kt
 * Created on: Mar 14, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.StringTokenizer

private val PROBLEM_NAME = "matrix"

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
    val n: Int = `in`.nextInt()
    val c = LongArray(n + 1)
    val d = Array(n + 1) { Array(n + 1) { -1L } }
    val s = Array(n + 1) { Array(n + 1) { "A" } }

    c[0] = `in`.nextLong()
    for (i in 1..(n - 1)) {
        c[i] = `in`.nextLong()
        `in`.nextLong()
    }
    c[n] = `in`.nextLong()

    fun r(l: Int, r: Int, c: LongArray, d: Array<Array<Long>>, s: Array<Array<String>>): Long {
        if (d[l][r] == -1L) {
            if (l == r - 1) {
                d[l][r] = 0
            } else {
                d[l][r] = Long.MAX_VALUE
                for (i in (l + 1)..(r - 1)) {
                    val t = c[l] * c[i] * c[r] + r(l, i, c, d, s) + r(i, r, c, d, s)
//                    println("$l - $r with $i = $t") DEBUG
                    if (d[l][r] > t) {
                        d[l][r] = t
                        s[l][r] = "(${ s[l][i] }${ s[i][r] })"
                    }
                }
            }
        }

        return d[l][r]
    }

    r(0, n, c, d, s)
    out.println(s[0][n])
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
