/*
 * Nariman Safiulin (woofilee)
 * File: A.kt
 * Created on: Dec 9, 2016
 */

import java.io.*
import java.util.*

private val PROBLEM_NAME = "schedule"


private fun solve(`in`: Scanner, out: BufferedWriter) {
    val n = `in`.int()
    val jobs = ArrayList<Pair<Int, Int>>()
    val times = TreeSet<Int>()
    var penalty = 0L

    repeat(n, { times.add(it); jobs.add(Pair(`in`.int(), `in`.int())) })
    jobs.sort { f, s -> s.second.compareTo(f.second) }
    jobs.forEach {
        if (times.first() >= it.first)
            penalty += it.second
        else
            times.remove(times.lower(it.first))
    }

    out.write(penalty.toString())
}

private class Scanner(f: File) {
    val r = f.bufferedReader()
    var t = StringTokenizer("")

    fun hasNext(): Boolean {
        while (!t.hasMoreTokens()) t = StringTokenizer(r.readLine() ?: return false)
        return true
    }

    fun next(): String = if (hasNext()) t.nextToken() else ""
    fun int(): Int = next().toInt()
    fun long(): Long = next().toLong()
    fun double(): Double = next().toDouble()
    fun close() = r.close()
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = File(PROBLEM_NAME + ".out").bufferedWriter()

    solve(`in`, out)

    `in`.close()
    out.close()
}
