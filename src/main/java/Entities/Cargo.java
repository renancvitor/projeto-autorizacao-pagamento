package Entities;

public class Cargo {

    private int id;
    private String nome;

    // Construtor

    public Cargo(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Cargo() {
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

    @Override
    public String toString() {
        return nome; // Isso garante que o ComboBox exiba o nome do departamento
    }
}
