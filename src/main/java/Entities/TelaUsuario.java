package Entities;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import Servicoes.Database;

public class TelaUsuario {
    private Usuario usuario;

    public TelaUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Tela do Usuário");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        Label nameLabel = new Label("Nome:");
        grid.add(nameLabel, 0, 0);

        TextField nameField = new TextField();
        grid.add(nameField, 1, 0);

        Label emailLabel = new Label("E-mail:");
        grid.add(emailLabel, 0, 1);

        TextField emailField = new TextField();
        grid.add(emailField, 1, 1);

        Button cadastrarButton = new Button("Cadastrar");
        grid.add(cadastrarButton, 1, 2);

        cadastrarButton.setOnAction(e -> {
            String nome = nameField.getText();
            String email = emailField.getText();

            // Adicionar lógica para cadastrar o usuário diretamente aqui
            cadastrarNovoUsuario(nome, email);

            // Limpar campos após cadastro
            nameField.clear();
            emailField.clear();
        });

        Scene scene = new Scene(grid, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void cadastrarNovoUsuario(String nome, String email) {
        String sql = "INSERT INTO usuarios (nome, email, senha) VALUES (?, ?, '123456')";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nome);
            pstmt.setString(2, email);
            pstmt.executeUpdate();
            System.out.println("Usuário cadastrado com sucesso!");
        } catch (SQLException ex) {
            System.out.println("Erro ao cadastrar usuário: " + ex.getMessage());
        }
    }
}
