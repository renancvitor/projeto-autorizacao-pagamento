package Servicoes;

import DAO.SolicitacaoDAO;
import Entities.Usuario;
import java.sql.SQLException;
import java.util.List;

public class SolicitacaoService {
    private SolicitacaoDAO solicitacaoDAO;

    public SolicitacaoService(SolicitacaoDAO solicitacaoDAO) {
        this.solicitacaoDAO = solicitacaoDAO;
    }

    public List<Solicitacao> getSolicitacoesVisiveisParaUsuario(Usuario usuario) throws SQLException {
        if (usuario.hasPermission("VISUALIZAR_TODAS_SOLICITACOES")) {
            return solicitacaoDAO.getTodasSolicitacoes();
        } else if (usuario.hasPermission("REALIZAR_SOLICITACOES")) {
            return solicitacaoDAO.getSolicitacoesPorUsuario(usuario.getId());
        }
        return List.of(); // Retorna uma lista vazia se o usuário não tiver permissões específicas
    }

    public List<Solicitacao> getSolicitacoesPorStatus(StatusSolicitacao status) throws SQLException {
        return solicitacaoDAO.getSolicitacoesPorStatus(status);
    }

    public void atualizarStatusSolicitacao(Solicitacao solicitacao, StatusSolicitacao novoStatus) {
        solicitacao.setStatus(novoStatus);
        try {
            solicitacaoDAO.atualizarSolicitacao(solicitacao);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
