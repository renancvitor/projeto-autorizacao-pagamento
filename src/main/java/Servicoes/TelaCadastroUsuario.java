package Servicoes;

import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DAO.UsuarioDAO;
import Entities.Usuario;

public class TelaCadastroUsuario {

    private Connection connection;

    public TelaCadastroUsuario(Connection connection) {
        this.connection = connection;
    }

    public void start(Stage stage) {
        VBox layout = new VBox(10);
        layout.getChildren().add(new Label("Cadastro de Novo Usuário"));

        TextField loginField = new TextField();
        loginField.setPromptText("Login");

        TextField senhaField = new TextField();
        senhaField.setPromptText("Senha");

        TextField cpfField = new TextField();
        cpfField.setPromptText("CPF");

        // Evento para formatar o CPF enquanto o usuário digita
        cpfField.textProperty().addListener((observable, oldValue, newValue) -> {
            String formattedCpf = formatInputAsCPF(newValue);
            if (!newValue.equals(formattedCpf)) {
                cpfField.setText(formattedCpf);
                cpfField.positionCaret(formattedCpf.length());  // Posiciona o cursor no final
            }
        });

        // ComboBox para seleção de permissões
        ComboBox<String> permissaoComboBox = new ComboBox<>();
        permissaoComboBox.getItems().addAll("ADMIN", "User", "Manager"); // Exemplos de permissões
        permissaoComboBox.setPromptText("Selecione a Permissão");

        // Botão para salvar
        Button saveButton = new Button("Salvar");

        // Evento para salvar os dados ao clicar no botão
        saveButton.setOnAction(event -> {
            String login = loginField.getText();
            String senha = senhaField.getText();
            String cpf = cpfField.getText();
            String permissoes = permissaoComboBox.getValue();

            // Validar campos
            if (login.isEmpty() || senha.isEmpty() || cpf.isEmpty() || permissoes == null) {
                // Adicionar lógica para mostrar mensagem de erro se necessário
                Alert alert = new Alert(Alert.AlertType.WARNING, "Preencha todos os campos!");
                alert.show();
            } else {
                try {
                    UsuarioDAO usuarioDAO = new UsuarioDAO(connection);

                    // Criar uma lista com a permissão selecionada
                    List<String> permissoesList = new ArrayList<>();
                    permissoesList.add(permissoes);  // Adiciona a permissão selecionada

                    // Passar a lista de permissões para o construtor
                    Usuario novoUsuario = new Usuario(0, login, senha, permissoesList, cpf);
                    usuarioDAO.inserirUsuario(novoUsuario);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Usuário cadastrado com sucesso!");
                    alert.show();

                    // Fechar a janela após o cadastro
                    stage.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    // Adicionar lógica para exibir mensagem de erro de SQL, se necessário
                }
            }
        });

        layout.getChildren().addAll(loginField, senhaField, cpfField, permissaoComboBox, saveButton);

        stage.setScene(new javafx.scene.Scene(layout, 300, 250));  // Define o tamanho da tela
        stage.setTitle("Cadastro de Usuário");
        stage.show();
    }

    // Método para formatar o CPF
    private String formatInputAsCPF(String input) {
        // Limpa caracteres não numéricos
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
    public boolean isCPFValid(String cpf) {
        // Remoção caracteres especiais para validação
        String digitsOnly = cpf.replaceAll("[^\\d]", "");

        // Valida se CPF tem 11 digitos
        return digitsOnly.length() == 11;
    }
}
