"""
Nariman Safiulin (woofilee)
File: C.py
"""

open("a.out", "w").write(str(sum(map(int, "".join(open("a.in", "r").readlines()).split()))))
