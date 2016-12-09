/*
 * Nariman Safiulin (woofilee)
 * File: B.kt
 * Created on: Dec 9, 2016
 */

import java.io.*
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

private val PROBLEM_NAME = "destroy"


private data class Squad(val u: Int, val v: Int, val w: Long, val i: Int, var used: Boolean = false)

private fun solve(`in`: Scanner, out: BufferedWriter) {
    val n = `in`.int()
    val m = `in`.int()
    val s = `in`.long()
    val edges = Array(m) { Squad(`in`.int() - 1, `in`.int() - 1, `in`.long(), it + 1) }
    val msu = Array(n) { it }
    val random = Random()
    val res = LinkedList<Int>()
    var sum = 0L

    Arrays.sort(edges, { f, s -> s.w.compareTo(f.w) })

    fun get(v: Int): Int {
        if (v != msu[v])
            msu[v] = get(msu[v])
        return msu[v]
    }

    fun unite(u: Int, v: Int) {
        val a: Int
        val b: Int

        if (random.nextBoolean()) {
            a = get(u)
            b = get(v)
        } else {
            a = get(v)
            b = get(u)
        }

        if (a != b) {
            msu[a] = b
        }
    }

    for (it in edges) {
        val u = it.u
        val v = it.v

        if (get(u) != get(v)) {
            unite(u ,v)
            it.used = true
        }
    }

    for (it in m - 1 downTo 0) {
        if (!edges[it].used) {
            sum += edges[it].w
            if (sum > s) break
            res.add(edges[it].i)
        }
    }

    res.sort()
    out.write("${res.size}\n")
    for (it in res) out.write("$it ")
}

private fun testgen(out: BufferedWriter) {
    val definitelyRandom = Random()
    val n = definitelyRandom.nextInt(50000) + 2
    val m = definitelyRandom.nextInt(100000) + 1
    val s = definitelyRandom.nextInt(1000000000) // 1e18 in statement

    out.write("$n $m $s\n")
    (1..m).forEach {
        val u = definitelyRandom.nextInt(n) + 1
        val v = definitelyRandom.nextInt(n) + 1
        val w = definitelyRandom.nextInt(s)
        out.write("$u $v $w\n")
    }

    print("N=$n, M=$m")
}

private fun judge() {
    var `in`: Scanner
    var out: BufferedWriter
    val tests = 150

    var startTime: Long
    var endTime: Long

    (1..tests).forEach { test ->
        println("Checking test #$test...")

        // Test generation

        print("   - test generation... ")
        startTime = System.nanoTime()

        out = File(PROBLEM_NAME + ".in").bufferedWriter()

        testgen(out)

        out.close()

        endTime = System.nanoTime()
        println(" OK, ${endTime - startTime} ns.")

        // Naive solution

        print("   - naive solution running (answer file)... ")
        startTime = System.nanoTime()

        B.main(null)

        endTime = System.nanoTime()
        println("OK, ${endTime - startTime} ns.")

        // Main solution

        print("   - main solution running... ")
        startTime = System.nanoTime()

        `in` = Scanner(File(PROBLEM_NAME + ".in"))
        out = File(PROBLEM_NAME + ".out").bufferedWriter()

        solve(`in`, out)

        `in`.close()
        out.close()

        endTime = System.nanoTime()
        println("OK, ${endTime - startTime} ns.")

        // Checking

        print("   - checking... ")

        val outLines = Files.readAllLines(Paths.get(PROBLEM_NAME + ".out"))
        val ansLines = Files.readAllLines(Paths.get(PROBLEM_NAME + ".ans"))

        if (outLines[0] == ansLines[0]) {
            println("OK")
        } else {
            println("WA")
            return
        }

        println()
    }
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

fun run() {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = File(PROBLEM_NAME + ".out").bufferedWriter()

    solve(`in`, out)

    `in`.close()
    out.close()
}

fun main(args: Array<String>) {
//    judge()
    run()
}
