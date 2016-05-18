/**
 * Nariman Safiulin (woofilee)
 * File: format.h
 * Created on: May 06, 2016
 */

#ifndef IFMO_CPP_FORMAT_H
#define IFMO_CPP_FORMAT_H

#include <string>

namespace Format {
    // %([\-+\s\#]*)([0-9]+|[\*])?(\.([0-9]+|[\*]))?(hh|h|l|ll|j|z|t|L)?([d|i|u|o|x|X|f|F|e|E|g|G|a|A|c|s|p|n|%])
    enum class Specifier {
        none,
        d, // Signed decimal integer
        i, // Signed decimal integer 
        u, // Unsigned decimal integer
        o, // Unsigned octal
        x, // Unsigned hexadecimal integer   
        X, // Unsigned hexadecimal integer (uppercase)   
        f, // Decimal floating point, lowercase
        F, // Decimal floating point, uppercase
        e, // Scientific notation (mantissa/exponent), lowercase 
        E, // Scientific notation (mantissa/exponent), uppercase 
        g, // Use the shortest representation: %e or %f
        G, // Use the shortest representation: %E or %F
        a, // Hexadecimal floating point, lowercase
        A, // Hexadecimal floating point, uppercase
        c, // Character  
        s, // String of characters   
        p, // Pointer address
        n, // Nothing printed
           // The corresponding argument must be a pointer to a signed int
           // The number of characters written so far is stored in the pointed 
           // location
        // %, // A % followed by another % character will write a single % to the 
        //    // stream
    };

    enum class Flag {
        none,
        minus, // Left-justify within the given field width; 
               // Right justification is the default (see width sub-specifier).
        plus,  // Forces to preceed the result with a plus or minus sign 
               // (+ or -) even for positive numbers. By default, only negative 
               // numbers are preceded with a - sign.
        space, // If no sign is going to be written, a blank space is inserted 
               // before the value.
        sharp, // Used with o, x or X specifiers the value is preceeded with 0, 
               // 0x or 0X respectively for values different than zero. 
               // Used with a, A, e, E, f, F, g or G it forces the written 
               // output to contain a decimal point even if no more digits 
               // follow. By default, if no digits follow, no decimal point is 
               // written.
        zero,  // Left-pads the number with zeroes (0) instead of spaces when 
               // padding is specified (see width sub-specifier).
    };

    enum class Width {
        none,
        number,
        asterisk,
    };

    enum class Precision {
        none,
        number,
        asterisk,
    };

    enum class Length {
        none,
        hh,
        h,
        l,
        ll,
        j,
        z,
        t,
        L,
    };

    class Token {
    public:
        Specifier specifier;
        Flag flag = Flag::none;
        Width width = Width::none;
        Precision precision = Precision::none;
        Length length = Length::none;

        std::string before;
        std::string after;

        Token(Specifier specifier, std::string before, std::string after);
    };

    class Tokenizer {
    private:
        std::string format;
        std::vector<Token> tokens;
        int position;
        bool resetted;

        void accumulate();
    public:
        Tokenizer(const std::string &format);
        Token next();
        tokenize();
        reset();
    };

    class Formatter {
    private:
        std::string format;
        Tokenizer &tokenizer;
        std::vector<Token> tokens;
        bool firstFormatted;
    public:
        Formatter(const std::string &format)
        std::string format(Args.. args);
    }
};

template <typename... Args>
std::string format(const std::string &format, Args... args);

#endif // IFMO_CPP_FORMAT_H
