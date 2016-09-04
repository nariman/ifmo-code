#!/usr/bin/python

import argparse
import os


TESTS_PATH = os.path.abspath("./../../tests") + "\\"
SRC_PATH = os.path.abspath(".") + "\\"

LANGUAGES = {
    "list": ["java", "javascript", "clojure"],
    "default": "clojure",
}

COMMANDS = {
    #  TODO
}

def create_parser():
    parser = argparse.ArgumentParser()
    parser.add_argument("-b", "--build", "-c", "--compile", action="store_true", default=False)
    parser.add_argument("-t", "--test", "-r", "--run", action="store_true", default=False)
    parser.add_argument("-l", "--language", choices=LANGUAGES["list"], default=LANGUAGES["default"])
    parser.add_argument("homework")
    parser.add_argument("difficulty", default="")
    return parser

if __name__ == "__main__":
    args = create_parser().parse_args()

    if args.homework:
        pass
    else:
        raise AssertionError("Homework number not provided")
