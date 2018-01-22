#include "aagReader.h"

using namespace std;
typedef chrono::high_resolution_clock Clock;

int main(int argc, char* argv[]) {
	if(argc < 4) {
		cerr << "Invalid parameters." << endl;
		cerr
			<< "Usage: " << argv[0] << " FILE1.aag FILE2.aag "
			<< "--type (type: sat or bdd)" << endl;

		return 1;
	}

	AAGReader reader1(argv[1]);
	AAGReader reader2(argv[2]);

	auto begin_time = Clock::now();
	AIG* graph1 = reader1.readFile();
	cout << "Leitura de " << argv[1] << " para AIG. (" << chrono::duration_cast<chrono::milliseconds>(Clock::now() - begin_time).count() << "ms)" << endl;

	begin_time = Clock::now();
	AIG* graph2 = reader2.readFile();
	cout << "Leitura de " << argv[2] << " para AIG. (" << chrono::duration_cast<chrono::milliseconds>(Clock::now() - begin_time).count() << "ms)" << endl;

	map<int, string> outputExpressions1;
	map<int, string> outputExpressions2;

	if(strcmp(argv[3], "--sat") == 0) {

	} else if (strcmp(argv[3], "--bdd") == 0) {
		cout << "Comparação utilizando BDD." << endl;

		// Transformação de AIG para expressão lógica do grafo 0
		begin_time = Clock::now();
		vector<string> expressions1 = graph1->getOutputExpressions();
		vector<string> outputExpressions1;
		for (size_t i = 0; i < expressions1.size(); i++) {
			vector<string> tokens = Util::split(expressions1[i], ' ');
			outputExpressions1.push_back(tokens[1]);
		}
		cout << "AIG 0 para Expressão Lógica. (" << chrono::duration_cast<chrono::milliseconds>(Clock::now() - begin_time).count() << "ms)" << endl;

		// Transformação de AIG para expressão lógica do grafo 1
		begin_time = Clock::now();
		vector<string> expressions2 = graph2->getOutputExpressions();
		vector<string> outputExpressions2;
		for (size_t i = 0; i < expressions2.size(); i++) {
			vector<string> tokens = Util::split(expressions2[i], ' ');
			outputExpressions2.push_back(tokens[1]);
		}
		cout << "AIG 1 para Expressão Lógica. (" << chrono::duration_cast<chrono::milliseconds>(Clock::now() - begin_time).count() << "ms)" << endl;

		// Execução do comparador BDD com as expressões lógicas de cada OUTPUT.
		for (size_t i = 0; i < outputExpressions1.size(); i++) {
			cout << "OUTPUT " << i;
			string exp1 = outputExpressions1[i];
			string exp2 = outputExpressions2[i];

			// Salvar expressões em arquivo para leitura pelo comparador.
			ofstream file;
			file.open("dst/expressoes.txt");
			file << exp1 << "\n" << exp2;
			file.close();

			string command = "./dst/bdd-cmp-file \"dst/expressoes.txt\"";
			begin_time = Clock::now();
			string response = Util::exec(command.c_str());
			cout << " (" << chrono::duration_cast<chrono::milliseconds>(Clock::now() - begin_time).count() << "ms" << ")";

			string responseFailure = "FALSE";
			if(response == responseFailure) {
				cout << " não-equivalente. Logo, estrutura não é equivalente." << endl;
				cout << "Encerrando programa..." << endl;
				return EXIT_SUCCESS;
			}
			cout << " equivalente." << endl;
		}
	}

	cout << "Estruturas totalmente equivalentes." << endl;
  return EXIT_SUCCESS;
}
