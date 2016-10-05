/**
 * Nariman Safiulin (woofilee)
 * File: D.kt
 * Created on: May 23, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.*

private val PROBLEM_NAME = "problem4"

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

private data class Transition(val from: Int, val to: Int)

private fun solve(`in`: Scanner, out: PrintWriter) {
    val MOD = 1000000000 + 7
    // val alpha = 'z' - 'a' + 1 // Wow

    val n = `in`.nextInt()
    val m = `in`.nextInt()
    val k = `in`.nextInt()
    val l = `in`.nextInt()
    val transitions = ArrayList<Transition>()
    val acceptStates = Array(n) { false }

    (1..k).forEach { acceptStates[`in`.nextInt() - 1] = true }
    (1..m).forEach { 
        transitions.add(Transition(`in`.nextInt() - 1, `in`.nextInt() - 1))
        `in`.next() // Unnecessary
    }

    var currDP = Array(n) { 0 }
    (0..n - 1).filter { acceptStates[it] } .forEach { currDP[it] = 1 }
    (0..l - 1).forEach {
        val nextDP = Array(n) { 0 }
        transitions.forEach { t ->
            nextDP[t.from] += currDP[t.to];
            nextDP[t.from] %= MOD
        }
        currDP = nextDP
    }

    out.println(currDP[0])
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
