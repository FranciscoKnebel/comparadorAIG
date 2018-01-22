#ifndef AAGREADER_H
#define AAGREADER_H

#include "util.h"
#include "aig.h"

#include <cstdlib>
#include <cstring>
#include <iostream>
#include <fstream>
#include <sstream>
#include <list>

#include <chrono>


using namespace std;

class AAGReader {
  private:
		// File Header
		int readHeader();
		int nNodes, nInputs, nFFs, nOutputs, nAnds;

    ifstream source;
    ofstream debug;
    string word;
    char buffer[250];
    istringstream line;

  public:
    AAGReader(string sourcePath);
    AIG* readFile();
};

#endif // AAGREADER_H
