/**
 * Nariman Safiulin (woofilee)
 * File: main.cpp
 * Created on: May 06, 2016
 */

#include <climits>
#include <iostream>
#include "format.h"

/**
 * Tests by @egormkn (https://github.com/egormkn)
 */
void test()
{ 
    try
    {
        printf("%s\n\n", format("test", 10).c_str());
    }
    catch (std::exception &e)
    {
        printf("Exception: %s\n\n", e.what());
    }

    long g = INT_MAX;
    printf("Original: %-5.20lx %.10d\n", g, 10);
    printf("%s\n\n", format("Format:   %-5.20lx %.10d", g, 10).c_str());


    int val = 0;
    printf("%10.5d sometext %n blah\n", 5, &val);
    printf("val = %d\n\n", val);
    val = 0;
    printf("%s\n", format("%10.5d sometext %n blah", 5, &val).c_str());
    printf("%s\n", format("val = %d\n", val).c_str());


    char zz[] = "foo%% test %% %%%% %i";
    printf("Need: ");
    printf(zz, 100);
    printf("\n");
    printf("Got:  %s\n\n", format(zz, 100).c_str());


    printf("Characters: %c %c \n", 'a', 65);
    printf("%s\n", format("Characters: %c %c \n", 'a', 65).c_str());

    printf("Decimals: %d %ld\n", 1977, 650000L);
    printf("%s\n", format("Decimals: %d %ld\n", 1977, 650000L).c_str());

    printf("Preceding with blanks: %10d \n", 1977);
    printf("%s\n", format("Preceding with blanks: %10d \n", 1977).c_str());

    printf("Preceding with zeros: %010d \n", 1977);
    printf("%s\n", format("Preceding with zeros: %010d \n", 1977).c_str());

    printf("Some different radices: %d %x %o %#x %#o \n", 100, 100, 100, 100, 100);
    printf("%s\n", format("Some different radices: %d %x %o %#x %#o \n", 100, 100, 100, 100, 100).c_str());

    printf("floats: %4.2f %+.0e %E \n", 3.1416, 3.1416, 3.1416);
    printf("%s\n", format("floats: %4.2f %+.0e %E \n", 3.1416, 3.1416, 3.1416).c_str());

    printf("Width trick: %*.*d \n", 5, 5, 10);
    printf("%s\n", format("Width trick: %*.*d \n", 5, 5, 10).c_str());

    printf("%s \n", "A string");
    printf("%s\n", format("%s \n", "A string").c_str());

    printf("floats: %4.2a %+.0A %A \n", 3.1416, 3.1416, 3.1416);
    printf("%s\n", format("floats: %4.2a %+.0A %A \n", 3.1416, 3.1416, 3.1416).c_str());

    printf("Strings:\n");
    printf("%s\n", format("Strings:\n").c_str());
 
    const char* s = "Hello";
    printf("\t.%10s.\n\t.%-10s.\n\t.%*s.\n", s, s, 10, s);
    printf("%s\n", format("\t.%10s.\n\t.%-10s.\n\t.%*s.\n", s, s, 10, s).c_str());
 
    printf("Characters:\t%c %%\n", 65);
    printf("%s\n", format("Characters:\t%c %%\n", 65).c_str());

    setlocale(LC_CTYPE, "rus"); 
    wchar_t wc[] = L"тест";
    printf("Wide characters:\t%lc %lc %%\n", wc[0], wc[1]);
    printf("%s\n", format("Wide characters:\t%lc %lc %%\n", wc[0], wc[1]).c_str());
 
    printf("Integers\n");
    printf("%s\n", format("Integers\n").c_str());

    printf("Decimal:\t%i %d %.6i %i %.0i %+i %u\n", 1, 2, 3, 0, 0, 4, -1);
    printf("%s\n", format("Decimal:\t%i %d %.6i %i %.0i %+i %u\n", 1, 2, 3, 0, 0, 4, -1).c_str());

    printf("Hexadecimal:\t%x %x %X %#x\n", 5, 10, 10, 6);
    printf("%s\n", format("Hexadecimal:\t%x %x %X %#x\n", 5, 10, 10, 6).c_str());

    printf("Octal:\t%o %#o %#o\n", 10, 10, 4);
    printf("%s\n", format("Octal:\t%o %#o %#o\n", 10, 10, 4).c_str());
 
    printf("Floating point\n");
    printf("%s\n", format("Floating point\n").c_str());

    printf("Rounding:\t%f %.0f %.32f\n", 1.5, 1.5, 1.3);
    printf("%s\n", format("Rounding:\t%f %.0f %.32f\n", 1.5, 1.5, 1.3).c_str());

    printf("Padding:\t%05.2f %.2f %5.2f\n", 1.5, 1.5, 1.5);
    printf("%s\n", format("Padding:\t%05.2f %.2f %5.2f\n", 1.5, 1.5, 1.5).c_str());

    printf("Scientific:\t%E %e\n", 1.5, 1.5);
    printf("%s\n", format("Scientific:\t%E %e\n", 1.5, 1.5).c_str());

    printf("Hexadecimal:\t%a %A\n", 1.5, 1.5);
    printf("%s\n", format("Hexadecimal:\t%a %A\n", (double)1.5, 1.5).c_str());

    printf("Special values:\t0/0=%g 1/0=%g\n", 0./0, 1./0);
    printf("%s\n", format("Special values:\t0/0=%g 1/0=%g\n", 0./0, 1./0).c_str());


    printf("Variable width control:\n");
    printf("%s\n", format("Variable width control:\n").c_str());

    printf("right-justified variable width: '%*c'\n", 5, 'x');
    printf("%s\n", format("right-justified variable width: '%*c'\n", 5, 'x').c_str());

    int r1, r2;
    printf("left-justified variable width : '%*c'%n\n", -5, 'x', &r1);
    printf("%s\n", format("left-justified variable width : '%*c'%n\n", -5, 'x', &r2).c_str());

    printf("(the last printf printed %d characters)\n", r1);
    printf("%s\n", format("(the last printf printed %d characters)\n", r2).c_str());

    printf("%17.4000d\n", 4000);
    printf("%s\n", format("%.4000d", 4000).c_str());

    int* p = nullptr;
    printf("%p\n", p);
    printf("%s\n", format("%p", p).c_str());
    
    
    
    std::nullptr_t k;
    printf("nullptr: %s\n", format("%@", k).c_str());

    int* k2 = nullptr;
    printf("nullptr<int>: %s\n", format("%@", k2).c_str());

    int b = 100500;
    int* k3 = &b;
    printf("ptr<int>: %s\n", format("%@", k3).c_str());

    const char k4[10] = "Hello!";
    printf("char[]: %s\n", format("%@", k4).c_str());

    int k5[3] = {100, 2, 3};
    printf("int[3]: %s\n", format("%@", k5).c_str());

    std::string k6 = "test";
    printf("string: %s\n", format("%@", k6).c_str());
}

int main()
{
    test();

    printf("%25.15d-\n", 15555);
    printf(format("%25.15d-\n", 15555).c_str());
    printf("%-25.15d-\n", 15555);
    printf(format("%-25.15d-\n", 15555).c_str());

    printf("%.10s\n", "yo-yo! hello my big world!");
    printf(format("%.10s\n", "yo-yo! hello my big world!").c_str());

    printf("%s\n", format("% 5E", 15).c_str());

    // TESTS FROM JUDGE SYSTEM
    printf("ORIG: hello");
    printf("\nTEST: %s\n", format("hello").c_str());
    printf("ORIG: %%");
    printf("\nTEST: %s\n", format("%%").c_str());
    printf("ORIG: hello%%world");
    printf("\nTEST: %s\n", format("hello%%world").c_str());
    printf("ORIG: %d",1234567);
    printf("\nTEST: %s\n", format("%d",1234567).c_str());
    printf("ORIG: %+d",1234567);
    printf("\nTEST: %s\n", format("%+d",1234567).c_str());
    printf("ORIG: %-5d",1234567);
    printf("\nTEST: %s\n", format("%-5d",1234567).c_str());
    printf("ORIG: % 5d",1234567);
    printf("\nTEST: %s\n", format("% 5d",1234567).c_str());
    printf("ORIG: %05d",1234567);
    printf("\nTEST: %s\n", format("%05d",1234567).c_str());
    printf("ORIG: %.5d",1234567);
    printf("\nTEST: %s\n", format("%.5d",1234567).c_str());
    printf("ORIG: %7.5d",1234567);
    printf("\nTEST: %s\n", format("%7.5d",1234567).c_str());
    printf("ORIG: %i",1234567);
    printf("\nTEST: %s\n", format("%i",1234567).c_str());
    printf("ORIG: %+i",1234567);
    printf("\nTEST: %s\n", format("%+i",1234567).c_str());
    printf("ORIG: %-5i",1234567);
    printf("\nTEST: %s\n", format("%-5i",1234567).c_str());
    printf("ORIG: % 5i",1234567);
    printf("\nTEST: %s\n", format("% 5i",1234567).c_str());
    printf("ORIG: %05i",1234567);
    printf("\nTEST: %s\n", format("%05i",1234567).c_str());
    printf("ORIG: %.5i",1234567);
    printf("\nTEST: %s\n", format("%.5i",1234567).c_str());
    printf("ORIG: %7.5i",1234567);
    printf("\nTEST: %s\n", format("%7.5i",1234567).c_str());
    printf("ORIG: %u",1234567);
    printf("\nTEST: %s\n", format("%u",1234567).c_str());
    printf("ORIG: %+u",1234567);
    printf("\nTEST: %s\n", format("%+u",1234567).c_str());
    printf("ORIG: %-5u",1234567);
    printf("\nTEST: %s\n", format("%-5u",1234567).c_str());
    printf("ORIG: % 5u",1234567);
    printf("\nTEST: %s\n", format("% 5u",1234567).c_str());
    printf("ORIG: %05u",1234567);
    printf("\nTEST: %s\n", format("%05u",1234567).c_str());
    printf("ORIG: %.5u",1234567);
    printf("\nTEST: %s\n", format("%.5u",1234567).c_str());
    printf("ORIG: %7.5u",1234567);
    printf("\nTEST: %s\n", format("%7.5u",1234567).c_str());
    printf("ORIG: %o",1234567);
    printf("\nTEST: %s\n", format("%o",1234567).c_str());
    printf("ORIG: %+o",1234567);
    printf("\nTEST: %s\n", format("%+o",1234567).c_str());
    printf("ORIG: %-5o",1234567);
    printf("\nTEST: %s\n", format("%-5o",1234567).c_str());
    printf("ORIG: % 5o",1234567);
    printf("\nTEST: %s\n", format("% 5o",1234567).c_str());
    printf("ORIG: %05o",1234567);
    printf("\nTEST: %s\n", format("%05o",1234567).c_str());
    printf("ORIG: %.5o",1234567);
    printf("\nTEST: %s\n", format("%.5o",1234567).c_str());
    printf("ORIG: %7.5o",1234567);
    printf("\nTEST: %s\n", format("%7.5o",1234567).c_str());
    printf("ORIG: %#o",1234567);
    printf("\nTEST: %s\n", format("%#o",1234567).c_str());
    printf("ORIG: %x",1234567);
    printf("\nTEST: %s\n", format("%x",1234567).c_str());
    printf("ORIG: %+x",1234567);
    printf("\nTEST: %s\n", format("%+x",1234567).c_str());
    printf("ORIG: %-5x",1234567);
    printf("\nTEST: %s\n", format("%-5x",1234567).c_str());
    printf("ORIG: % 5x",1234567);
    printf("\nTEST: %s\n", format("% 5x",1234567).c_str());
    printf("ORIG: %05x",1234567);
    printf("\nTEST: %s\n", format("%05x",1234567).c_str());
    printf("ORIG: %.5x",1234567);
    printf("\nTEST: %s\n", format("%.5x",1234567).c_str());
    printf("ORIG: %7.5x",1234567);
    printf("\nTEST: %s\n", format("%7.5x",1234567).c_str());
    printf("ORIG: %X",1234567);
    printf("\nTEST: %s\n", format("%X",1234567).c_str());
    printf("ORIG: %+X",1234567);
    printf("\nTEST: %s\n", format("%+X",1234567).c_str());
    printf("ORIG: %-5X",1234567);
    printf("\nTEST: %s\n", format("%-5X",1234567).c_str());
    printf("ORIG: % 5X",1234567);
    printf("\nTEST: %s\n", format("% 5X",1234567).c_str());
    printf("ORIG: %05X",1234567);
    printf("\nTEST: %s\n", format("%05X",1234567).c_str());
    printf("ORIG: %.5X",1234567);
    printf("\nTEST: %s\n", format("%.5X",1234567).c_str());
    printf("ORIG: %7.5X",1234567);
    printf("\nTEST: %s\n", format("%7.5X",1234567).c_str());
    printf("ORIG: %#4x",1234567);
    printf("\nTEST: %s\n", format("%#4x",1234567).c_str());
    printf("ORIG: %#X",1234567);
    printf("\nTEST: %s\n", format("%#X",1234567).c_str());
    printf("ORIG: %f",1234567.0);
    printf("\nTEST: %s\n", format("%f",1234567.0).c_str());
    printf("ORIG: %+f",1234567.0);
    printf("\nTEST: %s\n", format("%+f",1234567.0).c_str());
    printf("ORIG: %-5f",1234567.0);
    printf("\nTEST: %s\n", format("%-5f",1234567.0).c_str());
    printf("ORIG: % 5f",1234567.0);
    printf("\nTEST: %s\n", format("% 5f",1234567.0).c_str());
    printf("ORIG: %05f",1234567.0);
    printf("\nTEST: %s\n", format("%05f",1234567.0).c_str());
    printf("ORIG: %.5f",1234567.0);
    printf("\nTEST: %s\n", format("%.5f",1234567.0).c_str());
    printf("ORIG: %7.5f",1234567.0);
    printf("\nTEST: %s\n", format("%7.5f",1234567.0).c_str());
    printf("ORIG: %F",1234567.0);
    printf("\nTEST: %s\n", format("%F",1234567.0).c_str());
    printf("ORIG: %+F",1234567.0);
    printf("\nTEST: %s\n", format("%+F",1234567.0).c_str());
    printf("ORIG: %-5F",1234567.0);
    printf("\nTEST: %s\n", format("%-5F",1234567.0).c_str());
    printf("ORIG: % 5F",1234567.0);
    printf("\nTEST: %s\n", format("% 5F",1234567.0).c_str());
    printf("ORIG: %05F",1234567.0);
    printf("\nTEST: %s\n", format("%05F",1234567.0).c_str());
    printf("ORIG: %.5F",1234567.0);
    printf("\nTEST: %s\n", format("%.5F",1234567.0).c_str());
    printf("ORIG: %7.5F",1234567.0);
    printf("\nTEST: %s\n", format("%7.5F",1234567.0).c_str());
    printf("ORIG: %e",1234567.0);
    printf("\nTEST: %s\n", format("%e",1234567.0).c_str());
    printf("ORIG: %+e",1234567.0);
    printf("\nTEST: %s\n", format("%+e",1234567.0).c_str());
    printf("ORIG: %-5e",1234567.0);
    printf("\nTEST: %s\n", format("%-5e",1234567.0).c_str());
    printf("ORIG: % 5e",1234567.0);
    printf("\nTEST: %s\n", format("% 5e",1234567.0).c_str());
    printf("ORIG: %05e",1234567.0);
    printf("\nTEST: %s\n", format("%05e",1234567.0).c_str());
    printf("ORIG: %.5e",1234567.0);
    printf("\nTEST: %s\n", format("%.5e",1234567.0).c_str());
    printf("ORIG: %7.5e",1234567.0);
    printf("\nTEST: %s\n", format("%7.5e",1234567.0).c_str());
    printf("ORIG: %E",1234567.0);
    printf("\nTEST: %s\n", format("%E",1234567.0).c_str());
    printf("ORIG: %+E",1234567.0);
    printf("\nTEST: %s\n", format("%+E",1234567.0).c_str());
    printf("ORIG: %-5E",1234567.0);
    printf("\nTEST: %s\n", format("%-5E",1234567.0).c_str());
    printf("ORIG: % 5E",1234567.0);
    printf("\nTEST: %s\n", format("% 5E",1234567.0).c_str());
    printf("ORIG: %05E",1234567.0);
    printf("\nTEST: %s\n", format("%05E",1234567.0).c_str());
    printf("ORIG: %.5E",1234567.0);
    printf("\nTEST: %s\n", format("%.5E",1234567.0).c_str());
    printf("ORIG: %7.5E",1234567.0);
    printf("\nTEST: %s\n", format("%7.5E",1234567.0).c_str());
    printf("ORIG: %g",1234567.0);
    printf("\nTEST: %s\n", format("%g",1234567.0).c_str());
    printf("ORIG: %+g",1234567.0);
    printf("\nTEST: %s\n", format("%+g",1234567.0).c_str());
    printf("ORIG: %-5g",1234567.0);
    printf("\nTEST: %s\n", format("%-5g",1234567.0).c_str());
    printf("ORIG: % 5g",1234567.0);
    printf("\nTEST: %s\n", format("% 5g",1234567.0).c_str());
    printf("ORIG: %05g",1234567.0);
    printf("\nTEST: %s\n", format("%05g",1234567.0).c_str());
    printf("ORIG: %.5g",1234567.0);
    printf("\nTEST: %s\n", format("%.5g",1234567.0).c_str());
    printf("ORIG: %7.5g",1234567.0);
    printf("\nTEST: %s\n", format("%7.5g",1234567.0).c_str());
    printf("ORIG: %G",1234567.0);
    printf("\nTEST: %s\n", format("%G",1234567.0).c_str());
    printf("ORIG: %+G",1234567.0);
    printf("\nTEST: %s\n", format("%+G",1234567.0).c_str());
    printf("ORIG: %-5G",1234567.0);
    printf("\nTEST: %s\n", format("%-5G",1234567.0).c_str());
    printf("ORIG: % 5G",1234567.0);
    printf("\nTEST: %s\n", format("% 5G",1234567.0).c_str());
    printf("ORIG: %05G",1234567.0);
    printf("\nTEST: %s\n", format("%05G",1234567.0).c_str());
    printf("ORIG: %.5G",1234567.0);
    printf("\nTEST: %s\n", format("%.5G",1234567.0).c_str());
    printf("ORIG: %7.5G",1234567.0);
    printf("\nTEST: %s\n", format("%7.5G",1234567.0).c_str());
    printf("ORIG: %a",1234567.0);
    printf("\nTEST: %s\n", format("%a",1234567.0).c_str());
    printf("ORIG: %+a",1234567.0);
    printf("\nTEST: %s\n", format("%+a",1234567.0).c_str());
    printf("ORIG: %-5a",1234567.0);
    printf("\nTEST: %s\n", format("%-5a",1234567.0).c_str());
    printf("ORIG: % 5a",1234567.0);
    printf("\nTEST: %s\n", format("% 5a",1234567.0).c_str());
    printf("ORIG: %05a",1234567.0);
    printf("\nTEST: %s\n", format("%05a",1234567.0).c_str());
    printf("ORIG: %.5a",1234567.0);
    printf("\nTEST: %s\n", format("%.5a",1234567.0).c_str());
    printf("ORIG: %7.5a",1234567.0);
    printf("\nTEST: %s\n", format("%7.5a",1234567.0).c_str());
    printf("ORIG: %A",1234567.0);
    printf("\nTEST: %s\n", format("%A",1234567.0).c_str());
    printf("ORIG: %+A",1234567.0);
    printf("\nTEST: %s\n", format("%+A",1234567.0).c_str());
    printf("ORIG: %-5A",1234567.0);
    printf("\nTEST: %s\n", format("%-5A",1234567.0).c_str());
    printf("ORIG: % 5A",1234567.0);
    printf("\nTEST: %s\n", format("% 5A",1234567.0).c_str());
    printf("ORIG: %05A",1234567.0);
    printf("\nTEST: %s\n", format("%05A",1234567.0).c_str());
    printf("ORIG: %.5A",1234567.0);
    printf("\nTEST: %s\n", format("%.5A",1234567.0).c_str());
    printf("ORIG: %7.5A",1234567.0);
    printf("\nTEST: %s\n", format("%7.5A",1234567.0).c_str());
    printf("ORIG: %c",'a');
    printf("\nTEST: %s\n", format("%c",'a').c_str());
    printf("ORIG: %4c",'a');
    printf("\nTEST: %s\n", format("%4c",'a').c_str());
    printf("ORIG: %.4c",'a');
    printf("\nTEST: %s\n", format("%.4c",'a').c_str());
    printf("ORIG: %s","abcdefg");
    printf("\nTEST: %s\n", format("%s","abcdefg").c_str());
    printf("ORIG: %10s","abcdefg");
    printf("\nTEST: %s\n", format("%10s","abcdefg").c_str());
    printf("ORIG: %010s","abcdefg");
    printf("\nTEST: %s\n", format("%010s","abcdefg").c_str());
    printf("ORIG: %p",nullptr);
    printf("\nTEST: %s\n", format("%p",nullptr).c_str());
    printf("ORIG: %10p",nullptr);
    printf("\nTEST: %s\n", format("%10p",nullptr).c_str());
    printf("ORIG: %*d",5, 10);
    printf("\nTEST: %s\n", format("%*d",5, 10).c_str());
    printf("ORIG: %.*d",5, 10);
    printf("\nTEST: %s\n", format("%.*d",5, 10).c_str());
    printf("ORIG: %*.*d",5, 5, 10);
    printf("\nTEST: %s\n", format("%*.*d",5, 5, 10).c_str());
    printf("ORIG: %*.*d",5, 5, 1234567);
    printf("\nTEST: %s\n", format("%*.*d",5, 5, 1234567).c_str());
    printf("ORIG: %d%s%a%c", 5, "hello", 12, 'c');
    printf("\nTEST: %s\n", format("%d%s%a%c", 5, "hello", 12, 'c').c_str());

    printf("\nTEST: %s\n", format("%@", (int*)nullptr).c_str());
    printf("\nTEST: %s\n", format("%@", std::string("hello world")).c_str());
    printf("\nTEST: %s\n", format<int[3]>("%@", {1, 2, 3}).c_str());

    return 0;
}
