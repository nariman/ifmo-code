
// Created by woofi on 25.03.2016.
//

#ifndef IFMO_CPP_FORMAT_H
#define IFMO_CPP_FORMAT_H

#include <string>
#include <stdexcept>

template <typename ... Args>
std::string format(std::string const &format, Args ... args);

#endif IFMO_CPP_FORMAT_H
