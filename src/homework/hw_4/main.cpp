/**
 * Nariman Safiulin (woofilee)
 * File: main.cpp
 * Created on: May 06, 2016
 */

#include <cstdio>
#include <iostream>
#include "format.h"

int main() {
    std::string s = "Hello world";
    Format::Tokenizer tokenizer(s);
    printf("%\n");
    printf("%%\n");
    printf("%15%\n");
    return 0;
}