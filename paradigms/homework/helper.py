import argparse
import collections
import json
import os
import subprocess
import time

BIN_PATH = os.path.abspath("./../bin")
TESTS_PATH = os.path.abspath("./../tests")
SOURCES_PATH = os.path.abspath("./")

FILES_TO_CLEAN = (
    ".class"
)

TASK_PREFIX = "hw-"
CONFIG_FILE = "config.json"

COMPILE_LINE = "javac -cp \"{}\" {}"
RUN_LINE = "java -ea -cp \"{}\" {}"


def create_parser():
    parser = argparse.ArgumentParser()
    subparsers = parser.add_subparsers(dest="type")

    task = subparsers.add_parser("task")
    task.add_argument("-b", "--build", action="store_true", default=False)
    task.add_argument("-t", "--test", "-r", "--run", action="store_true", 
                      default=False)
    task.add_argument("-c", "--clean", action="store_true", default=False)
    task.add_argument("language", choices=["java", "js", "clojure"])
    task.add_argument("task", type=int)
    task.add_argument("difficulties", nargs=argparse.REMAINDER)

    clear = subparsers.add_parser("clear")
    clean = subparsers.add_parser("clean")

    return parser


if __name__ == "__main__":
    args = create_parser().parse_args()

    if args.type == "task":
        language_path = os.path.join(SOURCES_PATH, args.language)
        task_path = os.path.join(language_path, TASK_PREFIX + str(args.task))
        config_path = os.path.join(task_path, CONFIG_FILE)

        if not os.path.isdir(task_path):
            raise AssertionError("Task number is not correct")

        if not os.path.exists(config_path):
            raise AssertionError("Configuration file is not found")

        config = json.loads(open(config_path).read(),
                            object_pairs_hook=collections.OrderedDict)
        java_paths = ";".join([task_path, TESTS_PATH])

        if args.language == "clojure":
            java_paths = ";".join([java_paths, os.path.join(
                BIN_PATH, "clojure-1.8.0.jar")])

        if args.build:
            print("BUILDING...")

            print("- building task files...")
            for _ in config["build"]["task"]:
                os.system(COMPILE_LINE.format(java_paths, 
                                              os.path.join(task_path, _)))

            print("- building tests files...")
            for _ in config["build"]["tests"]:
                os.system(COMPILE_LINE.format(java_paths,
                                              os.path.join(TESTS_PATH, _)))

            print("BUILDING COMPLETED\n")

        if args.test:
            print("TESTING...")

            if args.language in ("js", "clojure"):
                os.chdir(task_path)

            difficulties = args.difficulties
            if not difficulties:
                difficulties = config["test"]["difficulty"].keys()

            for difficulty in difficulties:
                print("- difficulty: {}...".format(difficulty))
                time.sleep(3)
                
                if difficulty not in config["test"]["difficulty"]:
                    print("  - difficulty is not found in configuration file")
                    continue

                subprocess.call(RUN_LINE.format(
                    java_paths,
                    config["test"]["difficulty"][difficulty]
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
