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
        println()
        println("Extra")
        println("You can check the correctness of the output")
        println("Provide the name of the problem with \"checker\" via dash")
        println("If needed, answer and output filenames can be provided " +
                "(test.ans/test.out by default)")
        println("First argument for answer filename, second for output filename")
        println("Just answer filename can be provided too " +
                "(output filename will be default)")
        println()
        println("For example:")
        println(">> java -jar woofilee.jar first-checker tests/some.answer tests/some.output")
        println()
        return
    }

    val start = System.nanoTime()

    val problem: String = args[0]

    when (problem) {
        in listOf("first", "second", "third") -> {
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

            when (problem) {
                "first" -> First.solve(`in`, out)
                "second" -> Second.solve(`in`, out)
                "third" -> Third.solve(`in`, out)
                else -> {
                    println("Please, provide correct problem name")
                    return
                }
            }

            `in`.close()
            out.close()
        }
        in listOf("first-checker", "second-checker", "third-checker") -> {
            val answer: String = if (args.size > 1) args[1] else "test.ans"
            val output: String = if (args.size > 2) args[2] else "test.out"

            val ans: BufferedReader
            val out: BufferedReader

            try {
                ans = File(answer).bufferedReader()
                out = File(output).bufferedReader()
            } catch (e: IOException) {
                println("Please, provide correct answer/output filenames")
                return
            }

            when (problem) {
                "first-checker" -> First.check(ans, out)
                "second-checker" -> Second.check(ans, out)
                "third-checker" -> Third.check(ans, out)
                else -> {
                    println("Please, provide correct problem name")
                    return
                }
            }

            ans.close()
            out.close()
        }
        else -> {
            println("Please, provide correct problem name")
            return
        }
    }

    val end = System.nanoTime()

    println("Time of execution: ${end - start} ns.")
}
