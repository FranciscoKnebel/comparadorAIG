#include <cstdlib>
#include <iostream>
#include "gerentebdd.h"

using namespace std;

int main(int argc, char *argv[]) {
  gerentebdd g;
  set<string> conjunto_variaveis;

  if(argc != 3) {
    cout << "Please provide two expressions to compare" << endl;
    return EXIT_FAILURE;
  }

  string expr1 = argv[1];
  string expr2 = argv[2];

  nodobdd *nd1 = g.create_from_equation(expr1, conjunto_variaveis);
  nodobdd *nd2 = g.create_from_equation(expr2, conjunto_variaveis);

	//////////////////////////////////////////////////////////
	// Print das expressões e seus nodos.
	// Caso os nodos sejam iguais, as expressões também são.
	//
	// cout << "Expressão 1: " << expr1 << endl;
	// cout << nd1 << endl;
  //
	// cout << "Expressão 2: " << expr2 << endl;
	// cout << nd2 << endl;
	//////////////////////////////////////////////////////////

	if (nd1 == nd2) {
		cout << "TRUE";
	} else {
		cout << "FALSE";
	}

  return EXIT_SUCCESS;
}
