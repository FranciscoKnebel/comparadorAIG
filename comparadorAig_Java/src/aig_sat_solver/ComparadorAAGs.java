/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aig_sat_solver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 *
 * @author luciano
 */
public class ComparadorAAGs {

    int entradas1 = 0;
    int saidas1 = 0;
    int ands1 = 0;
    
    ArrayList<Integer> arrayEntradas1;
    ArrayList<Integer> arraySaidas1;
    ArrayList<AIG> arrayAigs1;
    ArrayList<String> arrayEquacoesBdd1;
    
        
    int entradas2 = 0;
    int saidas2 = 0;
    int ands2 = 0;
    
    ArrayList<Integer> arrayEntradas2; 
    ArrayList<Integer> arraySaidas2;
    ArrayList<AIG> arrayAigs2;
    ArrayList<String> arrayEquacoesBdd2;
    
    
        
    boolean compararSat(String arquivo1, String arquivo2) {
        //System.out.println(System.getProperty("user.dir"));
        //System.out.println(new File(arquivo1).getAbsolutePath());
        //System.out.println(new File(arquivo2).getAbsolutePath());
        readFile_parseAIG(arquivo1, 1);
        readFile_parseAIG(arquivo2, 2);
        for(int i =0; i< arrayAigs1.size(); i++){
            createTseytinExpr(arrayAigs1.get(i), 1);
        }
        for(int i =0; i< arrayAigs1.size(); i++){
            System.out.println(arrayAigs1.get(i).toString());
        }
  
        return false;
    }
    
    private void createTseytinExpr(AIG aig, int numberArray) {
        String tseytin1 = "";
        String tseytin2 = "";
        String tseytinFinal = "";
        AIG aigEntrada1 = null;
        AIG aigEntrada2 = null;
        boolean ehNegado1 = false;
        boolean ehNegado2 = false;
        if(numberArray == 1){
            aigEntrada1 = searchAig(arrayAigs1, aig.input1);
            aigEntrada2 = searchAig(arrayAigs1, aig.input1);
        } else{
            aigEntrada1 = searchAig(arrayAigs2, aig.input1);
            aigEntrada2 = searchAig(arrayAigs2, aig.input1);
        }
        
        if(aigEntrada1 != null){
            tseytin1 = aigEntrada1.equacao;
        }
        if(aigEntrada2 != null){
            tseytin2 = aigEntrada2.equacao;
        }
        
        if(aig.input1 %2 != 0){ // é impar
            tseytin1 = tseytin1 + "*(_" + (aig.input1 - 1) + "+" + "x" + (aig.input1 - 1) + ")" + "*(!_" + (aig.input1 - 1) + "+" + "!x" + (aig.input1 - 1) + ")";
            ehNegado1 = true;
        }
        if(aig.input2 %2 != 0){ // é impar
            tseytin2 = tseytin2 + "*(_" + (aig.input2 - 1) + "+" + "x" + (aig.input2 - 1) + ")" + "*(!_" + (aig.input2 - 1) + "+" + "!x" + (aig.input2 - 1) + ")";
            ehNegado2 = true;
        }
        //!_8+!_6+_14)*(_8+!_14)*(_6+!_14)
        // cria o and...
        if(!ehNegado1 && !ehNegado2){
            tseytinFinal = "(!_" + aig.input1 + "+" +  "!_" + aig.input2 + "+" + "_" + aig.output + ")"
                       + "*(_" + aig.input1 + "+" + "!_" + aig.output + ")*(_" + aig.input2 + "+" + "!_" + aig.output + ")";                       
        } else if(ehNegado1 && !ehNegado2){
            tseytinFinal = "(!x" + (aig.input1 - 1) + "+" +  "!_" + aig.input2 + "+" + "_" + aig.output + ")"
                           + "*(x" + (aig.input1 - 1) + "+" + "!_" + aig.output + ")*(_" + aig.input2 + "+" + "!_" + aig.output + ")";                           
        } else if(!ehNegado1 && ehNegado2){
            tseytinFinal = "(!_" + aig.input1 + "+" +  "!x" + (aig.input2 - 1) + "+" + "_" + aig.output + ")"
                           + "*(_" + aig.input1 + "+" + "!_" + aig.output + ")*(x" + (aig.input2 - 1) + "+" + "!_" + aig.output + ")";                           
        } else if(ehNegado1 && ehNegado2){
            tseytinFinal = "(!x" + (aig.input1 - 1) + "+" +  "!x" + (aig.input2 - 1) + "+" + "_" + aig.output + ")"
                           + "*(x" + (aig.input1 - 1) + "+" + "!_" + aig.output + ")*(x" + (aig.input2 - 1) + "+" + "!_" + aig.output + ")";                          
        }
        
        if(!tseytin1.equals("")){
            tseytinFinal = tseytinFinal + "*" + tseytin1;
        }
        if(!tseytin2.equals("")){
            tseytinFinal = tseytinFinal + "*" + tseytin2;
        }
        
        aig.equacao = tseytinFinal;
        
    }
    
    private AIG searchAig(ArrayList<AIG> arrayAigs, int outputToSearch){
        if(outputToSearch %2 == 0){ // par
            for(int i=0; i<arrayAigs.size();i++){
                if(arrayAigs.get(i).output == outputToSearch){
                    return arrayAigs.get(i);
                }
            }
        } else{ // impar
            for(int i=0; i<arrayAigs.size();i++){
                if(arrayAigs.get(i).output == outputToSearch - 1){
                    return arrayAigs.get(i);
                }
            }
        }
        return null;
    }
    
     
    public void readFile_parseAIG(String arquivo, int numero_arquivo){
        if(numero_arquivo == 1){
            arrayEntradas1 = new ArrayList<Integer>();
            arraySaidas1 = new ArrayList<Integer>();
            arrayAigs1 = new ArrayList<AIG>();
        } else {
            arrayEntradas2 = new ArrayList<Integer>();
            arraySaidas2 = new ArrayList<Integer>();
            arrayAigs2 = new ArrayList<AIG>();
        }
        
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(arquivo));
            String line;
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                //System.out.println(line);
                lineNumber++;
                if(lineNumber == 1){
                    String[] splitLine = line.split(" ");
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
                            arrayEntradas1.add(Integer.valueOf(line));
                       
                        } else if(lineNumber <= entradas1 + 1 + saidas1){                  
                            arraySaidas1.add(Integer.valueOf(line));               
                        } else if(lineNumber <= entradas1 + 1 + saidas1 + ands1){
                            String[] andLineSplit = line.split(" ");
                            AIG aig = new AIG(Integer.valueOf(andLineSplit[1]), Integer.valueOf(andLineSplit[2]), Integer.valueOf(andLineSplit[0]));
                            arrayAigs1.add(aig);
                        }
                    } else{
                        if(lineNumber <= entradas2 + 1){
                            arrayEntradas2.add(Integer.valueOf(line));
                        } else if(lineNumber <= entradas2 + 1 + saidas2){                  
                            arraySaidas2.add(Integer.valueOf(line));               
                        } else if(lineNumber <= entradas2 + 1 + saidas2 + ands2){
                            String[] andLineSplit = line.split(" ");
                            AIG aig = new AIG(Integer.valueOf(andLineSplit[1]), Integer.valueOf(andLineSplit[2]), Integer.valueOf(andLineSplit[0]));
                            arrayAigs2.add(aig);
                        }
                    }
                }
            }
                System.out.println("Entradas: ");
                if(numero_arquivo == 1){
                    for(int i=0; i<arrayEntradas1.size();i++){
                        System.out.println(arrayEntradas1.get(i));
                    }
                    System.out.println("Saidas: ");
                    for(int i=0; i<arraySaidas1.size();i++){
                        System.out.println(arraySaidas1.get(i));
                    }
                    /*System.out.println("Aigs: ");
                    for(int i=0; i<arrayAigs1.size();i++){
                        System.out.println(arrayAigs1.get(i).toString());
                    }*/
                } else{
                    for(int i=0; i<arrayEntradas2.size();i++){
                        System.out.println(arrayEntradas2.get(i));
                    }
                    System.out.println("Saidas: ");
                    for(int i=0; i<arraySaidas2.size();i++){
                        System.out.println(arraySaidas2.get(i));
                    }
                    /*System.out.println("Aigs: ");
                    for(int i=0; i<arrayAigs2.size();i++){
                        System.out.println(arrayAigs2.get(i).toString());
                    }*/
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

    boolean compararBdd(String arquivo1, String arquivo2) {
        readFile_parseAIG(arquivo1, 1);
        readFile_parseAIG(arquivo2, 2);
        
        // primeiro aag
        for(int i =0; i< arrayAigs1.size(); i++){
            createBddExpr(arrayAigs1.get(i), 1);
        }
        geraSaidasBdd(1);
        for(int i =0; i< arrayEquacoesBdd1.size(); i++){
            System.out.println(arrayEquacoesBdd1.get(i));       
        }
        
        // segundo aag
        for(int i =0; i< arrayAigs2.size(); i++){
            createBddExpr(arrayAigs2.get(i), 1);
        }
        geraSaidasBdd(2);
        for(int i=0; i< arrayEquacoesBdd2.size(); i++){
            System.out.println(arrayEquacoesBdd2.get(i));
        }
        
        int numeroSaidas = arrayEquacoesBdd1.size();
        // os aags tem o mesmo numero de saidas, então usa o de um só
        for(int i=0; i<numeroSaidas; i++){
            if(!isExpressionEquivalent(arrayEquacoesBdd1.get(i), arrayEquacoesBdd2.get(i))){
                return false;
            }
        }
        
        for(int i=0; i< arrayAigs1.size(); i++){
            System.out.println(arrayAigs1.get(i).equacao);
        }
        
        return true;
    }

    private void createBddExpr(AIG aig, int numberArray) {
        String expressao1 = "";
        String expressao2 = "";
        String expressaoFinal = "";
        AIG aigEntrada1 = null;
        AIG aigEntrada2 = null;
        
        if(numberArray == 1){
            aigEntrada1 = searchAig(arrayAigs1, aig.input1);
            aigEntrada2 = searchAig(arrayAigs1, aig.input2);
        } else{
            aigEntrada1 = searchAig(arrayAigs2, aig.input1);
            aigEntrada2 = searchAig(arrayAigs2, aig.input2);
        }
        
        
        if(ehEntrada(aig.input1, numberArray)){
            expressao1 = "v" + aig.input1;
        } else if (ehEntrada((aig.input1 - 1), numberArray)){
            expressao1 = "!v" + (aig.input1 - 1);
        } else{
            expressao1 = aigEntrada1.equacao;
            if(aig.input1 %2 != 0){ // é impar, nega a entrada
                expressao1 = "!(" + expressao1 + ")";
            }
        }
        if(ehEntrada(aig.input2, numberArray)){
            expressao2 = "v" + aig.input2;
        } else if (ehEntrada((aig.input2 - 1), numberArray)){
            expressao2 = "!v" + (aig.input2 - 1);
        } else{
            expressao2 = aigEntrada2.equacao;
            if(aig.input2 %2 != 0){ // é impar, nega a entrada
            expressao2 = "!("+expressao2+")";
        }
        }
        
        expressaoFinal = expressao1 + "*" + expressao2;                
        
        aig.equacao = expressaoFinal;
    }

    private void geraSaidasBdd(int numberArray){
        
        if(numberArray == 1){
            arrayEquacoesBdd1 = new ArrayList<String>();
            for(int i =0; i<arraySaidas1.size(); i++){
                AIG aig = searchAig(arrayAigs1, arraySaidas1.get(i));
                if(arraySaidas1.get(i) %2 == 0){ // é saida par
                    arrayEquacoesBdd1.add(aig.equacao);
                } else{ // é saida impar
                    arrayEquacoesBdd1.add("!(" + aig.equacao + ")");
                }
            }
        } else{
            arrayEquacoesBdd2 = new ArrayList<String>();
            for(int i =0; i<arraySaidas2.size(); i++){
                AIG aig = searchAig(arrayAigs2, arraySaidas2.get(i));
                if(arraySaidas2.get(i) %2 == 0){ // é saida par
                    arrayEquacoesBdd2.add(aig.equacao);
                } else{ // é saida impar
                    arrayEquacoesBdd2.add("!(" + aig.equacao + ")");
                }
            }
        }
    }
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

    private boolean isExpressionEquivalent(String expressao1, String expressao2) {
        try {
            //System.out.println(System.getProperty("user.dir"));
            //System.out.println(new File(arquivo2).getAbsolutePath());
            // create a process and execute notepad.exe and currect environment
            Process execute = new ProcessBuilder("./testBDD", expressao1, expressao2).start();

            BufferedReader stdInput = new BufferedReader(new 
            InputStreamReader(execute.getInputStream()));

            BufferedReader stdError = new BufferedReader(new 
            InputStreamReader(execute.getErrorStream()));

            String s = null;
            while ((s = stdInput.readLine()) != null) {
                System.out.println("Retornado: " + s);
                if(s.equals("As duas expressões são iguais.")){
                    return true;
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
    
}
