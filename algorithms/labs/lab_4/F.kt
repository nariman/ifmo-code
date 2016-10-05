/**
 * Nariman Safiulin (woofilee)
 * File: F.kt
 * Created on: Mar 20, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.StringTokenizer

private val PROBLEM_NAME = "palindrome"

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

private data class Pair(var first: Int, var second: Int)

private fun solve(`in`: Scanner, out: PrintWriter) {
    val s = `in`.next()

    // Typesafe
    if (s == null) return;

    val n = s.length
    val d = Array(n) { i -> Array(n) { j -> if (i < j) -1 else if (i == j) 1 else 0 } }
    val c = Array(n) { i -> Array(n) { j -> Pair(i, j) } }

    fun r(l: Int, r: Int): Int {
        if (d[l][r] == -1) {
            if (s[l] == s[r]) {
                d[l][r] = r(l + 1, r - 1) + 2
//                c[l][r] = Pair(l + 1, r - 1)
                c[l][r].first = l + 1
                c[l][r].second = r - 1
            } else {
                val a = r(l + 1, r)
                val b = r(l, r - 1)
                d[l][r] = Math.max(a, b)
                if (a >= b) {
                    c[l][r].first = l + 1
                    c[l][r].second = r
                } else  {
                    c[l][r].first = l
                    c[l][r].second = r - 1
                }
            }
        }

        return d[l][r]
    }
    r(0, n - 1)

    out.println(d[0][n - 1])
    fun ra(l: Int, r: Int) {
        if (r < l) return
        if (l == r) {
            out.print(s[l])
        } else {
            if (s[l] == s[r]) out.print(s[l])
            ra(c[l][r].first, c[l][r].second)
            if (s[l] == s[r]) out.print(s[r])
        }
    }
    ra(0, n - 1)
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
