package Servicoes;

import DAO.DepartamentoDAO;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;

public class TelaCadastroDepartamento extends Application {
    private Connection connection;
    private DepartamentoDAO departamentoDAO;

    private TextField nomeField;

    public TelaCadastroDepartamento(Connection connection) {
        this.connection = connection;
        this.departamentoDAO = new DepartamentoDAO(connection);
    }

    @Override
    public void start(Stage primaryStage) {
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

        // Lógica para cadastrar o departamento
        // departamentoDAO.inserir(new Departamento(nome));

        // Limpar o campo após cadastro
        nomeField.clear();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
