/**
 * Nariman Safiulin (woofilee)
 * File: K.kt
 * Created on: Apr 24, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.*

private val PROBLEM_NAME = "points"

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
    val m = `in`.nextInt()

    val graph = Array(n) { HashSet<Int>() }
    val res = TreeSet<Int>()
    val used = Array(n) { false }
    val inTime = Array(n) { Int.MAX_VALUE }
    val oTime = Array(n) { Int.MAX_VALUE }
    var currTime = 0;

    (0..m - 1).forEach { i ->
        val f = `in`.nextInt() - 1
        val s = `in`.nextInt() - 1
        graph[f].add(s)
        graph[s].add(f)
    }

    fun dfs(v: Int, p: Int) {
        used[v] = true;
        inTime[v] = currTime;
        oTime[v] = currTime++;
        var childrens = 0;

        graph[v].forEach { to ->
            if (to == p)
                return@forEach
            if (used[to])
                oTime[v] = Math.min(oTime[v], inTime[to])
            else {
                dfs(to, v);
                oTime[v] = Math.min(oTime[v], oTime[to])
                if (oTime[to] >= inTime[v] && p != -1)
                    res.add(v)
                childrens++
            }

            if (p == -1 && childrens > 1)
                res.add(v)
        }
    }

    dfs(0, -1)
    out.println(res.size)
    res.forEach { v ->
        out.print("${v + 1} ")
    }
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
    