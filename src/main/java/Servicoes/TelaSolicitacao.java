package Servicoes;

import DAO.SolicitacaoDAO;
import Entities.Usuario;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

public class TelaSolicitacao {
    private Usuario usuario;
    private Connection connection;

    public TelaSolicitacao(Usuario usuario) {
        this.usuario = usuario;
        this.connection = connection;
    }

    public void start(Stage stage) {
        stage.setTitle("Nova Solicitação");

        VBox layout = new VBox(10);
        Label label = new Label("Preencha os dados da solicitação:");
        layout.getChildren().add(label);

        TextField fornecedorField = new TextField();
        fornecedorField.setPromptText("Fornecedor");
        layout.getChildren().add(fornecedorField);

        TextField descricaoField = new TextField();
        descricaoField.setPromptText("Descrição");
        layout.getChildren().add(descricaoField);

        DatePicker dataPagamentoField = new DatePicker();
        dataPagamentoField.setPromptText("Data de Pagamento");
        layout.getChildren().add(dataPagamentoField);

        TextField formaPagamentoField = new TextField();
        formaPagamentoField.setPromptText("Forma de Pagamento");
        layout.getChildren().add(formaPagamentoField);

        TextField parcelasField = new TextField();
        parcelasField.setPromptText("Parcelas");
        layout.getChildren().add(parcelasField);

        TextField valorParcelasField = new TextField();
        valorParcelasField.setPromptText("Valor das Parcelas");
        layout.getChildren().add(valorParcelasField);

        TextField valorTotalField = new TextField();
        valorTotalField.setPromptText("Valor Total");
        layout.getChildren().add(valorTotalField);

        Button submitButton = new Button("Enviar Solicitação");
        submitButton.setOnAction(e -> {
            // Lógica para enviar a solicitação
            String fornecedor = fornecedorField.getText();
            String descricao = descricaoField.getText();
            Date dataPagamento = Date.valueOf(dataPagamentoField.getValue());
            String formaPagamento = formaPagamentoField.getText();
            int parcelas = Integer.parseInt(parcelasField.getText());
            double valorParcelas = Double.parseDouble(valorParcelasField.getText());
            double valorTotal = Double.parseDouble(valorTotalField.getText());

            // Crie uma nova solicitação
            Solicitacao solicitacao = new Solicitacao(0, fornecedor, descricao, new java.sql.Timestamp(System.currentTimeMillis()), dataPagamento, formaPagamento, parcelas, valorParcelas, valorTotal, usuario.getId());

            // Insira a solicitação no banco de dados
            SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO(connection);
            try {
                solicitacaoDAO.inserirSolicitacao(solicitacao);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

            System.out.println("Solicitação enviada por: " + usuario.getLogin());
            stage.close();
        });
        layout.getChildren().add(submitButton);

        Scene scene = new Scene(layout, 400, 400);
        stage.setScene(scene);
        stage.show();
    }
}
