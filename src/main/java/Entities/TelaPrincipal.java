package Entities;

import Servicoes.TelaSolicitacao;
import javafx.scene.control.Button;
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
        Label welcomeLabel = new Label("Bem-vindo, " + usuario.getLogin() + "!");
        layout.getChildren().add(welcomeLabel);

        Button novaSolicitacaoButton = new Button("Nova Solicitação");
        novaSolicitacaoButton.setOnAction(e -> {
            TelaSolicitacao telaSolicitacao = new TelaSolicitacao(usuario);
            telaSolicitacao.start(new Stage());
        });
        layout.getChildren().add(novaSolicitacaoButton);

        Scene scene = new Scene(layout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
