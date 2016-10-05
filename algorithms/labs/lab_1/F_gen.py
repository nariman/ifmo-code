__author__ = 'WooFi'
import sys
import random
import string

TASK = "antiqs"
ifile, ofile = open(TASK + ".in", "r"), open(TASK + ".out", "w")

n = int(ifile.readline())
a = [0] * n

if n == 1:
    a[0] = 1
elif n == 2:
    a[0] = 1
    a[1] = 2
elif n == 3:
    a[0] = 2
    a[1] = 1
    a[2] = 3
else:
    a[1] = 1
    a[2] = 3
    for step in range(4, n + 1):
        if (step & 1) == 0:
            a[step - 1] = a[(step >> 1) - 1]
        else:
            a[step - 1] = a[step >> 1]
            a[step >> 1] = step
    for i in range(n >> 1):
        a[i] = i + i + 2

ofile.write(" ".join(map(str, a)))