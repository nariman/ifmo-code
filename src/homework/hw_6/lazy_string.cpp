/**
 * Nariman Safiulin (woofilee)
 * File: lazy_string.cpp
 * Created on: May 20, 2016
 */

#include <iostream>
#include <memory>
#include <string>
#include "lazy_string.h"

lazy_string::lazy_char::lazy_char(lazy_string* ls, size_t pos) : ls(ls),
                                                                 pos(pos)
{}

lazy_string::lazy_char::operator char() const
{
    return (*ls->string)[ls->start + this->pos];
}

lazy_string::lazy_char& lazy_string::lazy_char::operator=(char c)
{
    if (ls->string.use_count() > 1)
    {
        ls->string = std::make_shared<std::string>(ls->string->substr(
            ls->start, ls->len));
        ls->start = 0;
    }
    (*ls->string)[ls->start + this->pos] = c;

    return *this;
}

lazy_string::lazy_string(const std::shared_ptr<std::string> string,
                         size_t start, size_t len) :
    string(string),
    start(0),
    len(len)
{}

lazy_string::lazy_string() : lazy_string(std::string())
{}

lazy_string::lazy_string(const std::string& string) :
    string(std::make_shared<std::string>(string)),
    start(0),
    len(string.length())
{}

size_t lazy_string::size() const
{
    return this->len;
}

size_t lazy_string::length() const
{
    return this->len;
}

lazy_string::lazy_char lazy_string::at(size_t pos)
{
    if (pos >= this->len)
        throw std::out_of_range(
            "Lazy string: given position is out of string length");

    return lazy_char(this, pos);
}

const char& lazy_string::at(size_t pos) const
{
    if (pos >= this->len)
        throw std::out_of_range(
            "Lazy string: given position is out of string length");

    return (*this->string)[this->start + pos];
}

lazy_string::lazy_char lazy_string::operator[](size_t pos)
{
    return at(pos);
}

const char& lazy_string::operator[](size_t pos) const
{
    return at(pos);
}

lazy_string lazy_string::substr(size_t pos, size_t len) const
{
    if (start >= this->len)
        throw std::out_of_range(
            "Lazy string: given position is out of string length");

    return lazy_string(this->string, this->start + pos,
                       pos + len > this->len ? this->len - pos : len);
}

lazy_string::operator std::string()
{
    return std::string(this->string->substr(this->start, this->len));
}

std::istream& operator>>(std::istream &is, lazy_string &ls)
{
    auto string = std::make_shared<std::string>();

    is >> *string;
    ls.string = string;
    ls.start = 0;
    ls.len = string->length();

    return is;
}

std::ostream& operator<<(std::ostream &os, lazy_string &ls)
{
    for (size_t i = 0; i < ls.len; ++i)
    {
        os << (*ls.string)[ls.start + i];
    }

    return os;
}
