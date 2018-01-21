#include <iostream>
#include <string>
#include <map>
#include <vector>
#include <fstream>
#include <sstream>
#include <set>

using namespace std;

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

	public:
		AIG(int);
		void insertInput(int index);
		void insertOutput(int index);
		void insertAND(int index_node, int input0, int input1);
		string buildNodeExpression(int node_index);
		vector<string> getOutputExpressions();
};
