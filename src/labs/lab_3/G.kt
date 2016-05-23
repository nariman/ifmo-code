/**
 * Nariman Safiulin (woofilee)
 * File: G.kt
 * Created on: May 23, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.*

private val PROBLEM_NAME = "equivalence"

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

private data class Pair(val first: Int, val second: Int)

private data class DFA(val n: Int, val m: Int, val k: Int) {
    val states = Array(n + 1) { Array('z' - 'a' + 1) { 0 } }
    val acceptStates = Array(n + 1) { false }
    val used = Array(n + 1) { false }
    fun addTransition(from: Int, to: Int, symbol: Int) { states[from][symbol] = to }
    fun setAcceptState(state: Int) { acceptStates[state] = true }
}

private fun solve(`in`: Scanner, out: PrintWriter) {
    val alpha = 'z' - 'a' + 1

    val DFAs = ArrayList<DFA>()
    (0..1).forEach { i ->
        DFAs.add(DFA(`in`.nextInt(), `in`.nextInt(), `in`.nextInt()))
        (1..DFAs[i].k).forEach { DFAs[i].setAcceptState(`in`.nextInt()) }
        (1..DFAs[i].m).forEach { DFAs[i].addTransition(`in`.nextInt(), `in`.nextInt(), `in`.next()[0] - 'a') }
    }

    fun bfsEq(): Boolean {
        val queue = ArrayDeque<Pair>()
        queue.addLast(Pair(1, 1))
        DFAs[0].used[1] = true
        DFAs[1].used[1] = true

        while (!queue.isEmpty()) {
            val p = queue.removeFirst()
            if (DFAs[0].acceptStates[p.first] != DFAs[1].acceptStates[p.second])
                return false

            (0..alpha - 1).forEach { symbol ->
                if (!DFAs[0].used[DFAs[0].states[p.first][symbol]] || !DFAs[1].used[DFAs[1].states[p.second][symbol]]) {
                    queue.addLast(Pair(DFAs[0].states[p.first][symbol], DFAs[1].states[p.second][symbol]))
                    DFAs[0].used[DFAs[0].states[p.first][symbol]] = true
                    DFAs[1].used[DFAs[1].states[p.second][symbol]] = true
                }
            }
        }

        return true
    }

    if (bfsEq()) out.println("YES") else out.println("NO")
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
