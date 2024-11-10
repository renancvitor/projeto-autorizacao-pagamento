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
    private int idTipoUsuario;
    private List<String> permissoes;

    // Construtor adicional
    public Usuario(int id, String login, String senha, List<String> permissoes, String cpf, int idTipoUsuario) {
        this.id = id;
        this.login = login;
        this.senha = senha;
        this.permissoes = permissoes;
        this.cpf = cpf;
        this.idTipoUsuario = idTipoUsuario;
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

    public int getIdTipoUsuario() {
        return idTipoUsuario;
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

    public void setIdTipoUsuario(int idTipoUsuario) {
        this.idTipoUsuario = idTipoUsuario;
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

    // Método para criar ComboBox de permissões
    public static ComboBox<String> criarComboBoxPermissoes() {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll("Admin", "User", "Viewer");
        return comboBox;
    }

    public void adicionarPermissao(String permissao) {
        if (this.permissoes == null) {
            this.permissoes = new ArrayList<>();
        }
        this.permissoes.add(permissao);
    }

}

