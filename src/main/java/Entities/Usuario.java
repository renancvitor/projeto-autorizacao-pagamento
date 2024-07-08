package Entities;

import java.util.List;

public class Usuario {
    private int id;
    private String login;
    private String senha;
    private int idTipoUsuario;
    private List<String> roles;

    public Usuario(int id, String login, String senha, int idTipoUsuario) {
        this.id = id;
        this.login = login;
        this.senha = senha;
        this.idTipoUsuario = idTipoUsuario;
    }

    public Usuario(int id, String login, String senha, int idTipoUsuario, List<String> roles) {
        this.id = id;
        this.login = login;
        this.senha = senha;
        this.idTipoUsuario = idTipoUsuario;
        this.roles = roles;
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

    public int getIdTipoUsuario() {
        return idTipoUsuario;
    }

    public void setIdTipoUsuario(int idTipoUsuario) {
        this.idTipoUsuario = idTipoUsuario;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public boolean isAdmin() {
        // Verifica se o login do usuário é "admin"
        if (this.getLogin() != null && "admin".equals(this.getLogin())) {
            return true;
        } else {
            System.out.println("Acesso negado!");
            return false;
        }
    }
}
