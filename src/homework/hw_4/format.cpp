/**
 * Nariman Safiulin (woofilee)
 * File: format.cpp
 * Created on: May 06, 2016
 */

#include <string>
#include "format.h"

namespace Format {
    Token::Token(Specifier specifier) {
        this->specifier = specifier;
    }

    void Tokenizer::skip() {

    }

    Tokenizer::Tokenizer(const char *format) {
        this->format = format;
    }

    Token Tokenizer::next() {
        // skip();
        return Token(Specifier::d);
    }
}

// template <typename... Args>
// std::string format(std::string const &format, Args... args) {
//     std::stringstream formatted;
//     return "Hello world";
// }
