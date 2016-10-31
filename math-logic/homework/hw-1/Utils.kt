/**
 * Nariman Safiulin (woofilee)
 * File: Main.kt
 * Created on: 31 Oct, 2016
 */

/**
 * Singleton with useful utilities
 */
object Utils {
    val whitespace = Regex("\\s")

    /**
     * Removes all whitespace characters from the string
     *
     * @param  string string to clean
     * @return        cleaned string
     */
    fun clean(string: String) = string.replace(whitespace, "")
}
