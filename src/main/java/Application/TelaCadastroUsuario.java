//package Application;
//
//import javafx.geometry.Insets;
//import javafx.scene.Scene;
//import javafx.scene.control.Alert;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.control.TextField;
//import javafx.scene.layout.GridPane;
//import javafx.stage.Stage;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//public class TelaCadastroUsuario {
//    private Connection conn;
//
//    public TelaCadastroUsuario(Connection conn) {
//        this.conn = conn;
//    }
//
//    public void mostrarTelaInicial(Stage primaryStage) {
//        primaryStage.setTitle("Sistema de Pagamento");
//
//        GridPane grid = new GridPane();
//        grid.setPadding(new Insets(10));
//        grid.setVgap(8);
//        grid.setHgap(10);
//
//        // Email
//        Label emailLabel = new Label("Email:");
//        GridPane.setConstraints(emailLabel, 0, 0);
//        TextField emailInput = new TextField();
//        emailInput.setPromptText("Digite seu email");
//        GridPane.setConstraints(emailInput, 1, 0);
//
//        // Senha
//        Label senhaLabel = new Label("Senha:");
//        GridPane.setConstraints(senhaLabel, 0, 1);
//        TextField senhaInput = new TextField();
//        senhaInput.setPromptText("Digite sua senha");
//        GridPane.setConstraints(senhaInput, 1, 1);
//
//        // Botão de Login
//        Button loginButton = new Button("Login");
//        GridPane.setConstraints(loginButton, 1, 2);
//
//        // Botão de Cadastrar
//        Button registerButton = new Button("Cadastrar");
//        GridPane.setConstraints(registerButton, 1, 3);
//
//        loginButton.setOnAction(e -> {
//            String email = emailInput.getText();
//            String senha = senhaInput.getText();
//
//            if (login(email, senha)) {
//                mostrarAlerta(Alert.AlertType.INFORMATION, "Login bem-sucedido!", "Bem-vindo!");
//                // Aqui você pode adicionar a lógica para continuar com o sistema após o login bem-sucedido
//            } else {
//                mostrarAlerta(Alert.AlertType.ERROR, "Erro no login", "Email ou senha incorretos.");
//            }
//        });
//
//        registerButton.setOnAction(e -> mostrarTelaCadastro());
//
//        grid.getChildren().addAll(emailLabel, emailInput, senhaLabel, senhaInput, loginButton, registerButton);
//
//        Scene scene = new Scene(grid, 300, 200);
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }
//
//    private void mostrarTelaCadastro() {
//        Stage cadastroStage = new Stage();
//        cadastroStage.setTitle("Cadastro de Usuário");
//
//        GridPane grid = new GridPane();
//        grid.setPadding(new Insets(10));
//        grid.setVgap(8);
//        grid.setHgap(10);
//
//        // Nome
//        Label nomeLabel = new Label("Nome:");
//        GridPane.setConstraints(nomeLabel, 0, 0);
//        TextField nomeInput = new TextField();
//        nomeInput.setPromptText("Digite seu nome");
//        GridPane.setConstraints(nomeInput, 1, 0);
//
//        // Email
//        Label emailLabel = new Label("Email:");
//        GridPane.setConstraints(emailLabel, 0, 1);
//        TextField emailInput = new TextField();
//        emailInput.setPromptText("Digite seu email");
//        GridPane.setConstraints(emailInput, 1, 1);
//
//        // Senha
//        Label senhaLabel = new Label("Senha:");
//        GridPane.setConstraints(senhaLabel, 0, 2);
//        TextField senhaInput = new TextField();
//        senhaInput.setPromptText("Digite sua senha");
//        GridPane.setConstraints(senhaInput, 1, 2);
//
//        // Botão de Cadastrar
//        Button cadastrarButton = new Button("Cadastrar");
//        GridPane.setConstraints(cadastrarButton, 1, 3);
//
//        cadastrarButton.setOnAction(e -> {
//            String novoNome = nomeInput.getText();
//            String novoEmail = emailInput.getText();
//            String novaSenha = senhaInput.getText();
//
//            if (novoNome.isEmpty() || novoEmail.isEmpty() || novaSenha.isEmpty()) {
//                mostrarAlerta(Alert.AlertType.ERROR, "Erro de Cadastro", "Preencha todos os campos antes de cadastrar.");
//                return;
//            }
//
//            cadastrarNovoUsuario(novoNome, novoEmail, novaSenha);
//            mostrarAlerta(Alert.AlertType.INFORMATION, "Cadastro realizado", "Usuário cadastrado com sucesso!");
//
//            // Fechar a janela de cadastro após o cadastro bem-sucedido
//            cadastroStage.close();
//        });
//
//        grid.getChildren().addAll(nomeLabel, nomeInput, emailLabel, emailInput, senhaLabel, senhaInput, cadastrarButton);
//
//        Scene scene = new Scene(grid, 300, 250);
//        cadastroStage.setScene(scene);
//        cadastroStage.show();
//    }
//
//    private void cadastrarNovoUsuario(String nome, String email, String senha) {
//        String sql = "INSERT INTO usuarios (nome, email, senha) VALUES (?, ?, ?)";
//
//        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
//            stmt.setString(1, nome);
//            stmt.setString(2, email);
//            stmt.setString(3, senha); // Armazenar a senha como texto simples (não recomendado em produção)
//            stmt.executeUpdate();
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
//    }
//
//    private boolean login(String email, String senha) {
//        String sql = "SELECT senha FROM usuarios WHERE email = ?";
//
//        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
//            stmt.setString(1, email);
//
//            try (ResultSet rs = stmt.executeQuery()) {
//                if (rs.next()) {
//                    String senhaArmazenada = rs.getString("senha");
//
//                    // Comparar a senha fornecida com a senha armazenada
//                    return senha.equals(senhaArmazenada);
//                }
//            }
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
//
//        return false; // Retorna false se não encontrar nenhum usuário ou a senha não corresponder
//    }
//
//    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
//        Alert alert = new Alert(tipo);
//        alert.setTitle(titulo);
//        alert.setHeaderText(null);
//        alert.setContentText(mensagem);
//        alert.showAndWait();
//    }
//}
