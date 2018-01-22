#include "aig.h"
#include "util.h"

AIGnode::AIGnode() {
	i0 = -1;
	i1 = -1;
	isInput = false;
	expression = "";
}

AIGnode::AIGnode(int param_i0, int param_i1, bool param_isInput, string param_expression) {
	i0 = param_i0;
	i1 = param_i1;
	isInput = param_isInput;
	expression = param_expression;
}

AIGnode::AIGnode(const AIGnode& node) :
	i0(node.i0),
	i1(node.i1),
	isInput(node.isInput),
	expression(node.expression)
{} // COPY CONSTRUCTOR

void AIGnode::printValues() {
	cout << "Input 0: " << i0 << endl;
	cout << "Input 1: " << i1 << endl;
	cout << "isInput: " << isInput << endl;
	cout << "expression: " << expression << endl;
}

AIG::AIG(int nNodes) {
	iterator = nodes.begin();

	// Initialize graph
	int index;
	int step = 2;
	for (index = 2; index < 2 * (nNodes) + 2; index += step) {
		nodes.insert(iterator, std::pair<int,AIGnode*>(index, new AIGnode(-1, -1, false, "")));
	}
}

void AIG::insertInput(int index) {
	// Add input node to graph
	nodes[index] = new AIGnode(-1, -1, true, "v" + std::to_string(index));
}

void AIG::insertOutput(int index) {
	AIGnode* node;
	bool isInput;
	string expression;

	if(Util::isEven(index)) {
		node = nodes.at(index);
	} else {
		node = nodes.at(index - 1);
	}

	isInput = node->isInput;
	expression = node->expression;

	// Add output node to graph
	nodes[index] = new AIGnode(-1, -1, isInput, expression);
	// Add index to output list
	output_indexes.push_back(index);
}

void AIG::insertAND(int index, int input0, int input1) {
	AIGnode* node;
	bool isInput;

	node = nodes.at(index);
	isInput = node->isInput;

	nodes[index] = new AIGnode(input0, input1, isInput, "");
}

string AIG::buildNodeExpression(int index) {
	AIGnode* node;
	if (Util::isEven(index)) {
		node = nodes.at(index);
	} else {
		node = nodes.at(index - 1);
	}

	if (node->isInput)
		return node->expression;

	// Get node expression recursively
	string expression;
	string i0_expression;
	string i1_expression;

	if(Util::isOdd(node->i0)) {
		i0_expression = "!(" + buildNodeExpression(node->i0 - 1) + ")";
	} else {
		i0_expression = buildNodeExpression(node->i0);
	}

	if(Util::isOdd(node->i1)) {
		i1_expression = "!(" + buildNodeExpression(node->i1 - 1) + ")";
	} else {
		i1_expression = buildNodeExpression(node->i1);
	}
	expression = i0_expression + "*" + i1_expression;

	// Is node output negated?
	if(Util::isOdd(index)) {
		return "!(" + expression + ")";
	}
	return expression;
}

vector<string> AIG::getOutputExpressions() {
	vector<string> expressions;

	// Output List
	// cout << "Output List size: " << output_indexes.size() << endl;
	for (size_t i = 0; i < output_indexes.size(); i++) {
		expressions.push_back(to_string(output_indexes[i]) + " " + buildNodeExpression(output_indexes[i]));

		// cout << "Output " << i << " (node " << output_indexes[i] << ")" << endl;
		// cout << "\t" << expressions[i] << endl;
	}

	// // Nodes
	// for (iterator=nodes.begin(); iterator!=nodes.end(); ++iterator)
  //   std::cout
	// 		<< iterator->first << " => "
	// 		<< iterator->second->i0 << " "
	// 		<< iterator->second->i1 << " "
	// 		<< "Input? " << iterator->second->isInput << " "
	// 		<< "Output? " << iterator->second->isOutput << " "
	// 		<< "E: " << iterator->second->expression << " " << endl;

	return expressions;
}
