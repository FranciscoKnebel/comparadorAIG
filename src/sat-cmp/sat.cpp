#include "sat-cmp.h"

using namespace std;
SAT_CMP::SAT_CMP(AIG* i0, AIG* i1) {
	graph0 = i0;
	graph1 = i1;
}

void SAT_CMP::solveSAT() {
	vector<string> expressions0 = graph0->getOutputExpressions();
	vector<string> expressions1 = graph1->getOutputExpressions();

	for (size_t i = 0; i < expressions0.size(); i++) {
		string A = Util::split(expressions0[i], ' ')[1];
		string B = Util::split(expressions1[i], ' ')[1];

		// Conjunctive normal form (CNF) for a XOR sub-expression
		// (!A|!B|!C)&(A|B|!C)&(A|!B|C)&(!A|B|C)
		string cnfXOR =
		"(!" + A + "|!" + B + "|!C)&(" + A + "|" + B + "|!C)&(" + A + "|!" + B + "|C)&(!" + A + "|" + B + "|C)";

		// Replace * with &
   	replace(cnfXOR.begin(), cnfXOR.end(), '*', '&');

		// Remove double negatives
		Util::findAndReplaceAll(cnfXOR, "!!", "");
		cout << "\nTODO: VERIFICAR VALIDADE" << endl;
		cout << cnfXOR << endl;

		// Pass boolean expression to Limboole (Boolean to CNF)

		// Save CNF file

		// Pass CNF file to SAT-solver.

		// if not satisfiable, quit and return false.
			// return;
		// else test next expression

	}

	// If all expressions where OK, EQUIVALENT!
	return;
}
