package Entities;

public abstract class Usuario {
    private String nome;
    private String setor;
    private String username;
    private String senha;
    private String email;
    private UserType tipo;

    public Usuario(String nome, String setor, String username, String senha, String email, UserType tipo) {
        this.nome = nome;
        this.setor = setor;
        this.username = username;
        this.senha = senha;
        this.email = email;
        this.tipo = tipo;
    }

    public String getEmail() {
        return email;
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

    public UserType getTipo() {
        return tipo;
    }

    public abstract void cadastrarUsuario();
}
