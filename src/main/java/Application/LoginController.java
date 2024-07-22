package Application;

import DAO.UsuarioDAO;
import Entities.Usuario;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class LoginController {

    private UsuarioDAO usuarioDAO; // Responsável por acessar métodos relacionados ao usuário no banco de dados
    private Usuario usuarioLogado; // Armazena o usuário atualmente autenticado na sessão

    public LoginController(Connection connection) {
        this.usuarioDAO = new UsuarioDAO(connection); // Inicializa o DAO com a conexão recebida
    }

    public boolean isLoggedIn() {
        return usuarioLogado != null; // Verifica se há um usuário logado
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado; // Retorna o usuário logado atualmente
    }

    public int getIdUsuarioLogado() {
        if (isLoggedIn()) {
            return usuarioLogado.getId(); // Retorna o ID do usuário logado
        } else {
            throw new IllegalStateException("Nenhum usuário logado.");
        }
    }

    // Método fictício para criar uma conexão com o banco de dados
    private static Connection createConnection() throws SQLException {
        // Aqui você deve implementar a lógica para obter a conexão com o banco de dados
        // Exemplo simples:
        String url = "jdbc:mysql://localhost:3306/sistema_pagamentos";
        String username = "root";
        String password = "123456789";
        return DriverManager.getConnection(url, username, password);
    }

    public boolean autenticar(String login, String senha) throws SQLException {
        Usuario usuario = usuarioDAO.getUsuarioByLogin(login, senha);
        if (usuario != null) {
            usuarioLogado = usuario;
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        Connection connection = null;

        try {
            // Criar uma conexão com o banco de dados
            connection = createConnection(); // Método fictício para criar a conexão, substitua conforme necessário

            // Cria uma instância do LoginController com a conexão
            LoginController loginController = new LoginController(connection);

            // Exemplo de processo de login
            String login = "admin"; // Login do usuário admin
            String senha = "adminNewAdmin@1900"; // Senha do usuário admin

            // Exemplo de verificação de login
            if (loginController.autenticar(login, senha)) {
                System.out.println("Login bem-sucedido para: " + loginController.getUsuarioLogado().getLogin());
            } else {
                System.out.println("Login falhou. Verifique suas credenciais.");
            }

            // Exemplo de operação com usuário logado
            if (loginController.isLoggedIn()) {
                int idUsuarioLogado = loginController.getIdUsuarioLogado();
                System.out.println("ID do usuário logado: " + idUsuarioLogado);
                // Exemplo: enviarSolicitacao(idUsuarioLogado);
            } else {
                System.out.println("Nenhum usuário logado. Operação não permitida.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Fechar a conexão com o banco de dados
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
