package Entities;

public class UsuarioComum extends Usuario {
    public UsuarioComum(String nome, String setor, String username, String senha) {
        super(nome, setor, username, senha);
    }

    @Override
    public void cadastrarUsuario() {
        // Implementação específica para UsuarioComum
    }
}
