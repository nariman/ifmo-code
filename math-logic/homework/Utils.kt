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

    /**
     * Checks, is given character is digit
     *
     * @param char character to check
     * @return     true, if character is digit, otherwise false
     */
    fun isDigit(char: Char) = '0' <= char && char <= '9'

    /**
     * Checks, is given character is upper case
     *
     * @param char character to check
     * @return     true, if character is upper case, otherwise false
     */
    fun isUpperCase(char: Char) = 'A' <= char && char <= 'Z'

    /**
     * Checks, is given character is lower case
     *
     * @param char character to check
     * @return     true, if character is lower case, otherwise false
     */
    fun isLowerCase(char: Char) = 'a' <= char && char <= 'z'
}
