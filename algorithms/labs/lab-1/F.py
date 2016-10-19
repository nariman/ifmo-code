"""
Nariman Safiulin (woofilee)
File: F.py
"""

PROBLEM_NAME = "antiqs"


def solve_s(fin, fout):
    a = list(map(int, fin.readline().split()))
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
    fout.write(" ".join(map(str, a)))


def solve_f(fin, fout):
    n = int(fin.readline())
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

    fout.write(" ".join(map(str, a)))


if __name__ == "__main__":
    fin = open(PROBLEM_NAME + ".in", "r")
    fout = open(PROBLEM_NAME + ".out", "w")

    solve_s(fin, fout)
    # solve_f(fin, fout)

    fout.close()
    fin.close()
