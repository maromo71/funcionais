package com.example;

@FunctionalInterface
interface OperacaoMatematica {
    int calcular(int a, int b);
}
public class Exemplo05 {
    public static void main(String[] args) {
       OperacaoMatematica soma = (a, b)-> {
            return a + b;
       };
       System.out.println("Soma de 5 e 3: " + soma.calcular(5, 3));
    }
}
