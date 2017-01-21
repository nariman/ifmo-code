/*
 * Nariman Safiulin (woofilee)
 * File: E.kt
 * Created on: Dec 10, 2016
 */

import java.io.*
import java.util.*

private val PROBLEM_NAME = "cycles"


private data class BitSet(val set: Int) {
    val size: Int

    init {
        size = 0.until(10).map { 1 shl it == set and (1 shl it) }.count { it }
    }

    operator fun plus(other: BitSet) = BitSet(set or other.set)
    operator fun minus(other: BitSet) = BitSet(set xor (set and other.set))

    fun subsets() = 0
            .until(1 shl 10)
            .filter { set and it == it }
            .map(::BitSet)

    fun set() = 0
            .until(10)
            .filter { 1 shl it == set and (1 shl it) }
            .map { BitSet(1 shl it) }

    fun isSubset(other: BitSet) = other.set == set and other.set

    override fun equals(other: Any?) = other is BitSet && set == other.set
    override fun hashCode() = set
}

private fun solve(`in`: Scanner, out: BufferedWriter) {
    val n = `in`.int()
    val m = `in`.int()
    val w: Array<Pair<Int, Int>> = Array(n) { Pair(`in`.int(), it) }
    val s = ArrayList<BitSet>()
    var r = 0
    var c = BitSet(0)

    repeat(m) {
        s.add(BitSet((1..`in`.int()).map { 1 shl (`in`.int() - 1) }.let {
            if (it.isNotEmpty()) it.reduce { a, b -> a or b } else 0
        }))
    }

    Arrays.sort(w, { a, b -> b.first.compareTo(a.first) })

    repeat(n) { i ->
        val t = c + BitSet(1 shl w[i].second)

        if (s.none { t.isSubset(it) }) {
            c = t
            r += w[i].first
        }
    }

    out.write("$r")
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
