#!/usr/bin/python
# -*- coding: utf-8 -*-
"""
Nariman Safiulin (woofilee)
File: A.py
Created on: May 02, 2016
"""

PROBLEM_NAME = "exam"


def solve(fin, fout):
    k, n = map(int, fin.readline().split())
    total = 0

    for _ in range(k):
        pi, mi = map(int, fin.readline().split())
        total += (pi / n) * (mi / 100)

    fout.write(str(total))


if __name__ == "__main__":
    fin = open(PROBLEM_NAME + ".in", "r")
    fout = open(PROBLEM_NAME + ".out", "w")

    solve(fin, fout)

    fout.close()
    fin.close()
