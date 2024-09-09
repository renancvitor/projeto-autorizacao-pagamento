package DAO;

import Entities.Cargo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CargoDAO {
    private Connection connection;

    public CargoDAO(Connection connection) {
        this.connection = connection;
    }

    public void salvar(Cargo cargo) throws SQLException {
        if (cargo.getNome() == null || cargo.getNome().isEmpty()) {
            throw new IllegalArgumentException("Nome do cargo não pode ser vazio.");
        }
        String sql = "INSERT INTO cargo (nome) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cargo.getNome());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao salvar cargo: " + e.getMessage());
            throw e;
        }
    }
}
