/**
 * Nariman Safiulin (woofilee)
 * File: 6E.kt
 * Created on: Apr 26, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.*

private val PROBLEM_NAME = "nextbrackets"

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
    var s = `in`.next()
    s!!
    var closed = 0
    var opened = 0
    for (i in s.length - 1 downTo 0) {
        if (s[i] == '(') {
            opened++
            if (closed > opened)
                break
        } else
            closed++
    }

    var sb = StringBuilder(s.substring(0, s.length - opened - closed))
    if (s.substring(0, s.length - opened - closed).equals("")) {
        out.println("-")
    } else {
        sb.append(')')
        (1..opened).forEach { i ->
            sb.append('(')
        }
        (1..closed - 1).forEach { i ->
            sb.append(')')
        }
        out.println(sb)
    }
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
    