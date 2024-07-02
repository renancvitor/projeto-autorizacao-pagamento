package Servicoes;

import Entities.Usuario;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TelaSolicitacao {
    private Usuario usuario;

    public TelaSolicitacao(Usuario usuario) {
        this.usuario = usuario;
    }

    public void start(Stage stage) {
        stage.setTitle("Nova Solicitação");

        VBox layout = new VBox(10);
        Label label = new Label("Preencha os dados da solicitação:");
        layout.getChildren().add(label);

        TextField descricaoField = new TextField();
        descricaoField.setPromptText("Descrição");
        layout.getChildren().add(descricaoField);

        Button submitButton = new Button("Enviar Solicitação");
        submitButton.setOnAction(e -> {
            // Lógica para enviar a solicitação
            System.out.println("Solicitação enviada por: " + usuario.getLogin());
            stage.close();
        });
        layout.getChildren().add(submitButton);

        Scene scene = new Scene(layout, 300, 200);
        stage.setScene(scene);
        stage.show();
    }
}
