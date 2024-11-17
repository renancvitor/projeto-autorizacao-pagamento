package Entities;

import DAO.PessoaDAO;
import Servicoes.TelaCadastroPessoa;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Usuario {
    private int id;
    private String login;
    private String senha;
    private String cpf;
    private UserType userType;
    private List<String> permissoes;

    public Usuario(int id, String login, String senha, List<String> permissoes, String cpf, int idTipoUsuario, UserType userType) {
        this.id = id;
        this.login = login;
        this.senha = senha;
        this.permissoes = permissoes;
        this.cpf = cpf;
        this.userType = userType;
    }

    public Usuario() {
    }

    public boolean hasPermission(List<String> permissao) {
        return permissoes.contains(permissao);
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getSenha() {
        return senha;
    }

    public String getCpf() {
        return cpf;
    }

    public UserType getUserType() {
        return userType;
    }

    public List<String> getPermissoes() {
        return permissoes;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public void setPermissoes(List<String> permissoes) {
        this.permissoes = permissoes;
    }

    public boolean isAdmin() {
        return permissoes.contains("ADMIN");
    }

    public boolean hasPermission(String permissao) {
        return permissoes.contains(permissao);
    }

    // Validação login único
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Usuario usuario = (Usuario) obj;
        return login.equals(usuario.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login);
    }

    public void adicionarPermissao(String permissao) {
        if (this.permissoes == null) {
            this.permissoes = new ArrayList<>();
        }
        this.permissoes.add(permissao);
    }

}

