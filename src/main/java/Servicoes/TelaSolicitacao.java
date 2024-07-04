package Servicoes;

import Entities.Usuario;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
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
            String dataPagamento = dataPagamentoField.getValue().toString();
            String formaPagamento = formaPagamentoField.getText();
            int parcelas = Integer.parseInt(parcelasField.getText());
            double valorParcelas = Double.parseDouble(valorParcelasField.getText());
            double valorTotal = Double.parseDouble(valorTotalField.getText());

            // Chame o método para enviar a solicitação usando os dados acima
            enviarSolicitacao(fornecedor, descricao, dataPagamento, formaPagamento, parcelas, valorParcelas, valorTotal);

            System.out.println("Solicitação enviada por: " + usuario.getLogin());
            stage.close();
        });
        layout.getChildren().add(submitButton);

        Scene scene = new Scene(layout, 400, 400);
        stage.setScene(scene);
        stage.show();
    }

    private void enviarSolicitacao(String fornecedor, String descricao, String dataPagamento, String formaPagamento, int parcelas, double valorParcelas, double valorTotal) {
        // Lógica para enviar a solicitação
    }
}
