package DAO;

import Entities.Departamento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


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

    public List<Departamento> buscarTodosDepartamentos() {
        List<Departamento> departamentos = new ArrayList<>();
        String sql = "SELECT * FROM departamento"; // Nome da tabela de departamentos

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Departamento departamento = new Departamento();
                departamento.setId(rs.getInt("id"));
                departamento.setNome(rs.getString("nome"));
                departamentos.add(departamento);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return departamentos;
    }
}
