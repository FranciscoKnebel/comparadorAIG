#include <cstdlib>
#include "aagReader.h"

using namespace std;

int main(int argc, char* argv[]) {
	if(argc < 2) {
		std::cerr << "Invalid parameters." << std::endl;
		std::cerr << "Usage: " << argv[0] << " FILE.aag" << std::endl;
		return 1;
	}

  AAGReader reader(argv[1]);
  AIG* graph = reader.readFile();

	vector<string> expressions = graph->getOutputExpressions();
	for (size_t i = 0; i < expressions.size(); i++) {
		cout << "Output " << i << ": " << expressions[i] << endl;
	}

  return EXIT_SUCCESS;
}
