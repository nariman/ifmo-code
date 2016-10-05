__author__='WooFi'
TASK = "antiqs"
import sys

ifile, ofile = open(TASK + ".in", "r"), open(TASK + ".out", "w")
a = list(map(int, ifile.readline().split()))
cnt = 0
def qsort(left, right):
    global cnt
    key = a[(left + right) // 2]
    i = left
    j = right
    while i <= j:
        while a[i] < key:
            i += 1
        while a[j] > key:
            j -= 1
        if i <= j:
            a[i], a[j] = a[j], a[i]
            cnt += 1
        i += 1
        j -= 1
        if left < j:
            qsort(left, j)
        if i < right:
            qsort(i, right)

qsort(0, len(a) - 1)

print(cnt)
ofile.write(" ".join(map(str, a)))