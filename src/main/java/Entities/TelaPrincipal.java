package Entities;

import DAO.SolicitacaoDAO;
import Servicoes.Solicitacao;
import Servicoes.TelaSolicitacao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class TelaPrincipal {
    private Usuario usuario;
    private Connection connection;

    public TelaPrincipal(Usuario usuario) {
        this.usuario = usuario;
        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sistema_pagamentos", "root", "123456789");
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
//        ComboBox<String> filterBox = new ComboBox<>();
//        filterBox.getItems().addAll("Todas", "Pendentes", "Aprovadas", "Rejeitadas");
//        filterBox.setValue("Todas");
//        layout.getChildren().add(filterBox);

        // Barra de Pesquisa
//        TextField searchField = new TextField();
//        searchField.setPromptText("Buscar solicitações...");
//        layout.getChildren().add(searchField);

        // Botão para novas solicitações
        Button novaSolicitacaoButton = new Button("Nova Solicitação");
        novaSolicitacaoButton.setOnAction(e -> {
            TelaSolicitacao telaSolicitacao = new TelaSolicitacao(usuario);
            telaSolicitacao.start(new Stage());
        });
        layout.getChildren().add(novaSolicitacaoButton);

        // Layout para Resumo Rápido e Botão
        HBox topoLayout = new HBox(10, resumoLabel, novaSolicitacaoButton);

        // TableView para exibir as solicitações
        TableView<Solicitacao> table = new TableView<>();
        table.setItems(getSolicitacoes());

        TableColumn<Solicitacao, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Solicitacao, String> fornecedorCol = new TableColumn<>("Fornecedor");
        fornecedorCol.setCellValueFactory(new PropertyValueFactory<>("fornecedor"));

        TableColumn<Solicitacao, String> descricaoCol = new TableColumn<>("Descrição");
        descricaoCol.setCellValueFactory(new PropertyValueFactory<>("descricao"));

        TableColumn<Solicitacao, String> dataCriacaoCol = new TableColumn<>("Data Criação");
        dataCriacaoCol.setCellValueFactory(new PropertyValueFactory<>("dataCriacao"));

        TableColumn<Solicitacao, String> dataPagamentoCol = new TableColumn<>("Data Pagamento");
        dataPagamentoCol.setCellValueFactory(new PropertyValueFactory<>("dataPagamento"));

        TableColumn<Solicitacao, String> formaPagamentoCol = new TableColumn<>("Forma Pagamento");
        formaPagamentoCol.setCellValueFactory(new PropertyValueFactory<>("formaPagamento"));

        TableColumn<Solicitacao, Integer> parcelasCol = new TableColumn<>("Parcelas");
        parcelasCol.setCellValueFactory(new PropertyValueFactory<>("parcelas"));

        TableColumn<Solicitacao, Double> valorParcelasCol = new TableColumn<>("Valor Parcelas");
        valorParcelasCol.setCellValueFactory(new PropertyValueFactory<>("valorParcelas"));

        TableColumn<Solicitacao, Double> valorTotalCol = new TableColumn<>("Valor Total");
        valorTotalCol.setCellValueFactory(new PropertyValueFactory<>("valorTotal"));

        table.getColumns().addAll(idCol, fornecedorCol, descricaoCol, dataCriacaoCol, dataPagamentoCol, formaPagamentoCol, parcelasCol, valorParcelasCol, valorTotalCol);

        // Layout Principal
        layout = new VBox(10, topoLayout, table);
        Scene scene = new Scene(layout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

//        Scene scene = new Scene(layout, 400, 300);
//        primaryStage.setScene(scene);
//        primaryStage.show();
    }

    private ObservableList<Solicitacao> getSolicitacoes() {
        SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO(connection);
        List<Solicitacao> solicitacoes = solicitacaoDAO.getTodasSolicitacoes();
        return FXCollections.observableArrayList(solicitacoes);
    }
}
