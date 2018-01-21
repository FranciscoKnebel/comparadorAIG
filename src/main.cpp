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
  reader.readFile();

  return EXIT_SUCCESS;
}
