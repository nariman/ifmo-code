__author__ = 'WooFi'
import sys
import random
import string

TASK = "radixsort"
ifile, ofile = open(TASK + ".in", "w"), open(TASK + ".out", "w")

def random_string(l):
   return ''.join(random.choice(string.ascii_lowercase) for i in range(l))

a = [random_string(10) for _ in range(10)]
ifile.write("10 10 10\n")
ifile.write("\n".join(map(str, a)))