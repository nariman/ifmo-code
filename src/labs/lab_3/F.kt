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

    fun next(): String? = if (hasNext()) st.nextToken() else null
    fun nextInt(): Int = Integer.parseInt(next())
    fun nextLong(): Long = java.lang.Long.parseLong(next())
    fun nextDouble(): Double = java.lang.Double.parseDouble(next())
    fun close() = br.close()
}

private fun solve(`in`: Scanner, out: PrintWriter) {
    val firstN = `in`.nextInt()
    val firstM = `in`.nextInt()
    val firstK = `in`.nextInt()
    val firstStates = Array(firstN + 1) { HashMap<Char, Int>() }
    val firstAcceptStates = HashSet<Int>()
    (0..firstK - 1).forEach { firstAcceptStates.add(`in`.nextInt()) }
    (1..firstM).forEach {
        val f = `in`.nextInt()
        val t = `in`.nextInt()
        val sym = `in`.next()!![0]
        firstStates[f].put(sym, t)
    }

    val secondN = `in`.nextInt()
    val secondM = `in`.nextInt()
    val secondK = `in`.nextInt()
    val secondStates = Array(secondN + 1) { HashMap<Char, Int>() }
    val secondAcceptStates = HashSet<Int>()
    (0..secondK - 1).forEach { secondAcceptStates.add(`in`.nextInt()) }
    (1..secondM).forEach {
        val f = `in`.nextInt()
        val t = `in`.nextInt()
        val sym = `in`.next()!![0]
        secondStates[f].put(sym, t)
    }

    val visited = Array(firstN + 1) { false }
    fun dfs(first: Int, second: Int): Boolean {
        visited[first] = true
        if (firstAcceptStates.contains(first) != secondAcceptStates.contains(second))
            return false

        var result = true
        firstStates[first].forEach { transition ->
            if (!secondStates[second].containsKey(transition.key))
                return false
            if (!visited[transition.value])
                result = result and dfs(transition.value, secondStates[second][transition.key]!!)
        }
        return true
    }

    if (dfs(1, 1)) out.println("YES") else out.println("NO")
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
