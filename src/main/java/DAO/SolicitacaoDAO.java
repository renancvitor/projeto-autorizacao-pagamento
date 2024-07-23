package DAO;

import Servicoes.Solicitacao;
import Servicoes.StatusSolicitacao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static DAO.ConnectionFactory.getConnection;

public class SolicitacaoDAO {
    private Connection connection;

    public SolicitacaoDAO(Connection connection) {
        this.connection = connection;
    }

    // Método para inserir uma nova solicitação na tabela 'solicitacoes'
    public void inserirSolicitacao(Solicitacao solicitacao) throws SQLException {
        String sql = "INSERT INTO solicitacoes (fornecedor, descricao, data_criacao, data_pagamento, forma_pagamento, valor_total, id_usuario, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, solicitacao.getFornecedor());
            pstmt.setString(2, solicitacao.getDescricao());
            pstmt.setTimestamp(3, solicitacao.getDataCriacao());
            pstmt.setDate(4, solicitacao.getDataPagamento());
            pstmt.setString(5, solicitacao.getFormaPagamento());
            pstmt.setDouble(6, solicitacao.getValorTotal());
            pstmt.setInt(7, solicitacao.getIdUsuario());
            pstmt.setString(8, solicitacao.getStatus().name()); // Assume que o status é uma enumeração

            pstmt.executeUpdate();
        }
    }

    // Método para obter todas as solicitações
    public List<Solicitacao> getTodasSolicitacoes() {
        List<Solicitacao> solicitacoes = new ArrayList<>();
        String sql = "SELECT * FROM solicitacoes";
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String fornecedor = rs.getString("fornecedor");
                String descricao = rs.getString("descricao");
                Timestamp dataCriacao = rs.getTimestamp("data_criacao");
                Date dataPagamento = rs.getDate("data_pagamento");
                String formaPagamento = rs.getString("forma_pagamento");
                double valorTotal = rs.getDouble("valor_total");
                int idUsuario = rs.getInt("id_usuario");

                // Obtém o status do enum
                String statusStr = rs.getString("status");
                StatusSolicitacao status = null;

                if (statusStr != null) {
                    try {
                        status = StatusSolicitacao.valueOf(statusStr);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Valor de status inválido: " + statusStr);
                        e.printStackTrace();
                    }
                } else {
                    System.err.println("Valor de status é nulo.");
                }

                Solicitacao solicitacao = new Solicitacao(id, fornecedor, descricao, dataCriacao, dataPagamento, formaPagamento, valorTotal, idUsuario, status);
                solicitacoes.add(solicitacao);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return solicitacoes;
    }

    // Método para atualizar o status de uma solicitação
    public void atualizarSolicitacao(Solicitacao solicitacao) throws SQLException {
        String sql = "UPDATE solicitacoes SET status = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, solicitacao.getStatus().toString()); // Convertendo enum para String
            stmt.setInt(2, solicitacao.getId());
            stmt.executeUpdate();
        }
    }

    public int contarSolicitacoesPorStatus(StatusSolicitacao status) throws SQLException {
        String sql = "SELECT COUNT(*) FROM solicitacoes WHERE status = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status.name());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public List<Solicitacao> getSolicitacoesAnalisadas() {
        List<Solicitacao> solicitacoes = new ArrayList<>();
        String sql = "SELECT * FROM solicitacoes WHERE status = ? OR status = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, StatusSolicitacao.APROVADA.name());
            stmt.setString(2, StatusSolicitacao.REPROVADA.name());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Solicitacao solicitacao = new Solicitacao(
                        rs.getInt("id"),
                        rs.getString("fornecedor"),
                        rs.getString("descricao"),
                        rs.getTimestamp("data_criacao"),
                        rs.getDate("data_pagamento"),
                        rs.getString("forma_pagamento"),
                        rs.getDouble("valor_total"),
                        rs.getInt("id_usuario"),
                        StatusSolicitacao.valueOf(rs.getString("status"))
                );
                solicitacoes.add(solicitacao);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return solicitacoes;
    }

    public List<Solicitacao> getSolicitacoesPorStatus(StatusSolicitacao status) {
        List<Solicitacao> solicitacoes = new ArrayList<>();
        String sql = "SELECT * FROM solicitacoes WHERE status = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status.name());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Solicitacao solicitacao = new Solicitacao(
                        rs.getInt("id"),
                        rs.getString("fornecedor"),
                        rs.getString("descricao"),
                        rs.getTimestamp("data_criacao"),
                        rs.getDate("data_pagamento"),
                        rs.getString("forma_pagamento"),
                        rs.getDouble("valor_total"),
                        rs.getInt("id_usuario"),
                        StatusSolicitacao.valueOf(rs.getString("status"))
                );
                solicitacoes.add(solicitacao);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return solicitacoes;
    }

    public List<Solicitacao> getSolicitacoesPorUsuario(int usuario) {
        List<Solicitacao> solicitacoes = new ArrayList<>();
        String sql = "SELECT * FROM solicitacoes WHERE id_usuario = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1,
                    usuario);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Solicitacao solicitacao = new Solicitacao(
                        rs.getInt("id"),
                        rs.getString("fornecedor"),
                        rs.getString("descricao"),
                        rs.getTimestamp("data_criacao"),
                        rs.getDate("data_pagamento"),
                        rs.getString("forma_pagamento"),
                        rs.getDouble("valor_total"),
                        rs.getInt("id_usuario"),
                        StatusSolicitacao.valueOf(rs.getString("status"))
                );
                solicitacoes.add(solicitacao);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return solicitacoes;
    }

    public List<Solicitacao> getSolicitacoesPendentes() {
        List<Solicitacao> solicitacoes = new ArrayList<>();
        String sql = "SELECT * FROM solicitacoes WHERE status = 'PENDENTE'";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Solicitacao solicitacao = new Solicitacao(
                        rs.getInt("id"),
                        rs.getString("fornecedor"),
                        rs.getString("descricao"),
                        rs.getTimestamp("data_criacao"),
                        rs.getDate("data_pagamento"),
                        rs.getString("forma_pagamento"),
                        rs.getDouble("valor_total"),
                        rs.getInt("id_usuario"),
                        StatusSolicitacao.valueOf(rs.getString("status")) // Converte a string para StatusSolicitacao
                );
                solicitacoes.add(solicitacao);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return solicitacoes;
    }

    private Connection getConnection() {
        // Implement your connection logic here
        return null;
    }


}