## COMPILER
CC = g++
CFLAGS = -Wall -g -std=c++11

## DIRECTORIES
SRC_DIR = src/
DST_DIR = dst/
OBJ_DIR = $(DST_DIR)obj/
HEADER_DIR = headers/

### MODULE DIRECTORIES
AAG_READER_DIR = $(SRC_DIR)aag-reader/
BDD_DIR = $(SRC_DIR)bdd-cmp/

MAIN_EXECUTABLE = $(DST_DIR)comparador
all: aag bdd
	$(CC) $(CFLAGS) $(AAG_OBJ_FILES) -o $(MAIN_EXECUTABLE) -I$(AAG_READER_DIR)$(HEADER_DIR) $(SRC_DIR)main.cpp
	@echo "Todos módulos compilados."

#############################################
## AAG READER
AAG_READER = $(DST_DIR)aag-reader
AAG_FILES = $(AAG_READER_DIR)aagReader.cpp $(AAG_READER_DIR)aig.cpp $(AAG_READER_DIR)util.cpp
AAG_OBJ_FILES = $(OBJ_DIR)aagReader.o $(OBJ_DIR)aig.o $(OBJ_DIR)util.o
aag: $(AAG_FILES)
	$(CC) $(CFLAGS) -c -o $(OBJ_DIR)aagReader.o -I$(AAG_READER_DIR)$(HEADER_DIR) $(AAG_READER_DIR)aagReader.cpp
	$(CC) $(CFLAGS) -c -o $(OBJ_DIR)aig.o -I$(AAG_READER_DIR)$(HEADER_DIR) $(AAG_READER_DIR)aig.cpp
	$(CC) $(CFLAGS) -c -o $(OBJ_DIR)util.o -I$(AAG_READER_DIR)$(HEADER_DIR) $(AAG_READER_DIR)util.cpp

aag-main: aag
	$(CC) $(CFLAGS) $(AAG_OBJ_FILES) -o $(AAG_READER) -I$(AAG_READER_DIR)$(HEADER_DIR) $(AAG_READER_DIR)main.cpp
#############################################
## BDD
BDD_TESTER = $(DST_DIR)bdd-cmp
BDD_FILES = $(BDD_DIR)testBDD.cpp $(BDD_DIR)gerentebdd.cpp
bdd: $(BDD_FILES)
	$(CC) $(BDD_FILES) -o $(BDD_TESTER) -I$(BDD_DIR)$(HEADER_DIR)
#############################################
## TEST
test: test-aag test-bdd test-main
	@echo "\n\nTESTES FINALIZADOS."

test-main: all
	@echo "\nTESTE DO EXECUTAVEL PRINCIPAL \n"
	./$(MAIN_EXECUTABLE) examples/C17.aag examples/C17-v1.aag --bdd
	@echo
	./$(MAIN_EXECUTABLE) examples/C432.aag examples/C432-v1.aag --bdd

LOG = $(DST_DIR)aagComentado.txt
test-aag: aag aag-main
	@echo "\nTESTE DO LEITOR DE AAG\n"
	./$(AAG_READER) examples/C17.aag
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
#############################################
clean:
	rm $(OBJ_DIR)* $(DST_DIR)* -f
