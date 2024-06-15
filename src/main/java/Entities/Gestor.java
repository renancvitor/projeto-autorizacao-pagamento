package Entities;

public class Gestor extends Usuario {
    public Gestor(String nome, String setor, String username, String senha) {
        super(nome, setor, username, senha);
    }

    @Override
    public void cadastrarUsuario() {
        // Implementação específica para Gestor
    }
}
