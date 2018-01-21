## COMPILER
CC = g++
CFLAGS = -Wall -g

## DIRECTORIES
SRC_DIR = src/
DST_DIR = dst/
HEADER_DIR = headers/

### MODULE DIRECTORIES
AAG_READER_DIR = $(SRC_DIR)aag-reader/
BDD_DIR = $(SRC_DIR)bdd-cmp/

all: aag bdd
	@echo "Todos módulos compilados."

#############################################
## AAG READER
AAG_READER = $(DST_DIR)aag-reader
AAG_FILES = $(AAG_READER_DIR)main.cpp $(AAG_READER_DIR)aagReader.cpp $(AAG_READER_DIR)aig.cpp
aag: $(AAG_FILES)
	$(CC) $(CFLAGS) $(AAG_FILES) -o $(AAG_READER) -I$(AAG_READER_DIR)$(HEADER_DIR)
#############################################
## BDD
BDD_TESTER = $(DST_DIR)testBDD
BDD_FILES = $(BDD_DIR)testBDD.cpp $(BDD_DIR)gerentebdd.cpp
bdd: $(BDD_FILES)
	$(CC) $(BDD_FILES) -o $(BDD_TESTER) -I$(BDD_DIR)$(HEADER_DIR)
#############################################
## TEST
test: test-aag test-bdd
	@echo "\n\nTESTES FINALIZADOS."

EXAMPLE_FILE = examples/C17.aag
LOG = $(DST_DIR)aagComentado.txt
test-aag: aag
	@echo "\nTESTE DO LEITOR DE AAG\n"
	./$(AAG_READER) $(EXAMPLE_FILE)
	@echo "Execução encerrada.\n"
	@echo "Dump do log.\n"
	@cat $(LOG)

test-bdd: bdd
	@echo "\nTESTE DO COMPARADOR UTILIZANDO BDD\n"
	@echo "FUNÇÕES EQUIVALENTES"
	./$(BDD_TESTER) "!(!(v2*v4*!(v2*v4))*!(!(v2*v4)*v2*v4))" "!(!(v2*v4)*!(v2*v4))*!(v2*v4*v2*v4)"
	@echo "\nFUNÇÕES NÃO-EQUIVALENTES"
	./$(BDD_TESTER) "!(v2*v4)" "!(!(v2*v4))"
	@echo
	./$(BDD_TESTER) "(!(v2*v4*!(v2*v4))*!(!(v2*v4)*v2*v4))" "!(!(v2*v4)*!(v2*v4))*!(v2*v4*v2*v4)"
#############################################
clean:
	rm $(DST_DIR)* -rf
