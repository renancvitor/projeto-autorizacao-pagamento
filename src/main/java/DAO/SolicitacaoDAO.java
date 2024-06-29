package DAO;

import Servicoes.Solicitacao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SolicitacaoDAO {
    private Connection connection;

    public SolicitacaoDAO(Connection connection) {
        this.connection = connection;
    }

    public void inserirSolicitacao(Solicitacao solicitacao) {
        String sql = "INSERT INTO solicitacoes (fornecedor, descricao, data_criacao, data_pagamento, " +
                "forma_pagamento, parcelas, valor_parcela, valor_total, id_usuario) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, solicitacao.getFornecedor());
            pstmt.setString(2, solicitacao.getDescricao());
            pstmt.setDate(3, new java.sql.Date(solicitacao.getDataCriacao().getTime()));
            pstmt.setDate(4, new java.sql.Date(solicitacao.getDataPagamento().getTime()));
            pstmt.setString(5, solicitacao.getFormaPagamento());
            pstmt.setInt(6, solicitacao.getParcelas());
            pstmt.setDouble(7, solicitacao.getValorParcela());
            pstmt.setDouble(8, solicitacao.getValorTotal());
            pstmt.setInt(9, solicitacao.getIdUsuario());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Outros métodos CRUD (atualizar, deletar, selecionar) podem ser adicionados aqui
}
