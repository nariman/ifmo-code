#!/usr/bin/python
"""
Nariman Safiulin (woofilee)
File: C.py
Created on: May 02, 2016
"""

PROBLEM_NAME = "lottery"


def solve(fin, fout):
    n, m = map(int, fin.readline().split())
    total_probability = 1
    total_sum = 0
    win_sum = []
    win_values = []

    for _ in range(m):
        ai, bi = map(int, fin.readline().split())
        win_values.append(ai)
        win_sum.append(bi)

    for i in range(m - 1):
        total_probability /= win_values[i]
        total_sum += win_sum[i] * total_probability * (win_values[i + 1] - 1) / \
                     win_values[i + 1]

    total_sum += win_sum[m - 1] * total_probability / win_values[m - 1]
    fout.write(str(n - total_sum))


if __name__ == "__main__":
    fin = open(PROBLEM_NAME + ".in", "r")
    fout = open(PROBLEM_NAME + ".out", "w")

    solve(fin, fout)

    fout.close()
    fin.close()
