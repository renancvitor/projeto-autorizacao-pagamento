package Servicoes;

import DAO.SolicitacaoDAO;
import Entities.Usuario;
import java.sql.SQLException;
import java.util.List;

public class SolicitacaoService {
    private SolicitacaoDAO solicitacaoDAO;
    private PermissaoService permissaoService;

    public SolicitacaoService(SolicitacaoDAO solicitacaoDAO, PermissaoService permissaoService) { // SUBSTITUI
        this.solicitacaoDAO = solicitacaoDAO;
        this.permissaoService = permissaoService;
    }

    public List<Solicitacao> getSolicitacoesVisiveisParaUsuario(Usuario usuario) throws SQLException {

        if (permissaoService.visualizarTodasSolicitacoes(usuario)) { // SUBSTITUI
            return solicitacaoDAO.getTodasSolicitacoes();
        } else if (permissaoService.realizarSolicitacoes(usuario)) { // SUBSTITUI
            return solicitacaoDAO.getSolicitacoesPorUsuario(usuario.getId());
        }
        return List.of();
    }

    public List<Solicitacao> getSolicitacoesPorStatus(StatusSolicitacao status) throws SQLException {
        return solicitacaoDAO.getSolicitacoesPorStatus(status);
    }

    public void atualizarStatusSolicitacao(Usuario usuario, PermissaoService permissaoService, Solicitacao solicitacao, StatusSolicitacao novoStatus) {
        if (permissaoService.aprovarReprovarSolicitacoes(usuario)) {
            solicitacao.setStatus(novoStatus);
            try {
                solicitacaoDAO.atualizarSolicitacao(solicitacao);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Acesso negado: Usuário não tem permissão para aprovar ou reprovar solicitações.");
        }
    }
}
