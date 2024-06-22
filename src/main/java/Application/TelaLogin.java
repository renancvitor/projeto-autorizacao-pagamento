package Application;

import Entities.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
            String username = usernameField.getText();
            String senha = passwordField.getText();

            // Valida login e redireciona para a tela apropriada
            Usuario usuario = validarLogin(username, senha);
            if (usuario != null) {
                // Redireciona para a tela principal do sistema, independente do tipo de usuário
                TelaPrincipal telaPrincipal = new TelaPrincipal(usuario);
                Stage stage = new Stage();
                telaPrincipal.start(stage);
                primaryStage.close();
            } else {
                // Exibe mensagem de erro
                System.out.println("Login inválido");
            }
        });

        Scene scene = new Scene(grid, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Usuario validarLogin(String username, String senha) {
        String sql = "SELECT * FROM usuarios WHERE username = ? AND senha = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/seu_banco_de_dados", "usuario", "senha");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, senha);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String nome = rs.getString("nome");
                String setor = rs.getString("setor");
                String email = rs.getString("email");
                String tipo = rs.getString("tipo");

                switch (UserType.valueOf(tipo.toUpperCase())) {
                    case ADMIN:
                        return new Admin(nome, setor, username, senha, email);
                    case GESTOR:
                        return new Gestor(nome, setor, username, senha, email);
                    case VISUALIZADOR:
                        return new Visualizador(nome, setor, username, senha, email);
                    case COMUM:
                        return new UsuarioComum(nome, setor, username, senha, email);
                    default:
                        throw new IllegalArgumentException("Tipo de usuário desconhecido: " + tipo);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Erro ao validar login: " + ex.getMessage());
        }
        return null; // Retorno null se não encontrar usuário com as credenciais informadas
    }
}
