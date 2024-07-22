package DAO;

import Entities.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    private Connection connection;

    public UsuarioDAO(Connection connection) {
        this.connection = connection;
    }

    public Usuario getUsuarioById(int id) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int userId = rs.getInt("id");
                    String login = rs.getString("login");
                    String senha = rs.getString("senha");

                    // Obter permissões do usuário
                    List<String> permissoes = getPermissoesById(userId);

                    return new Usuario(userId, login, senha, permissoes);
                } else {
                    return null; // ou lance uma exceção se preferir
                }
            }
        }
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

                    // Obter permissões do usuário
                    List<String> permissoes = getPermissoesById(id);

                    return new Usuario(id, retrievedLogin, retrievedSenha, permissoes);
                } else {
                    return null; // ou lance uma exceção se preferir
                }
            }
        }
    }

    private List<String> getPermissoesById(int userId) throws SQLException {
        String sql = "SELECT permissao FROM permissoes WHERE id_usuario = ?";
        List<String> permissoes = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    permissoes.add(rs.getString("permissao"));
                }
            }
        }
        return permissoes;
    }

    public void inserirUsuario(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuarios (login, senha) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, usuario.getLogin());
            stmt.setString(2, usuario.getSenha());
            stmt.executeUpdate();

            // Recupera o id gerado automaticamente
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int userId = generatedKeys.getInt(1);
                    usuario.setId(userId);
                    inserirPermissoesUsuario(usuario);
                }
            }
        }
    }

    private void inserirPermissoesUsuario(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO permissoes (id_usuario, permissao) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (String permissao : usuario.getPermissoes()) {
                stmt.setInt(1, usuario.getId());
                stmt.setString(2, permissao);
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }
}
