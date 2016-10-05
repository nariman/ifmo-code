/**
 * Nariman Safiulin (woofilee)
 * File: format.cpp
 * Created on: May 06, 2016
 */

#include <stdexcept>
#include "format.h"

namespace Format
{
    std::string Tokenizer::accumulate()
    {
        std::string cumulated = "";

        while(this->position < this->format.length() 
              && this->format[this->position] != '%')
        {
            cumulated.push_back(this->format[this->position]);
            this->position++;
        }

        return cumulated;
    }

    Tokenizer::Tokenizer(const std::string& format)
    {
        if (format.length() == 0)
            throw std::invalid_argument("Nothing to format");
        this->format = format;
        this->position = 0;
    }

    Token* Tokenizer::next()
    {
        if (this->position >= this->format.length())
            return NULL;

        Token *token = new Token();
        token->before = accumulate();

        if (this->position < this->format.length()
            && this->format[this->position] == '%')
            this->position++;
        else
            return token;
        
        // FLAGS

        while (this->position < this->format.length() 
            && (
                this->format[this->position] == '-' 
                || this->format[this->position] == '+' 
                || this->format[this->position] == ' ' 
                || this->format[this->position] == '#' 
                || this->format[this->position] == '0'
            ))
        {
            switch (this->format[this->position])
            {
                case '-':
                    token->flag |= Flag::minus;
                    token->flag &= ~Flag::zero;
                    break;
                case '+':
                    token->flag |= Flag::plus;
                    token->flag &= ~Flag::space;
                    break;
                case ' ':
                    if (!static_cast<bool>(token->flag & Flag::plus))
                        token->flag |= Flag::space;
                    break;
                case '0':
                    if (!static_cast<bool>(token->flag & Flag::minus))
                        token->flag |= Flag::zero;
                    break;
                case '#':
                    token->flag |= Flag::sharp;
                    break;
            }
            this->position++;
        }

        // WIDTH

        if (this->position < this->format.length())
        {
            if (this->format[this->position] == '*')
            {
                token->width = Width::asterisk;
                this->position++;
            }
            else if (isdigit(this->format[this->position]))
            {
                std::string width_value = "";
                while (this->position < this->format.length() 
                       && isdigit(this->format[this->position]))
                {
                    width_value.push_back(this->format[this->position]);
                    this->position++;
                }
                token->width = Width::number;
                token->width_value = std::stoi(width_value);
            }
        }

        // PRECISION

        if (this->position < this->format.length() - 1 
            && this->format[this->position] == '.')
        {
            this->position++;
            if (this->format[this->position] == '*')
            {
                token->precision = Precision::asterisk;
                this->position++;
            }
            else
            {
                token->precision = Precision::number;
                if (isdigit(this->format[this->position]))
                {
                    std::string precision_value = "";
                    while (this->position < this->format.length() 
                           && isdigit(this->format[this->position]))
                    {
                        precision_value.push_back(this->format[this->position]);
                        this->position++;
                    }
                    token->precision_value = std::stoi(precision_value);
                }
            }
        }

        // LENGTH

        while (this->position < this->format.length()
            && (
                this->format[this->position] == 'h' 
                || this->format[this->position] == 'l' 
                || this->format[this->position] == 'L'
                || this->format[this->position] == 'z' 
                || this->format[this->position] == 'j' 
                || this->format[this->position] == 't' 
            ))
        {
            switch (this->format[this->position])
            {
                case 'h': token->length = token->length == Length::h ? Length::hh : token->length == Length::none ? Length::h : Length::unknown; break;
                case 'l': token->length = token->length == Length::l ? Length::ll : token->length == Length::none ? Length::l : Length::unknown; break;
                case 'L': token->length = token->length == Length::none ? Length::L : Length::unknown; break;
                case 'z': token->length = token->length == Length::none ? Length::z : Length::unknown; break;
                case 'j': token->length = token->length == Length::none ? Length::j : Length::unknown; break;
                case 't': token->length = token->length == Length::none ? Length::t : Length::unknown; break;
            }
            this->position++;
        }

        if (token->length == Length::unknown)
            throw std::invalid_argument("Length specifier is not recognized");

        if (this->position >= this->format.length())
            throw std::invalid_argument("Type specifier is not recognized");

        switch (this->format[this->position])
        {
            case '%': token->type = Type::percent; break;
            case 'd': token->type = Type::d; break;
            case 'i': token->type = Type::i; break;
            case 'u': token->type = Type::u; break;
            case 'f': token->type = Type::f; break;
            case 'F': token->type = Type::F; break;
            case 'e': token->type = Type::e; break;
            case 'E': token->type = Type::E; break;
            case 'g': token->type = Type::g; break;
            case 'G': token->type = Type::G; break;
            case 'x': token->type = Type::x; break;
            case 'X': token->type = Type::X; break;
            case 'o': token->type = Type::o; break;
            case 's': token->type = Type::s; break;
            case 'c': token->type = Type::c; break;
            case 'p': token->type = Type::p; break;
            case 'a': token->type = Type::a; break;
            case 'A': token->type = Type::A; break;
            case 'n': token->type = Type::n; break;
            case '@': token->type = Type::at; break;
            default: std::invalid_argument("Type specifier is not recognized");
        }

        this->position++;
        token->after = accumulate();
        return token;
    }

    Formatter::Formatter(const std::string& format)
    {
        this->format = format;
        this->tokenizer = new Tokenizer(format);
    }

    std::string Formatter::apply(std::string& result, Token* token) {
        while (token != NULL)
        {
            if (token->type == Type::none || token->type == Type::percent)
            {
                result.append(token->before);
                if (token->type == Type::percent)
                    result.append("%");
                result.append(token->after);
            }
            else
            {
                throw std::out_of_range("Not enough arguments to format");
            }

            token = this->tokenizer->next();
        }

        return result;
    }
}
