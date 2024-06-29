package Application;

import DAO.UsuarioDAO;
import Entities.Usuario;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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
                // Redireciona para a tela apropriada com base no tipo de usuário
                System.out.println("Login bem-sucedido para o usuário: " + usuario.getLogin());
                // Aqui você pode adicionar a lógica para redirecionar para a tela correta
            } else {
                // Exibe mensagem de erro
                System.out.println("Login inválido");
            }
        });

        Scene scene = new Scene(grid, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Usuario validarLogin(String login, String senha) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/seu_banco_de_dados", "usuario", "senha")) {
            UsuarioDAO usuarioDAO = new UsuarioDAO(connection);
            Usuario usuario = usuarioDAO.getUsuarioByLogin(login);
            if (usuario != null && usuario.getSenha().equals(senha)) {
                return usuario;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
