import os
import sys

DIR = "E:\\CLionProjects\\activemq_cls"


def recur_transfer(dir_path):
    for _, subdirs, files in os.walk(dir_path):
        for file in files:
            file_path = os.path.join(dir_path, file)
            if os.path.exists(file_path) and file_path.endswith(".class"):
                command="javap -c " + file_path + " > " + file_path.replace(".class", ".txt")
                os.system(command)
        for subdir in subdirs:
            if subdir != "." and subdir != "..":
                recur_transfer(os.path.join(dir_path, subdir))


if __name__ == '__main__':
    if len(sys.argv)!=2:
        print("usage: cls2btc.py dir")

    changeCode = "chcp 65001"
    os.system(changeCode)

    recur_transfer(sys.argv[1])
