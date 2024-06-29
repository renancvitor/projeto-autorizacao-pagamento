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
import java.util.Arrays;

public class CadastroUsuarios {

    private Usuario usuarioLogado;
    private UsuarioDAO usuarioDAO;

    public CadastroUsuarios(Usuario usuarioLogado, UsuarioDAO usuarioDAO) {
        this.usuarioLogado = usuarioLogado;
        this.usuarioDAO = usuarioDAO;
    }

    public void exibirTelaCadastro() {
        Stage stage = new Stage();
        stage.setTitle("Cadastro de Usuários");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        Label nameLabel = new Label("Nome:");
        grid.add(nameLabel, 0, 0);
        TextField nameField = new TextField();
        grid.add(nameField, 1, 0);

        Label loginLabel = new Label("Login:");
        grid.add(loginLabel, 0, 1);
        TextField loginField = new TextField();
        grid.add(loginField, 1, 1);

        Label senhaLabel = new Label("Senha:");
        grid.add(senhaLabel, 0, 2);
        PasswordField senhaField = new PasswordField();
        grid.add(senhaField, 1, 2);

        Button cadastrarButton = new Button("Cadastrar");
        cadastrarButton.setOnAction(e -> {
            // Verifica se o usuário logado tem permissões de admin
            if (usuarioLogado != null && usuarioLogado.isAdmin()) {
                String login = loginField.getText();
                String senha = senhaField.getText();

                Usuario novoUsuario = new Usuario(0, login, senha, 4, Arrays.asList("Comum"));
                usuarioDAO.inserirUsuario(novoUsuario);

                System.out.println("Usuário cadastrado com sucesso!");
                limparCampos(loginField, senhaField);
            } else {
                System.out.println("Acesso não autorizado para cadastrar usuários.");
            }
        });
        grid.add(cadastrarButton, 1, 3);


        Scene scene = new Scene(grid, 300, 200);
        stage.setScene(scene);
        stage.show();
    }

    private void limparCampos(TextField... campos) {
        for (TextField campo : campos) {
            campo.clear();
        }
    }
}
