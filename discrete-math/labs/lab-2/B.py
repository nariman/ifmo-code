"""
Nariman Safiulin (woofilee)
File: B.py
Created on: May 02, 2016
"""

PROBLEM_NAME = "shooter"


def solve(fin, fout):
    n, m, k = map(int, fin.readline().split())
    probs = list(map(float, fin.readline().split()))
    kth_probability = 0
    total_probability = 0

    for i in range(n):
        total_probability += (1 - probs[i]) ** m
        if i == k - 1:
            kth_probability = (1 - probs[i]) ** m

    fout.write(str(kth_probability / total_probability))


if __name__ == "__main__":
    fin = open(PROBLEM_NAME + ".in", "r")
    fout = open(PROBLEM_NAME + ".out", "w")

    solve(fin, fout)

    fout.close()
    fin.close()
