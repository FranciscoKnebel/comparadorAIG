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
import java.util.ArrayList;

/**
 *
 * @author luciano
 */
public class ComparadorAAGs {

    ArrayList<Integer> arrayEntradas1;
    ArrayList<Integer> arraySaidas1;
    ArrayList<AIG> arrayAigs1;
    
    ArrayList<Integer> arrayEntradas2; 
    ArrayList<Integer> arraySaidas2;
    ArrayList<AIG> arrayAigs2;
        
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
            tseytin1 = aigEntrada1.tseytin;
        }
        if(aigEntrada2 != null){
            tseytin2 = aigEntrada2.tseytin;
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
        
        aig.tseytin = tseytinFinal;
        
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
        int entradas = 0;
        int saidas = 0;
        int ands = 0;
        
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
                    entradas = Integer.valueOf(splitLine[2]);
                    saidas = Integer.valueOf(splitLine[4]);
                    ands = Integer.valueOf(splitLine[5]);
                } else{
                    if(lineNumber <= entradas + 1){
                        if(numero_arquivo == 1){
                            arrayEntradas1.add(Integer.valueOf(line));
                        } else{
                            arrayEntradas2.add(Integer.valueOf(line));
                        }
                    } else if(lineNumber <= entradas + 1 + saidas){
                        if(numero_arquivo == 1){
                            arraySaidas1.add(Integer.valueOf(line));
                        } else{
                            arraySaidas2.add(Integer.valueOf(line)); 
                        }
                    } else if(lineNumber <= entradas + 1 + saidas + ands){
                        String[] andLineSplit = line.split(" ");
                        AIG aig = new AIG(Integer.valueOf(andLineSplit[1]), Integer.valueOf(andLineSplit[2]), Integer.valueOf(andLineSplit[0]));
                        if(numero_arquivo == 1){
                            arrayAigs1.add(aig);
                        } else{
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
                System.out.println("Aigs: ");
                for(int i=0; i<arrayAigs1.size();i++){
                    System.out.println(arrayAigs1.get(i).toString());
                }
            } else{
                for(int i=0; i<arrayEntradas2.size();i++){
                    System.out.println(arrayEntradas2.get(i));
                }
                System.out.println("Saidas: ");
                for(int i=0; i<arraySaidas2.size();i++){
                    System.out.println(arraySaidas2.get(i));
                }
                System.out.println("Aigs: ");
                for(int i=0; i<arrayAigs2.size();i++){
                    System.out.println(arrayAigs2.get(i).toString());
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

    boolean compararBdd(String arquivo1, String arquivo2) {
        readFile_parseAIG(arquivo1, 1);
        readFile_parseAIG(arquivo2, 2);
        return false;
    }
}
