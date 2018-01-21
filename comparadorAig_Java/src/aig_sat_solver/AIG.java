/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aig_sat_solver;

/**
 *
 * @author luciano
 */
public class AIG {
    int input1;
    int input2;
    int output;
    String equacao;
    
    public AIG(int input1, int input2, int output){
        this.input1 = input1;
        this.input2 = input2;
        this.output = output;
    }
    
    @Override
    public String toString(){
        return "Input1: "+input1+" Input2: "+input2+" Output: "+output + " Equacao: " + equacao;
    }
}
