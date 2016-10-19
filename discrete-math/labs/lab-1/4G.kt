/**
 * Nariman Safiulin (woofilee)
 * File: 4G.kt
 * Created on: Apr 24, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.*

private val PROBLEM_NAME = "part2sets"

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
    val k = `in`.nextInt()
    val parts = Array(n) { 0 }

    fun print() {
        val sb = Array(k) { StringBuilder() }
        (0..n - 1).forEach { i ->
            sb[parts[i]].append(i + 1).append(" ");
        }

        (0..k - 1).forEach { i ->
            out.println(sb[i]);
        }

        out.println();
    }

    fun part(i: Int, j: Int) {
        if (i == n) {
            if (j != k) return;
            print()
        } else {
            (0..j).forEach { l ->
                parts[i] = l;
                if (l == j) part(i + 1, j + 1) else part(i + 1, j);
            }
        }
    }

    part(0, 0)
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
    