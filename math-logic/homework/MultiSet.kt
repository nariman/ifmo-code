/*
 * Nariman Safiulin (woofilee)
 * File: MultiSet.kt
 */

import java.util.*

class MultiSet<in E> {
    private val map = HashMap<E, Int>()

    /**
     * Increase counter of the specified element in this set if it is present or adds it, if it
     * is not present, and sets the counter to 1.
     */
    fun add(e: E) {
        map.put(e, map.getOrPut(e, { 0 }) + 1)
    }

    /**
     * Decrease counter of the specified element in this set if it is present and removes it, if
     * counter sets to 0 after decreasing.
     */
    fun remove(e: E) {
        val m = map.getOrPut(e, { 1 })

        if (m == 1)
            map.remove(e)
        else
            map.put(e, m - 1)
    }

    operator fun contains(e: E) = map.contains(e)
}
