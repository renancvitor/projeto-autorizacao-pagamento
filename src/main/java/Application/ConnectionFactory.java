package Application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    public static Connection getConnection() throws SQLException {
        String url = ConfigLoader.getProperty("db.url");
        String user = ConfigLoader.getProperty("db.user");
        String password = ConfigLoader.getProperty("db.password");

        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            // Você pode adicionar tratamento específico para SQLExceptions aqui
            throw new SQLException("Erro ao conectar com o banco de dados", e);
        }
    }
}
