#include "aagReader.h"

AAGReader::AAGReader(string sourcePath) {
	source.open(sourcePath.c_str());
	debug.open("dst/aagComentado.txt",  ofstream::trunc);
}

int AAGReader::readHeader() {
	line >> word;
	nNodes = atoi(word.c_str());
	line >> word;
	nInputs = atoi(word.c_str());
	line >> word;
	nFFs = atoi(word.c_str());
	line >> word;
	nOutputs = atoi(word.c_str());
	line >> word;
	nAnds = atoi(word.c_str());

	if (nNodes != nInputs + nFFs + nAnds) {
		debug << "Wrong file header\n";
		return 0;
	}

	if (nFFs != 0) {
		debug << "FF not supported yet\n";
		return 0;
	}

	debug << "The file header is ok!\n";
	debug << "\nNodes: " << nNodes;
	debug << "\nInputs: " << nInputs;
	debug << "\nFFs: " << nFFs;
	debug << "\nOutputs: " << nOutputs;
	debug << "\nAnds: " << nAnds << "\n\n";

	return 1;
}

AIG* AAGReader::readFile() {
	//treating header
	source.getline(buffer, 250, '\n');
	string s = buffer;
	line.str(s);
	line >> word;

	if(strcmp("aag", word.c_str()) != 0) {
		debug << "the file is not an AAG file!";
		return NULL;
	}

	if(!readHeader())	return NULL;

	int i = 1;
	AIG* graph = new AIG(nNodes, nInputs, nFFs, nOutputs, nAnds);

	debug << "create the AIG and add all nodes\n";
	while(source) {
		source.getline(buffer, 250, '\n');
		s = buffer;

		line.seekg(0);
		line.str(s);
		line >> word;

		if(i >= 1 && i < 1 + nInputs) {
			// Reading inputs from file
			int node_index = stoi(word.substr(0).c_str());
			debug << "input " << node_index << endl;

			graph->insertInput(node_index);
		} else if(i >= 1 + nInputs && i < 1 + nInputs + nOutputs) {
			// Reading outputs from file
			int node_index = stoi(word.substr(0).c_str());
			debug << "output " << node_index << endl;

			graph->insertOutput(node_index);
		} else if(i >= 1 + nInputs + nOutputs && i < 1 + nInputs + nOutputs + nAnds) {
			// Reading AND operations from file
			// and index entrada0 entrada1
			int node_index = stoi(word.substr(0).c_str());
			line >> word;
			int input0_node = stoi(word.substr(0).c_str());
			line >> word;
			int input1_node = stoi(word.substr(0).c_str());

			debug << "and " << node_index << " ";
			debug << input0_node << " ";
			debug << input1_node << endl;

			graph->insertAND(node_index, input0_node, input1_node);
		} else if(word.substr(0).c_str()[0] == 'c') {
			debug << "the comments began. Ignore the file from here!\n";
			break;
		} else if(word.substr(0).c_str()[0] == 'i') { // naming inputs, ignored.
			debug << "(ignored) Input " << word.substr(0).c_str() << endl;
		} else if(word.substr(0).c_str()[0] == 'o') { // naming outputs, ignored.
			debug << "(ignored) Output " << word.substr(0).c_str() << endl;
		}

		i++;
	}

	debug << "returning the AIG\n";

	source.close();
	debug.close();

	return graph;
}
