package Servicoes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static final String URL = "jdbc:sqlite:solicitacoes.db";

    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public static void createNewDatabase() {
        try (Connection conn = DriverManager.getConnection(URL)) {
            if (conn != null) {
                Statement stmt = conn.createStatement();
                stmt.execute("CREATE TABLE IF NOT EXISTS usuarios (\n"
                        + " id integer PRIMARY KEY,\n"
                        + " nome text NOT NULL,\n"
                        + " setor text NOT NULL,\n"
                        + " username text NOT NULL,\n"
                        + " password text NOT NULL\n"
                        + ");");
                stmt.execute("CREATE TABLE IF NOT EXISTS solicitacoes (\n"
                        + " id integer PRIMARY KEY,\n"
                        + " usuario_id integer NOT NULL,\n"
                        + " descricao text NOT NULL,\n"
                        + " valor real NOT NULL,\n"
                        + " data text NOT NULL,\n"
                        + " tipo text NOT NULL,\n"
                        + " status text NOT NULL,\n"
                        + " FOREIGN KEY (usuario_id) REFERENCES usuarios (id)\n"
                        + ");");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
