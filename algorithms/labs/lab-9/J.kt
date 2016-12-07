/*
 * Nariman Safiulin (woofilee)
 * File: J.kt
 * Created on: Dec 3, 2016
 */

import java.io.*
import java.util.*

private val PROBLEM_NAME = "minimax"


private fun solve(`in`: Scanner, out: BufferedWriter) {
    val MAX = 1000000000

    val n = `in`.int()

    val cost = Array(n + 1) { IntArray(n + 1) }
    val weights: ArrayList<Int> = ArrayList<Int>(n * n)
    val used = BooleanArray(1000000 + 1)

    for (i in 1..n) {
        for (j in 1..n) {
            cost[i][j] = `in`.int()

            if (!used[cost[i][j]]) {
                weights.add(cost[i][j])
                used[cost[i][j]] = true
            }
        }
    }

    weights.sort(Int::compareTo)

    var l = 1
    var r = weights.size

    while (l < r) {
        val m = (l + r) shr 1
        val w = weights[m]

        val u = IntArray(n + 1)
        val v = IntArray(n + 1)
        val p = IntArray(n + 1)
        val way = IntArray(n + 1)

        for (i in 1..n) {
            p[0] = i
            var k = 0

            val minv = IntArray(n + 1) { MAX }
            val used = BooleanArray(n + 1)

            do {
                used[k] = true

                val l = p[k]
                var delta = MAX
                var m = 1

                for (j in 1..n)
                    if (!used[j]) {
                        val cur = (if (cost[l][j] < w) MAX - 1 else cost[l][j]) - u[l] - v[j]

                        if (cur < minv[j]) {
                            minv[j] = cur
                            way[j] = k
                        }

                        if (minv[j] < delta) {
                            delta = minv[j]
                            m = j
                        }
                    }

                for (j in 0..n)
                    if (used[j]) {
                        u[p[j]] += delta
                        v[j] -= delta
                    } else {
                        minv[j] -= delta
                    }

                k = m
            } while (p[k] != 0)

            do {
                val l = way[k]
                p[k] = p[l]
                k = l
            } while (k != 0)
        }

        if ((1..n).any { cost[p[it]][it] < w })
            r = m
        else
            l = m + 1
    }

    out.write(weights[l - 1].toString())
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
