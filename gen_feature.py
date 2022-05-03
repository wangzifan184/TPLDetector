import os
import pymysql

exe_path = r'E:\CLionProjects\myProject\cmake-build-debug\myProject.exe'


ADDR = "localhost"
USER = "root"
PASS = "rmname"
DB = "tpl"  # id(int) name(varchar) age(int)


def save_to_database(file_path, cursor):
    insert_sql = "insert into maven values(%s,%s,%s,%s,%s)"

    package = None
    cls = None
    method = None
    with open(file_path) as fp:
        line = fp.readline()
        package = line.split('\\')[-2]
        while line:
            line = fp.readline()
            if line.startswith('\t') and not line.startswith('\t\t'):
                cls_end = line.find('.txt')
                cls = line[0:cls_end].split('\\')[-1]
            elif line.startswith('\t\t'):
                colon = line.find(';')
                method = line[2:colon]

                left = colon + 2
                right = left
                while right < len(line) and line[right] != '>':
                    right += 1
                coarse_feature = line[left+1:right]

                left = right + 2
                right = left
                while right < len(line) and line[right] != '>':
                    right += 1
                fine_feature = line[left+1:right]

                # print(method)

                insert = cursor.execute(insert_sql, (package, cls, method, coarse_feature, fine_feature))


def conn_db():
    conn = pymysql.connect(host=ADDR, user=USER, passwd=PASS, db=DB)
    return conn


def commit_close(conn):
    conn.commit()
    conn.close()


def save2DB(log_path):
    conn = conn_db()
    save_to_database(log_path, conn.cursor())
    commit_close(conn)


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


def gen_feature(dir_path):
    recur_transfer(dir_path)
    dir_name = dir_path.split('\\')[-1]
    log_name = dir_name + '.txt'
    log_path = os.path.join(dir_path, log_name)
    os.system(exe_path + ' ' + dir_path + ' ' + log_path)
    save2DB(log_path)


def traverse(dir_path):
    for root, ds, fs in os.walk(dir_path):
        if 'META-INF' in ds:
            parent_dir = '\\'.join(dir_path.split('\\')[0:-1])
            dir_name = dir_path[39:].replace('\\', '+')
            new_dir_path = os.path.join(parent_dir, dir_name)
            os.rename(dir_path, new_dir_path)
            gen_feature(new_dir_path)
            return
        else:
            for d in ds:
                sub_d = os.path.join(dir_path, d)
                traverse(sub_d)


if __name__ == '__main__':
    base = r'C:\Users\DELL\Desktop\project\Maven_uj'
    traverse(base)