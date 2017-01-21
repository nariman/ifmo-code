/**
 * Nariman Safiulin (woofilee)
 * File: A.kt
 * Created on: Nov 3, 2016
 */

import java.io.*
import java.util.*

private val PROBLEM_NAME = "fullham"

private fun solve(`in`: BufferedReader, out: BufferedWriter) {
    val n = `in`.readLine().toInt()
    val matrix = Array(n) { Array(n) { false } }
    val queue = object : ArrayList<Int>(n) {
        override fun get(index: Int)               = super.get(index % n)
        override fun set(index: Int, element: Int) = super.set(index % n, element)
    }

    (0..n - 1).forEach { i ->
        queue.add(i)
        `in`.readLine().forEachIndexed { j, c ->
            matrix[i][j] = if (c == '1') true else false
            matrix[j][i] = matrix[i][j]
        }
    }

    var head = 0
    var unchanged = 0
    for (k in 0..n * (n - 1)) { // forEach is not rational in this situation
        if (!matrix[queue[head]][queue[head + 1]]) {
            var left = head + 1
            var middle = head + 2
            while (!matrix[queue[head]][queue[middle]] || !matrix[queue[head + 1]][queue[middle + 1]]) middle++
            while (left <= middle) Collections.swap(queue, left++, middle)
            unchanged = 0
        } else if (++unchanged > n) break
        head++
    }

    queue.forEach { out.write("${it + 1} ") }
}

fun main(args: Array<String>) {
    val `in` = File(PROBLEM_NAME + ".in").bufferedReader()
    val out = File(PROBLEM_NAME + ".out").bufferedWriter()

    solve(`in`, out)

    `in`.close()
    out.close()
}
