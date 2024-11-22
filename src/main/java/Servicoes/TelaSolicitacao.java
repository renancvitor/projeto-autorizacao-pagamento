package Servicoes;

import DAO.SolicitacaoDAO;
import Entities.Usuario;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class TelaSolicitacao {
    private Connection connection;
    private SolicitacaoDAO solicitacaoDAO;

    private TextField fornecedorField;
    private TextField descricaoField;
    private DatePicker dataPagamentoField;
    private TextField formaPagamentoField;
    private TextField valorTotalField;
    private Button submitButton;
    private SolicitacaoService solicitacaoService;
    private PermissaoService permissaoService;

    private Usuario usuarioLogado;
    private TableView<Solicitacao> tabelaSolicitacoes;

    public TelaSolicitacao(Connection connection, Usuario usuarioLogado, TableView<Solicitacao> tabelaSolicitacoes) {
        this.connection = connection;
        this.usuarioLogado = usuarioLogado;
        this.solicitacaoDAO = new SolicitacaoDAO(connection);
        this.tabelaSolicitacoes = tabelaSolicitacoes;
        this.permissaoService = new PermissaoService();
        this.solicitacaoService = new SolicitacaoService(solicitacaoDAO, permissaoService);
    }

    public void start(Stage stage) {
        stage.setTitle("Nova Solicitação");

        VBox layout = new VBox(10);
        Label label = new Label("Preencha os dados da solicitação:");
        layout.getChildren().add(label);

        fornecedorField = new TextField();
        fornecedorField.setPromptText("Fornecedor");
        layout.getChildren().add(fornecedorField);

        descricaoField = new TextField();
        descricaoField.setPromptText("Descrição");
        layout.getChildren().add(descricaoField);

        dataPagamentoField = new DatePicker();
        dataPagamentoField.setPromptText("Data de Pagamento");
        layout.getChildren().add(dataPagamentoField);

        formaPagamentoField = new TextField();
        formaPagamentoField.setPromptText("Forma de Pagamento");
        layout.getChildren().add(formaPagamentoField);

        valorTotalField = new TextField();
        valorTotalField.setPromptText("Valor Total");
        layout.getChildren().add(valorTotalField);

        submitButton = new Button("Enviar Solicitação");
        submitButton.setOnAction(e -> enviarSolicitacao());
        layout.getChildren().add(submitButton);

        Scene scene = new Scene(layout, 400, 400);
        stage.setScene(scene);
        stage.show();

        scene.getStylesheets().add(getClass().getResource("/calendario.css").toExternalForm());
    }

    private void enviarSolicitacao() {
        try {
            String fornecedor = fornecedorField.getText();
            String descricao = descricaoField.getText();
            Date dataPagamento = Date.valueOf(dataPagamentoField.getValue());
            String formaPagamento = formaPagamentoField.getText();
            double valorTotal = Double.parseDouble(valorTotalField.getText());
            int idUsuario = usuarioLogado.getId();

            StatusSolicitacao status = StatusSolicitacao.PENDENTE;

            Solicitacao solicitacao = new Solicitacao(
                    0,
                    fornecedor,
                    descricao,
                    new java.sql.Timestamp(System.currentTimeMillis()),
                    dataPagamento,
                    formaPagamento,
                    valorTotal,
                    idUsuario,
                    status
            );

            solicitacaoDAO.inserirSolicitacao(solicitacao);

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Solicitação enviada com sucesso.");
            alert.show();

            List<Solicitacao> solicitacoes = solicitacaoService.getSolicitacoesVisiveisParaUsuario(usuarioLogado);
            ObservableList<Solicitacao> observableListSolicitacoes = FXCollections.observableArrayList(solicitacoes);
            tabelaSolicitacoes.setItems(observableListSolicitacoes);

        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
