"""
Nariman Safiulin (woofilee)
File: A.py
Created on: May 09, 2016
"""
from functools import reduce

PROBLEM_NAME = "problem1"


def solve(fin, fout):
    word = fin.readline().strip()
    n, m, k = map(int, fin.readline().split())
    states, accept_states = {_: {} for _ in range(0, n + 1)}, list(map(int, fin.readline().split()))

    for _ in fin:
        f, t, sym = _.split()
        states[int(f)][sym] = int(t)

    fout.write("Accepts") \
        if reduce(
            lambda current_state, sym: \
                states[current_state][sym] \
                    if sym in states[current_state] \
                    else 0,
            word,
            1
        ) in accept_states \
        else fout.write("Rejects")


if __name__ == "__main__":
    fin = open(PROBLEM_NAME + ".in", "r")
    fout = open(PROBLEM_NAME + ".out", "w")

    solve(fin, fout)

    fout.close()
    fin.close()
