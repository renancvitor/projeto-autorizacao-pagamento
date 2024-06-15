package Application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MainApp {
    public static void main(String[] args) {
        // Obtendo a conexão com o banco de dados
        Connection conn = null;
        try {
            conn = obterConexao();

            // Crie um objeto TelaCadastroUsuario passando a conexão como argumento
            TelaCadastroUsuario telaCadastroUsuario = new TelaCadastroUsuario(conn);

            // Agora você pode chamar os métodos da classe TelaCadastroUsuario normalmente
            telaCadastroUsuario.cadastrarNovoUsuario("usuario", "senha");
        } catch (SQLException e) {
            e.printStackTrace();
            // Lida com exceções aqui
        } finally {
            // Certifique-se de fechar a conexão quando não precisar mais dela
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    // Lida com exceções aqui
                }
            }
        }
    }

    // Método para obter uma conexão com o banco de dados
    private static Connection obterConexao() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/sistema_pagamentos";
        String usuario = "root";
        String senha = "123456789";
        return DriverManager.getConnection(url, usuario, senha);
    }

}
