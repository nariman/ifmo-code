"""
Nariman Safiulin (woofilee)
File: E.py
"""
import random
import string

PROBLEM_NAME = "radixsort"


def solve(fin, fout):
    n, m, k = map(int, fin.readline().split())
    a = [fin.readline().strip() for _ in range(n)]

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
        fout.write(s + "\n")


def testgen(fout):
    def random_string(l):
        return "".join(random.choice(string.ascii_lowercase) for i in range(l))

    a = [random_string(10) for _ in range(10)]
    fout.write("10 10 10\n")
    fout.write("\n".join(map(str, a)))


if __name__ == "__main__":
    fout = open(PROBLEM_NAME + ".in", "w")
    testgen(fout)
    fout.close()

    fin = open(PROBLEM_NAME + ".in", "r")
    fout = open(PROBLEM_NAME + ".out", "w")

    solve(fin, fout)

    fout.close()
    fin.close()
