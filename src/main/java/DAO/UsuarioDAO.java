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

    public Usuario getUsuarioByLogin(String login) {
        String sql = "SELECT * FROM usuarios WHERE login = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, login);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String senha = rs.getString("senha");
                List<String> roles = getRolesByUserId(id); // Obtém os papéis do usuário
                return new Usuario(id, login, senha, roles); // Instancia o objeto Usuario com os papéis
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<String> getRolesByUserId(int userId) throws SQLException {
        List<String> roles = new ArrayList<>();
        String sql = "SELECT tipo_usuario FROM tipos_usuarios WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                roles.add(rs.getString("tipo_usuario"));
            }
        }
        return roles;
    }

    // Outros métodos CRUD (inserir, atualizar, deletar) podem ser adicionados aqui
}
