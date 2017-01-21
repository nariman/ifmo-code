/**
 * Nariman Safiulin (woofilee)
 * File: D.kt
 * Created on: Nov 3, 2016
 */

import java.io.*
import java.util.*

private val PROBLEM_NAME = "guyaury"

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
    val n = `in`.nextInt()
    val g = HashMap<Int, HashSet<Int>>()
    val p = ArrayList<Int>(n)
    val a = ArrayList<Int>(n)
    val c = ArrayList<Int>(n)

    g[0] = HashSet()
    (1..n - 1).forEach { i ->
        g[i] = HashSet()
        `in`.next().forEachIndexed { j, c -> if (c == '1') g[i]!!.add(j) else g[j]!!.add(i) }
    }

    fun chk(a: Int, b: Int) = g[a - 1]!!.contains(b - 1)

    var v = 1
    fun search(l: Int, r: Int): Unit =
            if (l == r) p.add(l, v) else ((l + r) / 2).let { m -> if (chk(p[m], v)) search(m + 1, r) else search(l, m) }
    do search(0, p.size) while (v++ < n)

    var k = 0
    val t = p[0]

    for (i in p.size - 1 downTo 0) {
        if (chk(p[i], t)) {
            k = i
            break
        }
    }

    if (k == n - 1) {
        out.write(p.joinToString(" "))
        return
    }

    c.addAll(p.subList(0, k + 1))
    a.addAll(p.subList(k + 1, p.size))

    f@for (i in 0..a.size - 1) {
        var v = a[i]

        for (j in 1..c.size - 1) {
            if (chk(v, c[j])) {
                c.add(j, v)
                continue@f
            }
        }

        for (k in i + 1..a.size - 1) {
            v = a[k]

            for (j in 1..c.size - 1) {
                if (chk(v, c[j])) {
                    c.add(j, v)
                    continue@f
                }
            }
        }
    }

    out.write(c.joinToString(" "))
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
