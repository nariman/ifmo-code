__author__ = 'WooFi'
TASK = "priorityqueue"

ifile, ofile = open(TASK + ".in", "r"), open(TASK + ".out", "w")

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

for s in ifile.readlines():
    s = s.split()
    if s[0][0] == "p":
        a.append((int(s[1]), len(b)))
        b.append(len(a) - 1)
    elif s[0][0] == "e":
        ofile.write(str(a[0][0]))
        del a[0]
        c -= 1
    else:
        a[b[int(s[1]) - 1] + c] = s[2]
        a.sort(key=lambda x: x[0])
    print(c, a, b)