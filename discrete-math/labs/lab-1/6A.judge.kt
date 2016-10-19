/**
 * Nariman Safiulin (woofilee)
 * File: 6A.judge.kt
 * Created on: Apr 25, 2016
 */

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

private val PROBLEM_NAME = "nextvector"

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

private fun generateAns(`in`: Scanner, out: PrintWriter) {
    val s = `in`.next()
    s!!
    val e = Array(s.length) { i -> Integer.parseInt(s[i].toString()) }

    var n = e.size - 1

    while (n >= 0 && e[n] != 1) {
        e[n] = 1
        n--
    }

    if (n == -1)
        out.print("-")
    else {
        e[n] = 0
        e.forEach { b -> out.print("$b") }
    }

    out.println()

    (0..s.length - 1).forEach { i -> e[i] = Integer.parseInt(s[i].toString()) }
    n = e.size - 1

    while (n >= 0 && e[n] != 0) {
        e[n] = 0
        n--
    }

    if (n == -1)
        out.print("-")
    else {
        e[n] = 1
        e.forEach { b -> out.print("$b") }
    }
}

private fun generateTest(out: PrintWriter, random: Random, maxLength: Int) {
    var vector = IntArray(maxLength)

    (0..maxLength - 1).forEach { i ->
        vector[i] = random.nextInt(2)
    }

    vector.forEach { b -> out.print("$b") }
}

private fun solve(exeSolutionName: String) {
    Runtime.getRuntime().exec(exeSolutionName).waitFor()
}

private fun judge() {
    val random = Random()
    val maxLength = 20000
    val exeSolutionName = "6A.exe"
    var tested = 0
    var correct = 0

    (1..100).forEach { test ->
        tested++
        println("=== Checking test #${test} ===")

        var currentLength = random.nextInt(maxLength) + 1
        println("Random length - ${currentLength}")
        print("Generating test... ")

        val testIn = PrintWriter(File(PROBLEM_NAME + ".in"))
        generateTest(testIn, random, currentLength)
        testIn.close()

        println("OK")
        print("Generating correct solution... ")

        val ansIn = Scanner(File(PROBLEM_NAME + ".in"))
        val ansOut = PrintWriter(File(PROBLEM_NAME + ".ans"))
        generateAns(ansIn, ansOut)
        ansIn.close()
        ansOut.close()

        println("OK")

        print("Starting program... ")
        solve(exeSolutionName)
        println("OK - Program execution complete")

        print("Checking answer... ")
        val outString = Files.readAllLines(Paths.get(PROBLEM_NAME + ".out"))
        val ansString = Files.readAllLines(Paths.get(PROBLEM_NAME + ".ans"))
        println("Matching... ")

        if (outString[0].equals(ansString[0]) && outString[1].equals(ansString[1])) {
            println("Test #${test} passed")
            correct++
        } else {
            println("Test #${test} failed !!!")
            println()
            println("Input:")
            println(Files.readAllLines(Paths.get(PROBLEM_NAME + ".in"))[0])
            println()
            println("Correct (judge) solution:")
            println(ansString[0])
            println(ansString[1])
            println()
            println("Incorrect solution:")
            println(outString[0])
            println(outString[1])
        }

        println()
    }

    println("=== Checking complete ===")
    println("$tested tested, $correct passed, ${tested - correct} failed")
}

fun main(args: Array<String>) {
    judge()
}
