/**
 * Nariman Safiulin (woofilee)
 * File: D.kt
 * Created on: May 04, 2016
 *
 * May The 4th Be With You...
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.*

private val PROBLEM_NAME = "absmarkchain"

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
    val matrix = Array(n) { DoubleArray(n) }
    val absorbing = BooleanArray(n)
    val pos = IntArray(n)

    repeat(m, { matrix[`in`.nextInt() - 1][`in`.nextInt() - 1] = `in`.nextDouble() })

    var na = n
    var q = 0
    var r = 0
    (0..n - 1).forEach { i ->
        if (matrix[i][i] == 1.0) {
            absorbing[i] = true
            na--
            pos[i] = r++
        } else {
            pos[i] = q++
        }
    }

    val size = Integer.max(q, r)
    val Q = Array(size) { DoubleArray(size) }
    val R = Array(size) { DoubleArray(size) }
    val N = Array(na) { DoubleArray(na) }
    val E = Array(na) { DoubleArray(na) }
    val G = Array(na) { DoubleArray(n - na) }

    (0..n - 1).forEach { i ->
        (0..n - 1).forEach { j ->
            if (!absorbing[i]) {
                if (absorbing[j])
                    R[pos[i]][pos[j]] = matrix[i][j]
                else
                    Q[pos[i]][pos[j]] = matrix[i][j]
            }
        }
    }

    (0..na - 1).forEach { i ->
        N[i][i] = 1.0
        E[i][i] = 1.0
        (0..na - 1).forEach { j -> E[i][j] -= Q[i][j] }
    }

    (0..na - 1).forEach { i ->
        var mul: Double

        if (E[i][i] != 1.0) {
            mul = E[i][i]
            (0..na - 1).forEach { j ->
                E[i][j] /= mul
                N[i][j] /= mul
            }
        }

        (0..na - 1).forEach { j ->
            if (i != j) {
                mul = E[j][i]
                (0..na - 1).forEach { k ->
                    E[j][k] -= mul * E[i][k]
                    N[j][k] -= mul * N[i][k]
                }
            }
        }
    }

    (0..na - 1).forEach { i ->
        (0..n - na - 1).forEach { j ->
            (0..na - 1).forEach { k ->
                G[i][j] += N[i][k] * R[k][j]
            }
        }
    }

    (0..n - 1).forEach { i ->
        var ans = 0.0
        if (absorbing[i]) {
            (0..na - 1).forEach { j -> ans += G[j][pos[i]] }
            ans++
            ans /= n.toDouble()
        }
        out.println(ans)
    }
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
