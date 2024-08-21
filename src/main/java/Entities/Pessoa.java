package Entities;

import java.time.LocalDate;

public class Pessoa {
    private int id;
    private String nome;
    private LocalDate datanascimento;
    private Departamento departamento;
    private Cargo cargo;
    private String cpf;

    public Pessoa() {
    }

    public Pessoa(int id, String nome, LocalDate datanascimento, Departamento departamento, Cargo cargo, String cpf) {
        this.id = id;
        this.nome = nome;
        this.datanascimento = datanascimento;
        this.departamento = departamento;
        this.cargo = cargo;
        this.cpf = cpf;
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

    public LocalDate getDatanascimento() {
        return datanascimento;
    }

    public void setDatanascimento(LocalDate datanascimento) {
        this.datanascimento = datanascimento;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}
