package Entities;

import Servicoes.TelaSolicitacao;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

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

        // Resumo Rápido
        Label resumoLabel = new Label("Resumo Rápido:");
        Label totalPendentesLabel = new Label("Pendentes: 10");
        Label totalAprovadasLabel = new Label("Aprovadas: 5");
        Label totalRejeitadasLabel = new Label("Rejeitadas: 2");
        layout.getChildren().addAll(resumoLabel, totalPendentesLabel, totalAprovadasLabel, totalRejeitadasLabel);

        // Filtros de Solicitações
        ComboBox<String> filterBox = new ComboBox<>();
        filterBox.getItems().addAll("Todas", "Pendentes", "Aprovadas", "Rejeitadas");
        filterBox.setValue("Todas");
        layout.getChildren().add(filterBox);

        // Barra de Pesquisa
        TextField searchField = new TextField();
        searchField.setPromptText("Buscar solicitações...");
        layout.getChildren().add(searchField);

        // Botão para novas solicitações
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
