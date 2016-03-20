/**
 * Nariman Safiulin (woofilee)
 * File: J.kt
 * Created on: Март 20, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.StringTokenizer

private val PROBLEM_NAME = "nice"

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
    var n: Int = `in`.nextInt()
    var m: Int = `in`.nextInt()
    if (n < m) { val t = n; n = m; m = t }
    val d = Array(n) { IntArray(1.shl(m)) }

    (0..(1.shl(m) - 1)).forEach { i -> d[0][i] = 1 }

    for (i in 1..(n - 1)) {
        for (j in 0..(1.shl(m) - 1)) {
            for (k in 0..(1.shl(m) - 1)) {
                var s1 = Integer.toBinaryString(j)
                var s2 = Integer.toBinaryString(k)
                while (s1.length != m) { s1 = "0" + s1 }
                while (s2.length != m) { s2 = "0" + s2 }
//                println(s1 + " " + s2)
                var b = true
                for (t in 1..(m - 1)) {
                    if (s1[t - 1] == s2[t - 1] && s1[t - 1] == s1[t] && s1[t] == s2[t]) {
                        b = false
                        break
                    }
                }

                if (b) {
                    d[i][j] += d[i - 1][k]
                }
            }
        }
    }

    var t = 0;
    (0..(1.shl(m) - 1)).forEach { i -> t += d[n - 1][i] }
    out.println(t);
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
