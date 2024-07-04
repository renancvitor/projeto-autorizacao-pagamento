package Application;

import Entities.Usuario;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Collections;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Criando e iniciando a tela de login
            TelaLogin telaLogin = new TelaLogin();
            telaLogin.start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

