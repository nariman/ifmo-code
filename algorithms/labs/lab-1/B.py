"""
Nariman Safiulin (woofilee)
File: B.py
"""

PROBLEM_NAME = "priorityqueue"


def solve(fin, fout):
    a = []
    b = []
    c = 0

    """
    def mine(x, y):
        if x[0] < y[0]:
            return -1
        elif x[0] > y[0]:
            b[x[1]], b[y[1]] = b[y[1]], b[x[1]]
            return 1
        else:
            return 0
    """

    for s in fin.readlines():
        s = s.split()
        if s[0][0] == "p":
            a.append((int(s[1]), len(b)))
            b.append(len(a) - 1)
        elif s[0][0] == "e":
            fout.write(str(a[0][0]))
            del a[0]
            c -= 1
        else:
            a[b[int(s[1]) - 1] + c] = s[2]
            a.sort(key=lambda x: x[0])
        print(c, a, b)


if __name__ == "__main__":
    fin = open(PROBLEM_NAME + ".in", "r")
    fout = open(PROBLEM_NAME + ".out", "w")

    solve(fin, fout)

    fout.close()
    fin.close()
