package DAO;

import Entities.Cargo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CargoDAO {
    private Connection connection;

    public CargoDAO(Connection connection) {
        this.connection = connection;
    }

    public void salvarCargo(Cargo cargo) throws SQLException {
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

    public List<Cargo> buscarTodosCargos() {
        List<Cargo> cargos = new ArrayList<>();
        String sql = "SELECT * FROM cargo"; // Nome da tabela de cargos

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Cargo cargo = new Cargo();
                cargo.setId(rs.getInt("id"));
                cargo.setNome(rs.getString("nome"));
                cargos.add(cargo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cargos;
    }
}
