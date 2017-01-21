/**
 * Nariman Safiulin (woofilee)
 * File: C.kt
 * Created on: Nov 3, 2016
 */

import java.io.*
import java.util.*

//private val PROBLEM_NAME = "interactive"

private fun solve(`in`: BufferedReader, out: BufferedWriter) {
    val n = `in`.readLine().toInt()
    val p = ArrayList<Int>()
    var v = 1

    fun ask(a: Int, b: Int): Boolean {
        out.write("1 $a $b\n")
        out.flush()
        return `in`.readLine() == "YES"
    }

    fun search(l: Int, r: Int): Unit =
            if (l == r) p.add(l, v) else ((l + r) / 2).let { m -> if (ask(p[m], v)) search(m + 1, r) else search(l, m) }

    do search(0, p.size) while (v++ < n)
    out.write("0 ${p.joinToString(" ")}\n")
}

fun main(args: Array<String>) {
    val `in` = System.`in`.bufferedReader()
    val out = System.out.bufferedWriter()

    solve(`in`, out)

    `in`.close()
    out.close()
}
