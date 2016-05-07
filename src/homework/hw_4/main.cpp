/**
 * Nariman Safiulin (woofilee)
 * File: main.cpp
 * Created on: May 06, 2016
 */

#include <cstdio>
#include "format.h"

int main() {
    const char *c = "Hello world";
    Format::Tokenizer tokenizer(c);
    // std::printf("%d", tokenizer->next()->specifier);
    return 0;
}