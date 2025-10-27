package com.example;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Exemplo03 {
    public static void main(String[] args) {
        Integer[] valores = {12, 13, 14, 67, 12};
        //Exibir os valores originais
        System.out.println("Valores originais");
        System.out.println("Valores originais: " + Arrays.asList(valores));
        //Classifica os valores em ordem crescente com pipelines
        System.out.printf("Valores ordenados %s%n",
        Arrays.stream(valores)
                .sorted()
                .collect(Collectors.toList()));
        //O mesmo exemplo iterando
        System.out.println("Valores ordenados");
        Arrays.stream(valores)
                .sorted()
                .forEach(v -> System.out.printf(v + " "));
        System.out.println();
        //Valores maiores que 20
        System.out.println("Valores maiores que 20");
        Arrays.stream(valores)
                .filter(v -> v > 20)
                .forEach(v -> System.out.println(v));
        System.out.println();
        //Valores impares ordenados em ordem do maior para o menor
        System.out.println("Valores impares ordenados");
        Arrays.stream(valores)
                .filter(v -> v % 2 != 0)
                .sorted((a, b) -> b - a)    
                .forEach(v -> System.out.printf(v + " "));
    }
}
