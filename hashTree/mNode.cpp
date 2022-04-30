//
// Created by DELL on 2022-04-20.
//

#include <vector>
#include <string>

#include "Node.h"
#include "..\Parser\hex_instruction.h"
#include "..\md5\md5.h"

using namespace std;



mNode::mNode(string descriptor, vector<string> instructions) :f_exists(false), raw_descriptor(descriptor), raw_instructions(instructions) {
    set_coarse_feature();
    set_fine_feature();
}

void mNode::set_coarse_feature(){
    int left=0, right=0;
    while(raw_descriptor[right]!='(') ++right;
    ++right;
    return_type.assign(raw_descriptor,left,right-left-1);
    while(right<raw_descriptor.size()){
        left=right;
        while(right<raw_descriptor.size() && raw_descriptor[right]!=',' && raw_descriptor[right]!=')') ++right;
        if(right<raw_descriptor.size()){
            string new_arg;
            new_arg.assign(raw_descriptor,left,right-left);
            if(new_arg.length()){
                args_type.push_back(new_arg);
            }
        }
        if(raw_descriptor[right]==')') break;
        ++right;
        while(raw_descriptor[right]==' ') ++right;
    }

    //delete method name in return_type
    int cut=return_type.length();
    while(cut>=0 && return_type[cut]!=' ') --cut;
    while(cut>=0 && return_type[cut]==' ') --cut;
    return_type.erase(cut+1,return_type.length()-1-cut);
}

void mNode::set_fine_feature() {
    auto mp=get_instruction_map();
    string inst_sequence_hex;
    for(const auto& ins:raw_instructions){
        inst_sequence_hex+=mp[ins];
    }
    fine_feature=MD5(inst_sequence_hex).toString();
}


string mNode::get_descriptor(){
    string descriptor=return_type;
    descriptor+="(";
    for(auto str:args_type) descriptor+=str+",";
    auto it=descriptor.end()-1;
    *it=')';
    return descriptor;
}

string mNode::get_coarse_feature(){
    if(!f_exists){
        feature=MD5(get_descriptor()).toString();
        f_exists=true;
    }
    return feature;
}

string mNode::get_raw_descriptor(){
    return raw_descriptor;
}

string mNode::get_fine_feature(){
    return fine_feature;
}