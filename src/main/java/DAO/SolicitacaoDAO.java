package DAO;

import Entities.UserType;
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

    public List<Solicitacao> getTodasSolicitacoes(int idUser) {
        List<Solicitacao> solicitacoes = new ArrayList<>();
        String sql = "";
        int idTipoUsuario;

        try {
            idTipoUsuario = getIdTipoUsuario(idUser);
            sql = "SELECT * FROM solicitacoes WHERE";
//            if (idTipoUsuario == 4) {
//                sql = "SELECT * FROM solicitacoes WHERE status = ? AND id_usuario = ?";
//            } else {
//                sql = "SELECT * FROM solicitacoes WHERE status = ?";
//            }

            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
//                if (idTipoUsuario == UserType.COMUM.getId()) {
//                    //pstmt.setString(1, status.name());
//                    pstmt.setInt(2, idUser);
//                } else {
//                    //pstmt.setString(1, status.name());
//                }

                try (ResultSet rs = pstmt.executeQuery()) {
                    int count = 0;
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
                        count++;
                    }
                    System.out.println("Número de solicitações retornadas: " + count);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao executar consulta SQL: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erro inesperado: " + e.getMessage());
            e.printStackTrace();
        }

        return solicitacoes;
    }

    // Método auxiliar para obter o tipo do usuário baseado no idUser
    private int getIdTipoUsuario(int idUser) throws SQLException {
        String sql = "SELECT id_tipo_usuario FROM usuarios WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idUser);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_tipo_usuario");
                } else {
                    throw new IllegalArgumentException("Usuário não encontrado com ID: " + idUser);
                }
            }
        }
    }


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

    // ALTERAR ESTA BAGAÇA

    public List<Solicitacao> getSolicitacoesPorStatus(StatusSolicitacao status, int idTipoUsuario, int idUser) {
        List<Solicitacao> solicitacoes = new ArrayList<>();
        String sql = "";

        if (idTipoUsuario == 4) {
            sql = "SELECT * FROM solicitacoes WHERE status = ? AND id_usuario = ?";
        } else {
            sql = "SELECT * FROM solicitacoes WHERE status = ?";
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (idTipoUsuario == 4) {
                stmt.setString(1, status.name());
                stmt.setInt(2, idUser);
            } else {
                stmt.setString(1, status.name());
            }

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

    private List<Solicitacao> mapResultSetToSolicitacoes(ResultSet resultSet) throws SQLException {
        List<Solicitacao> solicitacoes = new ArrayList<>();
        while (resultSet.next()) {
            Solicitacao solicitacao = new Solicitacao();
            solicitacao.setId(resultSet.getInt("id"));
            solicitacao.setFornecedor(resultSet.getString("fornecedor"));
            solicitacao.setDescricao(resultSet.getString("descricao"));
            solicitacao.setDataCriacao(Timestamp.valueOf(resultSet.getTimestamp("data_criacao").toLocalDateTime()));
            solicitacao.setDataPagamento(resultSet.getDate("data_pagamento"));
            solicitacao.setFormaPagamento(resultSet.getString("forma_pagamento"));
            solicitacao.setValorTotal(resultSet.getDouble("valor_total"));
            solicitacao.setStatus(StatusSolicitacao.valueOf(resultSet.getString("status")));
            solicitacoes.add(solicitacao);
        }
        return solicitacoes;
    }

    private Connection getConnection() {
        return null;
    }

    public List<Solicitacao> getSolicitacoesPorUsuario(int idUsuario, String tipoUsuario) {
        List<Solicitacao> solicitacoes = new ArrayList<>();
        String sql;

        // Define a consulta com base no tipo do usuário
        if ("COMUM".equalsIgnoreCase(tipoUsuario)) {
            sql = "SELECT * FROM solicitacoes WHERE status = 'PENDENTE' AND id_usuario = ?"; // Apenas solicitações do próprio usuário
        } else if ("ADMIN".equalsIgnoreCase(tipoUsuario)) {
            sql = "SELECT * FROM solicitacoes"; // Todos os registros
        } else {
            throw new IllegalArgumentException("Tipo de usuário inválido: " + tipoUsuario);
        }

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            // Para COMUM, passa o ID do usuário como parâmetro
            if ("COMUM".equalsIgnoreCase(tipoUsuario)) {
                pstmt.setInt(1, idUsuario);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return solicitacoes;
    }


}