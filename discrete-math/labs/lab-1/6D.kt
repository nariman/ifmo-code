/**
 * Nariman Safiulin (woofilee)
 * File: 6D.kt
 * Created on: Apr 26, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.*

private val PROBLEM_NAME = "nextsetpartition"

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
    var n = 0
    var k = 0
    var a = Array(n) { ArrayList<Int>() }
    var last = 0

    fun next() {
        val used = ArrayList<Int>()
        var ut = false

        for (i in last - 1 downTo 0) {
            if (!used.isEmpty()) {
                var value = Int.MAX_VALUE
                var index = -1

                (0..used.size - 1).forEach { e ->
                    if (used[e] < value && used[e] > a[i][a[i].size - 1]) {
                        value = used[e]
                        index = e
                    }
                }

                if (index != -1) {
                    a[i].add(value)
                    used.removeAt(index)
                    break
                }
            }

            for (j in a[i].size - 1 downTo 0) {
                if (!used.isEmpty() && j != 0) {
                    var value = Int.MAX_VALUE
                    var index = -1

                    (0..used.size - 1).forEach { e ->
                        if (used[e] < value && used[e] > a[i][j]) {
                            value = used[e]
                            index = e
                        }
                    }

                    if (index != -1) {
                        used.removeAt(index)
                        used.add(a[i][j])
                        a[i][j] = value
                        ut = true
                        break
                    }
                }

                used.add(a[i][j])
                a[i].removeAt(j)
                if (a[i].isEmpty()) last--
            }

            if (ut) break
        }

        used.sort({ f, s -> f - s })
        used.forEach { u -> a[last++].add(u) }
    }

    while (true) {
        n = `in`.nextInt()
        k = `in`.nextInt()

        if (n == 0 && k == 0) return
        last = k

        a = Array(n) { ArrayList<Int>() }
        (0..k - 1).forEach { i ->
            val st = StringTokenizer(`in`.br.readLine())
            while (st.hasMoreTokens()) {
                a[i].add(Integer.parseInt(st.nextToken()))
            }
        }

        next()

        out.println("$n $last");
        (0..last - 1).forEach { i ->
            (0..a[i].size - 1).forEach { j ->
                out.print("${a[i][j]} ");
            }
            out.println();
        }
        out.println();
    }
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
    