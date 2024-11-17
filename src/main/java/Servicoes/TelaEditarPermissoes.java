// Pausa desta tela por falta de compatibilidade com o projeto.

//package Servicoes;
//
//import DAO.UsuarioDAO;
//import Entities.UserType;
//import DAO.UserPermissionsDAO;
//import Entities.Usuario;
//import javafx.application.Application;
//import javafx.geometry.Insets;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.layout.VBox;
//import javafx.stage.Stage;
//
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.util.List;
//
//public class TelaEditarPermissoes extends Application {
//
//    private ComboBox<String> userTypeComboBox; // Alterado para String para corresponder à lista carregada
//    private ComboBox<Usuario> usuarioComboBox;
//    private UserPermissionsDAO userPermissionsDAO;
//    private Connection connection;
//
//    public TelaEditarPermissoes(Connection connection) {
//        this.connection = connection;
//        this.userPermissionsDAO = new UserPermissionsDAO(connection);
//    }
//
//    @Override
//    public void start(Stage stage) {
//        userTypeComboBox = new ComboBox<>();
//        usuarioComboBox = new ComboBox<>();
//        Button saveButton = new Button("Salvar Alterações");
//
//        // Carregar os tipos de usuário no ComboBox
//        carregarTiposUsuario();
//
//        // Carregar todos os usuários na ComboBox de usuários
//        carregarUsuarios();
//
//        // Ação do botão de salvar (alteração do tipo de usuário)
//        saveButton.setOnAction(e -> salvarAlteracoes());
//
//        // Layout da tela
//        VBox layout = new VBox(10, userTypeComboBox, usuarioComboBox, saveButton);
//        layout.setPadding(new Insets(20));
//
//        Scene scene = new Scene(layout, 300, 200);
//        stage.setTitle("Editar Tipo de Usuário");
//        stage.setScene(scene);
//        stage.show();
//    }
//
//    private void carregarTiposUsuario() {
//        try {
//            List<String> tiposUsuarios = userPermissionsDAO.getTiposUsuarios(); // Método que retorna nomes dos tipos de usuário
//            userTypeComboBox.getItems().setAll(tiposUsuarios);
//        } catch (SQLException e) {
//            e.printStackTrace();
//            mostrarAlerta("Erro ao carregar tipos de usuário.");
//        }
//    }
//
//    private void carregarUsuarios() {
//        UsuarioDAO usuarioDAO = new UsuarioDAO();
//        usuarioComboBox.getItems().clear();  // Limpar a ComboBox antes de adicionar novos itens
//
//        try {
//            // Chamar um DAO especializado para buscar apenas os usuários
//            List<Usuario> usuarios = usuarioDAO.getUsuarioByLogin();  // Supondo que você tenha um DAO para usuários
//
//            // Verificando se usuários foram carregados
//            if (usuarios != null && !usuarios.isEmpty()) {
//                usuarioComboBox.getItems().setAll(usuarios);  // Preenche a ComboBox com os usuários retornados
//            } else {
//                mostrarAlerta("Nenhum usuário encontrado.");
//            }
//        } catch (RuntimeException e) {
//            e.printStackTrace();
//            mostrarAlerta("Erro ao carregar usuários.");
//        }
//    }
//
//    private void salvarAlteracoes() {
//        UserType tipoUsuario = UserType.valueOf(userTypeComboBox.getValue());
//        Usuario usuario = usuarioComboBox.getValue();
//
//        if (usuario == null || tipoUsuario == null) {
//            mostrarAlerta("Selecione um usuário e tipo de usuário antes de salvar.");
//            return;
//        }
//
//        try {
//            // Atualiza o tipo de usuário no banco de dados
//            userPermissionsDAO.atualizarTipoUsuario(usuario, tipoUsuario);
//            mostrarAlerta("Tipo de usuário atualizado com sucesso!");
//        } catch (RuntimeException e) {
//            e.printStackTrace();
//            mostrarAlerta("Erro ao atualizar tipo de usuário.");
//        }
//    }
//
//    private void mostrarAlerta(String mensagem) {
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setContentText(mensagem);
//        alert.showAndWait();
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}
