package Application;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Inicia a tela de login
        TelaLogin telaLogin = new TelaLogin();
        telaLogin.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
