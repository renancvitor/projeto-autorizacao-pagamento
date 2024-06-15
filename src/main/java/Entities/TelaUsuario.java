package Entities;

import Servicoes.Solicitacao;
import Servicoes.StatusSolicitacao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

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

        // Formulário de solicitação
        Label descricaoLabel = new Label("Descrição:");
        grid.add(descricaoLabel, 0, 0);

        TextArea descricaoField = new TextArea();
        grid.add(descricaoField, 1, 0);

        Label valorLabel = new Label("Valor:");
        grid.add(valorLabel, 0, 1);

        TextField valorField = new TextField();
        grid.add(valorField, 1, 1);

        Label dataLabel = new Label("Data:");
        grid.add(dataLabel, 0, 2);

        DatePicker datePicker = new DatePicker();
        grid.add(datePicker, 1, 2);

        Label tipoLabel = new Label("Tipo:");
        grid.add(tipoLabel, 0, 3);

        ComboBox<String> tipoComboBox = new ComboBox<>();
        tipoComboBox.getItems().addAll("Alimentação", "Transporte", "Hospedagem");
        grid.add(tipoComboBox, 1, 3);

        Button enviarButton = new Button("Enviar Solicitação");
        grid.add(enviarButton, 1, 4);

        enviarButton.setOnAction(e -> {
            String descricao = descricaoField.getText();
            double valor = Double.parseDouble(valorField.getText());
            String data = datePicker.getValue().toString();
            String tipo = tipoComboBox.getValue();

            // Criar nova solicitação
            Solicitacao solicitacao = new Solicitacao(usuario, valor, descricao, StatusSolicitacao.PENDENTE, new Date(), data, tipo);

            // Adicionar solicitação ao banco de dados
            adicionarSolicitacao(solicitacao);

            // Mostrar mensagem de confirmação
            System.out.println("Solicitação enviada com sucesso!");

            // Limpar campos
            descricaoField.clear();
            valorField.clear();
            datePicker.setValue(null);
            tipoComboBox.setValue(null);

            // Atualizar tabela de histórico
            tableView.setItems(getSolicitacoesUsuario());
        });

        // Histórico de solicitações
        TableView<Solicitacao> tableView = new TableView<>();
        TableColumn<Solicitacao, String> colunaDescricao = new TableColumn<>("Descrição");
        colunaDescricao.setCellValueFactory(cellData -> cellData.getValue().descricaoProperty());

        TableColumn<Solicitacao, String> colunaValor = new TableColumn<>("Valor");
        colunaValor.setCellValueFactory(cellData -> cellData.getValue().valorProperty().asString());

        TableColumn<Solicitacao, String> colunaData = new TableColumn<>("Data");
        colunaData.setCellValueFactory(cellData -> cellData.getValue().dataProperty());

        TableColumn<Solicitacao, String> colunaTipo = new TableColumn<>("Tipo");
        colunaTipo.setCellValueFactory(cellData -> cellData.getValue().tipoProperty());

        TableColumn<Solicitacao, String> colunaStatus = new TableColumn<>("Status");
        colunaStatus.setCellValueFactory(cellData -> cellData.getValue().statusProperty().asString());

        tableView.getColumns().addAll(colunaDescricao, colunaValor, colunaData, colunaTipo, colunaStatus);
        tableView.setItems(getSolicitacoesUsuario());

        grid.add(tableView, 0, 5, 2, 1);

        Scene scene = new Scene(grid, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void adicionarSolicitacao(Solicitacao solicitacao) {
        String sql = "INSERT INTO solicitacoes(usuario_id, descricao, valor, data, tipo, status) VALUES(?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, usuario.getId()); // Supondo que você tenha um método getId() em Usuario
            pstmt.setString(2, solicitacao.getDescricao());
            pstmt.setDouble(3, solicitacao.getValor());
            pstmt.setString(4, solicitacao.getData());
            pstmt.setString(5, solicitacao.getTipo());
            pstmt.setString(6, solicitacao.getStatus().toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private ObservableList<Solicitacao> getSolicitacoesUsuario() {
        ObservableList<Solicitacao> solicitacoes = FXCollections.observableArrayList();
        String sql = "SELECT * FROM solicitacoes WHERE usuario_id = ?";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, usuario.getId()); // Supondo que você tenha um método getId() em Usuario
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Solicitacao solicitacao = new Solicitacao(
                        usuario,
                        rs.getDouble("valor"),
                        rs.getString("descricao"),
                        StatusSolicitacao.valueOf(rs.getString("status")),
                        new Date(), // Aqui você pode precisar converter a data de String para Date
                        rs.getString("data"),
                        rs.getString("tipo")
                );
                solicitacoes.add(solicitacao);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return solicitacoes;
    }
}
