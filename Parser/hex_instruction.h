//
// Created by DELL on 2022-04-29.
//

#ifndef MYPROJECT_HEX_INSTRUCTION_H
#define MYPROJECT_HEX_INSTRUCTION_H

#endif //MYPROJECT_HEX_INSTRUCTION_H

#include <unordered_map>
#include <fstream>
#include <iostream>
#include <string>

using namespace std;

unordered_map<string,string> get_instruction_map(){
    unordered_map<string,string> ins_map;
    string file_path="E:\\CLionProjects\\myProject\\Parser\\opcode.txt";
    ifstream fin(file_path);
    string buf;
    while(getline(fin, buf)){
        int pos=buf.find(' ');
        string name, code;
        code.assign(buf,0,pos);
        name.assign(buf,pos+1,buf.length()-pos);
        ins_map.emplace(name,code);
    }
    fin.close();
    return ins_map;
}
