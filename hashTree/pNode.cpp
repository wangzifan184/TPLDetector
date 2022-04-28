//
// Created by DELL on 2022-04-20.
//

#include <utility>
#include <vector>

#include "Node.h"


pNode::pNode(string _base_dir) {
    dir_path=std::move(_base_dir);
}

void pNode::set_class_nodes(vector<cNode> cNodes_vec){
    cNodes=std::move(cNodes_vec);
}

vector<cNode> pNode::get_class_nodes() {
    return cNodes;
}

string pNode::get_dir() {
    return dir_path;
}