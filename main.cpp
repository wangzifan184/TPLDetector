

#include <string>


#include "hashTree/javaTree.h"
#include "logger/logger.h"

using namespace std;




int main()
{
//    string path=R"(E:\CLionProjects\activemq_cls)";
    string path=R"(C:\Users\DELL\Desktop\match)";
    javaTree activemq(path);
    log_tree(activemq, R"(E:\CLionProjects\myProject\log_match.txt)");
    return 0;
}


