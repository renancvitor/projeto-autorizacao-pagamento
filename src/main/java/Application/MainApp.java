package Application;

import javafx.application.Application;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            Connection conn = obterConexao(); // Obtenção da conexão com o banco de dados

            TelaCadastroUsuario telaCadastroUsuario = new TelaCadastroUsuario(conn);
            telaCadastroUsuario.mostrarTelaInicial(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para obter uma conexão com o banco de dados
    private static Connection obterConexao() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/sistema_pagamentos";
        String usuario = "root";
        String senha = "123456789";
        return DriverManager.getConnection(url, usuario, senha);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
