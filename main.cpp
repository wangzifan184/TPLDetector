

#include <string>
#include <iostream>
#include <fstream>
#include <vector>

//#include "hashTree/javaTree.h"
//#include "logger/logger.h"
//#include "Parser/btc_file_parser.h"

using namespace std;




int main()
{
    string file=R"(E:\CLionProjects\myProject\opcode.txt)";
    fstream fin(file);
    ofstream fout(R"(E:\ClionProjects\myProject\opcode_out.txt)");

    string buf;
    while(getline(fin, buf)){
        int pos=buf.find("    ");
        buf.erase(pos,buf.length()-pos);
        fout<<buf;
    }
    fin.close();
    fout.close();
    return 0;
}


