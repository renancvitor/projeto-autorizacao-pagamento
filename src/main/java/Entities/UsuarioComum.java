package Entities;

public class UsuarioComum extends Usuario {
    public UsuarioComum(String nome, String setor, String username, String senha, String email) {
        super(nome, setor, username, senha, email, UserType.COMUM);
    }

    @Override
    public void cadastrarUsuario() {
        // Implementação específica para cadastro de usuário comum
        System.out.println("Cadastrando usuário comum");
    }
}