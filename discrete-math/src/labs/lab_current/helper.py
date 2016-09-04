#!/usr/bin/python

import argparse
import os

def create_parser():
    parser = argparse.ArgumentParser()
    parser.add_argument("-b", "--build", "-c", "--compile", action='store_true', default=False)
    parser.add_argument("-r", "--run", "-t", "--test", action='store_true', default=False)
    parser.add_argument("-l", "--language", choices=['py', 'kt', 'java'],
                        default='kt')
    parser.add_argument('problem')
    return parser

if __name__ == "__main__":
    args = create_parser().parse_args()

    if args.problem:
        if args.language == "py":
            if args.run:
                os.system("python {0}.py".format(args.problem))
        elif args.language == "kt":
            if args.build:
                os.system("kotlinc {0}.kt".format(args.problem))
            if args.run:
                os.system("kotlin {0}Kt".format(args.problem))
        elif args.language == "java":
            if args.build:
                os.system("javac {0}.java".format(args.problem))
            if args.run:
                os.system("java {0}".format(args.problem))
    else:
        raise AssertionError("Problem name not provided")
