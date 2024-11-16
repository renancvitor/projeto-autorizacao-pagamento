package Application;

import DAO.UsuarioDAO;
import Entities.TelaPrincipal;
import Entities.UserService;
import Entities.Usuario;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TelaLogin {

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Login");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        Label usernameLabel = new Label("Usuário:");
        grid.add(usernameLabel, 0, 0);

        TextField usernameField = new TextField();
        grid.add(usernameField, 1, 0);

        Label passwordLabel = new Label("Senha:");
        grid.add(passwordLabel, 0, 1);

        PasswordField passwordField = new PasswordField();
        grid.add(passwordField, 1, 1);

        Button loginButton = new Button("Login");
        grid.add(loginButton, 1, 2);

        loginButton.setOnAction(e -> {
            String login = usernameField.getText();
            String senha = passwordField.getText();

            // Valida login e redireciona para a tela apropriada
            Usuario usuario = validarLogin(login, senha);
            if (usuario != null) {
                // Login bem-sucedido, redireciona para TelaPrincipal
                TelaPrincipal telaPrincipal = new TelaPrincipal(usuario);
                telaPrincipal.start(primaryStage); // Abre uma nova janela para a tela principal
                // primaryStage.close(); // Fecha a janela de login
            } else {
                // Exibe mensagem de erro
                System.out.println("Login inválido");
            }
        });

        Scene scene = new Scene(grid, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();

        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
    }

    private Usuario validarLogin(String login, String senha) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sistema_pagamentos", "root", "123456789")) {
            UsuarioDAO usuarioDAO = new UsuarioDAO(connection);
            Usuario usuario = usuarioDAO.getUsuarioByLogin(login, senha);
            if (usuario != null) {
                UserService.setUsuarioLogado(usuario); // Definir o usuário logado na sessão
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Login bem-sucedido. Bem-vindo, " + usuario.getLogin());
                alert.show();

                return usuario;
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Usuário ou senha inválidos.");
                alert.show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
