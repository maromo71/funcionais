package com.example;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Exemplo04 {
    public static void main(String[] args) {
        List<String> nomes = Arrays.asList("Ana", "Marcos", "Carlos", "Beatriz", "Amanda");
        //Construindo o Pipeline
        List<String> nomesComAemMaisculos = nomes.stream()
            .filter(nome -> nome.startsWith("A"))
            .map(nome -> nome.toUpperCase())
            .sorted()
            .collect(Collectors.toList());
        System.out.println(nomesComAemMaisculos);

    }
}
