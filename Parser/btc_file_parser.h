//
// Created by DELL on 2022-04-29.
//

#ifndef MYPROJECT_BTC_FILE_PARSER_H
#define MYPROJECT_BTC_FILE_PARSER_H

#endif //MYPROJECT_BTC_FILE_PARSER_H



//input: java file
//output: methods

#pragma once

#include <vector>
#include <string>
#include <iostream>
#include <regex>
#include "..\hashTree\Node.h"


struct feature{
    vector<string> fine;
    string coarse;
};

void trim(string &s)
{
    if (s.empty())
    {
        return ;
    }
    s.erase(0,s.find_first_not_of(" "));
    s.erase(s.find_last_not_of(" ") + 1);
}

vector<feature> get_features_from_file(string file_path){
    ifstream fin(file_path);
    string buf;

    vector<feature> features;
    regex reg(R"(\s\s(\w+\s)+((\w+\.)+\w+\s)?\w+\(.*\).*;)");

    while(getline(fin, buf)){
        if(regex_match(buf, reg)){
            feature f;
            trim(buf);
            f.coarse=buf;
            getline(fin, buf);
            while(getline(fin, buf)){
                if(buf.length()>8 && buf[8]==':'){
                    string ins;
                    int pos=10;
                    while(pos<buf.length() && buf[pos]!=' ') ins.push_back(buf[pos++]);
                    f.fine.push_back(ins);
                }
                else break;
            }
            features.push_back(f);
        }
    }
    return features;
}


vector<mNode> get_methods_from_file(string file_path){

    vector<mNode> methods;

    auto features = get_features_from_file(file_path);

    for(const auto& f:features){
        mNode new_mNode(f.coarse, f.fine);
        methods.push_back(new_mNode);
    }

    return methods;
}
