package com.example;

public class Empregado {
    private int id;
    private String nome;
    private double salario;
    private String departamento;

    public Empregado(int id, String nome, double salario, String departamento) {
        this.id = id;
        this.nome = nome;
        this.salario = salario;
        this.departamento = departamento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }
    
    @Override
    public String toString() {
        return String.format("Matricula: %d | Nome: %-20s | Salario: %.2f | Departamento: %s",
             id, nome, salario, departamento);
    }
}
