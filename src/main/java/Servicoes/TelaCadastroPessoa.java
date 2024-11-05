package Servicoes;

import Application.MainApp;
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

public class TelaCadastroPessoa {
    private Connection connection;

    public TelaCadastroPessoa(Connection connection) {
        this.connection = connection;
    }
    // Definindo formato padrão data
    private static final String DATE_FORMAT = "dd/MM/yyyy";

    public void mostrarTela(Stage primaryStage) {
        VBox layout = new VBox(10); // layout espaçamento vertical 10px
        layout.setStyle("-fx-padding: 10;"); // Padding 10 para todos os lados

        // Campo Nome
        Label nomeLabel = new Label("Nome:");
        TextField nomeField = new TextField();

        // Campo Data Nascimento
        Label dataNascimentoLabel = new Label("Data de Nascimento: (dd/MM/yyyy):");
        TextField dataNascimentoField = new TextField();
        dataNascimentoField.setPromptText("dd/MM/yyyy");

        // Campo CPF
        Label cpfLabel = new Label("CPF:");
        TextField cpfField = new TextField();
        cpfField.setPromptText("000.000.000-00");

        // Campo Departamento
        Label departamentoLabel = new Label("Departamento:");
        TextField departamentoField = new TextField();
        departamentoField.setPromptText("");

        // Campo Cargo
        Label cargoLabel = new Label("Cargo:");
        TextField cargoField = new TextField();
        cargoField.setPromptText("");

        // Máscara data
        dataNascimentoField.textProperty().addListener((observableValue, oldValue, newValue) -> dataNascimentoField.setText(formatInputAsDate(newValue)));

        // Máscara CPF
        cpfField.textProperty().addListener((observableValue, oldValue, newValue) -> cpfField.setText(formatInputAsCPF(newValue)));

        // Botão salvar
        Button salvarButton = new Button("Salvar");
        salvarButton.setOnAction(event -> {
            String nome = nomeField.getText();
            LocalDate dataNascimento = parseDate(dataNascimentoField.getText());
            String cpf = cpfField.getText();

            if (dataNascimento != null && isCPFValid(cpf)) {
                // Cria o objeto Pessoa com os dados do formulário
                Pessoa pessoa = new Pessoa();
                pessoa.setNome(nome);
                pessoa.setDatanascimento(dataNascimento);
                pessoa.setCpf(cpf);

                try {
                    // Salva no banco de dados
                    PessoaDAO pessoaDAO = new PessoaDAO(connection); // Supondo que 'connection' está disponível aqui
                    pessoaDAO.salvarPessoa(pessoa);
                    System.out.println("Pessoa salva com sucesso!");
                } catch (SQLException e) {
                    System.err.println("Erro ao salvar pessoa: " + e.getMessage());
                }
            } else {
                System.out.println("Data de nascimento ou CPF inválido.");
            }
        });

        // Componentes layout
        layout.getChildren().addAll(nomeLabel, nomeField, dataNascimentoLabel, dataNascimentoField, cpfLabel, cpfField, salvarButton);

        // Confiração cena e palco
        Scene scene = new Scene(layout, 300, 250);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Cadastro de Pessoa");
        primaryStage.show();
    }

    // Método máscara data
    private String formatInputAsDate(String input) {
        // Clean caracteres não numéricos
        String digitsOnly = input.replaceAll("[^\\d]", "");

        // Formata inserindo as barras
        if (digitsOnly.length() >= 2 && digitsOnly.length() <= 4) {
            return digitsOnly.substring(0, 2) + "/" + digitsOnly.substring(2);
        } else if (digitsOnly.length() > 4) {
            return digitsOnly.substring(0, 2) + "/" + digitsOnly.substring(2, 4) + "/" + digitsOnly.substring(4);
        }
        return digitsOnly;
    }

    // Método máscara CPF
    private String formatInputAsCPF(String input) {
        // Clean caracteres não numéricos
        String digitsOnly = input.replaceAll("[^\\d]", "");

        // Formata inserindo pontos e traço
        if (digitsOnly.length() > 9) {
            return digitsOnly.substring(0, 3) + "." + digitsOnly.substring(3, 6) + "." + digitsOnly.substring(6, 9) + "-" + digitsOnly.substring(9);
        } else if (digitsOnly.length() > 6) {
            return digitsOnly.substring(0, 3) + "." + digitsOnly.substring(3, 6) + "." + digitsOnly.substring(6);
        } else if (digitsOnly.length() > 3) {
            return digitsOnly.substring(0, 3) + "." + digitsOnly.substring(3);
        }
        return digitsOnly;
    }

    // Valida CPF
    private boolean isCPFValid(String cpf) {
        // Remoção caracteres especiais para validação
        String digitsOnly = cpf.replaceAll("[^\\d]", "");

        // Valida se CPF tem 11 digitos
        return digitsOnly.length() == 11;
    }

    // Método para converter string para LocalDate
    private LocalDate parseDate(String dataStr) {
        DateTimeFormatter formater = DateTimeFormatter.ofPattern(DATE_FORMAT); // Já definido início código
        try {
            return LocalDate.parse(dataStr, formater);
        } catch (DateTimeParseException e) {
            return null; // Caso a data não possa ser analisada
        }
    }

}
