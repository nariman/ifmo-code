"""
Nariman Safiulin (woofilee)
File: A.py
"""

PROBLEM_NAME = "isheap"


def solve(fin, fout):
    n = int(fin.readline())
    a = list(map(int, fin.readline().split()))

    for i in range(n):
        if 2 * i + 1 < n and not (a[i] <= a[2 * i + 1]) or\
        2 * i + 2 < n and not (a[i] <= a[2 * i + 2]):
            break
    else:
        fout.write("YES")
        return
    fout.write("NO")


if __name__ == "__main__":
    fin = open(PROBLEM_NAME + ".in", "r")
    fout = open(PROBLEM_NAME + ".out", "w")

    solve(fin, fout)

    fout.close()
    fin.close()
