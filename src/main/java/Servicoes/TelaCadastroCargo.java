package Servicoes;

import DAO.CargoDAO;
import Entities.Cargo;
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

public class TelaCadastroCargo extends Application {
    private static Connection connection;
    private CargoDAO cargoDAO;
    private TextField nomeField;

    public static void setConnection(Connection conn) {
        connection = conn;
    }

    @Override
    public void start(Stage primaryStage) {
        if (connection == null) {
            throw new IllegalStateException("A conexão com o banco de dados não foi configurada.");
        }

        this.cargoDAO = new CargoDAO(connection);

        primaryStage.setTitle("Cadastro de Cargo");

        VBox layout = new VBox(10);
        Label label = new Label("Preencha os dados do cargo:");
        layout.getChildren().add(label);

        nomeField = new TextField();
        nomeField.setPromptText("Cargo");
        layout.getChildren().add(nomeField);

        Button cadastrarButton = new Button("Cadastrar");
        cadastrarButton.setOnAction(event -> cadastrarCargo());
        layout.getChildren().add(cadastrarButton);

        Scene scene = new Scene(layout, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
    }

    private void cadastrarCargo() {
        String nome = nomeField.getText();

        if (nome.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "O campo de nome não pode estar vazio.");
            alert.show();
            return;
        }

        try {
            Cargo cargo = new Cargo(0, nome);
            cargoDAO.salvarCargo(cargo);

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Cargo criado com sucesso!");
            alert.show();

            nomeField.clear();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao salvar cargo: " + e.getMessage());
            alert.show();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
