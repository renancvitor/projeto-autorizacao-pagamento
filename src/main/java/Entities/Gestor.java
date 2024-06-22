package Entities;

public class Gestor extends Usuario {
    public Gestor(String nome, String setor, String username, String senha, String email) {
        super(nome, setor, username, senha, email, UserType.GESTOR);
    }

    @Override
    public void cadastrarUsuario() {
        // Implementação específica para cadastro de usuário gestor
        System.out.println("Cadastrando usuário gestor");
    }
}