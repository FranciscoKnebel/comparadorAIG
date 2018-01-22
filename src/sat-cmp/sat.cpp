#include "sat-cmp.h"

typedef chrono::high_resolution_clock Clock;

using namespace std;
SAT_CMP::SAT_CMP(AIG* i0, AIG* i1) {
	graph0 = i0;
	graph1 = i1;
}

bool SAT_CMP::solveSAT() {
	ofstream outputFile;

	vector<string> expressions0 = graph0->getOutputExpressions();
	vector<string> expressions1 = graph1->getOutputExpressions();

	auto begin_time = Clock::now();
	for (size_t i = 0; i < expressions0.size(); i++) {
		cout << "[" << i << "] Conversão de expressão para CNF XOR";
		begin_time = Clock::now();
		string A = Util::split(expressions0[i], ' ')[1];
		string B = Util::split(expressions1[i], ' ')[1];
		// Conjunctive normal form (CNF) for a XOR sub-expression
		// (!A|!B|!C)&(A|B|!C)&(A|!B|C)&(!A|B|C)
		string cnfXOR =
		"(!" + A + "&" + B + ")|(" + A + "&!" + B + ")";
		// Replace * with &
   	replace(cnfXOR.begin(), cnfXOR.end(), '*', '&');
		// Remove double negatives
		Util::findAndReplaceAll(cnfXOR, "!!", "");
		cout << " (" << chrono::duration_cast<chrono::milliseconds>(Clock::now() - begin_time).count() << "ms" << ")" << endl;

		cout << "[" << i << "] Salvando expressão em arquivo externo";
		begin_time = Clock::now();
		outputFile.open("dst/limboole_exp.txt", ofstream::trunc);
		outputFile << cnfXOR;
		outputFile.close();
		cout << " (" << chrono::duration_cast<chrono::milliseconds>(Clock::now() - begin_time).count() << "ms" << ")" << endl;

		// Pass boolean expression to Limboole (Boolean to CNF to SAT solver)
		cout << "[" << i << "] Conversão expressão -> CNF -> SAT solver";
		begin_time = Clock::now();
		string command = "./ext/limboole.exe -s dst/limboole_exp.txt";
		string SATresponse = Util::exec(command.c_str());
		cout << " (" << chrono::duration_cast<chrono::milliseconds>(Clock::now() - begin_time).count() << "ms" << ")" << endl;

		// Save response to file
		cout << "[" << i << "] Resposta SAT para arquivo";
		begin_time = Clock::now();
		outputFile.open("dst/sat_response.txt", ofstream::trunc);
		outputFile << SATresponse;
		outputFile.close();
		cout << " (" << chrono::duration_cast<chrono::milliseconds>(Clock::now() - begin_time).count() << "ms" << ")" << endl;

		// Read file and find if SATISFIABLE
		// if found, quit and return false.
		// else test next expression
		ifstream inputFile;
		inputFile.open("dst/sat_response.txt");
		string line;
		size_t found;

		getline(inputFile, line);
		found = line.find("% UNSATISFIABLE formula");
		cout << "[" << i << "] " << line.substr(2) << endl;

		if(found == string::npos) {
			// not found, so IS SATISFIABLE
			return false;
		}
	}

	// If all expressions where OK, EQUIVALENT!
	return true;
}
