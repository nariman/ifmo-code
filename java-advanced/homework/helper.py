"""
Java Technologies (Java Advanced) helper for fast building and testing
"""

import argparse
import collections
import json
import os
import subprocess
import time

MAIN_PATH = os.path.dirname(os.path.abspath(__file__))

# OPTIONS

BIN_PATH = os.path.join(MAIN_PATH, "./../bin")
TESTS_PATH = os.path.join(MAIN_PATH, "./../tests")
SOURCES_PATH = os.path.join(MAIN_PATH, "./")

FILES_TO_CLEAN = (
    ".class"
)

TASK_PREFIX = "hw-"
CONFIG_FILE = "config.json"

COMPILE_LINE = "javac -cp \"{}\" {}"
RUN_LINE = "java -ea -cp \"{}\" {} {}"


def create_parser():
    """
    Returns a arguments parser for this helper.
    """
    parser = argparse.ArgumentParser()
    subparsers = parser.add_subparsers(dest="type")

    task = subparsers.add_parser("task")
    task.add_argument("-b", "--build", action="store_true", default=False)
    task.add_argument("-t", "--test", "-r", "--run", action="store_true",
                      default=False)
    task.add_argument("task", type=int)
    task.add_argument("difficulties", nargs=argparse.REMAINDER)

    subparsers.add_parser("clear")
    subparsers.add_parser("clean")

    return parser


# pylint: disable=C0103
if __name__ == "__main__":
    args = create_parser().parse_args()

    if args.type == "task":
        task_path = os.path.join(SOURCES_PATH, TASK_PREFIX + str(args.task))
        config_path = os.path.join(task_path, CONFIG_FILE)

        if not os.path.isdir(task_path):
            raise AssertionError("Task number is not correct")

        if not os.path.exists(config_path):
            raise AssertionError("Configuration file is not found")

        config = json.loads(open(config_path).read(),
                            object_pairs_hook=collections.OrderedDict)
        bins = map(lambda _: os.path.join(BIN_PATH, _ + ".jar"), config["bin"])
        java_paths = ";".join([task_path, TESTS_PATH] + list(bins))

        if args.build:
            print("BUILDING...")

            if len(config["build"]["task"]) > 0:
                print("- building task files...")
                for _ in config["build"]["task"]:
                    os.system(COMPILE_LINE.format(java_paths,
                                                  os.path.join(task_path, _)))

            if len(config["build"]["tests"]) > 0:
                print("- building tests files...")
                for _ in config["build"]["tests"]:
                    os.system(COMPILE_LINE.format(java_paths,
                                                  os.path.join(TESTS_PATH, _)))

            print("BUILDING COMPLETED\n")

        if args.test:
            print("TESTING...")
            os.chdir(task_path)

            difficulties = args.difficulties
            if not difficulties:
                difficulties = config["test"]["difficulties"]

            for difficulty in difficulties:
                print("- difficulty: {}...".format(difficulty))
                time.sleep(3)

                if difficulty not in config["test"]["difficulties"]:
                    print("  - difficulty is not found in configuration file")
                    continue

                subprocess.call(RUN_LINE.format(
                    java_paths,
                    config["test"]["testers"][difficulty],
                    config["test"]["entries"][difficulty]
                ))

                print("  - completed")

            print("TESTING COMPLETED\n")

    elif args.type in ("clear", "clean"):
        for path in (os.walk(SOURCES_PATH), os.walk(TESTS_PATH)):
            for root, dirs, files in path:
                for f in files:
                    p = os.path.abspath(os.path.join(root, f))
                    if p.endswith(FILES_TO_CLEAN):
                        os.remove(p)
