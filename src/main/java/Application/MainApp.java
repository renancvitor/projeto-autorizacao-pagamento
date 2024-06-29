package Application;

import Entities.Usuario;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Collections;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Criando o usuário e os papéis (roles)
            Usuario admin = new Usuario(1, "admin", "adminNewAdmin@1900", "Administrador", Collections.singletonList("Admin"));

            // Criando e iniciando a tela de login
            TelaLogin telaLogin = new TelaLogin();
            telaLogin.setOnCadastroClickListener(() -> {
                // Lógica para abrir a tela de cadastro de usuário
                System.out.println("Cadastrar Usuário clicado");
            });
            telaLogin.start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
