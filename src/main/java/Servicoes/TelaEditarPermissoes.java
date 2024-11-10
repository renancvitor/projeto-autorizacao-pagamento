package Servicoes;

import Entities.UserPermissions;
import Entities.UserType;
import DAO.UserPermissionsDAO;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.util.List;

public class TelaEditarPermissoes extends Application {

    private ComboBox<UserType> userTypeComboBox;
    private VBox permissionsContainer;
    private UserPermissionsDAO userPermissionsDAO;

    public TelaEditarPermissoes(Connection connection) {
        // Instanciando o DAO com a Connection fornecida
        userPermissionsDAO = new UserPermissionsDAO(connection);
    }

    @Override
    public void start(Stage stage) {
        userTypeComboBox = new ComboBox<>();
        permissionsContainer = new VBox(5);
        Button saveButton = new Button("Salvar Permissões");

        userTypeComboBox.getItems().addAll(UserType.values());
        userTypeComboBox.setOnAction(e -> carregarPermissoes(userTypeComboBox.getValue()));

        saveButton.setOnAction(e -> salvarPermissoes());

        VBox layout = new VBox(10, userTypeComboBox, permissionsContainer, saveButton);
        layout.setPadding(new Insets(20));

        stage.setScene(new Scene(layout, 300, 400));
        stage.setTitle("Editar Permissões de Usuário");
        stage.show();
    }

    private void carregarPermissoes(UserType userType) {
        permissionsContainer.getChildren().clear();

        List<String> todasPermissoes = userPermissionsDAO.buscarTodasPermissoes();
        List<String> permissoesAssociadas = userPermissionsDAO.buscarPermissoesPorTipoUsuario(userType);

        for (String permissao : todasPermissoes) {
            CheckBox checkBox = new CheckBox(permissao);
            checkBox.setSelected(permissoesAssociadas.contains(permissao));
            permissionsContainer.getChildren().add(checkBox);
        }
    }

    private void salvarPermissoes() {
        UserType userType = userTypeComboBox.getValue();
        if (userType == null) {
            mostrarAlerta("Selecione um tipo de usuário antes de salvar.");
            return;
        }

        List<String> permissoesSelecionadas = permissionsContainer.getChildren().stream()
                .filter(node -> node instanceof CheckBox && ((CheckBox) node).isSelected())
                .map(node -> ((CheckBox) node).getText())
                .toList();

        userPermissionsDAO.atualizarPermissoesUsuario(userType, permissoesSelecionadas);
        mostrarAlerta("Permissões atualizadas com sucesso!");
    }

    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
