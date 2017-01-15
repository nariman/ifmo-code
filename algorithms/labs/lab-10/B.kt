/*
 * Nariman Safiulin (woofilee)
 * File: B.kt
 * Created on: Jan 15, 2017
 */

import java.io.*
import java.util.*
import java.nio.file.*
import java.lang.Math.min

private val PROBLEM_NAME = "heavylight"

private class Scanner(val r: BufferedReader) {
    var t = StringTokenizer("")

    fun close() = r.close()
    fun has(): Boolean {
        while (!t.hasMoreTokens()) t = StringTokenizer(r.readLine() ?: return false)
        return true
    }

    fun string(): String = if (has()) t.nextToken() else ""
    fun int(): Int = string().toInt()
    fun long(): Long = string().toLong()
    fun double(): Double = string().toDouble()
}

private const val MAX_N = 200011 // cause 0-node is a reserved in a test
private const val MAX_M = 200010

private fun solve(`in`: Scanner, out: BufferedWriter) {
    var n = MAX_N
    var m = MAX_M
    val result = Array(MAX_M) { 0L }  // result of deletions of nodes

    val parent = Array(MAX_N) { 0 }  // parent of node i
    val left = Array(MAX_N) { 0 }    // left child of node i
    val right = Array(MAX_N) { 0 }   // right child of node i
    val size = Array(MAX_N) { 0 }    // size of subtree of node i (includes i)
    val queue = Array(MAX_M) { 0 }   // queue of deletions of nodes
    val index = Array(MAX_N) { 0 }   // index of deletion of node i
    val enter = Array(MAX_N) { 0 }   // enter time in dfs of node i
    val exit = Array(MAX_N) { 0 }    // exit time in dfs of node i
    val delay = Array(MAX_N) { 0 }   // delay of node i
    val parentC = Array(MAX_N) { 0 } // parent of node i 
    val nodeQueue = Array(MAX_N) { LinkedList<Int>() } // queue of deletions of node i
    var cnt = MAX_N                  // timer

    fun dfs(u: Int) {
        if (u == 0) return

        enter[u] = ++cnt
        size[u] = 1

        dfs(left[u])
        dfs(right[u])

        delay[u] = 2 * min(size[left[u]], size[right[u]]) + 1

        size[u] += size[left[u]] + size[right[u]]
        exit[u] = cnt

        if (index[u] == 0) {
            queue[++m] = u
            index[u] = m
        }
    }

    fun find(u: Int): Int {
        if (parentC[u] != u) parentC[u] = find(parentC[u])
        return parentC[u]
    }

    fun child(u: Int, v: Int) = enter[right[u]] <= enter[v] && enter[v] <= exit[right[u]]

    while (true) {
        n = `in`.int()
        if (n == 0) break

        // important zeroings 
        cnt = 0
        for (i in 0..n) {
            result[i] = 0
            parent[i] = 0
            size[i] = 0
            index[i] = 0
            parentC[i] = i
            nodeQueue[i].clear()
        }

        for (i in 1..n) {
            left[i] = `in`.int()
            right[i] = `in`.int()
            parent[left[i]] = i
            parent[right[i]] = i
        }

        m = `in`.int()
        val mc = m
        for (i in 1..m) {
            queue[i] = `in`.int()
            index[queue[i]] = i
        }

        dfs(1)

        for (i in m downTo 1) {
            var u = find(parent[queue[i]])
			while (u != 0) {
                nodeQueue[u].add(queue[i])
                if (--delay[u] == 0) parentC[u] = parent[u]
                u = find(parent[u])
            }
        }

        for (u in 1..n) {
            var t = if (size[right[u]] > size[left[u]]) right[u] else left[u]
            var l = 0
            var r = 0
            var c = 0

            for (v in nodeQueue[u])
                if (u != v) if (child(u, v)) r++ else l++

            nodeQueue[u].reverse()
            for (v in nodeQueue[u]) {
                result[c] += t.toLong()
                c = index[v]
                result[c] -= t.toLong()
                if (child(u, v)) r-- else l--
                t = if (r > l) right[u] else if (r < l) left[u] else t
            }
        }

        var res = 0L
        for (i in 0..mc) {
            res += result[i]
            out.write("$res\n")
        }
    }
}

private fun naive(`in`: Scanner, out: BufferedWriter) {
    var n = MAX_N
    var m = MAX_M
    var result: Long = MAX_N.toLong()    // result of deletions of nodes

    val parent = Array(MAX_N) { 0 }      // parent of node i
    val left = Array(MAX_N) { 0 }        // left child of node i
    val right = Array(MAX_N) { 0 }       // right child of node i
    val size = Array(MAX_N) { 0 }        // size of subtree of node i (includes i)
    val strongs = Array(MAX_N) { false } // is node i is under the strong edge

    fun dfs(u: Int) {
        if (u == 0) return
        size[u] = 1

        if (left[u] != 0 || right[u] != 0) {
            dfs(left[u])
            dfs(right[u])

            if (size[left[u]] >= size[right[u]]) {
                strongs[left[u]] = true
                result += left[u]
            } else {
                strongs[right[u]] = true
                result += right[u]
            }
        }

        size[u] += size[left[u]] + size[right[u]]
    }

    while (true) {
        n = `in`.int()
        if (n == 0) break

        // important zeroings 
        result = 0
        for (i in 0..n) {
            size[i] = 0
            strongs[i] = false
        }

        for (i in 1..n) {
            left[i] = `in`.int()
            right[i] = `in`.int()
            parent[left[i]] = i
            parent[right[i]] = i
        }

        dfs(1)
        out.write("$result\n")

        m = `in`.int()
        for (i in 1..m) {
            val u = `in`.int()
            var v = parent[u]

            size[u] = 0
            
            while (v != 0) {
                size[v] -= 1

                if (size[right[v]] == 0 && size[left[v]] == 0) {
                    if (strongs[right[v]]) {
                        strongs[right[v]] = false
                        result -= right[v]
                    }

                    if (strongs[left[v]]) {
                        strongs[left[v]] = false
                        result -= left[v]
                    }
                } else {
                    if (strongs[right[v]] && size[left[v]] > size[right[v]]) {
                        strongs[left[v]] = true
                        strongs[right[v]] = false
                        result -= right[v]
                        result += left[v]
                    }

                    if (strongs[left[v]] && size[right[v]] > size[left[v]]) {
                        strongs[right[v]] = true
                        strongs[left[v]] = false
                        result -= left[v]
                        result += right[v]
                    }
                }

                v = parent[v]
            }

            out.write("$result\n")
        }
    }
}

private fun testgen(`in`: Scanner, out: BufferedWriter) {
    val random = Random()

    val subs = random.nextInt(10)

    for (sub in 1..subs) {
        val n = random.nextInt(199999) + 2
        val m = random.nextInt(n - 1) + 1
        val nodes = ArrayList<Int>()
        val nexts = ArrayList<Int>()
        val parent = Array(n + 1) { 0 }
        val left = Array(n + 1) { 0 }
        val right = Array(n + 1) { 0 }
        val queue = ArrayList<Int>()
        val deletions = ArrayList<Int>()

        out.write("$n\n")
        
        for (u in 2..n) nodes.add(u)
        nexts.add(1)

        while (nodes.isNotEmpty()) {
            val u = nexts.removeAt(0)

            if (left[u] == 0 && random.nextInt(5) != 0) {
                var v = nodes.removeAt(0)

                while (v == u) {
                    nodes.add(v)
                    v = nodes.removeAt(0)
                }

                left[u] = v
                parent[v] = u
                nexts.add(v)
            }

            if (nodes.isEmpty()) break

            if (right[u] == 0 && random.nextInt(5) != 0) {
                var v = nodes.removeAt(0)

                while (v == u) {
                    nodes.add(v)
                    v = nodes.removeAt(0)
                }

                right[u] = v
                parent[v] = u
                nexts.add(v)
            }

            if (left[u] == 0 || right[u] == 0)
                nexts.add(u)
        }

        for (u in 1..n) {
            out.write("${left[u]} ${right[u]}\n")
        }

        for (u in 1..n)
            if (left[u] == 0 && right[u] == 0) // leaf
                queue.add(u)

        out.write("$m\n")

        while (queue.isNotEmpty()) {
            val u = queue.removeAt(random.nextInt(queue.size))

            if (u == 0)
                break

            when (u) {
                left[parent[u]] -> left[parent[u]] = 0
                right[parent[u]] -> right[parent[u]] = 0
            }

            if (left[parent[u]] == 0 && right[parent[u]] == 0)
                queue.add(parent[u])

            deletions.add(u)
        }

        out.write(deletions.joinToString(separator = " ", limit = m, truncated = "\n"))
    }

    out.write("0\n")
    print("$subs subtests ")
}

private fun run(name: String, ie: String, oe: String, f: (Scanner, BufferedWriter) -> Unit,
                files: Boolean = false) {
    if (files) print("$name running... ")
    val start = System.nanoTime()

    val `in`: Scanner
    val out: BufferedWriter

    if (files) {
        `in` = Scanner(File(PROBLEM_NAME + ".$ie").bufferedReader())
        out = File(PROBLEM_NAME + ".$oe").bufferedWriter()
    } else {
        `in` = Scanner(System.`in`.bufferedReader())
        out = System.out.bufferedWriter()
    }

    f(`in`, out)

    `in`.close()
    out.close()

    val end = System.nanoTime()
    if (files) println("OK, ${(end - start) / 1e6} ms.")
}

fun main(args: Array<String>) {
    val judge = false
    val testg = true
    val naive = true

    val tests = 2

    val files = true or judge // we cannot use system i/o, if judge mode is on

    val ie = "in"
    val ae = "ans"
    val oe = "out"

    if (!judge) {
        run("Solution", ie, oe, ::solve, files)
    } else {
        repeat(if (testg) tests else 1) {
            println("Checking test #${it + 1}...")

            if (testg) { print("  - "); run("Test generation", ie, ie, ::testgen, files) }
            if (naive) { print("  - "); run("Naive solution", ie, ae, ::naive, files) }
            print("  - "); run("Main solution", ie, oe, ::solve, files)

            if (naive) {
                print("  - Checking... ")

                if (Files.readAllLines(Paths.get(PROBLEM_NAME + ".$oe")) ==
                        Files.readAllLines(Paths.get(PROBLEM_NAME + ".$ae"))) {
                    println("OK")
                } else {
                    println("WA")
                    return
                }
            }

            println()
        }
    }
}
