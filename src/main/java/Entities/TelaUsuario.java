package Entities;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class TelaUsuario<T extends Usuario> {
    private T usuario;

    public TelaUsuario(T usuario) {
        this.usuario = usuario;
    }

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Tela do Usuário");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        Label nameLabel = new Label("Nome:");
        grid.add(nameLabel, 0, 0);

        TextField nameField = new TextField(usuario.getNome());
        grid.add(nameField, 1, 0);

        Label emailLabel = new Label("E-mail:");
        grid.add(emailLabel, 0, 1);

        TextField emailField = new TextField(usuario.getEmail());
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
        // Implemente a lógica para cadastrar o usuário no banco de dados ou realizar outras operações necessárias
        // Aqui, a lógica é simplificada para demonstração
        System.out.println("Cadastro do usuário: Nome=" + nome + ", Email=" + email);
    }
}
