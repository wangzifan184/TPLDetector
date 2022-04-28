//
// Created by DELL on 2022-04-20.
//

#include <vector>
#include <string>

#include "Node.h"
#include "..\md5\md5.h"

using namespace std;



mNode::mNode(string descriptor) :f_exists(false), raw_descriptor(descriptor) {
    int left=0, right=0;
    while(descriptor[right]!='(') ++right;
    ++right;
    return_type.assign(descriptor,left,right-left-1);
    while(right<descriptor.size()){
        left=right;
        while(right<descriptor.size() && descriptor[right]!=',' && descriptor[right]!=')') ++right;
        if(right<descriptor.size()){
            string new_arg;
            new_arg.assign(descriptor,left,right-left);
            args_type.push_back(new_arg);
        }
        if(descriptor[right]==')') break;
        ++right;
        while(descriptor[right]==' ') ++right;
    }
    //argument type in args_type has form [type name].
    //delete argument name in arg_type
    for(auto & str:args_type){
        int cut=str.length()-1;
        while(cut>=0 && str[cut]!=' ') --cut;
        while(cut>=0 && str[cut]==' ') --cut;
        //delete cut+1:end
        str.erase(cut+1,str.length()-1-cut);
    }

    //delete method name in return_type
    int cut=return_type.length();
    while(cut>=0 && return_type[cut]!=' ') --cut;
    while(cut>=0 && return_type[cut]==' ') --cut;
    return_type.erase(cut+1,return_type.length()-1-cut);
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