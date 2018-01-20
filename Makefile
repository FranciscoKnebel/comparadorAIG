CC = g++
CFLAGS = -Wall -g

EXECUTABLE = main
LOG = aagComentado.txt

HEADER_DIR = headers/
SRC_DIR = src/


FILES = $(SRC_DIR)main.cpp $(SRC_DIR)aagReader.cpp $(SRC_DIR)aig.cpp
all: $(FILES)
	$(CC) $(CFLAGS) $(FILES) -o $(EXECUTABLE) -I$(HEADER_DIR)

run: $(EXECUTABLE)
	./$(EXECUTABLE)
	@echo "Execução encerrada.\n"
	make dump

dump:
	@echo "Dump do log.\n"
	@tput setaf 1 			# Alterar cor do output para vermelho
	@cat $(LOG)
	@echo "\n"
	@tput setaf default # Retornar para cor padrão.
