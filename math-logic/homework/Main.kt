/*
 * Nariman Safiulin (woofilee)
 * File: Main.kt
 */

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.IOException

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Please, provide the name of the problem (first, second or third) and, if " +
                "needed, filenames for input and output files (test.in, test.out by default)")
        return
    }

    val problem: String = args[0]
    val input: String = if (args.size > 1) args[1] else "test.in"
    val output: String = if (args.size > 2) args[2] else "test.out"

    val `in`: BufferedReader
    val out: BufferedWriter

    try {
        `in` = File(input).bufferedReader()
        out = File(output).bufferedWriter()
    } catch (e: IOException) {
        println("Please, provide correct filenames for input and output files")
        return
    }

    val start = System.nanoTime()

    when (problem) {
        "first" -> First.solve(`in`, out)
        "second" -> Second.solve(`in`, out)
        "third" -> Third.solve(`in`, out)
        else -> {
            println("Please, provide correct problem name (first, second or third)")
            return
        }
    }

    val end = System.nanoTime()

    `in`.close()
    out.close()

    println("Time of execution: ${end - start} ns.")
}
