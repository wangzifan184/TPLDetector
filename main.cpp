

#include "hashTree/javaTree.h"
#include "logger/logger.h"

using namespace std;


int main()
{
    string base_dir=R"(E:\CLionProjects\myProject\activemq_mini)";
    javaTree activemq(base_dir);
    string log_file=R"(E:\CLionProjects\myProject\log.txt)";
    log_tree(activemq, log_file);
}

