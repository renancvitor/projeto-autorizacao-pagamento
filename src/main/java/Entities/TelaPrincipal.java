package Entities;

import DAO.SolicitacaoDAO;
import Servicoes.Solicitacao;
import Servicoes.SolicitacaoService;
import Servicoes.StatusSolicitacao;
import Servicoes.TelaSolicitacao;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.util.Duration;

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
    private SolicitacaoService solicitacaoService;
    private ObservableList<Solicitacao> observableList;

    public TelaPrincipal(Usuario usuario) {
        this.usuario = usuario;
        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sistema_pagamentos", "root", "123456789");
            SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO(connection);
            this.solicitacaoService = new SolicitacaoService(solicitacaoDAO);
            this.observableList = FXCollections.observableArrayList(); // Inicializa a ObservableList aqui
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
        atualizarResumoRapido();
        layout.getChildren().addAll(resumoLabel, totalPendentesLabel, totalAprovadasLabel, totalRejeitadasLabel);

        // Botão para novas solicitações
        Button novaSolicitacaoButton = new Button("Nova Solicitação");
        novaSolicitacaoButton.setOnAction(e -> {
            TelaSolicitacao telaSolicitacao = new TelaSolicitacao(connection, usuario, table);
            Stage stage = new Stage();
            telaSolicitacao.start(stage);
        });
        layout.getChildren().add(novaSolicitacaoButton);

        // Botão para acessar a tela de solicitações analisadas
        Button analisadosButton = new Button("Solicitações Analisadas");
        analisadosButton.setOnAction(e -> {
            TelaAnalisados telaAnalisados = new TelaAnalisados(connection);
            Stage stage = new Stage();
            telaAnalisados.start(stage);
        });
        layout.getChildren().add(analisadosButton);

        // Layout para Resumo Rápido e Botão
        HBox topoLayout = new HBox(10, resumoLabel, novaSolicitacaoButton, analisadosButton);

        // TableView para exibir as solicitações
        table = new TableView<>();
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

        // Coluna de Status
        TableColumn<Solicitacao, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(cellData -> {
            StatusSolicitacao status = cellData.getValue().getStatus();
            String statusStr = status == StatusSolicitacao.PENDENTE ? "PENDENTE" : status.toString();
            return new SimpleStringProperty(statusStr);
        });

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

        // Atualização periódica da tabela
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> refreshTable()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        // Carrega as solicitações pendentes na tabela
        refreshTable();
    }

    private void atualizarResumoRapido() {
        try {
            SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO(connection);
            int pendentes = solicitacaoDAO.contarSolicitacoesPorStatus(StatusSolicitacao.PENDENTE);
            int aprovadas = solicitacaoDAO.contarSolicitacoesPorStatus(StatusSolicitacao.APROVADA);
            int reprovadas = solicitacaoDAO.contarSolicitacoesPorStatus(StatusSolicitacao.REPROVADA);

            totalPendentesLabel.setText("Total Pendentes: " + pendentes);
            totalAprovadasLabel.setText("Total Aprovadas: " + aprovadas);
            totalRejeitadasLabel.setText("Total Reprovadas: " + reprovadas);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void refreshTable() {
        System.out.println("Refreshing table...");
        try {
            List<Solicitacao> solicitacoesPendentes = solicitacaoService.getSolicitacoesPorStatus(StatusSolicitacao.PENDENTE);
            Platform.runLater(() -> {
                observableList.setAll(solicitacoesPendentes);
                table.setItems(observableList);
                atualizarResumoRapido(); // Atualiza o resumo rápido sempre que a tabela for atualizada
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void aprovarSolicitacao(Solicitacao solicitacao) {
        solicitacaoService.atualizarStatusSolicitacao(solicitacao, StatusSolicitacao.APROVADA);
        refreshTable();
    }

    private void reprovarSolicitacao(Solicitacao solicitacao) {
        solicitacaoService.atualizarStatusSolicitacao(solicitacao, StatusSolicitacao.REPROVADA);
        refreshTable();
    }
}
