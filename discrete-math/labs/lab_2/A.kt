/**
 * Nariman Safiulin (woofilee)
 * File: A.kt
 * Created on: May 02, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.*

private val PROBLEM_NAME = "exam"

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
    val k = `in`.nextDouble()
    val n = `in`.nextDouble()
    var total = 0.0

    (1..k.toInt()).forEach { i ->
        val pi = `in`.nextDouble()
        val mi = `in`.nextDouble()
//        println("${pi / n} ${mi / 100} ${(pi / n) * (mi / 100)}")
        total += (pi / n) * (mi / 100)
    }

    out.println(total)
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
    