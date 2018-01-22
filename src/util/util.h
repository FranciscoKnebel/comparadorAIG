#include <iostream>
#include <vector>
#include <string>
#include <sstream>
#include <memory>
#include <stdexcept>
#include <chrono>

#ifndef UTIL_H
#define UTIL_H

using namespace std;
namespace Util {
	bool isEven(int number);
	bool isOdd(int number);
	vector<string> split(string str, char delimiter);
	void findAndReplaceAll(string & data, string toSearch, string replaceStr);

	string exec(const char* cmd);
};

#endif
