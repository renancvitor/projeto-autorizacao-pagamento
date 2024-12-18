package DAO;

import Servicoes.Solicitacao;
import Servicoes.StatusSolicitacao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
        String sql;
        int idTipoUsuario;

        try {
            idTipoUsuario = getIdTipoUsuario(idUser);
            sql = "SELECT " +
                    "s.*, u.login " +
                    "FROM " +
                    "solicitacoes s " +
                    "INNER JOIN usuarios u ON s.id_usuario = u.id " +
                    "WHERE s.status = 'PENDENTE'";

            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {

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
                                StatusSolicitacao.valueOf(rs.getString("status")),
                                rs.getString("login")
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

    public List<Solicitacao> getSolicitacoesAnalisadas(int idTipoUsuario, int idUsuario) {
        List<Solicitacao> solicitacoes = new ArrayList<>();
        String sql;

        if (idTipoUsuario == 4) {
            sql = "SELECT " +
                    "s.*, u.login " +
                    "FROM " +
                    "solicitacoes s " +
                    "INNER JOIN usuarios u ON s.id_usuario = u.id " +
                    "WHERE status IN ('APROVADA', 'REPROVADA') " +
                    "AND id_usuario = ?";
        } else {
            sql = "SELECT " +
                    "s.*, u.login " +
                    "FROM " +
                    "solicitacoes s " +
                    "INNER JOIN usuarios u ON s.id_usuario = u.id " +
                    "WHERE s.status IN ('APROVADA', 'REPROVADA')";
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (idTipoUsuario == 4) {
                stmt.setInt(1, idUsuario);
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
                        StatusSolicitacao.valueOf(rs.getString("status")),
                        rs.getString("login")
                );
                solicitacoes.add(solicitacao);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return solicitacoes;
    }

    public List<Solicitacao> getSolicitacoesPorStatus(StatusSolicitacao status, int idTipoUsuario, int idUser) {
        List<Solicitacao> solicitacoes = new ArrayList<>();
        String sql;

        if (idTipoUsuario == 4) {
            sql = "SELECT " +
                    "s.*, u.login " +
                    "FROM " +
                    "solicitacoes s " +
                    "INNER JOIN usuarios u ON s.id_usuario = u.id " +
                    "WHERE status = 'PENDENTE' " +
                    "AND id_usuario = ?";
        } else {
            sql = "SELECT " +
                    "s.*, u.login " +
                    "FROM " +
                    "solicitacoes s " +
                    "INNER JOIN usuarios u ON s.id_usuario = u.id " +
                    "WHERE s.status = 'PENDENTE'";
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            if (idTipoUsuario == 4) {
                stmt.setInt(1, idUser);
            }

//            if (idTipoUsuario == 4) {
//                stmt.setString(1, status.name());
//                stmt.setInt(2, idUser);
//            } else {
//                stmt.setString(1, status.name());
//            }

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
                        StatusSolicitacao.valueOf(rs.getString("status")),
                        rs.getString("login")
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
                        StatusSolicitacao.valueOf(rs.getString("status")), // Converte a string para StatusSolicitacao
                        rs.getString("login")
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

        if ("COMUM".equalsIgnoreCase(tipoUsuario)) {
            sql = "SELECT * FROM solicitacoes WHERE status = 'PENDENTE' AND id_usuario = ?"; // Apenas solicitações do próprio usuário
        } else if ("ADMIN".equalsIgnoreCase(tipoUsuario)) {
            sql = "SELECT * FROM solicitacoes";
        } else {
            throw new IllegalArgumentException("Tipo de usuário inválido: " + tipoUsuario);
        }

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
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
                            StatusSolicitacao.valueOf(rs.getString("status")),
                            rs.getString("login")
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