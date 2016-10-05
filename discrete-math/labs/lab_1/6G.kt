/**
 * Nariman Safiulin (woofilee)
 * File: 6G.kt
 * Created on: Apr 26, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.*

private val PROBLEM_NAME = "nextpartition"

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
    val s = `in`.next()
    s!!
    val e = ArrayList<Int>()
    val n = Integer.parseInt(s.split("=")[0])
    s.split("=")[1].split("+").forEach { r -> e.add(Integer.parseInt(r)) }

    if (e.size == 1) {
        out.println("No solution")
        return
    }

    e[e.size - 1]--
    e[e.size - 2]++
    if (e[e.size - 2] > e[e.size - 1]) {
        e[e.size - 2] += e[e.size - 1]
        e.removeAt(e.size - 1)
    } else {
        while (e[e.size - 2] * 2 <= e[e.size - 1]) {
            e.add(e[e.size - 1] - e[e.size - 2])
            e[e.size - 2] = e[e.size - 3]
        }
    }

    out.print("$n=${e.joinToString("+")}")
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
    