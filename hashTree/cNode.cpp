//
// Created by DELL on 2022-04-20.
//

//class cNode{
//private:
//    pNode parent_package;
//    string class_file;
//    string file_path;
//    vector<mNode*> mNodes;
//public:
//    void sort_methods();
//    string get_feature();
//};

#include <utility>
#include <vector>
#include <algorithm>

#include "Node.h"
#include "..\md5\md5.h"

cNode::cNode(vector<mNode *> mNodes_vec,string _file_path) :f_cas_exists(false),f_md5_exists(false),ff_cas_exists(false),ff_md5_exists(
        false) {
    file_path=std::move(_file_path);
    mNodes=std::move(mNodes_vec);
    sort_methods();
}

int comp(mNode* a, mNode* b){
    string f_a=a->get_coarse_feature();
    string f_b=b->get_coarse_feature();

    auto it_a=f_a.begin(), it_b=f_b.begin();
    while(it_a!=f_a.end() && *it_a==*it_b){
        ++it_a;
        ++it_b;
    }
    return it_a!=f_a.end() && (*it_a)<(*it_b);
}

vector<mNode*> cNode::get_methods() {
    return mNodes;
}

void cNode::sort_methods() {
    if(mNodes.empty() || mNodes.size()<1) return;
    sort(mNodes.begin(), mNodes.end(), comp);
}

string cNode::get_coarse_feature_cascade(){
    if(!f_cas_exists){
        feature_cascade="";
        for(auto method_p:mNodes) feature_cascade+=method_p->get_coarse_feature();
        f_cas_exists=true;
    }
    return feature_cascade;
}

string cNode::get_coarse_feature_md5(){
    if(!f_md5_exists){
        feature_md5=MD5(get_coarse_feature_cascade()).toString();
    }
    return feature_md5;
}

string cNode::get_fine_feature_cascade() {
    if(!ff_cas_exists){
        fine_feature_cascade="";
        for(auto method_p:mNodes) fine_feature_cascade+=method_p->get_fine_feature();
        ff_cas_exists= true;
    }
    return fine_feature_cascade;
}

string cNode::get_fine_feature_md5() {
    if(!ff_md5_exists){
        fine_feature_md5=MD5(get_fine_feature_cascade()).toString();
    }
    return fine_feature_md5;
}

string cNode::get_file() {
    return file_path;
}