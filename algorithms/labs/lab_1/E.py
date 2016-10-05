__author__ = 'WooFi'
TASK = "radixsort"
import sys

ifile, ofile = open(TASK + ".in", "r"), open(TASK + ".out", "w")
n, m, k = map(int, ifile.readline().split())
a = [ifile.readline().strip() for _ in range(n)]

def merge_radix_sort(a):
    # sort
    if len(a) == 1:
        return a
    left, right = merge_radix_sort(a[:len(a) // 2], f), merge_radix_sort(a[len(a) // 2:], f)
    # merge
    i, j, l, r, res = 0, 0, len(left), len(right), []
    while i < l and j < r:
        if left[i][-f] <= right[j][-f]:
            res.append(left[i])
            i += 1
        else:
            res.append(right[j])
            j += 1
    for i in range(i, l):
        res.append(left[i])
    for j in range(j, r):
        res.append(right[j])
    return res

a = merge_radix_sort(a)

for s in a:
    ofile.write(s + "\n")