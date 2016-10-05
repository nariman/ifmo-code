/**
 * Nariman Safiulin (woofilee)
 * File: B.kt
 * Created on: May 09, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.*

private val PROBLEM_NAME = "problem2"

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

    fun next(): String = if (hasNext()) st.nextToken() else ""
    fun nextInt(): Int = Integer.parseInt(next())
    fun nextLong(): Long = java.lang.Long.parseLong(next())
    fun nextDouble(): Double = java.lang.Double.parseDouble(next())
    fun close() = br.close()
}

private fun solve(`in`: Scanner, out: PrintWriter) {
    val word = `in`.next()
    val n = `in`.nextInt()
    val m = `in`.nextInt()
    val k = `in`.nextInt()
    val states = Array(n) { Array('z' - 'a' + 1) { HashSet<Int>() } }
    val acceptStates = Array(n) { false }

    (1..k).forEach { acceptStates[`in`.nextInt() - 1] = true }
    (1..m).forEach {
        val f = `in`.nextInt() - 1
        val t = `in`.nextInt() - 1
        val sym = `in`.next()[0] - 'a'
        states[f][sym].add(t)
    }

    var currR = HashSet<Int>()
    currR.add(0)
    word.forEach { sym ->
        val nextR = HashSet<Int>()
        currR.forEach { f -> nextR.addAll(states[f][sym - 'a']) }
        currR = nextR
        if (currR.isEmpty()) { out.println("Rejects"); return }
    }

    (0..n - 1).forEach { i ->
        if (acceptStates[i] && currR.contains(i)) { out.println("Accepts"); return }
    }

    out.println("Rejects");
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
