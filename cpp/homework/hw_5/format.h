/**
 * Nariman Safiulin (woofilee)
 * File: format.h
 * Created on: May 06, 2016
 */

#ifndef IFMO_CPP_FORMAT_H
#define IFMO_CPP_FORMAT_H

#include <cstddef>
#include <sstream>
#include <stdexcept>
#include <typeinfo>

namespace Format
{
    enum class Flag { none = 0, minus = 1 << 0,     // As BitMask
                                plus  = 1 << 1,
                                space = 1 << 2,
                                zero  = 1 << 3,
                                sharp = 1 << 4 };
    enum class Width { none, number, asterisk };
    enum class Precision { none, number, asterisk };
    enum class Length { none, hh, h, l, ll, L, z, j, t, unknown };
    enum class Type { none, percent, d, i, u, f, F, e, E, g, G, x, X, o, s, c, p, a, A, n, at, unknown };

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

    inline Flag &operator|=(Flag &l, Flag r) { l = l | r; return l; }
    inline Flag &operator&=(Flag &l, Flag r) { l = l & r; return l; }

    class Token
    {
    public:
        Type type = Type::none;
        Flag flag = Flag::none;
        Width width = Width::none;
        Precision precision = Precision::none;
        Length length = Length::none;
        std::string before = "", after = "";
        int width_value = -1, precision_value = -1;
    };

    class Tokenizer
    {
    private:
        std::string format;
        int position;

        /**
         * Parses a text until `%` symbol from the format string.
         * 
         * @return A matched text
         */
        std::string accumulate();
    public:
        /**
         * Creates a new tokenizer with specified format string.
         */
        Tokenizer(const std::string &format);

        /**
         * Parses a next token in the format string and returns it.
         * 
         * @return A token
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

        template <typename T>
        std::string format_number(Token* token, std::string type, 
                                  const T& value, std::string length)
        {
            std::string t = "%";
            if (static_cast<bool>(token->flag & Flag::minus)) t.append("-");
            if (static_cast<bool>(token->flag & Flag::plus))  t.append("+");
            if (static_cast<bool>(token->flag & Flag::space)) t.append(" ");
            if (static_cast<bool>(token->flag & Flag::zero))  t.append("0");
            if (static_cast<bool>(token->flag & Flag::sharp)) t.append("#");
            if (token->width_value >= 0)                      t.append(std::to_string(token->width_value));
            if (token->precision_value >= 0)                  t.append("." + std::to_string(token->precision_value));
                                                              t.append(length);
                                                              t.append(type);

            char buff[snprintf(NULL, 0, t.c_str(), value) + 2]; // Long buffer not for snprintf, i want w/o it
            snprintf(buff, sizeof(buff), t.c_str(), value);
            return std::string(buff);
        }

        template<typename T>
        typename std::enable_if<!std::is_convertible<T, std::string>::value
                                    && !std::is_pointer<T>::value
                                    && !std::is_integral<T>::value,
                                std::string>::type
        format_at(const T& value)
        {
            throw std::invalid_argument("Invalid argument type");
        }

        template<typename T, size_t dimsize>
        typename std::enable_if<!std::is_convertible<T*, std::string>::value,
                                std::string>::type
        format_at(const T (&array)[dimsize])
        {
            std::string r = "[";
            for (size_t i = 0; i < dimsize; i++)
            {
                r.append(std::to_string(array[i]));
                if (i + 1 != dimsize)
                    r.append(",");
            }
            r.append("]");
            return r;
        }

        template<typename T>
        typename std::enable_if<std::is_integral<T>::value, std::string>::type
        format_at(const T& value)
        {
            return std::to_string(value);
        }

        template<typename T>
        typename std::enable_if<std::is_convertible<T, std::string>::value, 
                                std::string>::type
        format_at(const T& value)
        {
            return value;
        }

        template<typename T>
        typename std::enable_if<!std::is_convertible<T, std::string>::value
                                    && !std::is_array<T>::value
                                    && std::is_pointer<T>::value,
                                std::string>::type 
        format_at(const T& value)
        {
            std::string r;
            std::string t = typeid(*value).name();

            if (t == "i")
                t = "int";
            else if (t == "Ss")
                t = "std::string";

            if (value == 0)
            {
                r.append("nullptr<")
                 .append(t)
                 .append(">");  
            }
            else
            {
                Formatter formatter("%@");
                r.append("ptr<")
                 .append(t)
                 .append(">(")
                 .append(formatter.apply(*value))
                 .append(")");
            }
            return r;
        }

        /**
         * Takes arguments and formats them according to the format string. 
         * 
         * @param   result  Accumulating result string
         * @param   token   Token with format data
         *
         * @return  Formatted string
         */
        std::string apply(std::string& result, Token* token);

        /**
         * Takes arguments and formats them according to the format string. 
         * 
         * @param   result  Accumulating result string
         * @param   token   Token with format data
         * @param   first   Arguments to format
         * @param   rest    Arguments to format
         *
         * @return  Formatted string
         */
        template <typename First, typename... Rest>
        std::string apply(std::string& result, Token* token, const First& first, 
                          const Rest&... rest)
        {
            if (token == NULL || token->type == Type::none)
                return result;

            // ADDITIONAL ARGUMENTS

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

            // FORMAT OPTIONS

            std::ostringstream token_result;

            if (token->width == Width::number)
            {
                if (token->width_value < 0)
                {
                    token->width_value *= -1;
                    token->flag |= Flag::minus;
                    token->flag &= ~Flag::zero;
                }

                token_result.width(token->width_value);
            }

            if (token->precision == Precision::number)
            {
                if (token->precision_value < 0)
                    throw std::invalid_argument(
                        "Precision specifier cannot be negative");
            }

            if (static_cast<bool>(token->flag & Flag::minus))
                token_result << std::left;

            // TYPES MATCHING

            std::string type = "";
            switch (token->type)
            {
                case Type::percent:
                    token_result << '%';
                    break;
                case Type::d: if (token->type == Type::d) type = "d";
                case Type::i: if (token->type == Type::i) type = "i";
                    switch (token->length)
                    {
                        case Length::none: token_result << format_number(token, type, cast<int>          (first), ""  ); break;
                        case Length::hh:   token_result << format_number(token, type, cast<signed char>  (first), "hh"); break;
                        case Length::h:    token_result << format_number(token, type, cast<short int>    (first), "h" ); break;
                        case Length::l:    token_result << format_number(token, type, cast<long int>     (first), "l" ); break;
                        case Length::ll:   token_result << format_number(token, type, cast<long long int>(first), "ll"); break;
                        case Length::z:    token_result << format_number(token, type, cast<size_t>       (first), "z" ); break;
                        case Length::j:    token_result << format_number(token, type, cast<intmax_t>     (first), "j" ); break;
                        case Length::t:    token_result << format_number(token, type, cast<ptrdiff_t>    (first), "t" ); break;
                        default: throw std::invalid_argument("Length specifier is not supported");
                    }
                    break;
                case Type::X: if (token->type == Type::X) type = "X";
                case Type::u: if (token->type == Type::u) type = "u";
                case Type::o: if (token->type == Type::o) type = "o";
                case Type::x: if (token->type == Type::x) type = "x";
                    switch (token->length)
                    {
                        case Length::none: token_result << format_number(token, type, cast<unsigned int>          (first), ""  ); break;
                        case Length::hh:   token_result << format_number(token, type, cast<unsigned char>         (first), "hh"); break;
                        case Length::h:    token_result << format_number(token, type, cast<unsigned short int>    (first), "h" ); break;
                        case Length::l:    token_result << format_number(token, type, cast<unsigned long int>     (first), "l" ); break;
                        case Length::ll:   token_result << format_number(token, type, cast<unsigned long long int>(first), "ll"); break;
                        case Length::z:    token_result << format_number(token, type, cast<size_t>                (first), "z" ); break;
                        case Length::j:    token_result << format_number(token, type, cast<uintmax_t>             (first), "j" ); break;
                        case Length::t:    token_result << format_number(token, type, cast<ptrdiff_t>             (first), "t" ); break;
                        default: throw std::invalid_argument("Length specifier is not supported");
                    }
                    break;
                case Type::F: if (token->type == Type::F) type = "F";
                case Type::E: if (token->type == Type::E) type = "E";
                case Type::G: if (token->type == Type::G) type = "G";
                case Type::A: if (token->type == Type::A) type = "A";
                case Type::f: if (token->type == Type::f) type = "f";
                case Type::e: if (token->type == Type::e) type = "e";
                case Type::g: if (token->type == Type::g) type = "g";
                case Type::a: if (token->type == Type::a) type = "a";
                    switch (token->length)
                    {
                        case Length::none: token_result << format_number(token, type, cast<double>     (first), ""); break;
                        case Length::L:    token_result << format_number(token, type, cast<long double>(first), "L"); break;
                        default: throw std::invalid_argument("Length specifier is not supported");
                    }
                    break;
                case Type::s:
                    if (token->length == Length::none)
                    {
                        std::string s = cast<std::string>(first);

                        if (token->precision == Precision::number)
                            token_result << s.substr(0, token->precision_value);
                        else
                            token_result << s;
                    }
                    else if (token->length == Length::l)
                    {
                        // WIDE STRING VERY HARD
                    }
                    else
                    {
                        throw std::invalid_argument("Length specifier is not supported");
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
                case Type::p: 
                    if (token->length != Length::none)
                        throw std::invalid_argument("Unsupported length specifier");
                    if (cast<void*>(first) != NULL)
                        token_result << cast<void*>(first);
                    else
                        token_result << "(nil)";
                    break;
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
                case Type::at:
                    if (typeid(first) == typeid(std::nullptr_t))
                        token_result << "nullptr";
                    // else if (std::is_convertible<First, std::string>::value)
                        // token_result << cast<std::string>(first);
                    else
                        token_result << format_at(first);
                    break;
                default:
                    throw std::invalid_argument("Type specifier is not supported");
            }

            result.append(token->before);
            result.append(token_result.str());
            result.append(token->after);
            if (token->type == Type::percent)
                return apply(result, this->tokenizer->next(), first, rest...);
            else
                return apply(result, this->tokenizer->next(), rest...);
        }

    public:
        /**
         * Creates a new formatter with specified format string.
         */
        Formatter(const std::string &format);

        /**
         * Takes arguments and formats them according to the format string. 
         * 
         * @param   args    Arguments to format
         *
         * @return  Formatted string
         */
        template <typename... Args>
        std::string apply(const Args&... args)
        {
            std::string result = "";
            return apply(result, this->tokenizer->next(), args...);
        }
    };
};


/**
 * Returns a formatted string according to the format string.
 * Extra specifier `@`:
 *     If argument type is nullptr_t, prints `nullptr`
 *     If argument type is pointer with value 0, prints `ptr<TYPE>`
 *     If argument type is pointer with non-zero value, prints `ptr<TYPE>(format("%@", VALUE))`
 *     If argument type is array, prints array content in square brackets
 *     If argument can be converted into string, prints this string
 *     Otherwise an exception will be thrown
 *
 * @param   fmt     Format string
 * @param   args    Arguments, accroding to the specifiers in the format string.
 *                  If more arguments provided, than needed, the extra arguments
 *                  are ignored.
 *
 * @return  Formatted string
 *
 * @throws  std::invalid_argument If a format string contains an unexpected 
 *                                specifier, an argument can not be converted to 
 *                                required format, or other errors
 * @throws  std::out_of_range     If there are not enough arguments
 */
template <typename... Args>
std::string format(const std::string& format, const Args&... args)
{
    Format::Formatter formatter(format);
    return formatter.apply(args...);
}

#endif // IFMO_CPP_FORMAT_H
