#!/usr/bin/python
# -*- coding: utf-8 -*-
"""
Nariman Safiulin (woofilee)
File: D.py
Created on: May 04, 2016

May The 4th Be With You...
"""

"""
UNFORTUNATELY, THIS SOLUTION FACED WITH TIME LIMIT
EVEN WITH PYPY :(
"""
PROBLEM_NAME = "absmarkchain"


def solve(fin, fout):
    n, m = map(int, fin.readline().split())
    matrix = [[0.0] * n for _ in range(n)]
    absorbing = [False] * n
    pos = [0] * n

    for _ in range(m):
        f, t, l = map(float, fin.readline().split())
        matrix[int(f) - 1][int(t) - 1] = l

    na = n
    q = r = 0
    for i in range(n):
        if matrix[i][i] == 1.0:
            absorbing[i] = True
            na -= 1
            pos[i] = r
            r += 1
        else:
            pos[i] = q
            q += 1

    size = max(q, r)
    q_m = [[0.0] * size for _ in range(size)]
    r_m = [[0.0] * size for _ in range(size)]
    n_m = [[0.0] * na for _ in range(na)]
    e_m = [[0.0] * na for _ in range(na)]
    g_m = [[0.0] * (n - na) for _ in range(na)]

    for i in range(n):
        for j in range(n):
            if not absorbing[i]:
                if absorbing[j]:
                    r_m[pos[i]][pos[j]] = matrix[i][j]
                else:
                    q_m[pos[i]][pos[j]] = matrix[i][j]

    for i in range(na):
        n_m[i][i] = e_m[i][i] = 1.0
        for j in range(na):
            e_m[i][j] -= q_m[i][j]

    for i in range(na):
        # mul = 0.0

        if e_m[i][i] != 1.0:
            mul = e_m[i][i]
            for j in range(na):
                e_m[i][j] /= mul
                n_m[i][j] /= mul

        for j in range(na):
            if i != j:
                mul = e_m[j][i]
                for k in range(na):
                    e_m[j][k] -= mul * e_m[i][k]
                    n_m[j][k] -= mul * n_m[i][k]

    for i in range(na):
        for j in range(n - na):
            for k in range(na):
                g_m[i][j] += n_m[i][k] * r_m[k][j]

    for i in range(n):
        ans = 0.0
        if absorbing[i]:
            for j in range(na):
                ans += g_m[j][pos[i]]
            ans += 1
            ans /= n
        fout.write(str(ans) + "\n")


if __name__ == "__main__":
    fin = open(PROBLEM_NAME + ".in", "r")
    fout = open(PROBLEM_NAME + ".out", "w")

    solve(fin, fout)

    fout.close()
    fin.close()
