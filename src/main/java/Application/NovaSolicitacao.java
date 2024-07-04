//package Application;
//
//import Servicoes.Solicitacao;
//import DAO.SolicitacaoDAO;
//import javafx.geometry.Insets;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.layout.GridPane;
//import javafx.stage.Stage;
//
//public class NovaSolicitacao {
//    public static void show(Stage primaryStage, SolicitacaoDAO solicitacaoDAO) {
//        primaryStage.setTitle("Nova Solicitação");
//
//        GridPane grid = new GridPane();
//        grid.setPadding(new Insets(10, 10, 10, 10));
//        grid.setVgap(8);
//        grid.setHgap(10);
//
//        // Fornecedor
//        Label fornecedorLabel = new Label("Fornecedor:");
//        GridPane.setConstraints(fornecedorLabel, 0, 0);
//        TextField fornecedorInput = new TextField();
//        GridPane.setConstraints(fornecedorInput, 1, 0);
//
//        // Descrição
//        Label descricaoLabel = new Label("Descrição:");
//        GridPane.setConstraints(descricaoLabel, 0, 1);
//        TextField descricaoInput = new TextField();
//        GridPane.setConstraints(descricaoInput, 1, 1);
//
//        // Data de pagamento
//        Label dataPagamentoLabel = new Label("Data de Pagamento:");
//        GridPane.setConstraints(dataPagamentoLabel, 0, 2);
//        DatePicker dataPagamentoInput = new DatePicker();
//        GridPane.setConstraints(dataPagamentoInput, 1, 2);
//
//        // Forma de pagamento
//        Label formaPagamentoLabel = new Label("Forma de Pagamento:");
//        GridPane.setConstraints(formaPagamentoLabel, 0, 3);
//        TextField formaPagamentoInput = new TextField();
//        GridPane.setConstraints(formaPagamentoInput, 1, 3);
//
//        // Parcelas
//        Label parcelasLabel = new Label("Parcelas:");
//        GridPane.setConstraints(parcelasLabel, 0, 4);
//        TextField parcelasInput = new TextField();
//        GridPane.setConstraints(parcelasInput, 1, 4);
//
//        // Valor das parcelas
//        Label valorParcelasLabel = new Label("Valor das Parcelas:");
//        GridPane.setConstraints(valorParcelasLabel, 0, 5);
//        TextField valorParcelasInput = new TextField();
//        GridPane.setConstraints(valorParcelasInput, 1, 5);
//
//        // Valor total
//        Label valorTotalLabel = new Label("Valor Total:");
//        GridPane.setConstraints(valorTotalLabel, 0, 6);
//        TextField valorTotalInput = new TextField();
//        GridPane.setConstraints(valorTotalInput, 1, 6);
//
//        // Botão Salvar
//        Button salvarButton = new Button("Salvar");
//        GridPane.setConstraints(salvarButton, 1, 7);
//        salvarButton.setOnAction(e -> {
//            String fornecedor = fornecedorInput.getText();
//            String descricao = descricaoInput.getText();
//            java.sql.Date dataPagamento = java.sql.Date.valueOf(dataPagamentoInput.getValue());
//            String formaPagamento = formaPagamentoInput.getText();
//            int parcelas = Integer.parseInt(parcelasInput.getText());
//            double valorParcelas = Double.parseDouble(valorParcelasInput.getText());
//            double valorTotal = Double.parseDouble(valorTotalInput.getText());
//
//            Solicitacao solicitacao = new Solicitacao(fornecedor, descricao, dataPagamento, formaPagamento, parcelas, valorParcelas, valorTotal);
//            solicitacaoDAO.inserirSolicitacao(solicitacao);
//
//            // Voltar para a tela principal ou exibir mensagem de sucesso
//            primaryStage.setScene(MainApp.getMainScene(primaryStage, solicitacaoDAO));
//        });
//
//        grid.getChildren().addAll(fornecedorLabel, fornecedorInput, descricaoLabel, descricaoInput, dataPagamentoLabel, dataPagamentoInput, formaPagamentoLabel, formaPagamentoInput, parcelasLabel, parcelasInput, valorParcelasLabel, valorParcelasInput, valorTotalLabel, valorTotalInput, salvarButton);
//
//        Scene scene = new Scene(grid, 400, 400);
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }
//}
