/*
 * Nariman Safiulin (woofilee)
 * File: Second.kt
 */

import java.io.BufferedReader
import java.io.BufferedWriter

/**
 * Solver for the second problem in a homework
 */
object Second : Solver {
    /**
     * Singleton for expression parsing
     */

    override fun solve(`in`: BufferedReader, out: BufferedWriter) {
        val title: List<String> = Utils.clean(`in`.readLine()).split("|-")

        val hypotheses =
                if (title[0].isNotEmpty())
                    Parser.many(title[0])
                else
                    emptyList()

        val unproven: Expression? =
                if (title[1].isNotEmpty())
                    Parser.single(title[1])
                else
                    null

        out.write("${hypotheses.map { it.toString() }.joinToString(",")}|-$unproven\n")

        var counter = 0

        `in`.forEachLine { line ->
            counter++
            val expression = Parser.single(line)

//            out.write("($counter)")
            out.write("$expression\n")
        }
    }
}
