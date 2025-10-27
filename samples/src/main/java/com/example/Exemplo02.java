package com.example;

import java.util.stream.IntStream;

//Exemplo com programação funcional. iteração interna
public class Exemplo02 {
    public static void main(String[] args) {
        int valores[] = {12, 13, 14, 67, 12};
        int soma = IntStream.of(valores).sum();
        System.out.println("Soma " + soma);
        //iteração interna apresentando os valores no console
        IntStream.of(valores).forEach(v -> System.out.printf(v + " "));
        //Apresentando o menor valor
        int min = IntStream.of(valores).min().getAsInt();
        System.out.println("\nMenor valor " + min);
        //Apresentando o maior valor
        int max = IntStream.of(valores).max().getAsInt();
        System.out.println("Maior valor " + max);
        double media = IntStream.of(valores).average().getAsDouble();
        System.out.println("Média " + media);
        //soma dos valores com o reduce
        soma = IntStream.of(valores).reduce(0, (a, b) -> a + b);
        System.out.println("Soma " + soma);
        //soma dos quadrados dos valores usando reduce
        soma = IntStream.of(valores).reduce(0,(a, b) -> a + b * b);
        System.out.println("Soma dos quadrados " + soma);
        //produto dos valores
        int produto = IntStream.of(valores).reduce(1, (a, b) -> a * b);
        System.out.println("Produto " + produto);
        //valores pares ordenados
        System.out.println("Pares ordenados");
        IntStream.of(valores)
                .filter(v -> v % 2 == 0)
                .sorted()
                .forEach(v -> System.out.printf(v + " "));
        System.out.println();
        //Valores impares multiplicados por dez e ordenados
        System.out.println("Impares multiplicados por dez");
        IntStream.of(valores)
                .filter(v -> v % 2 != 0)
                .map(v -> v * 10)
                .sorted()
                .forEach(v -> System.out.printf(v + " "));
        System.out.println();

    }
}
