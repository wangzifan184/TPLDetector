//
// Created by DELL on 2022-04-17.
//

#ifndef MYPROJECT_FILENODE_H
#define MYPROJECT_FILENODE_H

#endif //MYPROJECT_FILENODE_H
#pragma once

#include <vector>
#include <string>
#include <algorithm>

#include "..\md5\md5.h"

using namespace std;

class mNode;
class cNode;

class pNode{
private:
    vector<cNode> cNodes;
    string dir_path;
public:
    explicit pNode(string _base_dir);

    void set_class_nodes(vector<cNode> cNode_vec);

    vector<cNode> get_class_nodes();

    string get_dir();

    ~pNode()=default;
};

class cNode{
private:
    vector<mNode*> mNodes;
    string file_path;
    string feature_cascade;
    string feature_md5;
    bool f_cas_exists;
    bool f_md5_exists;
public:
    cNode(vector<mNode*> mNodes_vec, string _file_path);

    vector<mNode*> get_methods();

    void sort_methods();

    string get_coarse_feature_cascade();

    string get_coarse_feature_md5();

    string get_file();

    ~cNode()=default;
};

class mNode{
private:
    vector<string> args_type;
    string raw_descriptor;
    string return_type;
    string feature;
    bool f_exists;
public:
    //descriptor: return_type(arg1_type,arg2_type,...)
    explicit mNode(string descriptor);

    string get_descriptor();

    string get_coarse_feature();

    string get_raw_descriptor();

    ~mNode()=default;
};
