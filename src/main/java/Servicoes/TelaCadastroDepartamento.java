package Servicoes;

import DAO.DepartamentoDAO;
import Entities.Departamento;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;

public class TelaCadastroDepartamento extends Application {
    private static Connection connection;
    private DepartamentoDAO departamentoDAO;
    private TextField nomeField;

    public static void setConnection(Connection conn) {
        connection = conn;
    }

//    public TelaCadastroDepartamento(Connection connection) {
//        this.connection = connection;

    @Override
    public void start(Stage primaryStage) {
        if (connection == null) {
            throw new IllegalStateException("A conexão com o banco de dados não foi configurada.");
        }

        this.departamentoDAO = new DepartamentoDAO(connection);

        primaryStage.setTitle("Cadastro de Departamento");

        VBox layout = new VBox(10);
        Label label = new Label("Preencha os dados do departamento:");
        layout.getChildren().add(label);

        nomeField = new TextField();
        nomeField.setPromptText("Departamento");
        layout.getChildren().add(nomeField);

        Button cadastrarButton = new Button("Cadastrar");
        cadastrarButton.setOnAction(event -> cadastrarDepartamento());
        layout.getChildren().add(cadastrarButton);

        Scene scene = new Scene(layout, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void cadastrarDepartamento() {
        String nome = nomeField.getText();

        if (nome.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "O campo de nome não pode estar vazio.");
            alert.show();
            return;
        }

        try {
            Departamento departamento = new Departamento(0, nome);

            departamentoDAO.salvarDepartamento(departamento);
            System.out.println("Departamento criado com sucesso");

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Departamento criado com sucesso!");
            alert.show();

            nomeField.clear();

        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao salvar o departamento: " + e.getMessage());
            alert.show();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
