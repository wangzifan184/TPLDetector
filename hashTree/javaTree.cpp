//
// Created by DELL on 2022-04-20.
//

#include <stdio.h>
#include <io.h>
#include <string>
#include <iostream>

#include "javaTree.h"
#include "../Parser/btc_file_parser.h"

using namespace std;


javaTree::javaTree(string _base_dir):base_dir(norm(_base_dir)) {
    root_p=new pNode(base_dir);

    root_p->set_class_nodes(collect_cNodes(base_dir));

}

vector<cNode> javaTree::collect_cNodes(string dir) {

    vector<cNode> cNodes;

    long handle;
    struct _finddata_t file_info;
    handle=_findfirst((dir+"*").c_str(),&file_info);
    do{
        if(file_info.attrib==_A_SUBDIR && file_info.name[0]!='.'){
            string subdir=dir+file_info.name+"\\";
            vector<cNode> subdir_cNodes= collect_cNodes(subdir);
            cNodes.insert(cNodes.end(),subdir_cNodes.begin(),subdir_cNodes.end());
        }
        else if(is_java_file(file_info.name)){
            cNode new_cNode= gen_cNode(dir+file_info.name);
            cNodes.push_back(new_cNode);
        }
    } while(!_findnext(handle,&file_info));

    _findclose(handle);

    return cNodes;
}

string javaTree::norm(string dir) {
    string ret=dir;
    if(*(dir.end()-1)!='\\'){
        ret+="\\";
    }
    return ret;
}

bool javaTree::is_java_file(char *file_name) {
    int ptr=0;
    while(file_name[ptr]!='\0') ++ptr;
    if(ptr<=5) return false;
    string extension(file_name+ptr-5,5);
    return (extension==".java");
}

cNode javaTree::gen_cNode(string file_path) {
    vector<mNode *> methods = get_methods_from_file(file_path);
    cNode new_cNode(methods,file_path);
    return new_cNode;
}

pNode javaTree::get_pNode() {
    return *root_p;
}