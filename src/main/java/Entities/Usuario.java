package Entities;

import java.util.List;

public class Usuario {
    private int id;
    private String login;
    private String senha;
    private String email;
    private String nome;
    private int idTipoUsuario;
    private List<String> roles; // Lista de papéis (roles) do usuário

    public Usuario(int id, String login, String senha, List<String> roles) {
        this.id = id;
        this.login = login;
        this.senha = senha;
        this.roles = roles;
        // this.idTipoUsuario = idTipoUsuario;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

//    public int getIdTipoUsuario() {
//        return idTipoUsuario;
//    }

//    public void setIdTipoUsuario(int idTipoUsuario) {
//        this.idTipoUsuario = idTipoUsuario;
//    }


    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
