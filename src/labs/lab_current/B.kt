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

    fun next(): String? = if (hasNext()) st.nextToken() else null
    fun nextInt(): Int = Integer.parseInt(next())
    fun nextLong(): Long = java.lang.Long.parseLong(next())
    fun nextDouble(): Double = java.lang.Double.parseDouble(next())
    fun close() = br.close()
}

private fun solve(`in`: Scanner, out: PrintWriter) {
    val word = `in`.next()!!
    val n = `in`.nextInt()
    val m = `in`.nextInt()
    val k = `in`.nextInt()
    val states = Array(n + 1) { HashMap<Char, ArrayList<Int>>() }
    val acceptStates = HashSet<Int>(k)

    (1..k).forEach { acceptStates.add(`in`.nextInt()) }

    (1..m).forEach {
        val f = `in`.nextInt()
        val t = `in`.nextInt()
        val sym = `in`.next()!![0]

        if (!states[f].contains(sym)) {
            states[f].put(sym, ArrayList<Int>())
        }
        states[f][sym]!!.add(t)
    }

    fun f(currentState: Int, position: Int): Boolean {
        if (position == word.length) {
            if (acceptStates.contains(currentState)) {
                return true
            }
            return false
        }

        if (states[currentState].contains(word[position])) {
            states[currentState][word[position]]!!.forEach { nextState ->
                if (f(nextState, position + 1)) {
                    return true
                }
            }
        }
        return false
    }

    if (f(1, 0)) out.println("Accepts") else out.println("Rejects")
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
