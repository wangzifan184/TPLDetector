

#include <string>


#include "hashTree/javaTree.h"
#include "logger/logger.h"

using namespace std;




int main()
{
    string path=R"(C:\Users\DELL\Desktop\activemq-1.0)";
//    string path=R"(C:\Users\DELL\Desktop\match)";
    javaTree activemq(path);
    log_tree(activemq, R"(E:\CLionProjects\myProject\log_activemq-1.0.txt)");
    return 0;
}


