package Entities;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

public class TelaPrincipal {
    private Usuario usuario;

    public TelaPrincipal(Usuario usuario) {
        this.usuario = usuario;
    }

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Sistema Principal");

        VBox layout = new VBox();
        Label welcomeLabel = new Label("Bem-vindo, " + usuario.getNome() + "!");
        layout.getChildren().add(welcomeLabel);

        // Adicione componentes e lógica específica da tela principal aqui,
        // incluindo a lógica para gerenciar permissões com base no tipo de usuário

        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
