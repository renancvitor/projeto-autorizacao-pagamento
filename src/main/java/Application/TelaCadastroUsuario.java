package Application;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TelaCadastroUsuario {
    private Connection conn;

    public TelaCadastroUsuario(Connection conn) {
        this.conn = conn;
    }

    public void mostrarTelaInicial(Stage primaryStage) {
        primaryStage.setTitle("Sistema de Pagamento");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        // Nome
        Label nomeLabel = new Label("Nome:");
        GridPane.setConstraints(nomeLabel, 0, 0);
        TextField nomeInput = new TextField();
        GridPane.setConstraints(nomeInput, 1, 0);

        // Email
        Label emailLabel = new Label("Email:");
        GridPane.setConstraints(emailLabel, 0, 1);
        TextField emailInput = new TextField();
        GridPane.setConstraints(emailInput, 1, 1);

        // Botão de Login
        Button loginButton = new Button("Login");
        GridPane.setConstraints(loginButton, 1, 2);

        // Botão de Cadastrar
        Button registerButton = new Button("Cadastrar");
        GridPane.setConstraints(registerButton, 1, 3);

        loginButton.setOnAction(e -> {
            String nome = nomeInput.getText();
            String email = emailInput.getText();

            if (login(nome, email)) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Login bem-sucedido!", "Bem-vindo!");
                // Continue com o sistema após o login bem-sucedido
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Erro no login", "Nome ou email incorretos.");
            }
        });

        registerButton.setOnAction(e -> mostrarTelaCadastro());

        grid.getChildren().addAll(nomeLabel, nomeInput, emailLabel, emailInput, loginButton, registerButton);

        Scene scene = new Scene(grid, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void mostrarTelaCadastro() {
        Stage cadastroStage = new Stage();
        cadastroStage.setTitle("Cadastro de Usuário");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        // Nome
        Label nomeLabel = new Label("Nome:");
        GridPane.setConstraints(nomeLabel, 0, 0);
        TextField nomeInput = new TextField();
        GridPane.setConstraints(nomeInput, 1, 0);

        // Email
        Label emailLabel = new Label("Email:");
        GridPane.setConstraints(emailLabel, 0, 1);
        TextField emailInput = new TextField();
        GridPane.setConstraints(emailInput, 1, 1);

        // Botão de Cadastrar
        Button cadastrarButton = new Button("Cadastrar");
        GridPane.setConstraints(cadastrarButton, 1, 2);

        cadastrarButton.setOnAction(e -> {
            String novoNome = nomeInput.getText();
            String novoEmail = emailInput.getText();

            if (novoNome.isEmpty() || novoEmail.isEmpty()) {
                mostrarAlerta(Alert.AlertType.ERROR, "Erro de Cadastro", "Preencha todos os campos antes de cadastrar.");
                return;
            }

            cadastrarNovoUsuario(novoNome, novoEmail);
            mostrarAlerta(Alert.AlertType.INFORMATION, "Cadastro realizado", "Usuário cadastrado com sucesso!");

            // Fechar a janela de cadastro após o cadastro bem-sucedido
            cadastroStage.close();
        });

        grid.getChildren().addAll(nomeLabel, nomeInput, emailLabel, emailInput, cadastrarButton);

        Scene scene = new Scene(grid, 300, 150);
        cadastroStage.setScene(scene);
        cadastroStage.show();
    }

    private void cadastrarNovoUsuario(String nome, String email) {
        String sql = "INSERT INTO usuarios (nome, email) VALUES (?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.setString(2, email);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private boolean login(String nome, String email) {
        String sql = "SELECT * FROM usuarios WHERE nome = ? AND email = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.setString(2, email);

            try (var rs = stmt.executeQuery()) {
                return rs.next(); // Retorna true se encontrar algum usuário
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false; // Retorna false se não encontrar nenhum usuário
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
