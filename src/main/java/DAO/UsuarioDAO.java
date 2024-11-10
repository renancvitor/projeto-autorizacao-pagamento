package DAO;

import Entities.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    private Connection connection;

    public UsuarioDAO(Connection connection) {
        this.connection = connection;
    }

    // Método para obter o idpessoa pelo CPF
    private int getIdPessoaByCpf(String cpf) throws SQLException {
        String sql = "SELECT idpessoa FROM pessoa WHERE cpf = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("idpessoa");
                }
            }
        }
        return -1; // Retorna -1 se o CPF não for encontrado
    }

    // Método para verificar se a pessoa já tem um usuário
    private boolean isUsuarioExistente(int idPessoa) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE idpessoa = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idPessoa);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Retorna true se já existir um usuário
                }
            }
        }
        return false;
    }

    public Usuario getUsuarioByLogin(String login, String senha) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE login = ? AND senha = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, login);
            stmt.setString(2, senha);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String retrievedLogin = rs.getString("login");
                    String retrievedSenha = rs.getString("senha");
                    String cpf = rs.getString("cpf");
                    int idTipoUsuario = rs.getInt("id_tipo_usuario");

                    // Obter permissões do usuário
                    List<String> permissoes = getPermissoesByUsuarioId(id);

                    return new Usuario(id, retrievedLogin, retrievedSenha, permissoes, cpf, idTipoUsuario);
                }
            }
        }
        return null;
    }

    private List<String> getPermissoesByUsuarioId(int usuarioId) throws SQLException {
        List<String> permissoes = new ArrayList<>();
        String sql = """
                SELECT p.nome_permissao 
                FROM permissoes p
                INNER JOIN tipos_usuarios_permissoes tup ON p.id = tup.id_permissao
                INNER JOIN usuarios u ON tup.id_tipo_usuario = u.id_tipo_usuario
                WHERE u.id = ?
            """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    permissoes.add(rs.getString("nome_permissao"));
                }
            }
        }
        return permissoes;
    }

//    public void inserirUsuario(Usuario usuario) throws SQLException {
//        // Valida o CPF antes de inserir o usuário
//        int idPessoa = getIdPessoaByCpf(usuario.getCpf());
//        if (idPessoa == -1) {
//            throw new IllegalArgumentException("CPF não cadastrado na tabela Pessoa!");
//        }
//
//        // Verifica se já existe um usuário vinculado à pessoa
//        if (isUsuarioExistente(idPessoa)) {
//            throw new IllegalArgumentException("Esta pessoa já tem um usuário vinculado!");
//        }
//
//        // Inserir o usuário com o idpessoa ao invés de CPF
//        String sql = "INSERT INTO usuarios (login, senha, idpessoa) VALUES (?, ?, ?)";
//        try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
//            stmt.setString(1, usuario.getLogin());
//            stmt.setString(2, usuario.getSenha());
//            stmt.setInt(3, idPessoa); // Usa o idpessoa para vincular o usuário
//            stmt.executeUpdate();
//
//            // Recupera o id gerado automaticamente
//            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
//                if (generatedKeys.next()) {
//                    int userId = generatedKeys.getInt(1);
//                    usuario.setId(userId);
//                    consultarPermissoesUsuario(usuario);
//                }
//            }
//        }
//    }

    public void inserirUsuario(Usuario usuario) throws SQLException {
        // Valida o CPF antes de inserir o usuário
        int idPessoa = getIdPessoaByCpf(usuario.getCpf());
        if (idPessoa == -1) {
            throw new IllegalArgumentException("CPF não cadastrado na tabela Pessoa!");
        }

        // Verifica se já existe um usuário vinculado à pessoa
        if (isUsuarioExistente(idPessoa)) {
            throw new IllegalArgumentException("Esta pessoa já tem um usuário vinculado!");
        }

        // Inserir o usuário com o idpessoa, id_tipo_usuario e cpf
        String sql = "INSERT INTO usuarios (login, senha, idpessoa, id_tipo_usuario, cpf) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, usuario.getLogin());
            stmt.setString(2, usuario.getSenha());
            stmt.setInt(3, idPessoa); // Usa o idpessoa para vincular o usuário
            stmt.setInt(4, usuario.getIdTipoUsuario()); // Passa o id_tipo_usuario
            stmt.setString(5, usuario.getCpf()); // Passa o cpf

            stmt.executeUpdate();

            // Recupera o id gerado automaticamente
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int userId = generatedKeys.getInt(1);
                    usuario.setId(userId);
                    consultarPermissoesUsuario(usuario);
                }
            }
        }
    }


    // Método para buscar o ID da permissão a partir do nome
    private int obterIdPermissaoPorNome(String nomePermissao) throws SQLException {
        String sql = "SELECT id FROM permissoes WHERE nome_permissao = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nomePermissao);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }
        throw new IllegalArgumentException("Permissão não encontrada: " + nomePermissao);
    }

    private void consultarPermissoesUsuario(Usuario usuario) throws SQLException {
        // Consulta para obter as permissões do tipo de usuário
        String sql = "SELECT p.nome_permissao FROM tipos_usuarios_permissoes tp "
                + "JOIN permissoes p ON tp.id_permissao = p.id "
                + "WHERE tp.id_tipo_usuario = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Passando o id_tipo_usuario do usuário
            stmt.setInt(1, usuario.getId());

            try (ResultSet rs = stmt.executeQuery()) {
                // Processando o resultado da consulta
                while (rs.next()) {
                    String permissao = rs.getString("nome_permissao");
                    usuario.adicionarPermissao(permissao);  // Método para adicionar a permissão ao usuário
                }
            }
        }
    }

//    private void inserirPermissoesUsuario(Usuario usuario) throws SQLException {
//        String sql = "INSERT INTO tipos_usuarios_permissoes (id_tipo_usuario, id_permissao) VALUES (?, ?)";
//        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
//            for (String permissao : usuario.getPermissoes()) {
//                // Obter o ID da permissão a partir do nome
//                int idPermissao = obterIdPermissaoPorNome(permissao);
//
//                stmt.setInt(1, usuario.getId());
//                stmt.setInt(2, idPermissao); // Agora estamos inserindo o ID da permissão
//                stmt.addBatch();
//            }
//            stmt.executeBatch();
//        }
//    }

}
