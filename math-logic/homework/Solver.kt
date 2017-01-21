/*
 * Nariman Safiulin (woofilee)
 * File: Solver.kt
 */

import java.io.BufferedReader
import java.io.BufferedWriter

/**
 * Interface for the problem solvers
 */
interface Solver {
    fun solve(`in`: BufferedReader, out: BufferedWriter)

    fun check(ans: BufferedReader, out: BufferedReader)
}
