/**
 * Nariman Safiulin (woofilee)
 * File: I.kt
 * Created on: May 22, 2016
 */

// THIS PROBLEM IS NOT DONE

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.PrintWriter
import java.util.*

private val PROBLEM_NAME = "fastminimization"

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

    fun next(): String = if (hasNext()) st.nextToken() else ""
    fun nextInt(): Int = Integer.parseInt(next())
    fun nextLong(): Long = java.lang.Long.parseLong(next())
    fun nextDouble(): Double = java.lang.Double.parseDouble(next())
    fun close() = br.close()
}

data class Pair(val cls: HashSet<Int>, val chr: Int)

private fun solve(`in`: Scanner, out: PrintWriter) {
    val alpha = Array('z' - 'a' + 1) { i -> i }
    val n = `in`.nextInt()
    val m = `in`.nextInt()
    val k = `in`.nextInt()

    val acceptStates = Array(n) { false }
    val trueAcceptStates = HashSet<Int>()
    val falseAcceptStates = HashSet<Int>()

    (0..n - 1).forEach { i -> falseAcceptStates.add(i) }
    (1..k).forEach {
        val s = `in`.nextInt() - 1
        acceptStates[s] = true
        trueAcceptStates.add(s)
        falseAcceptStates.remove(s)
    }

    val states = Array(n) { Array('z' - 'a' + 1) { -1 } }
    val statesBack = Array(n) { Array('z' - 'a' + 1) { ArrayList<Int>() } }
    (1..m).forEach {
        val f = `in`.nextInt() - 1
        val t = `in`.nextInt() - 1
        val sym = `in`.next()[0] - 'a'
        states[f][sym] = t
        statesBack[t][sym].add(f)
    }

    val cls = Array(n) { 0 }

    fun removeExplicit(v: Int): Array<Boolean> {
        val used = Array(n) { false }

        fun recursive(v: Int) {
            if (used[v]) return
            used[v] = true
            alpha.forEach { c -> 
                if (states[v][c] != -1)
                    recursive(states[v][c])
            }
        }

        recursive(v)
        return used
    }

    fun removeUnreacheble(list: Collection<Int>): Array<Boolean> {
        val used = Array(n) { false }

        fun recursive(list: Collection<Int>) {
            list.forEach { v ->
                if (used[v]) return@forEach
                used[v] = true;
                alpha.forEach { c -> 
                    if (statesBack[v][c].size > 0)
                        recursive(statesBack[v][c])
                }
            }
        }

        recursive(list)
        return used
    }

    var used = removeExplicit(0)
    (0..n - 1).forEach { i ->
        if (!used[i]) {
            trueAcceptStates.remove(i);
            falseAcceptStates.remove(i);
            cls[i] = -1;
        }
    }

    used = removeUnreacheble(trueAcceptStates)
    (0..n - 1).forEach { i ->
        if (!used[i]) {
            trueAcceptStates.remove(i);
            falseAcceptStates.remove(i);
            cls[i] = -1;
        }
    }

    fun find(): ArrayList<HashSet<Int>> {
        
        val queue = ArrayDeque<Pair>();
        val parts = ArrayList<HashSet<Int>>()

        if (falseAcceptStates.size > 0) parts.add(falseAcceptStates)
        parts.add(trueAcceptStates)
        if (falseAcceptStates.size > 0) trueAcceptStates.forEach { s -> cls[s] = 1 } // Otherwise 0

        alpha.forEach { s ->
            queue.addLast(Pair(trueAcceptStates, s))
            if (falseAcceptStates.size > 0) queue.addLast(Pair(falseAcceptStates, s))
        }

        while (!queue.isEmpty()) {
            val p = queue.removeLast()
            val pCls = p.cls
            val pChr = p.chr
            val involved = HashMap<Int, ArrayList<Int>>()

            pCls.forEach { s ->
                statesBack[s][pChr].forEach { r ->
                    if (involved[cls[r]] == null) {
                        involved.put(cls[r], ArrayList<Int>())
                    }
                    involved[cls[r]]?.add(r)
                }
            }

            involved.keys.forEach { i ->
                val pI = parts[i]
                if (involved[i]!!.size < pI.size) {
                    val j = parts.size
                    parts.add(HashSet<Int>())
                    val pJ = parts[j]

                    involved[i]?.forEach { r ->
                        pI.remove(r)
                        pJ.add(r)
                    }

                    if (parts[j].size > pI.size) {
                        parts.set(i, pJ)
                        parts.set(j, pI)
                    }

                    pJ.forEach { r -> cls[r] = j }
                    alpha.forEach { s -> queue.addFirst(Pair(pJ, s)) }
                }
            }
        }
        return parts;
    }

    val parts = find()

    //(0..parts.size - 1).forEach { i ->
    //    println("BigV #$i contains: ${parts[i].joinToString(" ")}")
    //}

    var vertexCounter = parts.size
    var transitionCounter = 0

    val oldToNew = Array(n) { i -> i }
    (0..vertexCounter - 1).forEach { new -> parts[new].forEach { old -> oldToNew[old] = new } }

    val newAcceptStates = HashSet<Int>()
    val newStates = ArrayList<Array<Int>>()
    //val backStates = ArrayList<ArrayList<Int>>()
    repeat(vertexCounter, { newStates.add(Array('z' - 'a' + 1) { -1 }) })
    //repeat(vertexCounter, { backStates.add(ArrayList<Int>()) })

    /////

    val isIn = Array(vertexCounter) { false }

    (0..vertexCounter - 1).forEach { bigV ->
        parts[bigV].forEach { oldV ->
            if (acceptStates[oldV]) newAcceptStates.add(bigV)
            alpha.forEach { s ->
                if (states[oldV][s] != -1) {
                    newStates[bigV][s] = oldToNew[states[oldV][s]]
                    //backStates[oldToNew[states[oldV][s]]].add(newStates[bigV][s])
                    isIn[oldToNew[states[oldV][s]]] = true
                    ++transitionCounter
                }
            }
        }
    }

    //println("CounterIn: ${isIn.joinToString(" ")}")

    /*
    //////////////////////////////////////////////////
    var coeff = 0
    isIn.forEachIndexed { k, v ->
        if (v) {
            newStates.removeAt(k + coeff)
            backStates.removeAt(k + coeff)
            newAcceptStates.remove(k)
            --coeff
        }
    }
    vertexCounter = newStates.size
    //////////////////////////////////////////////////

    val useful = Array(vertexCounter) { false }

    fun useful(): Array<Boolean> {
        fun recursive(f: Int) {
            if (useful[f]) return
            useful[f] = true
            backStates[f].forEach { t -> if (!useful[t]) recursive(t) }
        }

        (0..vertexCounter - 1).forEach { v -> if (newAcceptStates.contains(v)) recursive(v) }
        return useful
    }

    //////////////////////////////////////////////////
    coeff = 0
    useful.forEachIndexed { k, v ->
        if (!v) {
            newStates.removeAt(k + coeff)
            backStates.removeAt(k + coeff)
            newAcceptStates.remove(k)
            --coeff
        }
    }
    vertexCounter = newStates.size
    //////////////////////////////////////////////////

    (0..vertexCounter - 1).forEach { new -> newStates[new].forEach { old ->
        if (old != -1) oldToNew[old] = new
    }}
    */

    

    val newToNew = Array(vertexCounter) { i -> i }
    newToNew[oldToNew[0]] = 0
    newToNew[0] = oldToNew[0]

    //println("Total: $vertexCounter")

    out.println("$vertexCounter $transitionCounter ${newAcceptStates.size}")
    newAcceptStates.forEach { state -> out.print("${state + 1} ") }
    out.println()

    (0..vertexCounter - 1).forEach { f ->
        alpha.forEach { s ->
            if (newStates[f][s] != -1)
                out.println("${newToNew[f] + 1} ${newToNew[newStates[f][s]] + 1} ${'a'.plus(s)}")
        }
    }
}

fun main(args: Array<String>) {
    val `in` = Scanner(File(PROBLEM_NAME + ".in"))
    val out = PrintWriter(File(PROBLEM_NAME + ".out"))

    solve(`in`, out)

    `in`.close()
    out.close()
}
