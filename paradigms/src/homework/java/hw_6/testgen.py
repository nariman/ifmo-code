import random
import math

res = ""

probability_factor = 0.8

opening_bracket_num = 500
opening_bracket_probability = 0.2  # 0 is Never
unclosed_opening_bracket_probability = 0
unexpected_closing_bracket_probability = 0

variables = ["x", "y", "z"]
variables_probability = 0.4

unary_operators = ["abs", "-"]
unary_operators_probability = 0
unary_operators_space_after_probability = 0.8
unexpected_unary_operators_probability = 0
binary_operators = ["+", "-", "*"]
binary_operators_probability = 0.8
binary_operators_space_after_probability = 0.8
unexpected_binary_operators_probability = 0


# === OPERATORS

def generate_unary_operator():
    return random.choice(unary_operators) + (
        " " if random.random() <= unary_operators_space_after_probability else "")


def generate_binary_operator():
    return random.choice(binary_operators) + (
        " " if random.random() <= unary_operators_space_after_probability else "")


# === VALUES

def generate_value_const():
    t = random.randint(-100, 100)
    while abs(t) < 4:
        t = random.randint(-100, 100)
    return str(t)


def generate_value_variable():
    return random.choice(variables)


def generate_value():
    ret_res = ""

    while random.random() <= unary_operators_probability:
        ret_res += generate_unary_operator()

    if random.random() <= variables_probability:
        return ret_res + generate_value_variable()
    else:
        return ret_res + generate_value_const()


# === OBJECTS

def generate_unexpected():
    if random.random() <= unexpected_closing_bracket_probability:
        return ")"

    if random.random() <= unexpected_unary_operators_probability:
        return random.choice(unary_operators) + (
            " " if random.random() <= unary_operators_space_after_probability else "")

    if random.random() <= unexpected_binary_operators_probability:
        return random.choice(binary_operators) + (
            " " if random.random() <= unary_operators_space_after_probability else "")

    return ""


def generate_object():
    global opening_bracket_num
    ret_res = ""

    ret_res += generate_unexpected()

    if opening_bracket_num > 0 and random.random() <= opening_bracket_probability:
        opening_bracket_num -= 1
        ret_res += "("

        ret_res += generate_unexpected()

        ret_res += generate_expression()

        ret_res += generate_unexpected()

        if random.random() > unclosed_opening_bracket_probability:
            ret_res += ")"
    else:
        ret_res += generate_value()

    ret_res += generate_unexpected()

    return ret_res


def generate_expression():
    ret_res = ""

    ret_res += generate_object()

    if random.random() <= probability_factor * binary_operators_probability:
        ret_res += generate_binary_operator()
        ret_res += generate_object()

    return ret_res

res += generate_object()
while opening_bracket_num > 0:
    res += generate_binary_operator()
    res += generate_expression()

open("test.txt", "w").write(res)
