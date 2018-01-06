"""
Nariman Safiulin (woofilee)
File: C.py
"""

PROBLEM_NAME = "sort"


def solve(fin, fout):
    n = int(fin.readline())

    def merge_sort(a):
        # sort
        if len(a) == 1:
            return a
        left, right = merge_sort(a[:len(a) // 2]), merge_sort(a[len(a) // 2:])
        # merge
        i, j, l, r, res = 0, 0, len(left), len(right), []
        while i < l and j < r:
            if left[i] <= right[j]:
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

    fout.write(" ".join(map(
        str,
        merge_sort(list(map(
            int,
            fin.readline().split()
        )))
    )))


if __name__ == "__main__":
    fin = open(PROBLEM_NAME + ".in", "r")
    fout = open(PROBLEM_NAME + ".out", "w")

    solve(fin, fout)

    fout.close()
    fin.close()
