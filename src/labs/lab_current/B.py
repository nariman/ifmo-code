#!/usr/bin/python
# -*- coding: utf-8 -*-
"""
Nariman Safiulin (woofilee)
File: B.py
Created on: May 09, 2016
"""
from functools import reduce

PROBLEM_NAME = "problem2"


def solve(fin, fout):
    word = fin.readline().strip()
    n, m, k = map(int, fin.readline().split())
    states, accept_states = {_: {} for _ in range(0, n + 1)}, list(map(int, fin.readline().split()))

    for _ in fin:
        f, t, sym = _.split()
        if sym in states[int(f)]:
            states[int(f)][sym].add(int(t))
        else:
            states[int(f)].update({sym: set([int(t)])})

    # def f(current_state, position):
    #     if position == len(word):
    #         if current_state in accept_states:
    #             return True
    #         return False

    #     if word[position] in states[current_state]:
    #         for next_state in states[current_state][word[position]]:
    #             if f(next_state, position + 1):
    #                 return True
    #     return False

    # fout.write("Accepts") if f(1, 0) else fout.write("Rejects")

    fout.write("Accepts") \
        if (lambda f, *a: f(f, *a))(
            lambda f, current_state, position: \
                (True \
                    if current_state in accept_states \
                    else False) \
                        if position == len(word) \
                        else False \
                            if word[position] not in states[current_state] \
                            else reduce(
                                lambda status, next_state: \
                                    status | f(f, next_state, position + 1),
                                states[current_state][word[position]],
                                False,
                            ),
            1,
            0
        ) \
        else fout.write("Rejects")


if __name__ == "__main__":
    fin = open(PROBLEM_NAME + ".in", "r")
    fout = open(PROBLEM_NAME + ".out", "w")

    solve(fin, fout)

    fout.close()
    fin.close()
