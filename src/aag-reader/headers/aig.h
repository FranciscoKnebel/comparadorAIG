#include <iostream>
#include <string>
#include <map>
#include <vector>
#include <fstream>
#include <sstream>
#include <set>

using namespace std;

#ifndef AIG_H
#define AIG_H

class AIGnode {
	public:
		int i0;
		int i1;
		bool isInput;
		string expression;

		AIGnode();
		AIGnode(int i0, int i1, bool isInput, string expression);
		AIGnode(const AIGnode& node);
		void printValues();
};

class AIG {
	map<int,AIGnode*>::iterator iterator;
	map<int,AIGnode*> nodes;

	vector<int> output_indexes;
	int nNodes, nInputs, nFFs, nOutputs, nAnds;

	public:
		AIG(int params_nNodes, int params_nInputs, int params_nFFs, int params_nOutputs, int params_nAnds);
		void insertInput(int index);
		void insertOutput(int index);
		void insertAND(int index_node, int input0, int input1);
		string buildNodeExpression(int node_index);
		vector<string> getOutputExpressions();
};

#endif
