/**
 * Nariman Safiulin (woofilee)
 * File: A.kt
 * Created on: Mar 08, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.StringTokenizer

private val PROBLEM_NAME = "lis"

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
    val arr = IntArray(n)
    val d = IntArray(n)
    val s = IntArray(n)
    var max = -1
    var pos = -1
    var prev: Int

    for (i in 0..n - 1) {
        arr[i] = `in`.nextInt()
        d[i] = 1
        s[i] = -1
        for (j in 0..i - 1) {
            if (arr[j] < arr[i] && d[i] < 1 + d[j]) {
                d[i] = 1 + d[j]
                s[i] = j
            }
        }

        if (max < d[i]) {
            max = d[i]
            pos = i
        }
    }

    prev = -1
    while (pos != -1) {
        d[pos] = prev
        prev = pos
        pos = s[pos]
    }

    out.println(max)
    pos = prev
    while (pos != -1) {
        out.print(arr[pos])
        out.print(" ")
        pos = d[pos]
    }
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
