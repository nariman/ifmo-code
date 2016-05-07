#!/usr/bin/python
# -*- coding: utf-8 -*-
"""
Nariman Safiulin (woofilee)
File: E.py
Created on: May 04, 2016

May The 4th Be With You...
"""

"""
ON PYTHON 3.5 FACED WITH TIME LIMIT
USE PYPY (2.4.0 is ok)
"""
PROBLEM_NAME = "markchain"


def solve(fin, fout):
    n = int(fin.readline())
    matrix = [list(map(float, fin.readline().split())) for _ in range(n)]

    for _ in range(10):
        # matrix @= matrix
        matrix = [[sum([matrix[i][k] * matrix[k][j] for k in range(n)]) for j in
                   range(n)] for i in range(n)]

    for i in range(n):
        fout.write(str(matrix[0][i]) + "\n")


if __name__ == "__main__":
    fin = open(PROBLEM_NAME + ".in", "r")
    fout = open(PROBLEM_NAME + ".out", "w")

    solve(fin, fout)

    fout.close()
    fin.close()
