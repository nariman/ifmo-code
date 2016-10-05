/**
 * Nariman Safiulin (woofilee)
 * File: lazy_string.cpp
 * Created on: May 20, 2016
 */

#include <atomic>
#include <iostream>
#include <memory>
#include <string>
#include <thread>
#include "lazy_string.h"

lazy_lock::lazy_lock() : readers(0)
{}

lazy_lock::lazy_lock(const lazy_lock& lock) : readers(0) // A new instance
{}                                                       // Not a copy

void lazy_lock::read_lock()
{
    this->mutex.lock();
    this->readers++;
    this->mutex.unlock();
}

void lazy_lock::read_unlock()
{
    this->readers--;
}

void lazy_lock::write_lock()
{
    this->mutex.lock();
    while (this->readers.load() > 0)
    {
        std::this_thread::yield();
    }
}

void lazy_lock::write_unlock()
{
    this->mutex.unlock();
}

lazy_string::lazy_char::lazy_char(lazy_string* ls, size_t pos) : ls(ls),
                                                                 pos(pos)
{}

lazy_string::lazy_char::operator char() const
{
    ls->lock->read_lock();
    if (this->pos >= ls->len)
        throw std::out_of_range(
            "Lazy string: given position is out of string length");

    char l_c = (*ls->string)[ls->start + this->pos];
    ls->lock->read_unlock();
    return l_c;
}

lazy_string::lazy_char& lazy_string::lazy_char::operator=(char c)
{
    ls->lock->write_lock();
    if (this->pos >= ls->len)
        throw std::out_of_range(
            "Lazy string: given position is out of string length");
    std::shared_ptr<lazy_lock> l_lock = ls->lock;

    if (ls->string.use_count() > 1)
    {
        ls->string = std::make_shared<std::string>(ls->string->substr(
            ls->start, ls->len));
        ls->lock = std::make_shared<lazy_lock>();
        ls->start = 0;
    }

    (*ls->string)[ls->start + this->pos] = c;
    l_lock->write_unlock();
    return *this;
}

lazy_string::lazy_string(const std::shared_ptr<std::string> string,
                         const std::shared_ptr<lazy_lock> lock,
                         size_t start, size_t len) :
    string(string),
    lock(lock),
    start(0),
    len(len)
{}

lazy_string::lazy_string() : lazy_string(std::string())
{}

lazy_string::lazy_string(const std::string& string) :
    string(std::make_shared<std::string>(string)),
    lock(std::make_shared<lazy_lock>()),
    start(0),
    len(string.length())
{}

size_t lazy_string::length() const
{
    this->lock->read_lock();
    size_t l_len = this->len;
    this->lock->read_unlock();
    return l_len;
}

size_t lazy_string::size() const
{
    return length();
}

lazy_string::lazy_char lazy_string::at(size_t pos)
{
    return lazy_char(this, pos);
}

const char& lazy_string::at(size_t pos) const
{
    this->lock->read_lock();
    if (pos >= this->len)
        throw std::out_of_range(
            "Lazy string: given position is out of string length");

    char& l_c = (*this->string)[this->start + pos];
    this->lock->read_unlock();
    return l_c;
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
    this->lock->read_lock();
    if (start >= this->len)
        throw std::out_of_range(
            "Lazy string: given position is out of string length");

    std::shared_ptr<std::string> l_string = this->string;
    std::shared_ptr<lazy_lock> l_lock = this->lock;
    size_t l_start = this->start;
    size_t l_len = this->len;

    this->lock->read_unlock();
    return lazy_string(l_string, l_lock, l_start + pos,
                       pos + len > l_len ? l_len - pos : len);
}

lazy_string::operator std::string()
{
    this->lock->read_lock();
    std::string l_string = std::string(this->string->substr(this->start,
                                                            this->len));
    this->lock->read_unlock();
    return l_string;
}

std::istream& operator>>(std::istream &is, lazy_string &ls)
{
    auto string = std::make_shared<std::string>();

    is >> *string;
    ls.lock->write_lock();
    std::shared_ptr<lazy_lock> l_lock = ls.lock;

    ls.string = string;
    ls.lock = std::make_shared<lazy_lock>();
    ls.start = 0;
    ls.len = string->length();

    l_lock->write_unlock();
    return is;
}

std::ostream& operator<<(std::ostream &os, lazy_string &ls)
{
    ls.lock->read_lock();
    for (size_t i = 0; i < ls.len; ++i)
    {
        os << (*ls.string)[ls.start + i];
    }
    ls.lock->read_unlock();

    return os;
}
