/**
 * Nariman Safiulin (woofilee)
 * File: 5J.kt
 * Created on: Апр. 25, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.*

private val PROBLEM_NAME = "part2num"

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
    val e = ArrayList<Int>()
    `in`.next()!!.split("+").forEach { r -> e.add(Integer.parseInt(r)) }
    var n = e.sum()
    val d = Array(n + 1) { Array(n + 1) { 0L } }


    (1..n).forEach { i ->
        (1..n).forEach { j ->
            if (i != j) {
                (j..i).forEach { l ->
                    d[j][i] += d[l][i - j]
                }
            } else {
                d[i][j] = 1
            }
        }
    }

    var num = 0L;
    var min = 1;
    (0..e.size - 1).forEach { i ->
        while (min < e[i]) {
            num += d[min][n]
            ++min
        }
        n -= min
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
    