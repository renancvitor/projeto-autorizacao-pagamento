package Entities;

import DAO.PessoaDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Usuario {
    private int id;
    private String login;
    private String senha;
    private String cpf;
    private List<String> permissoes;

    // Construtor adicional
    public Usuario(int id, String login, String senha, String cpf, List<String> permissoes) {
        this.id = id;
        this.login = login;
        this.senha = senha;
        this.cpf = cpf;
        this.permissoes = permissoes;
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

    public void setPermissoes(List<String> permissoes) {
        this.permissoes = permissoes;
    }

    // Validação CPF
    public boolean validarCpfPessoa(PessoaDAO pessoaDAO) {
        return pessoaDAO.existeCpf(cpf);
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

    public boolean isAdmin() {
        return permissoes.contains("ADMIN");
    }

    public boolean hasPermission(String permissao) {
        return permissoes.contains(permissao);
    }
}

