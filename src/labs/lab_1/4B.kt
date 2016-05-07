/**
 * Nariman Safiulin (woofilee)
 * File: 4B.kt
 * Created on: Apr 14, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.*

private val PROBLEM_NAME = "permutations"

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
    val a = Array(n) { i -> i + 1 }

    fun swap(i: Int, j: Int) {
        val t = a[i]
        a[i] = a[j]
        a[j] = t
    }

    fun nextPermutation(): Boolean {
        var i = n - 2
        while (i != -1 && a[i] >= a[i + 1]) i--
        if (i == -1)
            return false

        var j = n - 1
        while (a[i] >= a[j]) j--
        swap(i, j);

        var k = i + 1
        var t = n - 1
        while (k < t)
            swap(k++, t--);
        return true
    }

    a.forEach { e -> out.print("$e ") }
    while(nextPermutation()) {
        out.println()
        a.forEach { e -> out.print("$e ") }
    }
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
    