//
// Nariman Safiulin (woofilee)
// File: rational.cpp
// Created on: Mar 5, 2016
//

#include "rational.h"

int rational::gcd(int a, int b) const {
    return b ? gcd(b, a % b) : a;
}

void rational::simplify() {
    int gcd_res = gcd(this->num, this->denom);
    this->num /= gcd_res;
    this->denom /= gcd_res;
}

rational::rational(int num, int denom) {
    this->num = num;
    this->denom = denom;
    simplify();
}

rational::rational(int num) {
    this->num = num;
    this->denom = 1;
    simplify();
}

int rational::getNum() const {
    return this->num;
}

int rational::getDenom() const {
    return this->denom;
}

rational rational::operator +(rational const &other) const {
    return rational(this->num * other.denom + this->denom * other.num, this->denom * other.denom);
}

rational rational::operator -(rational const &other) const {
    return rational(this->num * other.denom - this->denom * other.num, this->denom * other.denom);
}

rational rational::operator *(rational const &other) const {
    return rational(this->num * other.num, this->denom * other.denom);
}

rational rational::operator /(rational const &other) const {
    return rational(this->num * other.denom, this->denom * other.num);
}