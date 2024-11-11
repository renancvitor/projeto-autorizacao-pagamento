package Servicoes;

import DAO.SolicitacaoDAO;
import Entities.Usuario;
import java.sql.SQLException;
import java.util.List;

public class SolicitacaoService {
    private SolicitacaoDAO solicitacaoDAO;
    private PermissaoService permissaoService;

    // ***** PENDENTE VALIDAÇÃO
    // public SolicitacaoService(SolicitacaoDAO solicitacaoDAO) {
    public SolicitacaoService(SolicitacaoDAO solicitacaoDAO, PermissaoService permissaoService) { // SUBSTITUI
        this.solicitacaoDAO = solicitacaoDAO;
        this.permissaoService = permissaoService;
    }

    // Método para obter solicitações visíveis ao usuário
    public List<Solicitacao> getSolicitacoesVisiveisParaUsuario(Usuario usuario) throws SQLException {
        // Usando PermissaoService para verificar permissões

        // ***** PENDENTE VALIDAÇÃO
        // if (usuario.hasPermission("VISUALIZAR_TODAS_SOLICITACOES")) {
        if (permissaoService.visualizarTodasSolicitacoes(usuario)) { // SUBSTITUI
            return solicitacaoDAO.getTodasSolicitacoes();

        // ***** PENDENTE VALIDAÇÃO
        // } else if (usuario.hasPermission("REALIZAR_SOLICITACOES")) {
        } else if (permissaoService.realizarSolicitacoes(usuario)) { // SUBSTITUI
            return solicitacaoDAO.getSolicitacoesPorUsuario(usuario.getId());
        }
        return List.of(); // Retorna uma lista vazia se o usuário não tiver permissões específicas
    }

    public List<Solicitacao> getSolicitacoesPorStatus(StatusSolicitacao status) throws SQLException {
        return solicitacaoDAO.getSolicitacoesPorStatus(status);
    }

    // ***** PENDENTE VALIDAÇÃO
//    public void atualizarStatusSolicitacao(Solicitacao solicitacao, StatusSolicitacao novoStatus) {
//        solicitacao.setStatus(novoStatus);
//        try {
//            solicitacaoDAO.atualizarSolicitacao(solicitacao);
//        } catch (SQLException e) {
//            e.printStackTrace();

    // Método para atualizar o status da solicitação
    // SUBSTITUI todo o método penden acima (atualizarStatusSolicitacao)
    public void atualizarStatusSolicitacao(Usuario usuario, PermissaoService permissaoService, Solicitacao solicitacao, StatusSolicitacao novoStatus) {
        // Verificação se o usuário tem permissão para aprovar/reprovar solicitações
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
