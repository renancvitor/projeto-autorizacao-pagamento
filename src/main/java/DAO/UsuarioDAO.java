package DAO;

import Entities.UserType;
import Entities.Usuario;
import Servicoes.PermissaoService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    private Connection connection;
    private final PermissaoService permissaoService;

    public UsuarioDAO(Connection connection) {
        this.connection = connection;
        this.permissaoService = new PermissaoService();
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

    // Método estático para obter UserType pelo id_tipo_usuario
    private UserType getUserTypeFromId(int idTipoUsuario) {
        return switch (idTipoUsuario) {
            case 1 -> UserType.ADMIN;
            case 2 -> UserType.GESTOR;
            case 3 -> UserType.VISUALIZADOR;
            case 4 -> UserType.COMUM;
            default -> throw new IllegalArgumentException("Tipo de usuário inválido: " + idTipoUsuario);
        };
    }

    private int getIdFromUserType(UserType userType) {
        return switch (userType) {
            case ADMIN -> 1;
            case GESTOR -> 2;
            case VISUALIZADOR -> 3;
            case COMUM -> 4;
        };
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
                    UserType userType = getUserTypeFromId(idTipoUsuario);

                    // Obter permissões do usuário
                    List<String> permissoes = getPermissoesByUsuarioId(id);

                    return new Usuario(id, retrievedLogin, retrievedSenha, permissoes, cpf, idTipoUsuario, userType);
                }
            }
        }
        return null;
    }

    public List<String> getPermissoesByUsuarioId(int usuarioId) throws SQLException {
        // Buscar o id_tipo_usuario do usuário no banco de dados
        String sql = "SELECT id_tipo_usuario FROM usuarios WHERE id = ?";
        int tipoUsuarioId = -1;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    tipoUsuarioId = rs.getInt("id_tipo_usuario");
                }
            }
        }

        // Verifica se o tipoUsuarioId foi encontrado e mapeia o UserType
        UserType userType = switch (tipoUsuarioId) {
            case 1 -> UserType.ADMIN;
            case 2 -> UserType.GESTOR;
            case 3 -> UserType.VISUALIZADOR;
            case 4 -> UserType.COMUM;
            default -> null;
        };

        // Retorna as permissões baseadas no UserType
        return (userType != null) ? permissaoService.getPermissoesByUserType(userType) : List.of();
    }

    public void inserirUsuario(Usuario usuario) throws SQLException {
        // Valida o CPF antes de inserir o usuário
        int idPessoa = getIdPessoaByCpf(usuario.getCpf());
        if (idPessoa == -1) {
            throw new IllegalArgumentException("CPF não cadastrado na tabela Pessoa!");
        }

        if (isUsuarioExistente(idPessoa)) {
            throw new IllegalArgumentException("Esta pessoa já tem um usuário vinculado!");
        }

        String sql = "INSERT INTO usuarios (login, senha, idpessoa, id_tipo_usuario, cpf) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, usuario.getLogin());
            stmt.setString(2, usuario.getSenha());
            stmt.setInt(3, idPessoa); // Usa o idpessoa para vincular o usuário
            stmt.setInt(4, getIdFromUserType(usuario.getUserType())); // Obtém o id_tipo_usuario do UserType
            stmt.setString(5, usuario.getCpf());

            stmt.executeUpdate();

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

}
