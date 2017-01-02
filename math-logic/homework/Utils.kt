/*
 * Nariman Safiulin (woofilee)
 * File: Utils.kt
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

    fun isDigit(char: Char) = '0' <= char && char <= '9'
}
