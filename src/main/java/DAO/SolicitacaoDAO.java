package DAO;

import Servicoes.Solicitacao;

import java.sql.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class SolicitacaoDAO {
    private Connection connection;

    public SolicitacaoDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Solicitacao> getTodasSolicitacoes() {
        List<Solicitacao> solicitacoes = new ArrayList<>();
        String sql = "SELECT * FROM solicitacoes";
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String fornecedor = rs.getString("fornecedor");
                String descricao = rs.getString("descricao");
                Timestamp dataCriacao = rs.getTimestamp("data_criacao"); // Mantendo como Timestamp
                Date dataPagamento = rs.getDate("data_pagamento"); // Mantendo como Date
                String formaPagamento = rs.getString("forma_pagamento");
                int parcelas = rs.getInt("parcelas");
                double valorParcelas = rs.getDouble("valor_parcelas");
                double valorTotal = rs.getDouble("valor_total");
                int idUsuario = rs.getInt("id_usuario");

                // Convertendo Timestamp para String
                String strDataCriacao = dataCriacao.toString();
                // Convertendo Date para String
                String strDataPagamento = dataPagamento.toString();

                Solicitacao solicitacao = new Solicitacao(id, fornecedor, descricao, dataCriacao, dataPagamento, formaPagamento, parcelas, valorParcelas, valorTotal, idUsuario);
                solicitacoes.add(solicitacao);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return solicitacoes;
    }
}
