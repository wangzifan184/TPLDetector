//
// Created by DELL on 2022-04-23.
//

#ifndef MYPROJECT_LOGGER_H
#define MYPROJECT_LOGGER_H

#endif //MYPROJECT_LOGGER_H

#include <string>
#include <iostream>
#include <fstream>

#include "../hashTree/javaTree.h"

using namespace std;


void log_tree(javaTree jTree, const string& path){
    ofstream log_stream;
    log_stream.open(path, ios::out|ios::app);
    if(!log_stream.is_open()) {
        cout << "open log failure.";
        return;
    }
    //log package
    log_stream<<jTree.get_pNode().get_dir()<<endl;

    //log class
    for(auto cls:jTree.get_pNode().get_class_nodes()){
        log_stream<<"\t"<<cls.get_file()<<endl;
        for(auto mhd:cls.get_methods()){
            log_stream<<"\t\t"<<mhd->get_raw_descriptor()<<" <"<<mhd->get_coarse_feature()<<">"<<" <"<<mhd->get_fine_feature()<<">\n";
        }
        log_stream<<endl;
    }
    log_stream<<endl<<endl;

    log_stream.close();
}