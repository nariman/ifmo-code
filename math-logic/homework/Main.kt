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
        println("Please, provide the name of the problem (first, second or third)")
        println("If needed, input and output filenames can be provided " +
                "(test.in/test.out by default)")
        println("First argument for input filename, second for output filename")
        println("Just input filename can be provided too " +
                "(output filename will be default)")
        println()
        println("For example:")
        println(">> java -jar woofilee.jar first tests/some.input tests/some.output")
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
        println("Please, provide correct input/output filenames")
        return
    }

    val start = System.nanoTime()

    when (problem) {
        "first" -> First.solve(`in`, out)
        "second" -> Second.solve(`in`, out)
        "third" -> Third.solve(`in`, out)
        else -> {
            println("Please, provide correct problem name")
            return
        }
    }

    val end = System.nanoTime()

    `in`.close()
    out.close()

    println("Time of execution: ${end - start} ns.")
}
