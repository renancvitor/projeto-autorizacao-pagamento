package Servicoes;

import DAO.UserPermissionsDAO;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import DAO.UsuarioDAO;
import Entities.Usuario;

public class TelaCadastroUsuario {

    private Connection connection;
    private ComboBox<String> tipoUsuarioComboBox;

    public TelaCadastroUsuario(Connection connection) {
        this.connection = connection;
        this.tipoUsuarioComboBox = new ComboBox<>();
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

        // Carregar os tipos de usuários no ComboBox
        carregarTiposUsuario();

        // Botão para salvar
        Button saveButton = new Button("Salvar");

        // Evento para salvar os dados ao clicar no botão
        saveButton.setOnAction(event -> {
            String login = loginField.getText();
            String senha = senhaField.getText();
            String cpf = cpfField.getText();
            String tipoUsuarioSelecionado = tipoUsuarioComboBox.getValue();

            // Validar campos
            if (login.isEmpty() || senha.isEmpty() || cpf.isEmpty() || tipoUsuarioSelecionado == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Preencha todos os campos!");
                alert.show();
            } else {
                try {
                    UsuarioDAO usuarioDAO = new UsuarioDAO(connection);

                    UserPermissionsDAO permissionsDAO = new UserPermissionsDAO(connection);

                    // Obtenha o idTipoUsuario com base no tipo selecionado
                    // int idTipoUsuario = getIdTipoUsuarioByNome(tipoUsuarioSelecionado);
                    int idTipoUsuario = permissionsDAO.getIdTipoUsuario(tipoUsuarioSelecionado);

                    // Obtenha as permissões de acordo com o tipo de usuário selecionado
                    // UserPermissionsDAO permissionsDAO = new UserPermissionsDAO(connection);
                    List<String> permissoes = permissionsDAO.getPermissoesByTipo(tipoUsuarioSelecionado);

                    // Cria o usuário com o tipo selecionado e as permissões
                    Usuario novoUsuario = new Usuario(0, login, senha, permissoes, cpf, idTipoUsuario);
                    usuarioDAO.inserirUsuario(novoUsuario);

                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Usuário cadastrado com sucesso!");
                    alert.show();
                    stage.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        layout.getChildren().addAll(loginField, senhaField, cpfField, tipoUsuarioComboBox, saveButton);

        stage.setScene(new javafx.scene.Scene(layout, 300, 250));  // Define o tamanho da tela
        stage.setTitle("Cadastro de Usuário");
        stage.show();
    }

    // Método para carregar os tipos de usuário no ComboBox
    private void carregarTiposUsuario() {
        UserPermissionsDAO permissionsDAO = new UserPermissionsDAO(connection);
        try {
            // Aqui você deve carregar os tipos de usuários e não as permissões
            List<String> tiposUsuarios = permissionsDAO.getTiposUsuarios(); // Método que deve retornar os tipos de usuário
            tipoUsuarioComboBox.getItems().setAll(tiposUsuarios);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para obter o ID do tipo de usuário com base no nome
    private int getIdTipoUsuarioByNome(String tipoUsuarioNome) throws SQLException {
        UserPermissionsDAO permissionsDAO = new UserPermissionsDAO(connection);
        return permissionsDAO.getIdTipoUsuarioByNome(tipoUsuarioNome);
    }

    // Método para formatar o CPF
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

    // Valida CPF
    public boolean isCPFValid(String cpf) {
        String digitsOnly = cpf.replaceAll("[^\\d]", "");
        return digitsOnly.length() == 11;
    }
}
