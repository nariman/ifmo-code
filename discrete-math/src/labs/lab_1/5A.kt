/**
 * Nariman Safiulin (woofilee)
 * File: 5A.kt
 * Created on: Apr 24, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.*

private val PROBLEM_NAME = "num2perm"

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
    val n = `in`.nextLong()
    var k = `in`.nextLong() + 1
    val used = HashSet<Long>();
    val sb = StringBuilder();

    fun fact(i: Long): Long = if (i <= 1L) 1L else i * fact(i - 1)

    fun num2perm() {
        (0..n - 1).forEach { i ->
            val f = fact(n - i - 1)
            val num = (k - 1) / f + 1
            var result = 1L
            var current = 0L
            while (result <= n) {
                if(!used.contains(result))
                    ++current
                if(num == current)
                    break
                ++result
            }
            used.add(result)
            sb.append(result).append(" ")
            k = (k - 1) % f + 1
        }
    }

    num2perm()
    out.println(sb.toString().trim());
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
    