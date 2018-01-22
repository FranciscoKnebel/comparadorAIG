#include "util.h"

namespace Util {
	// Numbers
	bool isOdd(int number) {
		return (number % 2);
	}

	bool isEven(int number) {
		return !isOdd(number);
	}

	// Strings
	vector<string> split(string str, char delimiter) {
	  vector<string> internal;
	  stringstream ss(str); // Turn the string into a stream.
	  string tok;

	  while(getline(ss, tok, delimiter)) {
	    internal.push_back(tok);
	  }

	  return internal;
	}

	void findAndReplaceAll(string & data, string toSearch, string replaceStr) {
		// Get the first occurrence
		size_t pos = data.find(toSearch);

		// Repeat till end is reached
		while(pos != string::npos) {
			// Replace this occurrence of Sub String
			data.replace(pos, toSearch.size(), replaceStr);
			// Get the next occurrence from the current position
			pos = data.find(toSearch, pos + toSearch.size());
		}
	}

	// Others
	string exec(const char* cmd) {
	  array<char, 128> buffer;
	  string result;
	  shared_ptr<FILE> pipe(popen(cmd, "r"), pclose);
	  if (!pipe) throw runtime_error("popen() failed!");
	  while (!feof(pipe.get())) {
	    if (fgets(buffer.data(), 128, pipe.get()) != nullptr)
      	result += buffer.data();
	  }
	  return result;
	}
}
