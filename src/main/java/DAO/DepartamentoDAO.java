package DAO;

import Entities.Departamento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DepartamentoDAO {
    private Connection connection;

    public DepartamentoDAO(Connection connection) {
        this.connection = connection;
    }

    public void salvarDepartamento(Departamento departamento) throws SQLException {
        if (departamento.getNome() == null || departamento.getNome().isEmpty()) {
            throw new IllegalArgumentException("Nome do departamento não pode ser vazio.");
        }
        String sql = "INSERT INTO departamento (nome) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, departamento.getNome());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao salvar departamento: " + e.getMessage());
            throw e;
        }
    }
}
