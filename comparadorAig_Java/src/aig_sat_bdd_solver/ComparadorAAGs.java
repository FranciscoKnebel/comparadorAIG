package aig_sat_bdd_solver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * @author luciano/rodrigo
 */

public class ComparadorAAGs {

    //AIG1
    int entradas1 = 0;
    int saidas1 = 0;
    int ands1 = 0;
    
    ArrayList<Integer> arrayEntradas1;
    ArrayList<Integer> arraySaidas1;
    ArrayList<AigNode> arrayAigNodesBdd1;
    ArrayList<AigNode> arrayAigNodesSat1;
    ArrayList<String> arrayEquacoesBdd1;
    ArrayList<String> arrayEquacoesSat1;
    
    //AIG2
    int entradas2 = 0;
    int saidas2 = 0;
    int ands2 = 0;
    
    ArrayList<Integer> arrayEntradas2; 
    ArrayList<Integer> arraySaidas2;
    ArrayList<AigNode> arrayAigNodesBdd2;
    ArrayList<AigNode> arrayAigNodesSat2;
    ArrayList<String> arrayEquacoesBdd2;
    ArrayList<String> arrayEquacoesSat2;
  
    
//------------------------------ SAT -----------------------------------------------------------------
    
    //FUNÇÃO PRINCIPAL
    //chama o parser para ler os arquivos
    //chama a criação da expressãoSAT
    //chama a geração das saidasSAT
    //chama o teste de equivalênciaSAT
    boolean compararSat(String arquivo1, String arquivo2) {
        
        //chama o parser para ler os arquivos
        readFile_parseAIG(arquivo1, 1);
        readFile_parseAIG(arquivo2, 2);
        
        for(int i =0; i< arrayAigNodesSat1.size(); i++){
              //chama a criação da expressãoSAT
            createSatExpr(arrayAigNodesSat1.get(i), 1);
        }
        
        //chama a geração das saidasSAT
        geraSaidasSat(1);
        
        for(int i =0; i< arrayAigNodesSat1.size(); i++){
            System.out.println(arrayAigNodesSat1.get(i).toString());
        }
        
        for(int i =0; i< arrayAigNodesSat2.size(); i++){
            //chama a criação da expressãoSAT
            createSatExpr(arrayAigNodesSat2.get(i), 2);
        }
        
        //chama a geração das saidasSAT
        geraSaidasSat(2);
        
        for(int i =0; i< arrayAigNodesSat2.size(); i++){
            System.out.println(arrayAigNodesSat2.get(i).toString());
        }
  
        int numeroSaidas = arrayEquacoesSat1.size();
        
        // os aags tem o mesmo numero de saidas, então usa o de um só
        for(int i=0; i<numeroSaidas; i++){
            //chama o teste de equivalênciaSAT
            if(!isSatExpressionEquivalent(arrayEquacoesSat1.get(i), arrayEquacoesSat2.get(i))){
                return false;
            }
        }
        
        for(int i=0; i< arrayAigNodesSat1.size(); i++){
            System.out.println(arrayAigNodesSat1.get(i).equacao);
        }
        
        return true;
    }
    
    //recebe o nodo e cria sua expressão
    private void createSatExpr(AigNode aig, int numberArray) {
        
        String expressao1 = "";
        String expressao2 = "";
        String expressaoFinal = "";
        
        AigNode aigEntrada1 = null;
        AigNode aigEntrada2 = null;
        
        //procura as entradas do nodo
        if(numberArray == 1){
            aigEntrada1 = searchAigNode(arrayAigNodesSat1, aig.input1);
            aigEntrada2 = searchAigNode(arrayAigNodesSat1, aig.input2);
        } else{
            aigEntrada1 = searchAigNode(arrayAigNodesSat2, aig.input1);
            aigEntrada2 = searchAigNode(arrayAigNodesSat2, aig.input2);
        }
        
        //testa se variavel é entrada do aag ou não
        if(ehEntrada(aig.input1, numberArray)){
            expressao1 = "n" + aig.input1;
        } else if (ehEntrada((aig.input1 - 1), numberArray)){
            expressao1 = "!n" + (aig.input1 - 1);
        } else{
            expressao1 = aigEntrada1.equacao;
            if(aig.input1 %2 != 0){ // é impar, nega a entrada
                expressao1 = "!("+expressao1+")";
            }
        }
        
        //testa se variavel é entrada do aag ou não
        if(ehEntrada(aig.input2, numberArray)){
            expressao2 = "n" + aig.input2;
        } else if (ehEntrada((aig.input2 - 1), numberArray)){
            expressao2 = "!n" + (aig.input2 - 1);
        } else{
            expressao2 = aigEntrada2.equacao;
            if(aig.input2 %2 != 0){ // é impar, nega a entrada
            expressao2 = "!("+expressao2+")";
        }
        }
        
        //faz o and das duas entradas e seta o nodo
        expressaoFinal = expressao1 + " & " + expressao2;                
        
        aig.equacao = expressaoFinal;  
    }
       
    //gera as saidas SAT
    private void geraSaidasSat(int numberArray) {
      
        if(numberArray == 1){
            arrayEquacoesSat1 = new ArrayList<String>();
            for(int i =0; i<arraySaidas1.size(); i++){
                AigNode aig = searchAigNode(arrayAigNodesSat1, arraySaidas1.get(i));
                if(arraySaidas1.get(i) %2 == 0){ // é saida par
                    arrayEquacoesSat1.add(aig.equacao);
                } else{ // é saida impar
                    arrayEquacoesSat1.add("!("+aig.equacao+")");
                }
            }
        } else{
            arrayEquacoesSat2 = new ArrayList<String>();
            for(int i =0; i<arraySaidas2.size(); i++){
                AigNode aig = searchAigNode(arrayAigNodesSat2, arraySaidas2.get(i));
                if(arraySaidas2.get(i) %2 == 0){ // é saida par
                    arrayEquacoesSat2.add(aig.equacao);
                } else{ // é saida impar
                    arrayEquacoesSat2.add("!(" + aig.equacao + ")");
                }
            }
        }
    }
    
      //faz o xor entre duas expressões
    private String exp1XorExp2(String expressao1, String expressao2) {
        return "("
            + "!(" + expressao1 + ")" + "&" + "(" + expressao2 + ")"
            + ")|("
            + "(" + expressao1 + ")" + "&" + "!(" + expressao2 + "))";    
    }
        
    //testa se duas expressoes sao SAT equivalentes
    //salva as expressõesSAT em um arquivo
    //executa o limboole para o teste de sat
    private boolean isSatExpressionEquivalent(String expressao1, String expressao2) {
        
        try {
             //salva as expressõesSAT em um arquivo
            File fout = new File("satExpressions.txt");
            FileOutputStream fos = new FileOutputStream(fout);
 
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
 
            // xor entre expressao1, expressao2 e saida S
            bw.write(exp1XorExp2(expressao1, expressao2));
            bw.close();
           
            //executa o limboole para o teste de sat
            Process execute = new ProcessBuilder("./ext/limboole", "-s", "satExpressions.txt").start();

            BufferedReader stdInput = new BufferedReader(new 
            InputStreamReader(execute.getInputStream()));

            BufferedReader stdError = new BufferedReader(new 
            InputStreamReader(execute.getErrorStream()));

            String s = null;
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
                if(s.contains("UNSATISFIABLE")){
                    return true;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

//------------------------------ SAT -----------------------------------------------------------------
    
    
    
    
//------------------------------ BDD -----------------------------------------------------------------    
    
    //FUNÇÃO PRINCIPAL
    // chama o parser para ler os arquivos
    // chama criação das expressoesBDD
    // chama a geração de saidasBDD
    // chama o teste de equivalênciaBDD
    boolean compararBdd(String arquivo1, String arquivo2) {
        
        // chama o parser para ler os arquivos
        
        readFile_parseAIG(arquivo1, 1);
        readFile_parseAIG(arquivo2, 2);
        
        for(int i =0; i< arrayAigNodesBdd1.size(); i++){
            // chama criação das expressoesBDD
            createBddExpr(arrayAigNodesBdd1.get(i), 1);
        }
        
        // chama a geração de saidasBDD
        geraSaidasBdd(1);
        
        for(int i =0; i< arrayEquacoesBdd1.size(); i++){
            System.out.println(arrayEquacoesBdd1.get(i));       
        }
        
        for(int i =0; i< arrayAigNodesBdd2.size(); i++){
            // chama criação das expressoesBDD
            createBddExpr(arrayAigNodesBdd2.get(i), 1);
        }
        
        // chama a geração de saidasBDD
        geraSaidasBdd(2);
        
        for(int i=0; i< arrayEquacoesBdd2.size(); i++){
            System.out.println(arrayEquacoesBdd2.get(i));
        }
        
        int numeroSaidas = arrayEquacoesBdd1.size();
        
        // os aags tem o mesmo numero de saidas, então usa o de um só
        for(int i=0; i<numeroSaidas; i++){
              // chama o teste de equivalênciaBDD
            if(!isBddExpressionEquivalent(arrayEquacoesBdd1.get(i), arrayEquacoesBdd2.get(i))){
                return false;
            }
        }
        
        for(int i=0; i< arrayAigNodesBdd1.size(); i++){
            System.out.println(arrayAigNodesBdd1.get(i).equacao);
        }
        
        return true;
    }
    
    //cria a expressão do BDD, expressão lógica simples com ands e nots
    private void createBddExpr(AigNode aig, int numberArray) {
        
        String expressao1 = "";
        String expressao2 = "";
        String expressaoFinal = "";
        
        AigNode aigEntrada1 = null;
        AigNode aigEntrada2 = null;
        
        //procura as entradas do nodo
        if(numberArray == 1){
            aigEntrada1 = searchAigNode(arrayAigNodesBdd1, aig.input1);
            aigEntrada2 = searchAigNode(arrayAigNodesBdd1, aig.input2);
        } else{
            aigEntrada1 = searchAigNode(arrayAigNodesBdd2, aig.input1);
            aigEntrada2 = searchAigNode(arrayAigNodesBdd2, aig.input2);
        }
        
        //testa se variavel é entrada do aag ou não
        if(ehEntrada(aig.input1, numberArray)){
            expressao1 = "n" + aig.input1;
        } else if (ehEntrada((aig.input1 - 1), numberArray)){
            expressao1 = "!n" + (aig.input1 - 1);
        } else{
            expressao1 = aigEntrada1.equacao;
            if(aig.input1 %2 != 0){ // é impar, nega a entrada
                expressao1 = "!(" + expressao1 + ")";
            }
        }
        
        //testa se variavel é entrada do aag ou não
        if(ehEntrada(aig.input2, numberArray)){
            expressao2 = "n" + aig.input2;
        } else if (ehEntrada((aig.input2 - 1), numberArray)){
            expressao2 = "!n" + (aig.input2 - 1);
        } else{
            expressao2 = aigEntrada2.equacao;
            if(aig.input2 %2 != 0){ // é impar, nega a entrada
            expressao2 = "!("+expressao2+")";
        }
        }
        
        //faz o and das duas expressões e seta o nodo
        expressaoFinal = expressao1 + "*" + expressao2;                
        
        aig.equacao = expressaoFinal;
    }
    
    //gera as saidas dos BDDs
    private void geraSaidasBdd(int numberArray){
        
        if(numberArray == 1){
            arrayEquacoesBdd1 = new ArrayList<String>();
            for(int i =0; i<arraySaidas1.size(); i++){
                AigNode aig = searchAigNode(arrayAigNodesBdd1, arraySaidas1.get(i));
                if(arraySaidas1.get(i) %2 == 0){ // é saida par
                    arrayEquacoesBdd1.add(aig.equacao);
                } else{ // é saida impar
                    arrayEquacoesBdd1.add("!(" + aig.equacao + ")");
                }
            }
        } else{
            arrayEquacoesBdd2 = new ArrayList<String>();
            for(int i =0; i<arraySaidas2.size(); i++){
                AigNode aig = searchAigNode(arrayAigNodesBdd2, arraySaidas2.get(i));
                if(arraySaidas2.get(i) %2 == 0){ // é saida par
                    arrayEquacoesBdd2.add(aig.equacao);
                } else{ // é saida impar
                    arrayEquacoesBdd2.add("!(" + aig.equacao + ")");
                }
            }
        }
    }  
    
    //testa se duas expressoes sao equivalentes via BDD
    //salva as expressões a serem testadas em um arquivo
    //passa este arquivo como parametro para o resolvedor de bdd do professor
    private boolean isBddExpressionEquivalent(String expressao1, String expressao2) {
        try {
            
            //salva as expressões a serem testadas em um arquivo
            File fout = new File("bddExpressions.txt");
            FileOutputStream fos = new FileOutputStream(fout);
 
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
 
            bw.write(expressao1);
            bw.newLine();
            bw.write(expressao2);
            bw.close();
 
            //passa este arquivo como parametro para o resolvedor de bdd do professor
            Process execute = new ProcessBuilder("./ext/bdd-cmp-file", "bddExpressions.txt").start();

            BufferedReader stdInput = new BufferedReader(new 
            InputStreamReader(execute.getInputStream()));

            BufferedReader stdError = new BufferedReader(new 
            InputStreamReader(execute.getErrorStream()));

            String s = null;
            while ((s = stdInput.readLine()) != null) {
                System.out.println("Retornado: " + s);
                if(s.equals("TRUE")){
                    return true;
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

//------------------------------ BDD -----------------------------------------------------------------
    
    
      
    
//--------------------FUNÇÕES AUXILIARES---------------------------------------------------------------
    
    //procura por um nodo no array que tenha a saida igual a outputToSearch e retorna ele
    private AigNode searchAigNode(ArrayList<AigNode> arrayAigNodes, int outputToSearch){
        
        if(outputToSearch %2 == 0){ // par
            for(int i=0; i<arrayAigNodes.size();i++){
                if(arrayAigNodes.get(i).output == outputToSearch){
                    return arrayAigNodes.get(i);
                }
            }
        } else{ // impar
            for(int i=0; i<arrayAigNodes.size();i++){
                if(arrayAigNodes.get(i).output == outputToSearch - 1){
                    return arrayAigNodes.get(i);
                }
            }
        }
        return null;
    }
    
    //teste se uma determinada variavel é uma entrada do aag
    private boolean ehEntrada(int numeroEntrada, int numberArray) {
       
        if(numberArray == 1){
            for(int i=0; i<arrayEntradas1.size(); i++){
                if(arrayEntradas1.get(i) == numeroEntrada){
                    return true;
                }
            }
            return false;
        } else{
            for(int i=0; i<arrayEntradas2.size(); i++){
                if(arrayEntradas2.get(i) == numeroEntrada){
                    return true;
                }
            }
            return false;
        }
    }
    
    // le o arquivo aag e parseia pra um array de AIG 
    public void readFile_parseAIG(String arquivo, int numero_arquivo){
        
        //criação dos arrays
        if(numero_arquivo == 1){
            arrayEntradas1 = new ArrayList<Integer>();
            arraySaidas1 = new ArrayList<Integer>();
            arrayAigNodesBdd1 = new ArrayList<AigNode>();
            arrayAigNodesSat1 = new ArrayList<AigNode>();
        } else {
            arrayEntradas2 = new ArrayList<Integer>();
            arraySaidas2 = new ArrayList<Integer>();
            arrayAigNodesBdd2 = new ArrayList<AigNode>();
            arrayAigNodesSat2 = new ArrayList<AigNode>();
        }
        
        BufferedReader br = null;
        
        try {
            br = new BufferedReader(new FileReader(arquivo));
            String line;
            int lineNumber = 0;
            
            while ((line = br.readLine()) != null) {
                lineNumber++;
                if(lineNumber == 1){//primeira linha do aag
                    String[] splitLine = line.split(" ");
                    
                    //seta quantidade de entradas, saidas e ands
                    if(numero_arquivo == 1){
                        entradas1 = Integer.valueOf(splitLine[2]);
                        saidas1 = Integer.valueOf(splitLine[4]);
                        ands1 = Integer.valueOf(splitLine[5]);
                    } else{
                        entradas2 = Integer.valueOf(splitLine[2]);
                        saidas2 = Integer.valueOf(splitLine[4]);
                        ands2 = Integer.valueOf(splitLine[5]);
                    }
                } else{
                    if(numero_arquivo == 1){
                        if(lineNumber <= entradas1 + 1){
                            //adiciona as entradas no seu respectivo array
                            arrayEntradas1.add(Integer.valueOf(line));
                            
                        } else if(lineNumber <= entradas1 + saidas1 + 1){
                            //adiciona as saidas no seu respectivo array
                            arraySaidas1.add(Integer.valueOf(line));    
                            
                        } else if(lineNumber <= entradas1 + saidas1 + ands1 + 1){
                            String[] andLineSplit = line.split(" ");
                            
                            //cria os nodos do aig, setando suas 2 entradas e sua saida 
                            //e adiciona o nodo no seu respectivo array
                            AigNode aig = new AigNode(Integer.valueOf(andLineSplit[1]), Integer.valueOf(andLineSplit[2]), Integer.valueOf(andLineSplit[0]));
                            arrayAigNodesBdd1.add(aig);
                            arrayAigNodesSat1.add(aig);
                        }
                    } else{
                        if(lineNumber <= entradas2 + 1){
                            //adiciona as entradas no seu respectivo array
                            arrayEntradas2.add(Integer.valueOf(line));
                            
                        } else if(lineNumber <= entradas2 + saidas2 + 1){                  
                            //adiciona as saidas no seu respectivo array
                            arraySaidas2.add(Integer.valueOf(line));    
                            
                        } else if(lineNumber <= entradas2 + saidas2 + ands2 + 1){
                            String[] andLineSplit = line.split(" ");
                            
                            //cria os nodos do aig, setando suas 2 entradas e sua saida 
                            //e adiciona o nodo no seu respectivo array
                            AigNode aig = new AigNode(Integer.valueOf(andLineSplit[1]), Integer.valueOf(andLineSplit[2]), Integer.valueOf(andLineSplit[0]));
                            arrayAigNodesBdd2.add(aig);
                            arrayAigNodesSat2.add(aig);
                        }
                    }
                }
            }
                //printa entradas e saidas
                System.out.println("Entradas: ");
                if(numero_arquivo == 1){
                    for(int i=0; i<arrayEntradas1.size();i++){
                        System.out.println(arrayEntradas1.get(i));
                    }
                    System.out.println("Saidas: ");
                    for(int i=0; i<arraySaidas1.size();i++){
                        System.out.println(arraySaidas1.get(i));
                    }
                } else{
                    for(int i=0; i<arrayEntradas2.size();i++){
                        System.out.println(arrayEntradas2.get(i));
                    }
                    System.out.println("Saidas: ");
                    for(int i=0; i<arraySaidas2.size();i++){
                        System.out.println(arraySaidas2.get(i));
                    }
                }
            
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
//--------------------FUNÇÕES AUXILIARES--------------------------------------------------------------   
}

