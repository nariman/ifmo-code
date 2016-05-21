/**
 * Nariman Safiulin (woofilee)
 * File: lazy_string.h
 * Created on: May 20, 2016
 */

#ifndef IFMO_CPP_LAZY_STRING_H
#define IFMO_CPP_LAZY_STRING_H

#include <iostream>
#include <memory>
#include <string>

/**
 * Lazy String class
 * Implements technique `copy-on-write`
 */
class lazy_string
{
private:
    struct lazy_char
    {
        friend class lazy_string;
    private:
        const size_t pos;
        lazy_string* ls;
        
        /**
         * Creates lazy char, that points to the char from a lazy string.
         *
         * @param   ls      lazy string object
         * @param   pos     position of the character in string
         */
        lazy_char(lazy_string* ls, size_t pos);
    public:
        /**
         * Returns a char.
         */
        operator char() const;

        /**
         * Writes a new char to the instance position in a lazy string.
         */
        lazy_char& operator=(char c);
    };
    
    std::shared_ptr<std::string> string;
    size_t start;
    size_t len;

    /**
     * Creates lazy string from existing lazy string.
     *
     * @param   string  existing string by reference
     * @param   start   position of the first character in string
     * @param   len     length of string
     */
    lazy_string(const std::shared_ptr<std::string> string, size_t start,
                size_t len);
public:
    /**
     * Creates empty lazy string.
     */
    lazy_string();

    /**
     * Creates lazy string from existing string (std::string).
     *
     * @param   string  existing string
     */
    lazy_string(const std::string& string);

    /**
     * Returns length of current string.
     *
     * @return  string's length
     */
    size_t size() const;
    
    /**
     * Returns length of current string.
     *
     * @return  string's length
     */
    size_t length() const;

    /**
     * Returns character at the specified index.
     * 
     * @param   pos     character's index
     *
     * @return  character at the specified index
     *
     * @throws  std::out_of_range   when pos >= size()
     */
    lazy_char at(size_t pos);
    const char& at(size_t pos) const;

    /**
     * Returns character at the specified index.
     * 
     * @param   pos     character's index
     *
     * @return  character at the specified index
     *
     * @throws  std::out_of_range   when pos >= size()
     */
    lazy_char operator[](size_t pos);
    const char& operator[](size_t pos) const;
    
    /**
     * Returns a new lazy string with lazy-copied substring of this lazy string.
     *
     * @param   pos     position of the first character to be copied
     * @param   len     number of characters to copy
     *
     * @return  a new lazy string with a substring of this lazy string
     * 
     * @throws  std::out_of_range   when pos >= size()
     */
    lazy_string substr(size_t pos = 0, size_t len = std::string::npos) const;

    /**
     * Returns a string (std::string) with copied content of this lazy string.
     *
     * @return  a string with copied content
     */
    operator std::string();

    /**
     * Extracts a string from the input stream (std::istream), and replaces the 
     * current value of the lazy string by an extracted string.
     *
     * @param   is      input stream
     * @param   ls      lazy string object
     *
     * @return  input stream `is`
     */
    friend std::istream& operator>>(std::istream& is, lazy_string& ls);

    /**
     * Writes content of the lazy string to the output stream (std::ostream).
     *
     * @param   os      output stream
     * @param   ls      lazy string object
     *
     * @return  output stream `os`
     */
    friend std::ostream& operator<<(std::ostream& os, lazy_string& ls);
};

#endif // IFMO_CPP_LAZY_STRING_H
