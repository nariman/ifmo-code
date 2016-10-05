__author__ = 'WooFi'
TASK = "isheap"
import sys

ifile, ofile = open(TASK + ".in", "r"), open(TASK + ".out", "w")
n = int(ifile.readline())
a = list(map(int, ifile.readline().split()))

for i in range(n):
    if 2 * i + 1 < n and not (a[i] <= a[2 * i + 1]) or\
       2 * i + 2 < n and not (a[i] <= a[2 * i + 2]):
        break
else:
    ofile.write("YES")
    sys.exit(0)
ofile.write("NO")
