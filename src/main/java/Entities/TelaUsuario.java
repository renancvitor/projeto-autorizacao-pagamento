package Entities;

import Servicoes.SistemaSolicitacao;
import Servicoes.Solicitacao;
import Servicoes.StatusSolicitacao;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class TelaUsuario {
    private Usuario usuario;

    public TelaUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Tela do Usuário");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        Label descricaoLabel = new Label("Descrição:");
        grid.add(descricaoLabel, 0, 0);

        TextArea descricaoField = new TextArea();
        grid.add(descricaoField, 1, 0);

        Label valorLabel = new Label("Valor:");
        grid.add(valorLabel, 0, 1);

        TextField valorField = new TextField();
        grid.add(valorField, 1, 1);

        Button enviarButton = new Button("Enviar Solicitação");
        grid.add(enviarButton, 1, 2);

        enviarButton.setOnAction(e -> {
            String descricao = descricaoField.getText();
            double valor = Double.parseDouble(valorField.getText());

            // Criar nova solicitação
            Solicitacao solicitacao = new Solicitacao(usuario, valor, descricao, StatusSolicitacao.PENDENTE, new java.util.Date());

            // Adicionar solicitação ao sistema
            SistemaSolicitacao sistema = SistemaSolicitacao.getInstance();
            sistema.adicionarSolicitacao(solicitacao);

            // Mostrar mensagem de confirmação
            System.out.println("Solicitação enviada com sucesso!");

            // Limpar campos
            descricaoField.clear();
            valorField.clear();
        });

        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
