package Entities;

public abstract class Usuario {
    private String nome;
    private String setor;
    private String username;
    private String senha;
    private String email;

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public Usuario(String email) {
        this.email = email;
    }

    public Usuario(String nome, String setor, String username, String senha) {
        this.nome = nome;
        this.setor = setor;
        this.username = username;
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public String getSetor() {
        return setor;
    }

    public String getUsername() {
        return username;
    }

    public String getSenha() {
        return senha;
    }

    public abstract void cadastrarUsuario();
}
