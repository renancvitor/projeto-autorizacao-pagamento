package Application;

import DAO.UsuarioDAO;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TelaAlterarSenha {
    private Connection connection;

    public TelaAlterarSenha() {
        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sistema_pagamentos", "root", "123456789");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void exibir() {
        Stage stage = new Stage();
        stage.setTitle("Alterar Senha");

        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 10;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Nome de Usuário");

        PasswordField senhaAtualField = new PasswordField();
        senhaAtualField.setPromptText("Senha Atual");

        PasswordField novaSenhaField = new PasswordField();
        novaSenhaField.setPromptText("Nova Senha");

        PasswordField confirmarSenhaField = new PasswordField();
        confirmarSenhaField.setPromptText("Confirmar Nova Senha");

        Button alterarButton = new Button("Alterar Senha");
        alterarButton.setOnAction(e -> {
            String username = usernameField.getText();
            String senhaAtual = senhaAtualField.getText();
            String novaSenha = novaSenhaField.getText();
            String confirmarSenha = confirmarSenhaField.getText();

            if (novaSenha.equals(confirmarSenha)) {
                alterarSenha(username, senhaAtual, novaSenha, stage);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "As senhas não coincidem.");
                alert.show();
            }
        });

        layout.getChildren().addAll(usernameField, senhaAtualField, novaSenhaField, confirmarSenhaField, alterarButton);

        Scene scene = new Scene(layout, 300, 250);
        stage.setScene(scene);
        stage.show();

        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
    }

    private void alterarSenha(String username, String senhaAtual, String novaSenha, Stage stage) {
        try {
            UsuarioDAO usuarioDAO = new UsuarioDAO(connection);
            if (usuarioDAO.verificarSenhaPorUsuario(username, senhaAtual)) {
                if (usuarioDAO.alterarSenhaPorUsuario(username, novaSenha)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Senha alterada com sucesso.");
                    alert.showAndWait();
                    stage.close();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao alterar a senha.");
                    alert.show();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Nome de usuário ou senha atual inválidos.");
                alert.show();
            }
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro no banco de dados: " + e.getMessage());
            alert.show();
            e.printStackTrace();
        }
    }

}
