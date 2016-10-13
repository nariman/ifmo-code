/**
 * Nariman Safiulin (woofilee)
 * File: 5G.kt
 * Created on: Apr 25, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.math.BigInteger
import java.util.*

private val PROBLEM_NAME = "num2brackets2"

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
    var k = `in`.nextLong() + 1
    val d = Array(n * 2 + 1) { Array(n + 1) { 0L } }
    d[0][0] = 1L

    (0..n * 2 - 1).forEach { i ->
        (0..n).forEach { j ->
            if (j + 1 <= n)
                d[i + 1][j + 1] += d[i][j]
            if (j > 0)
                d[i + 1][j - 1] += d[i][j]
        }
    }

    var ans = StringBuilder()
    val stack = CharArray(n * 2)
    var depth = 0
    var index = 0
    (n * 2 - 1 downTo 0).forEach { i ->
        var cur: Long
        // -> (

        if (depth + 1 <= n)
            cur = d[i][depth + 1].shl((i - depth - 1) / 2)
        else
            cur = 0L

        if (cur >= k) {
            ans.append('(')
            stack[index++] = '('
            ++depth
            return@forEach
        }

        k -= cur
        // -> )

        if (index > 0 && stack[index - 1] == '(' && depth - 1 >= 0)
            cur = d[i][depth - 1].shl((i - depth + 1) / 2)
        else
            cur = 0L
        if (cur >= k) {
            ans.append(')')
            --index
            --depth
            return@forEach
        }

        k -= cur
        // -> [

        if (depth + 1 <= n)
            cur = d[i][depth + 1].shl((i - depth - 1) / 2)
        else
            cur = 0L
        if (cur.compareTo(k) >= 0) {
            ans.append('[')
            stack[index++] = '['
            ++depth
            return@forEach
        }

        k -= cur
        // -> ]

        ans.append(']')
        --index
        --depth
    }

    out.println(ans)
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
    