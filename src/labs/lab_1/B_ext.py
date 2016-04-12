__author__ = 'WooFi'
TASK = "priorityqueue"

ifile, ofile = open(TASK + ".in", "r"), open(TASK + ".out", "w")
a = []
cnt = 1

for s in ifile.readlines():
    s = s.split()
    if s[0][0] == "p":
        a.append([int(s[1]), cnt])
        cnt += 1
        a.sort(key=lambda x: x[0])
    elif s[0][0] == "e":
        if len(a) > 0:
            ofile.write(str(a[0][0]) + "\n")
            del a[0]
        else:
            ofile.write("*\n")
    else:
        k, v = int(s[1]), int(s[2])
        for i in range(len(a)):
            if a[i][1] == k:
                a[i][0] = v
        a.sort(key=lambda x: x[0])
    print(a)
