package Servicoes;

import Application.MainApp;
import DAO.CargoDAO;
import DAO.DepartamentoDAO;
import DAO.PessoaDAO;
import Entities.Cargo;
import Entities.Departamento;
import Entities.Pessoa;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class TelaCadastroPessoa {
    private Connection connection;

    private ComboBox<Departamento> departamentoComboBox;
    private ComboBox<Cargo> cargoComboBox;

    public TelaCadastroPessoa(Connection connection) {
        this.connection = connection;
    }

    private static final String DATE_FORMAT = "dd/MM/yyyy";

    public void mostrarTela(Stage primaryStage) {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 10;");

        Label nomeLabel = new Label("Nome:");
        TextField nomeField = new TextField();

        Label dataNascimentoLabel = new Label("Data de Nascimento: (dd/MM/yyyy):");
        TextField dataNascimentoField = new TextField();
        dataNascimentoField.setPromptText("dd/MM/yyyy");

        Label cpfLabel = new Label("CPF:");
        TextField cpfField = new TextField();
        cpfField.setPromptText("000.000.000-00");

        departamentoComboBox = new ComboBox<>();
        cargoComboBox = new ComboBox<>();
        departamentoComboBox.setPromptText("Selecionar Departamento");
        cargoComboBox.setPromptText("Selecionar Cargo");

        carregarDepartamentos();
        carregarCargos();

        dataNascimentoField.textProperty().addListener((observableValue, oldValue, newValue) -> dataNascimentoField.setText(formatInputAsDate(newValue)));

        cpfField.textProperty().addListener((observableValue, oldValue, newValue) -> cpfField.setText(formatInputAsCPF(newValue)));

        Button salvarButton = new Button("Salvar");
        salvarButton.setOnAction(event -> {
            String nome = nomeField.getText();
            LocalDate dataNascimento = parseDate(dataNascimentoField.getText());
            String cpf = cpfField.getText();

            Departamento departamentoSelecionado = departamentoComboBox.getValue();
            Cargo cargoSelecionado = cargoComboBox.getValue();

            if (dataNascimento != null && isCPFValid(cpf)) {
                Pessoa pessoa = new Pessoa();
                pessoa.setNome(nome);
                pessoa.setDatanascimento(dataNascimento);
                pessoa.setDepartamento(departamentoSelecionado);
                pessoa.setCargo(cargoSelecionado);
                pessoa.setCpf(cpf);

                try {
                    PessoaDAO pessoaDAO = new PessoaDAO(connection);
                    pessoaDAO.salvarPessoa(pessoa);

                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Pessoa salva com sucesso!");
                    alert.show();

                } catch (SQLException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao salvar pessoa: " + e.getMessage());
                    alert.show();
                }
            } else {
                System.out.println("Data de nascimento ou CPF inválidos.");
            }
        });

        layout.getChildren().addAll(
                nomeLabel, nomeField,
                dataNascimentoLabel, dataNascimentoField,
                cpfLabel, cpfField,
                departamentoComboBox, cargoComboBox,
                salvarButton
        );

        Scene scene = new Scene(layout, 400, 350);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Cadastro de Pessoa");
        primaryStage.show();

        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
    }

    private void carregarDepartamentos() {
        DepartamentoDAO departamentoDAO = new DepartamentoDAO(connection);
        List<Departamento> departamentos = departamentoDAO.buscarTodosDepartamentos();
        departamentoComboBox.getItems().addAll(departamentos);
    }

    private void carregarCargos() {
        CargoDAO cargoDAO = new CargoDAO(connection);
        List<Cargo> cargos = cargoDAO.buscarTodosCargos();
        cargoComboBox.getItems().addAll(cargos);
    }

    private String formatInputAsDate(String input) {
        String digitsOnly = input.replaceAll("[^\\d]", "");

        if (digitsOnly.length() >= 2 && digitsOnly.length() <= 4) {
            return digitsOnly.substring(0, 2) + "/" + digitsOnly.substring(2);
        } else if (digitsOnly.length() > 4) {
            return digitsOnly.substring(0, 2) + "/" + digitsOnly.substring(2, 4) + "/" + digitsOnly.substring(4);
        }
        return digitsOnly;
    }

    private String formatInputAsCPF(String input) {
        String digitsOnly = input.replaceAll("[^\\d]", "");

        if (digitsOnly.length() > 9) {
            return digitsOnly.substring(0, 3) + "." + digitsOnly.substring(3, 6) + "." + digitsOnly.substring(6, 9) + "-" + digitsOnly.substring(9);
        } else if (digitsOnly.length() > 6) {
            return digitsOnly.substring(0, 3) + "." + digitsOnly.substring(3, 6) + "." + digitsOnly.substring(6);
        } else if (digitsOnly.length() > 3) {
            return digitsOnly.substring(0, 3) + "." + digitsOnly.substring(3);
        }
        return digitsOnly;
    }

    public boolean isCPFValid(String cpf) {
        String digitsOnly = cpf.replaceAll("[^\\d]", "");

        return digitsOnly.length() == 11;
    }

    private LocalDate parseDate(String dataStr) {
        DateTimeFormatter formater = DateTimeFormatter.ofPattern(DATE_FORMAT); // Já definido início código
        try {
            return LocalDate.parse(dataStr, formater);
        } catch (DateTimeParseException e) {
            return null; // Caso a data não possa ser analisada
        }
    }
}
