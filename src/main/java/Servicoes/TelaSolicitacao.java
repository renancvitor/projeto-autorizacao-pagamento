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
    private Connection connection;
    private SolicitacaoDAO solicitacaoDAO;

    private TextField fornecedorField;
    private TextField descricaoField;
    private DatePicker dataPagamentoField;
    private TextField formaPagamentoField;
    private TextField parcelasField;
    private TextField valorParcelasField;
    private TextField valorTotalField;
    private Button submitButton;

    private Usuario usuarioLogado;

    public TelaSolicitacao(Connection connection, Usuario usuarioLogado) {
        this.connection = connection;
        this.usuarioLogado = usuarioLogado;
        this.solicitacaoDAO = new SolicitacaoDAO(connection);
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

        parcelasField = new TextField();
        parcelasField.setPromptText("Parcelas");
        layout.getChildren().add(parcelasField);

        valorParcelasField = new TextField();
        valorParcelasField.setPromptText("Valor das Parcelas");
        layout.getChildren().add(valorParcelasField);

        valorTotalField = new TextField();
        valorTotalField.setPromptText("Valor Total");
        layout.getChildren().add(valorTotalField);

        submitButton = new Button("Enviar Solicitação");
        submitButton.setOnAction(e -> enviarSolicitacao());
        layout.getChildren().add(submitButton);

        Scene scene = new Scene(layout, 400, 400);
        stage.setScene(scene);
        stage.show();
    }

    private void enviarSolicitacao() {
        try {
            String fornecedor = fornecedorField.getText();
            String descricao = descricaoField.getText();
            Date dataPagamento = Date.valueOf(dataPagamentoField.getValue());
            String formaPagamento = formaPagamentoField.getText();
            int parcelas = Integer.parseInt(parcelasField.getText());
            double valorParcelas = Double.parseDouble(valorParcelasField.getText());
            double valorTotal = Double.parseDouble(valorTotalField.getText());

            // Criação do objeto Solicitacao
            // Note que a data de criação está sendo definida automaticamente como o timestamp atual
            Solicitacao solicitacao = new Solicitacao(0, fornecedor, descricao, new java.sql.Timestamp(System.currentTimeMillis()), dataPagamento, formaPagamento, parcelas, valorParcelas, valorTotal, usuarioLogado.getId());

            // Inserção da solicitação no banco de dados através do DAO
            solicitacaoDAO.inserirSolicitacao(solicitacao);

            System.out.println("Solicitação enviada por: " + usuarioLogado.getLogin());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            // Trate aqui erros de conversão de dados (por exemplo, se um campo numérico não puder ser convertido corretamente)
        } catch (SQLException e) {
            e.printStackTrace();
            // Trate aqui erros relacionados ao banco de dados
        }
    }
}
