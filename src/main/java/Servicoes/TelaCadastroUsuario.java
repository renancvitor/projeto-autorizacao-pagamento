package Servicoes;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.sql.Connection;

public class TelaCadastroUsuario {

    private Connection connection;

    public TelaCadastroUsuario(Connection connection) {
        this.connection = connection;
    }

    public void start (Stage stage) {
        VBox layout = new VBox(10);
        layout.getChildren().add(new Label("Cadastro de Novo Usuário"));

        TextField loginField = new TextField();
        loginField.setPromptText("Login");
        TextField senhaField = new TextField();
        senhaField.setPromptText("Senha");


    }
}
