package DAO;

import Entities.Usuario;
import Servicoes.Solicitacao;
import Servicoes.StatusSolicitacao;

import java.sql.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UsuarioDAO {
    private Connection connection;

    public UsuarioDAO(Connection connection) {
        this.connection = connection;
    }

    // Método para inserir um novo usuário na tabela 'usuarios'
    public void inserirUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuarios (login, senha, id_tipo_usuario, tipo_usuario) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, usuario.getLogin());
            pstmt.setString(2, usuario.getSenha());
            pstmt.setInt(3, usuario.getIdTipoUsuario());
            pstmt.setString(4, String.join(",", usuario.getRoles())); // Convertendo a lista de roles para uma string separada por vírgulas
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para buscar um usuário pelo login e senha na tabela 'usuarios'
//    public Usuario getUsuarioByLogin(String login, String senha) {
//        String sql = "SELECT * FROM usuarios WHERE login = ? AND senha = ?";
//        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
//            pstmt.setString(1, login);
//            pstmt.setString(2, senha);
//            ResultSet rs = pstmt.executeQuery();
//            if (rs.next()) {
//                String usuarioLogin = rs.getString("login");
//                String usuarioSenha = rs.getString("senha");
//                int idTipoUsuario = rs.getInt("id_tipo_usuario");
//
//                return new Usuario(usuarioLogin, usuarioSenha, idTipoUsuario);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    // Método para buscar um usuário pelo login e senha
    public Usuario getUsuarioByLogin(String login, String senha) {
        String sql = "SELECT * FROM usuarios WHERE login = ? AND senha = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, login);
            pstmt.setString(2, senha);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                int idTipoUsuario = rs.getInt("id_tipo_usuario");

                // Retorna um novo objeto Usuario com os dados encontrados no banco de dados
                return new Usuario(id, login, senha, idTipoUsuario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    // Método para recuperar todas as solicitações da tabela 'solicitacoes'
    public List<Solicitacao> getTodasSolicitacoes() {
        List<Solicitacao> solicitacoes = new ArrayList<>();
        String sql = "SELECT * FROM solicitacoes"; // Assumindo que a tabela se chama "solicitacoes"
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String fornecedor = rs.getString("fornecedor");
                String descricao = rs.getString("descricao");
                Timestamp dataCriacao = rs.getTimestamp("data_criacao");
                Date dataPagamento = rs.getDate("data_pagamento");
                String formaPagamento = rs.getString("forma_pagamento");
                double valorTotal = rs.getDouble("valor_total");
                int idUsuario = rs.getInt("id_usuario");

                // Obtém o status (supondo que você tenha um método para isso)
                StatusSolicitacao status = getStatusFromResultSet(rs);

                // Criação do objeto Solicitacao
                Solicitacao solicitacao = new Solicitacao(id, fornecedor, descricao, dataCriacao, dataPagamento, formaPagamento, valorTotal, idUsuario, status);

                solicitacoes.add(solicitacao);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return solicitacoes;
    }

    // Método para obter o status a partir do ResultSet (exemplo)
    private StatusSolicitacao getStatusFromResultSet(ResultSet rs) throws SQLException {
        // Implemente aqui a lógica para obter o status do ResultSet
        // Exemplo:
        String statusStr = rs.getString("status"); // Supondo que o status seja uma string no ResultSet
        return StatusSolicitacao.valueOf(statusStr); // Convertendo a string para o enum StatusSolicitacao
    }



}
