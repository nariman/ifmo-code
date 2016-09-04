//
// Nariman Safiulin (woofilee)
// File: rational.h
// Created on: Mar 5, 2016
//

#ifndef IFMO_CPP_RATIONAL_H
#define IFMO_CPP_RATIONAL_H

class rational {
private:
    int num;
    int denom;

    int gcd(int a, int b) const;

    void simplify();

public:
    rational(int num, int denom);

    rational(int num);

    int getNum() const;

    int getDenom() const;

    rational operator +(rational const &other) const;

    rational operator -(rational const &other) const;

    rational operator *(rational const &other) const;

    rational operator /(rational const &other) const;
};

#endif IFMO_CPP_RATIONAL_H
