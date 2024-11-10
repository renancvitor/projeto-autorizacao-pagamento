package DAO;

import Entities.UserType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserPermissionsDAO {
    private final Connection connection;

    public UserPermissionsDAO(Connection connection) {
        this.connection = connection;
    }

    public List<String> buscarTodasPermissoes() {
        List<String> permissoes = new ArrayList<>();
        String sql = "SELECT nome FROM permissoes";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                permissoes.add(rs.getString("nome"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return permissoes;
    }

    public List<String> buscarPermissoesPorTipoUsuario(UserType userType) {
        List<String> permissoes = new ArrayList<>();
        String sql = "SELECT p.nome FROM permissoes p " +
                "JOIN tipos_usuarios_permissoes tup ON p.id = tup.permissao_id " +
                "JOIN tipos_usuarios tu ON tu.id = tup.tipo_usuario_id " +
                "WHERE tu.nome = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, userType.name());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    permissoes.add(rs.getString("nome"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return permissoes;
    }

    public void atualizarPermissoesUsuario(UserType userType, List<String> permissoesSelecionadas) {
        String deleteSQL = "DELETE FROM tipos_usuarios_permissoes WHERE tipo_usuario_id = " +
                "(SELECT id FROM tipos_usuarios WHERE nome = ?)";
        String insertSQL = "INSERT INTO tipos_usuarios_permissoes (tipo_usuario_id, permissao_id) " +
                "VALUES ((SELECT id FROM tipos_usuarios WHERE nome = ?), " +
                "(SELECT id FROM permissoes WHERE nome = ?))";

        try (PreparedStatement deleteStmt = connection.prepareStatement(deleteSQL);
             PreparedStatement insertStmt = connection.prepareStatement(insertSQL)) {

            connection.setAutoCommit(false);
            deleteStmt.setString(1, userType.name());
            deleteStmt.executeUpdate();

            for (String permissao : permissoesSelecionadas) {
                insertStmt.setString(1, userType.name());
                insertStmt.setString(2, permissao);
                insertStmt.executeUpdate();
            }
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
