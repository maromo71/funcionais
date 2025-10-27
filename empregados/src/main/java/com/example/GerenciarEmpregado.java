package com.example;

import java.util.List;

public class GerenciarEmpregado {
    public static void main(String[] args) {
        List<Empregado> empregados = List.of(
            new Empregado(1, "Ana", 1200.00, "TI"),
            new Empregado(2, "Carlos", 1500.0, "Comercial"),
            new Empregado(3, "Beatriz", 2000.0, "Financeiro"),
            new Empregado(4, "Marcos", 1800.00, "TI"),
            new Empregado(5, "Tereza", 3000.00, "TI")
        );
        //Apresentar a lista de funcionarios
        System.out.println("Dados de todos os empregados");
        empregados.forEach(System.out::println);

        //Apresentar uma nova lista de empregado que trabalha no departamento financeiro
        System.out.println("Dados dos empregados do departamento TI");
        empregados.stream()
            .filter(e -> e.getDepartamento().equals("TI"))
            .forEach(System.out::println);
        //lista dos empregados que receber igual ou acima de 2000
        System.out.println("Dados dos empregados que recebem acima de 2000 em ordem por nome de funcionarios");
        empregados.stream()
            .filter(e -> e.getSalario() >= 2000)
            .sorted((e1, e2) -> e2.getNome().compareTo(e1.getNome()))
            .forEach(System.out::println);
        
        
    }
}