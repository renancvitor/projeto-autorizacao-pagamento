package Servicoes;

import DAO.UserPermissionsDAO;
import Entities.UserType;
import javafx.scene.Scene;
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
    private UsuarioController usuarioController;

    public TelaCadastroUsuario(Connection connection) {
        this.connection = connection;
        this.tipoUsuarioComboBox = new ComboBox<>();
    }

    public void setUsuarioController(UsuarioController usuarioController) {
        this.usuarioController = usuarioController;
    }

    private UserType getUserTypeFromId(int idTipoUsuario) {
        return switch (idTipoUsuario) {
            case 1 -> UserType.ADMIN;
            case 2 -> UserType.GESTOR;
            case 3 -> UserType.VISUALIZADOR;
            case 4 -> UserType.COMUM;
            default -> throw new IllegalArgumentException("Tipo de usuário inválido: " + idTipoUsuario);
        };
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

        cpfField.textProperty().addListener((observable, oldValue, newValue) -> {
            String formattedCpf = formatInputAsCPF(newValue);
            if (!newValue.equals(formattedCpf)) {
                cpfField.setText(formattedCpf);
                cpfField.positionCaret(formattedCpf.length());  // Posiciona o cursor no final
            }
        });

        carregarTiposUsuario();

        Button saveButton = new Button("Salvar");

        saveButton.setOnAction(event -> {
            String login = loginField.getText();
            String senha = senhaField.getText();
            String cpf = cpfField.getText();
            String tipoUsuarioSelecionado = tipoUsuarioComboBox.getValue();

            if (login.isEmpty() || senha.isEmpty() || cpf.isEmpty() || tipoUsuarioSelecionado == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Preencha todos os campos!");
                alert.show();
            } else {
                try {
                    UsuarioDAO usuarioDAO = new UsuarioDAO(connection);
                    UserPermissionsDAO permissionsDAO = new UserPermissionsDAO(connection);

                    int idTipoUsuario = getIdTipoUsuarioByNome(tipoUsuarioSelecionado);
                    UserType userType = getUserTypeFromId(idTipoUsuario);

                    List<String> permissoes = permissionsDAO.getPermissoesByTipo(tipoUsuarioSelecionado);

                    Usuario novoUsuario = new Usuario(0, login, senha, permissoes, cpf, idTipoUsuario, userType);
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

        Scene scene = new Scene(layout, 400, 350);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        // stage.setScene(new javafx.scene.Scene(layout, 300, 250));
        stage.setTitle("Cadastro de Usuário");
        stage.setScene(scene);
        stage.show();
    }

    private void carregarTiposUsuario() {
        UserPermissionsDAO permissionsDAO = new UserPermissionsDAO(connection);
        try {
            List<String> tiposUsuarios = permissionsDAO.getTiposUsuarios(); // Método que retorna nomes dos tipos de usuário
            tipoUsuarioComboBox.getItems().setAll(tiposUsuarios);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getIdTipoUsuarioByNome(String tipoUsuarioNome) throws SQLException {
        UserPermissionsDAO permissionsDAO = new UserPermissionsDAO(connection);
        return permissionsDAO.getIdTipoUsuarioByNome(tipoUsuarioNome);
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
}
