package DAO;

import Entities.UserType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserPermissionsDAO {
    private final Connection connection;

    public UserPermissionsDAO(Connection connection) {
        this.connection = connection;
    }

    public int getIdTipoUsuario(String nomeTipo) throws SQLException {
        String sql = "SELECT id FROM tipos_usuarios WHERE tipo_usuario = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nomeTipo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }
        throw new SQLException("Tipo de usuário não encontrado: " + nomeTipo);
    }

    public List<String> getPermissoesByTipo(String nomeTipo) throws SQLException {
        int idTipoUsuario = getIdTipoUsuario(nomeTipo);
        List<String> permissoes = new ArrayList<>();

        String sql = "SELECT p.nome_permissao FROM permissoes p "
                + "JOIN tipos_usuarios_permissoes tup ON p.id = tup.id_permissao "
                + "JOIN tipos_usuarios tu ON tu.id = tup.id_tipo_usuario "
                + "WHERE tu.tipo_usuario = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idTipoUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    permissoes.add(rs.getString("nome"));
                }
            }
        }
        return permissoes;
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

    public List<String> getTiposUsuarios() throws SQLException {
        List<String> tiposUsuarios = new ArrayList<>();
        String query = "SELECT tipo_usuario FROM tipos_usuarios";  // Consulta para buscar tipos de usuários

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                tiposUsuarios.add(rs.getString("tipo_usuario"));
            }
        }

        return tiposUsuarios;
    }


    public List<String> getAllPermissions() throws SQLException {
        List<String> permissions = new ArrayList<>();
        String query = "SELECT nome_permissao FROM permissoes";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                permissions.add(rs.getString("nome_permissao"));
            }
        }
        return permissions;
    }

    public int getIdTipoUsuarioByNome(String tipoUsuarioNome) throws SQLException {
        String sql = "SELECT id FROM tipos_usuarios WHERE tipo_usuario = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, tipoUsuarioNome);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }
        throw new SQLException("Tipo de usuário não encontrado");
    }

}
