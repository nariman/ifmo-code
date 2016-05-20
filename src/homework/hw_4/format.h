/**
 * Nariman Safiulin (woofilee)
 * File: format.h
 * Created on: May 06, 2016
 */

#ifndef IFMO_CPP_FORMAT_H
#define IFMO_CPP_FORMAT_H

#include <cstddef>
#include <cstdio>
#include <functional>
#include <iomanip>
#include <iostream>
#include <sstream>
#include <string>

namespace Format
{
    // %[parameter][flags][width][.precision][length]type

    enum class Flag // As BitMask
    {
        none  =      0,
        minus = 1 << 0, // Left-align the output of this placeholder. (the 
                        // default is to right-align the output).
        plus  = 1 << 1, // Prepends a plus for positive signed-numeric types. 
                        // positive = '+', negative = '-'. (the default doesn't
                        // prepend anything in front of positive numbers).
        space = 1 << 2, // Prepends a space for positive signed-numeric types. 
                        // positive = ' ', negative = '-'. This flag is ignored 
                        // if the '+' flag exists. (the default doesn't prepend 
                        // anything in front of positive numbers).
        zero  = 1 << 3, // When the 'width' option is specified, prepends zeros 
                        // for numeric types. (the default prepends spaces). 
                        // For example, printf("%2X",3) produces " 3", while 
                        // printf("%02X",3) produces in "03".
        sharp = 1 << 4, // Alternate form:
                        // For 'g' and 'G' types, trailing zeros are not 
                        // removed.
                        // For 'f', 'F', 'e', 'E', 'g', 'G' types, the output 
                        // always contains a decimal point.
                        // For 'o', 'x', 'X' types, the text '0', '0x', '0X', 
                        // respectively, is prepended to non-zero numbers.
    };


    inline constexpr Flag operator|(Flag l, Flag r)
    { 
        return static_cast<Flag>(static_cast<int>(l) | static_cast<int>(r));
    }

    inline constexpr Flag operator&(Flag l, Flag r)
    { 
        return static_cast<Flag>(static_cast<int>(l) & static_cast<int>(r));
    }

    inline constexpr Flag operator~(Flag c)
    { 
        return static_cast<Flag>(~static_cast<int>(c));
    }

    inline constexpr Flag operator!(Flag c)
    {
        return static_cast<Flag>(!static_cast<int>(c));
    }

    inline Flag &operator|=(Flag &l, Flag r)
    {
        l = l | r; return l;
    }

    inline Flag &operator&=(Flag &l, Flag r)
    {
        l = l & r; return l;
    }


    enum class Width
    {
        none,
        number,   // Minimum number of characters to be printed. If the value to
                  // be printed is shorter than this number, the result is 
                  // padded with blank spaces. The value is not truncated even 
                  // if the result is larger.
        asterisk, // The width is not specified in the format string, but as an 
                  // additional integer value argument preceding the argument 
                  // that has to be formatted.
    };


    enum class Precision
    {
        none,
        number,   // For integer specifiers (d, i, o, u, x, X): precision 
                  // specifies the minimum number of digits to be written. If 
                  // the value to be written is shorter than this number, the 
                  // result is padded with leading zeros. The value is not 
                  // truncated even if the result is longer. A precision of 0 
                  // means that no character is written for the value 0.
                  // For a, A, e, E, f and F specifiers: this is the number of 
                  // digits to be printed after the decimal point (by default, 
                  // this is 6).
                  // For g and G specifiers: This is the maximum number of 
                  // significant digits to be printed.
                  // For s: this is the maximum number of characters to be 
                  // printed. By default all characters are printed until the 
                  // ending null character is encountered.
                  // If the period is specified without an explicit value for 
                  // precision, 0 is assumed.
        asterisk, // The precision is not specified in the format string, but as
                  // an additional integer value argument preceding the argument 
                  // that has to be formatted.
    };


    enum class Length
    {
        none,
        hh,  // For integer types, causes printf to expect an int-sized integer 
             // argument which was promoted from a char.
        h,   // For integer types, causes printf to expect an int-sized integer 
             // argument which was promoted from a short.
        l,   // For integer types, causes printf to expect a long-sized integer 
             // argument.
             // For floating point types, causes printf to expect a double 
             // argument.
        ll,  // For integer types, causes printf to expect a long long-sized 
             // integer argument.
        L,   // For floating point types, causes printf to expect a long double 
             // argument.
        z,   // For integer types, causes printf to expect a size_t-sized 
             // integer argument.
        j,   // For integer types, causes printf to expect a intmax_t-sized 
             // integer argument.
        t,   // For integer types, causes printf to expect a ptrdiff_t-sized 
             // integer argument.
        // I,
        // I32,
        // I64,
        // q,
        unknown,
    };


    enum class Type
    {
        none,
        percent, // A % followed by another % character will write a single % to
                 // the stream
        d,       // Signed decimal integer
        i,       // Signed decimal integer 
        u,       // Unsigned decimal integer
        f,       // Decimal floating point, lowercase
        F,       // Decimal floating point, uppercase
        e,       // Scientific notation (mantissa/exponent), lowercase 
        E,       // Scientific notation (mantissa/exponent), uppercase 
        g,       // Use the shortest representation: %e or %f
        G,       // Use the shortest representation: %E or %F
        x,       // Unsigned hexadecimal integer   
        X,       // Unsigned hexadecimal integer (uppercase)   
        o,       // Unsigned octal
        s,       // String of characters   
        c,       // Character  
        p,       // Pointer address
        a,       // Hexadecimal floating point, lowercase
        A,       // Hexadecimal floating point, uppercase
        n,       // Nothing printed
                 // The corresponding argument must be a pointer to a signed int
                 // The number of characters written so far is stored in the
                 // pointed location
        unknown,
    };


    class Token
    {
    public:
        Type type;
        Flag flag = Flag::none;
        Width width = Width::none;
        Precision precision = Precision::none;
        Length length = Length::none;

        std::string before = "";
        std::string after = "";
        int width_value = 0;
        int precision_value = 0;
    };


    class Tokenizer
    {
    private:
        std::string format;
        int position;

        /**
         * Parses a text until `%` symbol from the format string.
         * 
         * @return a matched text
         */
        std::string accumulate();
    public:
        Tokenizer(const std::string &format);

        /**
         * Parses a next token in the format string and returns it.
         * 
         * @return a token
         */
        Token* next();
    };


    class Formatter
    {
    private:
        std::string format;
        Tokenizer *tokenizer;

        template<typename To, typename From>
        typename std::enable_if<std::is_convertible<From, To>::value, To>::type
        cast(From value)
        {
            return static_cast<To>(value);
        }

        template<typename To, typename From>
        typename std::enable_if<!std::is_convertible<From, To>::value, To>::type
        cast(From value)
        {
            throw std::invalid_argument("Invalid argument type");
        }

        /**
         * Takes arguments and formats them according to the format string. 
         * 
         * @param   result  accumulating result string
         * @param   token   token with format data
         *
         * @return  formatted string
         */
        std::string apply(std::string& result, Token* token);

        /**
         * Takes arguments and formats them according to the format string. 
         * 
         * @param   result  accumulating result string
         * @param   token   token with format data
         * @param   first   arguments to format
         * @param   rest    arguments to format
         *
         * @return  formatted string
         */
        template <typename First, typename... Rest>
        std::string apply(std::string& result, Token* token, First& first, 
                          Rest&... rest)
        {
            if (token == NULL)
                return result;

            if (token->width == Width::asterisk) {
                token->width = Width::number;
                token->width_value = cast<int>(first);
                return apply(result, token, rest...);
            }

            if (token->precision == Precision::asterisk) {
                token->precision = Precision::number;
                token->precision_value = cast<int>(first);
                return apply(result, token, rest...);
            }

            std::ostringstream token_result;

            if (static_cast<bool>(token->flag & Flag::minus))
                token_result << std::left;
            if (static_cast<bool>(token->flag & Flag::plus))
                token_result << std::showpos;
            if (static_cast<bool>(token->flag & Flag::sharp))
                token_result << std::showbase << std::showpoint;
            if (token->width_value != 0)
                token_result << std::setw(token->width_value);
            if (token->precision_value >= 0)
                token_result << std::setprecision(token->precision_value);

            switch (token->type)
            {
                case Type::percent:
                    token_result << "%";
                    break;
                case Type::d: 
                case Type::i:
                    switch (token->length)
                    {
                        case Length::none: token_result << cast<int>           (first); break;
                        case Length::hh:   token_result << cast<signed char>   (first); break;
                        case Length::h:    token_result << cast<short int>     (first); break;
                        case Length::l:    token_result << cast<long int>      (first); break;
                        case Length::ll:   token_result << cast<long long int> (first); break;
                        case Length::z:    token_result << cast<size_t>        (first); break;
                        case Length::j:    token_result << cast<intmax_t>      (first); break;
                        case Length::t:    token_result << cast<ptrdiff_t>     (first); break;
                        default: throw std::invalid_argument("Length specifier is not supported");
                    }
                    break;
                case Type::X:
                    token_result << std::uppercase;
                case Type::u:
                case Type::o: 
                case Type::x: 
                    switch (token->length)
                    {
                        case Length::none: token_result << cast<unsigned int>           (first); break;
                        case Length::hh:   token_result << cast<unsigned char>          (first); break;
                        case Length::h:    token_result << cast<unsigned short int>     (first); break;
                        case Length::l:    token_result << cast<unsigned long int>      (first); break;
                        case Length::ll:   token_result << cast<unsigned long long int> (first); break;
                        case Length::z:    token_result << cast<size_t>                 (first); break;
                        case Length::j:    token_result << cast<uintmax_t>              (first); break;
                        case Length::t:    token_result << cast<ptrdiff_t>              (first); break;
                        default: throw std::invalid_argument("Length specifier is not supported");
                    }
                    break;
                case Type::F:
                case Type::E:
                case Type::G:
                case Type::A:
                    token_result << std::uppercase;
                case Type::f:
                case Type::e:
                case Type::g:
                case Type::a:
                    switch (token->length)
                    {
                        case Length::none: token_result << cast<double>      (first); break;
                        case Length::L:    token_result << cast<long double> (first); break;
                        default: throw std::invalid_argument("Length specifier is not supported");
                    }
                case Type::s:
                    // TODO: test precision specifier
                    switch (token->length)
                    {
                        case Length::none: token_result << cast<std::string>  (first); break;
                        // case Length::l:    token_result << cast<std::wstring> (first); break;
                        default: throw std::invalid_argument("Length specifier is not supported");
                    }
                    break;
                case Type::c:
                    switch (token->length)
                    {
                        case Length::none: token_result << cast<char>        (first); break;
                        case Length::l:    token_result << cast<std::wint_t> (first); break;
                        default: throw std::invalid_argument("Length specifier is not supported");
                    }
                    break;
                case Type::p: break;
                case Type::n:
                    switch (token->length)
                    {
                        case Length::none: *(cast<int*>           (first)) = result.length(); break;
                        case Length::hh:   *(cast<signed char*>   (first)) = result.length(); break;
                        case Length::h:    *(cast<short int*>     (first)) = result.length(); break;
                        case Length::l:    *(cast<long int*>      (first)) = result.length(); break;
                        case Length::ll:   *(cast<long long int*> (first)) = result.length(); break;
                        case Length::z:    *(cast<size_t*>        (first)) = result.length(); break;
                        case Length::j:    *(cast<intmax_t*>      (first)) = result.length(); break;
                        case Length::t:    *(cast<ptrdiff_t*>     (first)) = result.length(); break;
                        default: throw std::invalid_argument("Length specifier is not supported");
                    }
                    break;
                default:
                    throw std::invalid_argument("Type specifier is not supported");
            }

            result.append(token->before);
            result.append(token_result.str());
            result.append(token->after);
            return apply(result, this->tokenizer->next(), rest...);
        }
    public:
        Formatter(const std::string &format);

        /**
         * Takes arguments and formats them according to the format string. 
         * 
         * @param   args    arguments to format
         *
         * @return  formatted string
         */
        template <typename... Args>
        std::string apply(Args... args)
        {
            std::string result = "";
            return apply(result, this->tokenizer->next(), args...);
        }
    };
};


/**
 * Returns a formatted string according to the format string.
 *
 * @param   fmt     format string
 * @param   args    arguments required by the format specifiers in the format
 *                  string. If there are more arguments than format specifiers,
 *                  the extra arguments are ignored. The number of arguments is
 *                  variable and may be zero.
 * @throws  std::invalid_format If a format string contains an unexpected 
 *                              specifier, an argument can not be converted to 
 *                              required format, or in other illegal conditions.
 * @throws  std::out_of_range   If there are not enough arguments in args list.
 *
 * @return  formatted string
 */
template <typename... Args> std::string format(const std::string& format,
                                               Args... args)
{
    Format::Formatter formatter(format);
    return formatter.apply(args...);
}

#endif // IFMO_CPP_FORMAT_H
