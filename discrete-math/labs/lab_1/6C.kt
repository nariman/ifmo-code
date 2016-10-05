/**
 * Nariman Safiulin (woofilee)
 * File: 6C.kt
 * Created on: Apr 26, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.*

private val PROBLEM_NAME = "nextchoose"

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

    if (n == k) {
        out.print(-1)
        return
    }

    val cur = Array(k) { `in`.nextInt() }
    Arrays.sort(cur)

    var min = n
    for (i in cur.indices.reversed()) {
        if (cur[i] == min) {
            min--
        } else {
            cur[i]++
            for (j in i + 1..cur.size - 1) {
                cur[j] = cur[j - 1] + 1
            }
            break
        }
    }

    if (min != n - k) {
        for (i in 0..k - 1) {
            out.print(cur[i].toString() + " ")
        }
        out.println()
        return
    }

    out.print(-1)
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
    