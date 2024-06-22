package Entities;

public class Admin extends Usuario {
    public Admin(String nome, String setor, String username, String senha, String email) {
        super(nome, setor, username, senha, email, UserType.ADMIN);
    }

    @Override
    public void cadastrarUsuario() {
        // Implementação específica para cadastro de usuário admin
        System.out.println("Cadastrando usuário admin");
    }
}