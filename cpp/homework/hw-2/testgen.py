"""
Nariman Safiulin (woofilee)
File: testgen.py
"""

import random

alpha = "abcdefgihjklmnopqrstuvwxyzABCDEFGIHJKLMNOPQRSTUVWXYZ"
digits = "0123456789"
id = 0
io = open("contacts.txt", "w")

#for _ in range(random.randint(10, 500)):
for _ in range(15):
    id += 1
    io.write(str(id) + " ")

    name_length = random.randint(1, 50)
    number_length = random.randint(1, 50)

    for _ in range(name_length):
        io.write(random.choice(alpha))
    io.write(" ")

    if random.randint(0, 1):
        io.write("+")
    for _ in range(number_length):
        io.write(random.choice(digits))
        if not random.randint(0, 50):
            io.write("(")
        if not random.randint(0, 50):
            io.write(")")
        if not random.randint(0, 25):
            io.write("-")

    io.write("\n")

io.close()
