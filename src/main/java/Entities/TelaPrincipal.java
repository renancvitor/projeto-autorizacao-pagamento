package Entities;

import DAO.SolicitacaoDAO;
import Servicoes.Solicitacao;
import Servicoes.StatusSolicitacao;
import Servicoes.TelaSolicitacao;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class TelaPrincipal {
    private Usuario usuario;
    private Connection connection;
    private TableView<Solicitacao> table;
    private Label totalPendentesLabel;
    private Label totalAprovadasLabel;
    private Label totalRejeitadasLabel;

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
        totalPendentesLabel = new Label();
        totalAprovadasLabel = new Label();
        totalRejeitadasLabel = new Label();
        layout.getChildren().addAll(resumoLabel, totalPendentesLabel, totalAprovadasLabel, totalRejeitadasLabel);

        // Botão para novas solicitações
        Button novaSolicitacaoButton = new Button("Nova Solicitação");
        novaSolicitacaoButton.setOnAction(e -> {
            TelaSolicitacao telaSolicitacao = new TelaSolicitacao(connection, usuario, table);
            Stage stage = new Stage();
            telaSolicitacao.start(stage);
        });
        layout.getChildren().add(novaSolicitacaoButton);

        // Layout para Resumo Rápido e Botão
        HBox topoLayout = new HBox(10, resumoLabel, novaSolicitacaoButton);

        // TableView para exibir as solicitações
        table = new TableView<>();
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

        TableColumn<Solicitacao, Double> valorTotalCol = new TableColumn<>("Valor Total");
        valorTotalCol.setCellValueFactory(new PropertyValueFactory<>("valorTotal"));

        TableColumn<Solicitacao, StatusSolicitacao> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Coluna para Botão de Aprovar
        TableColumn<Solicitacao, Void> approveCol = new TableColumn<>("Aprovar");
        Callback<TableColumn<Solicitacao, Void>, TableCell<Solicitacao, Void>> cellFactoryApprove = new Callback<>() {
            @Override
            public TableCell<Solicitacao, Void> call(final TableColumn<Solicitacao, Void> param) {
                final TableCell<Solicitacao, Void> cell = new TableCell<>() {
                    private final Button approveButton = new Button("✔");

                    {
                        approveButton.setOnAction(event -> {
                            Solicitacao solicitacao = getTableView().getItems().get(getIndex());
                            aprovarSolicitacao(solicitacao);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(approveButton);
                        }
                    }
                };
                return cell;
            }
        };
        approveCol.setCellFactory(cellFactoryApprove);

        // Coluna para Botão de Reprovar
        TableColumn<Solicitacao, Void> rejectCol = new TableColumn<>("Reprovar");
        Callback<TableColumn<Solicitacao, Void>, TableCell<Solicitacao, Void>> cellFactoryReject = new Callback<>() {
            @Override
            public TableCell<Solicitacao, Void> call(final TableColumn<Solicitacao, Void> param) {
                final TableCell<Solicitacao, Void> cell = new TableCell<>() {
                    private final Button rejectButton = new Button("✘");

                    {
                        rejectButton.setOnAction(event -> {
                            Solicitacao solicitacao = getTableView().getItems().get(getIndex());
                            reprovarSolicitacao(solicitacao);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(rejectButton);
                        }
                    }
                };
                return cell;
            }
        };
        rejectCol.setCellFactory(cellFactoryReject);

        table.getColumns().addAll(idCol, fornecedorCol, descricaoCol, dataCriacaoCol, dataPagamentoCol, formaPagamentoCol, valorTotalCol, statusCol, approveCol, rejectCol);

        // Layout Principal
        layout.getChildren().add(topoLayout);
        layout.getChildren().add(table);

        Scene scene = new Scene(layout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Listener para atualizar o resumo rápido
        table.getItems().addListener(new ListChangeListener<Solicitacao>() {
            @Override
            public void onChanged(Change<? extends Solicitacao> c) {
                atualizarResumoRapido();
            }
        });

        // Inicializar o resumo rápido
        atualizarResumoRapido();
    }

    private void atualizarResumoRapido() {
        try {
            SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO(connection);
            int pendentes = solicitacaoDAO.contarSolicitacoesPorStatus(StatusSolicitacao.PENDENTE);
            int aprovadas = solicitacaoDAO.contarSolicitacoesPorStatus(StatusSolicitacao.APROVADA);
            int reprovadas = solicitacaoDAO.contarSolicitacoesPorStatus(StatusSolicitacao.REPROVADA);

            totalPendentesLabel.setText("Pendentes: " + pendentes);
            totalAprovadasLabel.setText("Aprovadas: " + aprovadas);
            totalRejeitadasLabel.setText("Reprovadas: " + reprovadas);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void aprovarSolicitacao(Solicitacao solicitacao) {
        try {
            solicitacao.setStatus(StatusSolicitacao.APROVADA);
            SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO(connection);
            solicitacaoDAO.atualizarSolicitacao(solicitacao);
            refreshTable();
            atualizarResumoRapido();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void reprovarSolicitacao(Solicitacao solicitacao) {
        try {
            solicitacao.setStatus(StatusSolicitacao.REPROVADA);
            SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO(connection);
            solicitacaoDAO.atualizarSolicitacao(solicitacao);
            refreshTable();
            atualizarResumoRapido();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ObservableList<Solicitacao> getSolicitacoes() {
        SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO(connection);
        List<Solicitacao> solicitacoes = solicitacaoDAO.getTodasSolicitacoes();
        return FXCollections.observableArrayList(solicitacoes);
    }

    private void refreshTable() {
        ObservableList<Solicitacao> solicitacoes = getSolicitacoes();
        table.setItems(solicitacoes);
        table.refresh(); // Garantir que a tabela seja atualizada visualmente
        atualizarResumoRapido(); // Atualizar o resumo rápido após a atualização da tabela
    }
}
