cmake_minimum_required(VERSION 3.21)
project(myProject)

set(CMAKE_CXX_STANDARD 11)

add_executable(myProject main.cpp hashTree/Node.h md5/md5.h md5/md5.cpp Parser/file_parser.h hashTree/mNode.cpp hashTree/cNode.cpp hashTree/pNode.cpp hashTree/javaTree.h hashTree/javaTree.cpp logger/logger.h Parser/btc_file_parser.h Parser/hex_instruction.h)
