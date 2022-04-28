//
// Created by DELL on 2022-04-20.
//

#ifndef MYPROJECT_JAVATREE_H
#define MYPROJECT_JAVATREE_H

#endif //MYPROJECT_JAVATREE_H

#pragma once

#include <string>

#include "Node.h"

using namespace std;

class javaTree{
private:
    string base_dir;
    pNode* root_p;
public:
    explicit javaTree(string _base_dir);

    static string norm(string dir);

    static bool is_java_file(char* file_name);

    vector<cNode> collect_cNodes(string dir_path);

    static cNode gen_cNode(string file_path);

    pNode get_pNode();
};