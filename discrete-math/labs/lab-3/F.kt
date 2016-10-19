/**
 * Nariman Safiulin (woofilee)
 * File: F.kt
 * Created on: May 18, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.*

private val PROBLEM_NAME = "isomorphism"

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

private data class DFA(val n: Int, val m: Int, val k: Int) {
    val states = Array(n) { Array('z' - 'a' + 1) { -1 } }
    val acceptStates = Array(n) { false }
    fun addTransition(from: Int, to: Int, symbol: Int) { states[from][symbol] = to }
    fun setAcceptState(state: Int) { acceptStates[state] = true }
}

private fun solve(`in`: Scanner, out: PrintWriter) {
    val DFAs = ArrayList<DFA>()
    (0..1).forEach { i ->
        DFAs.add(DFA(`in`.nextInt(), `in`.nextInt(), `in`.nextInt()))
        (1..DFAs[i].k).forEach { DFAs[i].setAcceptState(`in`.nextInt() - 1) }
        (1..DFAs[i].m).forEach { DFAs[i].addTransition(`in`.nextInt() - 1, `in`.nextInt() - 1, `in`.next()[0] - 'a') }
    }

    val visited = Array(DFAs[0].n) { false }
    fun dfs(first: Int, second: Int): Boolean {
        visited[first] = true
        if (DFAs[0].acceptStates[first] != DFAs[1].acceptStates[second])
            return false

        var result = true
        DFAs[0].states[first].forEachIndexed { symbol, to ->
            if (to == -1)
                return@forEachIndexed
            if (DFAs[1].states[second][symbol] == -1)
                return false
            if (!visited[to])
                result = result and dfs(to, DFAs[1].states[second][symbol])
        }
        return true
    }

    if (dfs(0, 0)) out.println("YES") else out.println("NO")
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
