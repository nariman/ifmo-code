"""
Nariman Safiulin (woofilee)
File: B_ext.py
"""

PROBLEM_NAME = "priorityqueue"


def solve(fin, fout):
    a = []
    cnt = 1

    for s in fin.readlines():
        s = s.split()
        if s[0][0] == "p":
            a.append([int(s[1]), cnt])
            cnt += 1
            a.sort(key=lambda x: x[0])
        elif s[0][0] == "e":
            if len(a) > 0:
                fout.write(str(a[0][0]) + "\n")
                del a[0]
            else:
                fout.write("*\n")
        else:
            k, v = int(s[1]), int(s[2])
            for i in range(len(a)):
                if a[i][1] == k:
                    a[i][0] = v
            a.sort(key=lambda x: x[0])
        print(a)


if __name__ == "__main__":
    fin = open(PROBLEM_NAME + ".in", "r")
    fout = open(PROBLEM_NAME + ".out", "w")

    solve(fin, fout)

    fout.close()
    fin.close()
