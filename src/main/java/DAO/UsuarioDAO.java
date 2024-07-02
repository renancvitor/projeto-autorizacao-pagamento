package DAO;

import Entities.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class UsuarioDAO {
    private Connection connection;

    public UsuarioDAO(Connection connection) {
        this.connection = connection;
    }

    // Método para inserir um novo usuário
    public void inserirUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuarios (login, senha, id_tipo_usuario, tipo_usuario) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, usuario.getLogin());
            pstmt.setString(2, usuario.getSenha());
            pstmt.setInt(3, usuario.getIdTipoUsuario());
            pstmt.setString(4, String.join(",", usuario.getRoles())); // Convertendo a lista de roles para uma string
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para buscar um usuário pelo login
    public Usuario getUsuarioByLogin(String login, String senha) {
        String sql = "SELECT * FROM usuarios WHERE login = ? AND senha = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, login);
            pstmt.setString(2, senha);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                login = rs.getString("login");
                senha = rs.getString("senha");
                int idTipoUsuario = rs.getInt("id_tipo_usuario");

                // String tipoUsuario = rs.getString("tipo_usuario");
                // Convertendo a string de tipo_usuario para uma lista de roles
                // List<String> roles = Arrays.asList(tipoUsuario.split(","));

                return new Usuario(login, senha, idTipoUsuario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Outros métodos CRUD podem ser adicionados conforme necessário
}
