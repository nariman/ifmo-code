/**
 * Nariman Safiulin (woofilee)
 * File: main.cpp
 * Created on: May 20, 2016
 */

#include <cstdio>
#include <mutex>
#include <sstream>
#include <string>
#include "lazy_string.h"

void test()
{
    printf("\nChecking lengths:\n\n");

    printf("Checking empty constructor\n");
    lazy_string ecls; // empty constructor ls
    printf("Expected: 0\n");
    printf("Returned: %d\n", ecls.length());

    printf("Checking empty string\n");
    lazy_string els; // empty ls
    printf("Expected: 0\n");
    printf("Returned: %d\n", els.length());

    std::string s = "hello world"; // string
    lazy_string ls(s); // ls
    printf("Checking string: %s\n", s.c_str());
    printf("Expected: %d\n", s.length());
    printf("Returned: %d\n", ls.length());

    printf("\nChecking immutability of constructor:\n\n");

    s = "hello world";
    printf("Checking string: %s\n", s.c_str());
    s[4] = ' ';
    printf("symbol 4 changed ->: %s\n", s.c_str());
    lazy_string cls(s); // constructor ls
    printf("Expected: %c\n", s[4]);
    printf("Returned: %c\n", cls[4]);

    printf("\nChecking substring:\n\n");

    s = "hello world";
    std::string sss = s.substr(0, 4); // substring string
    printf("Checking string: %s\n", s.c_str());
    lazy_string fssls(s); // for substring ls
    lazy_string ssls = fssls.substr(0, 4); // substring ls
    printf("Expected: %d\n", sss.length());
    printf("Returned: %d\n", ssls.length());
    printf("Expected: %s\n", sss.c_str());
    printf("Returned: %s\n", static_cast<std::string>(ssls).c_str());

    printf("\nChecking immutability of constructor of substring:\n\n");

    s = "hello world";
    sss = s.substr(0, 4);
    printf("Checking string: %s\n", s.c_str());
    lazy_string fcfssls(s); // first for substring ls
    lazy_string fcssls = fcfssls.substr(0, 4); // first substring ls
    sss[3] = 'p';
    fcssls[3] = 'p';
    printf("Expected: %s %s\n", s.c_str(), sss.c_str());
    printf("Returned: %s %s\n", static_cast<std::string>(fcfssls).c_str(), static_cast<std::string>(fcssls).c_str());
    s = "hello world";
    sss = s.substr(0, 4);
    lazy_string scfssls(s); // second for substring ls
    lazy_string scssls = scfssls.substr(0, 4); // second substring ls
    s[3] = 'p';
    scfssls[3] = 'p';
    printf("Expected: %s %s\n", s.c_str(), sss.c_str());
    printf("Returned: %s %s\n", static_cast<std::string>(scfssls).c_str(), static_cast<std::string>(scssls).c_str());

    printf("\nChecking immutability of const lazy string:\n\n");

    s = "hello world";
    const lazy_string cmols(s); // const mutability of ls
    const char* c = &cmols[1];

    printf("\nChecking streams:\n\n");

    printf("Checking input stream\n");
    std::stringstream ss; // stringstream
    std::string yas = "yet_another hello world"; // yet another string
    lazy_string yals; // yet another ls
    ss << yas;
    ss >> yals;
    ss = std::stringstream();
    ss << yas;
    std::string yasc; // yet another string check
    ss >> yasc;
    printf("Expected: %s\n", yasc.c_str());
    printf("Returned: %s\n", static_cast<std::string>(yals).c_str());

    printf("Checking output stream\n");
    yas = "yet_another hello world"; // yet another string
    lazy_string yalsfo(yas); // yet another ls for output
    ss = std::stringstream();
    ss << yas;
    printf("Expected: %s\n", ss.str().c_str());
    ss = std::stringstream();
    ss << yalsfo;
    printf("Returned: %s\n", ss.str().c_str());
}

int main()
{
    test();
    return 0;
}
