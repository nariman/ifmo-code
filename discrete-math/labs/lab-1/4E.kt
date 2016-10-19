/**
 * Nariman Safiulin (woofilee)
 * File: 4E.kt
 * Created on: Apr 14, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.*

private val PROBLEM_NAME = "partition"

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
    val a = ArrayList<String>()

    fun f(current: Int, prev: Int, generated: String) {
        if (current < 0)
            return

        if (current == 0) {
            a.add(generated)
            return
        }

        (prev..current).forEach { next ->
            f(
                    current - next,
                    next,
                    generated + (if (current == n) "$next" else "+$next")
            )
        }
    }

    f(n, 1, "")
    a.forEach { g -> out.println(g) }
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
