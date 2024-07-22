package Entities;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private int id;
    private String login;
    private String senha;
    private List<String> permissoes;

    // Construtor original
    public Usuario(int id, String login, String senha) {
        this.id = id;
        this.login = login;
        this.senha = senha;
        this.permissoes = new ArrayList<>();
    }

    // Construtor adicional
    public Usuario(int id, String login, String senha, List<String> permissoes) {
        this.id = id;
        this.login = login;
        this.senha = senha;
        this.permissoes = permissoes;
    }

    // Getters e setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public String getSenha() {
        return senha;
    }

    public List<String> getPermissoes() {
        return permissoes;
    }

    public boolean isAdmin() {
        return permissoes.contains("ADMIN");
    }

    public boolean hasPermission(String permissao) {
        return permissoes.contains(permissao);
    }
}

