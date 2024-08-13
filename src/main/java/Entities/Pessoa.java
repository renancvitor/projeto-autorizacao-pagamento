package Entities;

import java.time.LocalDateTime;

public class Pessoa {
    private int id;
    private String nome;
    private LocalDateTime datanascimento;

    // Construtor

    public Pessoa(int id, String nome, LocalDateTime datanascimento) {
        this.id = id;
        this.nome = nome;
        this.datanascimento = datanascimento;
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

    public LocalDateTime getDatanascimento() {
        return datanascimento;
    }

    public void setDatanascimento(LocalDateTime datanascimento) {
        this.datanascimento = datanascimento;
    }
}
