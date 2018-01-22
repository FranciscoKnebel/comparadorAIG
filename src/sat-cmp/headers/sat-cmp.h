#ifndef SATCMP_H
#define SATCMP_H

#include <string>
#include <iostream>
#include <algorithm>
#include <map>
#include <vector>
#include <fstream>
#include <sstream>

#include "util.h"
#include "aig.h"

using namespace std;

class SAT_CMP {
	private:
		AIG* graph0;
		AIG* graph1;

	public:
		SAT_CMP(AIG* i0, AIG* i1);
		bool solveSAT();
};

#endif
