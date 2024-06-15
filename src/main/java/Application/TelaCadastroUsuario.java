package Application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TelaCadastroUsuario {
    private Connection conn; // Certifique-se de que a conexão seja atribuída corretamente

    // Construtor para injetar a conexão
    public TelaCadastroUsuario(Connection conn) {
        this.conn = conn;
    }

    public void cadastrarNovoUsuario(String nome, String email) {
        String sql = "INSERT INTO usuario (nome, email) VALUES (?, ?)";
        System.out.println("SQL: " + sql); // Debug: Imprime a consulta SQL
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sistema_pagamentos", "root", "123456789")) {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, nome);
                stmt.setString(2, email);
                stmt.executeUpdate();
                System.out.println("Usuário cadastrado com sucesso!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erro ao cadastrar usuário: " + e.getMessage());
        }
    }
}
