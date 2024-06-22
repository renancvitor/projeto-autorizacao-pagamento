package Application;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Importando a classe TelaLogin corretamente
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
