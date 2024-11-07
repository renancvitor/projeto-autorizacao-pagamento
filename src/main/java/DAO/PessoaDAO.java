package DAO;

import Entities.Pessoa;

import java.sql.*;

public class PessoaDAO {
    private Connection connection;

    public PessoaDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean existeCpf(String cpf) {
        String sql = "SELECT COUNT(*) FROM Pessoa WHERE cpf = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void salvarPessoa(Pessoa pessoa) throws SQLException {
        if (pessoa.getNome() == null || pessoa.getNome().isEmpty()) {
            throw new IllegalArgumentException("Nome da pessoa não pode ser vazio.");
        }
        String sql = "INSERT INTO pessoa (nome, datanascimento, iddepartamento, idcargo, cpf) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, pessoa.getNome());
            stmt.setDate(2, Date.valueOf(pessoa.getDatanascimento()));
            stmt.setInt(3, pessoa.getDepartamento().getId());
            stmt.setInt(4, pessoa.getCargo().getId());
            stmt.setString(5, pessoa.getCpf());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao salvar pessoa: " + e.getMessage());
            throw e;
        }
    }
}
