/**
 * Nariman Safiulin (woofilee)
 * File: 5C.kt
 * Created on: Apr 25, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.*

private val PROBLEM_NAME = "num2choose"

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
    var n = `in`.nextInt();
    var k = `in`.nextInt();
    var m = `in`.nextLong();
    val c = Array(n) { Array(k) { 0L } };

    (0..n - 1).forEach { i ->
        var j = 0
        while (j <= i && j < k) {
            if (j == 0 || i == j) {
                c[i][j] = 1;
            } else {
                c[i][j] = c[i - 1][j - 1] + c[i - 1][j];
            }
            j++
        }
    }

    var next = 1;
    val q = ArrayDeque<Int>();

    while (k > 0) {
        if (m < c[n - 1][k - 1]) {
            q.addLast(next);
            k--;
        } else {
            m -= c[n - 1][k - 1];
        }
        n--;
        next++;
    }
    while (!q.isEmpty()) {
        out.print(q.pop().toString() + " ");
    }
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
    