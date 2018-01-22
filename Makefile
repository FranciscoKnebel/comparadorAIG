## COMPILER
CC = g++
CFLAGS = -Wall -g -std=c++11

## DIRECTORIES
SRC_DIR = src/
DST_DIR = dst/
OBJ_DIR = $(DST_DIR)obj/
HEADER_DIR = headers/

### MODULE DIRECTORIES
UTIL_DIR = $(SRC_DIR)util/
AAG_READER_DIR = $(SRC_DIR)aag-reader/
BDD_DIR = $(SRC_DIR)bdd-cmp/
SAT_DIR = $(SRC_DIR)sat-cmp/

MAIN_EXECUTABLE = $(DST_DIR)comparador
all: util aag bdd bdd-file sat
	$(CC) $(CFLAGS) $(AAG_OBJ_FILES) $(UTIL_OBJ_FILES) $(SAT_OBJ_FILES) -o $(MAIN_EXECUTABLE) -I $(AAG_READER_DIR)$(HEADER_DIR) -I $(SAT_DIR)$(HEADER_DIR) -I $(UTIL_DIR) $(SRC_DIR)main.cpp
	@echo "Todos módulos compilados."

UTIL_FILES = $(UTIL_DIR)util.cpp
UTIL_OBJ_FILES = $(OBJ_DIR)util.o
util: $(UTIL_FILES)
	$(CC) $(CFLAGS) -c -o $(OBJ_DIR)util.o -I $(UTIL_DIR) $(UTIL_DIR)util.cpp

#############################################
## AAG READER
AAG_READER = $(DST_DIR)aag-reader
AAG_FILES = $(AAG_READER_DIR)aagReader.cpp $(AAG_READER_DIR)aig.cpp
AAG_OBJ_FILES = $(OBJ_DIR)aagReader.o $(OBJ_DIR)aig.o
aag: $(AAG_FILES)
	$(CC) $(CFLAGS) $(UTIL_OBJ_FILES) -c -o $(OBJ_DIR)aagReader.o -I $(AAG_READER_DIR)$(HEADER_DIR) -I $(UTIL_DIR) $(AAG_READER_DIR)aagReader.cpp
	$(CC) $(CFLAGS) $(UTIL_OBJ_FILES) -c -o $(OBJ_DIR)aig.o -I $(AAG_READER_DIR)$(HEADER_DIR) -I $(UTIL_DIR) $(AAG_READER_DIR)aig.cpp

aag-main: aag
	$(CC) $(CFLAGS) $(AAG_OBJ_FILES) $(UTIL_OBJ_FILES) -o $(AAG_READER) -I $(AAG_READER_DIR)$(HEADER_DIR) -I $(UTIL_DIR) $(AAG_READER_DIR)main.cpp
#############################################
## BDD
BDD_TESTER = $(DST_DIR)bdd-cmp
BDD_FILES = $(BDD_DIR)gerentebdd.cpp
bdd: $(BDD_FILES)
	$(CC) $(BDD_DIR)testBDD.cpp $(BDD_FILES) -o $(BDD_TESTER) -I $(BDD_DIR)$(HEADER_DIR)

bdd-file: $(BDD_FILES)
	$(CC) $(BDD_DIR)testBDD-file.cpp $(BDD_FILES) -o $(BDD_TESTER)-file -I $(BDD_DIR)$(HEADER_DIR)
#############################################
## SAT
SAT_FILES = $(SAT_DIR)sat.cpp
SAT_OBJ_FILES = $(OBJ_DIR)sat.o
sat: $(SAT_FILES)
	$(CC) $(CFLAGS) -c -o $(OBJ_DIR)sat.o -I $(SAT_DIR)$(HEADER_DIR) -I $(AAG_READER_DIR)$(HEADER_DIR) -I $(UTIL_DIR) $(SAT_DIR)sat.cpp

#############################################
## TEST
test: test-aag test-bdd test-main
	@echo "\n\nTESTES FINALIZADOS."

test-main: all
	@echo "\nTESTE DO EXECUTAVEL PRINCIPAL - SAT \n"
	./$(MAIN_EXECUTABLE) examples/aag/C432.aag examples/aag/C432-v1.aag --sat
	@echo
	#./$(MAIN_EXECUTABLE) examples/aag/C432.aag examples/aag/C432-v1.aag --bdd

LOG = $(DST_DIR)aagComentado.txt
test-aag: aag aag-main
	@echo "\nTESTE DO LEITOR DE AAG\n"
	./$(AAG_READER) examples/aag/C17.aag
	@echo "Execução encerrada.\n"
	@cat $(LOG)

test-bdd: bdd
	@echo "\nTESTE DO COMPARADOR UTILIZANDO BDD\n"
	@echo "FUNÇÕES EQUIVALENTES"
	./$(BDD_TESTER) "!(!(v2*v4*!(v2*v4))*!(!(v2*v4)*v2*v4))" "!(!(v2*v4)*!(v2*v4))*!(v2*v4*v2*v4)"
	@echo
	./$(BDD_TESTER) "!(!(v2*v4*!(v2*v4))*!(!(v4*v2)*v2*v4))" "!(!(v4*v2)*!(v4*v2))*!(v2*v4*v2*v4)"
	@echo
	./$(BDD_TESTER) "!(v2*v4)" "!(!(!(v2*v4)))"
	@echo "\nFUNÇÕES NÃO-EQUIVALENTES"
	./$(BDD_TESTER) "!(v2*v4)" "!(!(v2*v4))"
	@echo
	./$(BDD_TESTER) "(!(v2*v4*!(v2*v4))*!(!(v2*v4)*v2*v4))" "!(!(v2*v4)*!(v2*v4))*!(v2*v4*v2*v4)"
	@echo

test-bdd-file: bdd-file dst/expressoes.txt
	@echo "\nTESTE DO COMPARADOR UTILIZANDO BDD, utilizando arquivos.\n"
	./$(BDD_TESTER)-file dst/expressoes.txt

test-minisat: ext/cryptominisat5
	./ext/cryptominisat5 --verb 0 examples/cnf/satisfiable.cnf
	./ext/cryptominisat5 --verb 0 examples/cnf/unsatisfiable.cnf

#############################################
clean:
	rm $(OBJ_DIR)* $(DST_DIR)* -f
