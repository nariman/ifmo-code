/**
 * Nariman Safiulin (woofilee)
 * File: format.cpp
 * Created on: May 06, 2016
 */

#include <string>
#include "format.h"

namespace Format {
    /* ================= */
    /* ===== TOKEN ===== */
    /* ================= */

    Token::Token(Specifier specifier, std::string before, std::string after) {
        this->specifier = specifier;
        this->before = before;
        this->after = after;
    }

    /* ===================== */
    /* ===== TOKENIZER ===== */
    /* ===================== */

    void Tokenizer::accumulate() {
        std::string cumulated = "";

        while(this->position < this->format.length() && this->format[this->position] != '%') {
            cumulated.push_back(this->format[this->position]);
            this->position++;
        }

        return cumulated;
    }

    Tokenizer::Tokenizer(const std::string &format) {
        this->format = format;
        this->position = 0;
    }

    Token Tokenizer::next() {
        std::string before = accumulate();

        // - Строка закончилась, ничего не вернулось
        // - Строка закончилась, но есть возвращаемый текст
        // - Строка не закончилась, есть токены

        // ---

        std::string after = accumulate();

        return Token(Specifier::d, before, after);
    }

    /* ===================== */
    /* ===== FORMATTER ===== */
    /* ===================== */
    
    Formatter::Formatter(const std::string &format) {
        this->format = format;
        this->tokenizer = new Tokenizer(format);
    }

    std::string format(Args... args) {
        std::string result = "";
        Tokenizer tokenizer = Tokenizer
    }
}

template <typename... Args>
std::string format(const std::string &format, Args... args) {
    return "Nil";
}
