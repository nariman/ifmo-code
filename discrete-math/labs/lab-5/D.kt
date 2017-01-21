/*
 * Nariman Safiulin (woofilee)
 * File: D.kt
 * Created on: Dec 9, 2016
 */

import java.io.*
import java.util.*

private val PROBLEM_NAME = "check"


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
    val s = ArrayList<BitSet>()

    repeat(m) {
        s.add(BitSet((1..`in`.int()).map { 1 shl (`in`.int() - 1) }.let {
            if (it.isNotEmpty()) it.reduce { a, b -> a or b } else 0
        }))
    }

    fun first() = s.contains(BitSet(0))
    fun second() = s.map(BitSet::subsets).flatten().distinct().map { s.contains(it) }.contains(false).not()
    fun third() = s.map { a ->
        s.filter { it.size + 1 == a.size }.map { b ->
            (a - b).set().map { it -> s.contains(b + it) }.contains(true)
        }
    }.flatten().contains(false).not()

    out.write(if (first() && second() && third()) "YES" else "NO")
}

private fun fast(`in`: Scanner, out: BufferedWriter) {
    val n = `in`.int()
    val m = `in`.int()
    val s = ArrayList<BitSet>()

    repeat(m) {
        s.add(BitSet((1..`in`.int()).map { 1 shl (`in`.int() - 1) }.let {
            if (it.isNotEmpty()) it.reduce { a, b -> a or b } else 0
        }))
    }

    fun first() = s.contains(BitSet(0))
    fun second() = s.map(BitSet::subsets).flatten().distinct().map { s.contains(it) }.contains(false).not()
    fun third(): Boolean {
        for (a in s) {
            b@ for (b in s.filter { it.size + 1 == a.size }) {
                val e = a - b

                for (it in e.set()) {
                    if (s.contains(b + it))
                        continue@b
                }

                return false
            }
        }

        return true
    }

    out.write(if (first() && second() && third()) "YES" else "NO")
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
