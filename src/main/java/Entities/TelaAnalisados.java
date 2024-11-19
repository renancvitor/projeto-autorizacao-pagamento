package Entities;

import DAO.SolicitacaoDAO;
import Servicoes.Solicitacao;
import Servicoes.StatusSolicitacao;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class TelaAnalisados {
    private Connection connection;
    private TableView<Solicitacao> table;

    public TelaAnalisados(Connection connection) {
        this.connection = connection;
    }

    public void start(Stage stage) {
        stage.setTitle("Solicitações Analisadas");

        VBox layout = new VBox();

        // TableView para exibir as solicitações
        table = new TableView<>();
        table.setItems(getSolicitacoesAnalisadas());

        TableColumn<Solicitacao, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(50);

        TableColumn<Solicitacao, String> fornecedorCol = new TableColumn<>("Fornecedor");
        fornecedorCol.setCellValueFactory(new PropertyValueFactory<>("fornecedor"));
        fornecedorCol.setPrefWidth(150);
        fornecedorCol.setCellFactory(column -> new TableCell<Solicitacao, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setTooltip(null);
                } else {
                    setText(item);
                    Tooltip tooltip = new Tooltip("Fornecedor: " + item);
                    setTooltip(tooltip);
                }
            }
        });

        TableColumn<Solicitacao, String> descricaoCol = new TableColumn<>("Descrição");
        descricaoCol.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        descricaoCol.setPrefWidth(215);
        descricaoCol.setCellFactory(tc -> new TableCell<>() {
            private final Label label = new Label();
            private final Tooltip tooltip = new Tooltip();

            {
                label.setWrapText(true);
                label.setStyle("-fx-font-size: 12px; -fx-text-fill: #ffffff;");
                label.setMaxWidth(Double.MAX_VALUE);
                tooltip.setWrapText(true);
                tooltip.setStyle("-fx-font-size: 12px;");

                // Associar o Tooltip ao Label
                Tooltip.install(label, tooltip);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    label.setText(item);
                    tooltip.setText(item);
                    setGraphic(label);
                }
            }
        });

//        TableColumn<Solicitacao, String> dataCriacaoCol = new TableColumn<>("Data Criação");
//        dataCriacaoCol.setCellValueFactory(new PropertyValueFactory<>("dataCriacao"));
//        dataCriacaoCol.setPrefWidth(150);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        TableColumn<Solicitacao, LocalDate> dataCriacaoCol = new TableColumn<>("Data de Criação");
        dataCriacaoCol.setCellValueFactory(new PropertyValueFactory<>("dataCriacao"));
        dataCriacaoCol.setPrefWidth(150);

        dataCriacaoCol.setCellValueFactory(cellData -> {
            Timestamp timestamp = cellData.getValue().getDataCriacao();
            if (timestamp != null) {
                return new SimpleObjectProperty<>(timestamp.toLocalDateTime().toLocalDate());
            }
            return new SimpleObjectProperty<>(null);
        });

        dataCriacaoCol.setCellFactory(column -> new TableCell<Solicitacao, LocalDate>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.format(formatter));
                }
            }
        });

//        TableColumn<Solicitacao, String> dataPagamentoCol = new TableColumn<>("Data Pagamento");
//        dataPagamentoCol.setCellValueFactory(new PropertyValueFactory<>("dataPagamento"));
//        dataPagamentoCol.setPrefWidth(120);

        TableColumn<Solicitacao, LocalDate> dataPagamentoCol = new TableColumn<>("Data Pagamento");
        dataPagamentoCol.setCellValueFactory(new PropertyValueFactory<>("dataPagamento"));
        dataPagamentoCol.setPrefWidth(120);

        dataPagamentoCol.setCellValueFactory(cellData -> {
            java.sql.Date sqlDate  = cellData.getValue().getDataPagamento();
            if (sqlDate  != null) {
                return new SimpleObjectProperty<>(sqlDate .toLocalDate());
            }
            return new SimpleObjectProperty<>(null);
        });

        dataPagamentoCol.setCellFactory(column -> new TableCell<Solicitacao, LocalDate>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.format(formatter));
                }
            }
        });

        TableColumn<Solicitacao, String> formaPagamentoCol = new TableColumn<>("Forma Pagamento");
        formaPagamentoCol.setCellValueFactory(new PropertyValueFactory<>("formaPagamento"));
        formaPagamentoCol.setPrefWidth(140);

        TableColumn<Solicitacao, Double> valorTotalCol = new TableColumn<>("Valor Total");
        valorTotalCol.setCellValueFactory(new PropertyValueFactory<>("valorTotal"));
        valorTotalCol.setPrefWidth(100);
        valorTotalCol.setCellFactory(new Callback<TableColumn<Solicitacao, Double>, TableCell<Solicitacao, Double>>() {
            @Override
            public TableCell<Solicitacao, Double> call(TableColumn<Solicitacao, Double> param) {
                return new TableCell<Solicitacao, Double>() {
                    @Override
                    protected void updateItem(Double item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            // Formatar o valor como moeda
                            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
                            setText(currencyFormat.format(item));
                        }
                    }
                };
            }
        });

        TableColumn<Solicitacao, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(80);

        table.getColumns().addAll(idCol, fornecedorCol, descricaoCol, dataCriacaoCol, dataPagamentoCol, formaPagamentoCol, valorTotalCol, statusCol);

        layout.getChildren().add(table);

        Scene scene = new Scene(layout, 1086, 600);
        stage.setScene(scene);
        stage.show();

        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
    }

    private ObservableList<Solicitacao> getSolicitacoesAnalisadas() {
        SolicitacaoDAO solicitacaoDAO = new SolicitacaoDAO(connection);
        List<Solicitacao> solicitacoes = solicitacaoDAO.getSolicitacoesAnalisadas();
        return FXCollections.observableArrayList(solicitacoes);
    }
}
