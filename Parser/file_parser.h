//
// Created by DELL on 2022-04-20.
//

#ifndef MYPROJECT_FILE_PARSER_H
#define MYPROJECT_FILE_PARSER_H

#endif //MYPROJECT_FILE_PARSER_H


//input: java file
//output: methods

#include <vector>
#include <string>
#include <iostream>
#include <regex>
#include "..\hashTree\Node.h"

void trim(string &s)
{
    if (s.empty())
    {
        return ;
    }
    s.erase(0,s.find_first_not_of(" "));
    s.erase(s.find_last_not_of(" ") + 1);
}

bool is_method(const string & buf){
    if(buf.find('=')!=string::npos) return false;
    regex reg("\\s+(public|private|protected)?.*\\(.*\\).*(;|\\{)");
    return regex_match(buf, reg);
}

vector<mNode*> get_methods_from_file(string file_path){
    ifstream fin;
    fin.open(file_path, ifstream::in);


    vector<mNode*> methods;

    if(fin.is_open()){
        string buf;
        while(getline(fin,buf)){
            if(buf.find("/*")!=string::npos){
                int note_end=buf.find("*/")+1;
                int note_start=buf.find("/*");
                buf.erase(note_start, note_end-note_start+1);
                if(buf.find('{')!=string::npos) buf.erase(buf.find('{'),1);
                if(buf.find('}')!=string::npos) buf.erase(buf.find('}'),1);
            }
            if(is_method(buf)){
                buf.erase(buf.length()-1,1);
                trim(buf);
                mNode* method_p=new mNode(buf);
                methods.push_back(method_p);
            }
        }
    }
    else {
        cout<<"open failure"<<file_path<<endl;
    }
    return methods;
}
