//package DAO;
//
//import Servicoes.Solicitacao;
//
//import java.sql.Connection;
//import java.sql.Date;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Timestamp;
//import java.util.ArrayList;
//import java.util.List;
//
//public class SolicitacaoDAO {
//    private Connection connection;
//
//    public SolicitacaoDAO(Connection connection) {
//        this.connection = connection;
//    }
//
//    public void inserirSolicitacao(Solicitacao solicitacao) {
//        String sql = "INSERT INTO solicitacoes (fornecedor, descricao, data_criacao, data_pagamento, forma_pagamento, parcelas, valor_parcelas, valor_total, id_usuario) " +
//                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
//        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
//            pstmt.setString(1, solicitacao.getFornecedor());
//            pstmt.setString(2, solicitacao.getDescricao());
//            pstmt.setTimestamp(3, solicitacao.getDataCriacao());
//            pstmt.setDate(4, new Date(solicitacao.getDataPagamento().getTime()));
//            pstmt.setString(5, solicitacao.getFormaPagamento());
//            pstmt.setInt(6, solicitacao.getParcelas());
//            pstmt.setDouble(7, solicitacao.getValorParcelas());
//            pstmt.setDouble(8, solicitacao.getValorTotal());
//            pstmt.setInt(9, solicitacao.getIdUsuario());
//
//            pstmt.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public List<Solicitacao> getTodasSolicitacoes() {
//        List<Solicitacao> solicitacoes = new ArrayList<>();
//        String sql = "SELECT * FROM solicitacoes";
//        try (PreparedStatement pstmt = connection.prepareStatement(sql);
//             ResultSet rs = pstmt.executeQuery()) {
//
//            while (rs.next()) {
//                int id = rs.getInt("id");
//                String fornecedor = rs.getString("fornecedor");
//                String descricao = rs.getString("descricao");
//                Timestamp dataCriacao = rs.getTimestamp("data_criacao");
//                Date dataPagamento = rs.getDate("data_pagamento");
//                String formaPagamento = rs.getString("forma_pagamento");
//                int parcelas = rs.getInt("parcelas");
//                double valorParcelas = rs.getDouble("valor_parcelas");
//                double valorTotal = rs.getDouble("valor_total");
//                int idUsuario = rs.getInt("id_usuario");
//
//                Solicitacao solicitacao = new Solicitacao(id, fornecedor, descricao, dataCriacao, dataPagamento, formaPagamento, parcelas, valorParcelas, valorTotal, idUsuario);
//                solicitacoes.add(solicitacao);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return solicitacoes;
//    }
//}


package DAO;

import Servicoes.Solicitacao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class SolicitacaoDAO {
    private Connection connection;

    public SolicitacaoDAO(Connection connection) {
        this.connection = connection;
    }

    // Método para inserir uma nova solicitação
    public void inserirSolicitacao(Solicitacao solicitacao) throws SQLException {
        String sql = "INSERT INTO solicitacoes (fornecedor, descricao, data_criacao, data_pagamento, forma_pagamento,valor_total, id_usuario) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, solicitacao.getFornecedor());
            pstmt.setString(2, solicitacao.getDescricao());
            pstmt.setTimestamp(3, new Timestamp(solicitacao.getDataCriacao().getTime()));
            pstmt.setDate(4, new Date(solicitacao.getDataPagamento().getTime()));
            pstmt.setString(5, solicitacao.getFormaPagamento());
//            pstmt.setInt(6, solicitacao.getParcelas());
//            pstmt.setDouble(7, solicitacao.getValorParcelas());
            pstmt.setDouble(8, solicitacao.getValorTotal());
            pstmt.setInt(9, solicitacao.getIdUsuario());

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
//                int parcelas = rs.getInt("parcelas");
//                double valorParcelas = rs.getDouble("valor_parcelas");
                double valorTotal = rs.getDouble("valor_total");
                int idUsuario = rs.getInt("id_usuario");

                Solicitacao solicitacao = new Solicitacao(id, fornecedor, descricao, dataCriacao, dataPagamento, formaPagamento, valorTotal, idUsuario);
                solicitacoes.add(solicitacao);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return solicitacoes;
    }
}
