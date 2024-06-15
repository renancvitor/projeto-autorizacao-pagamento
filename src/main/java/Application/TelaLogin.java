package Application;

import Entities.Gestor;
import Entities.TelaGestor;
import Entities.TelaUsuario;
import Entities.Usuario;
import Entities.UsuarioComum;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class TelaLogin {

    private CadastroListener onCadastroClickListener;

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Login");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        Label userNameLabel = new Label("Username:");
        grid.add(userNameLabel, 0, 0);

        TextField userNameField = new TextField();
        grid.add(userNameField, 1, 0);

        Label passwordLabel = new Label("Password:");
        grid.add(passwordLabel, 0, 1);

        PasswordField passwordField = new PasswordField();
        grid.add(passwordField, 1, 1);

        Button loginButton = new Button("Login");
        grid.add(loginButton, 1, 2);

        Button cadastroButton = new Button("Cadastrar Usuário");
        grid.add(cadastroButton, 1, 3);

        loginButton.setOnAction(e -> {
            String username = userNameField.getText();
            String password = passwordField.getText();

            // Valida login e redireciona para a tela apropriada
            if (validarLogin(username, password)) {
                Usuario usuario = obterUsuario(username);
                if (usuario instanceof Gestor) {
                    // Redireciona para a tela do gestor
                    TelaGestor telaGestor = new TelaGestor((Gestor) usuario);
                    Stage stage = new Stage();
                    telaGestor.start(stage);
                } else {
                    // Redireciona para a tela do usuário
                    TelaUsuario telaUsuario = new TelaUsuario(usuario);
                    Stage stage = new Stage();
                    telaUsuario.start(stage);
                }
                primaryStage.close();
            } else {
                // Exibe mensagem de erro
                System.out.println("Login inválido");
            }
        });

        cadastroButton.setOnAction(e -> {
            if (onCadastroClickListener != null) {
                onCadastroClickListener.onCadastroClicked();
            }
        });

        Scene scene = new Scene(grid, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private boolean validarLogin(String username, String password) {
        // Implementa a lógica de validação do login
        // Por exemplo, comparar username e password com valores fixos
        return "admin".equals(username) && "password".equals(password);
    }

    private Usuario obterUsuario(String username) {
        // Implementa a lógica para obter o usuário pelo username
        // Aqui estamos apenas criando um exemplo de usuário
        if ("admin".equals(username)) {
            return new Gestor("Admin", "Financeiro", "admin", "password");
        } else {
            return new UsuarioComum("User", "RH", "user", "123456");
        }
    }

    public void setOnCadastroClickListener(CadastroListener listener) {
        this.onCadastroClickListener = listener;
    }
}
