package com.example;

//Programação tradicional - iteração externa - não funcional
public class Exemplo01 {
    public static void main(String[] args) {
       int valores[] = {12, 13, 14, 67, 12};
       int soma = 0;
       for(int i=0; i<valores.length; i++){
            soma+= valores[i];
       }
       System.out.println("Soma " + soma);
    }
}