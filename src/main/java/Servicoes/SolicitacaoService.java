package Servicoes;

import DAO.SolicitacaoDAO;
import Entities.UserType;
import Entities.Usuario;
import javafx.scene.control.Alert;

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
//        if (permissaoService.visualizarTodasSolicitacoes(usuario)) { // SUBSTITUI
//            return solicitacaoDAO.getTodasSolicitacoes();
//        } else if (permissaoService.realizarSolicitacoes(usuario)) { // SUBSTITUI
//            return solicitacaoDAO.getSolicitacoesPorUsuario(usuario.getId());
//        }
//        return List.of();
//    }
        // Caso o usuário tenha permissão para visualizar todas as solicitações (ADMIN, GESTOR, VISUALIZADOR)
        if (permissaoService.visualizarTodasSolicitacoes(usuario)) {
            return solicitacaoDAO.getTodasSolicitacoes(usuario.getId());
        }

        // Se for um usuário do tipo COMUM, ele só pode ver suas próprias solicitações
        if (usuario.getUserType() == UserType.COMUM) {
            // Usuários COMUNS só podem ver suas próprias solicitações
            return solicitacaoDAO.getSolicitacoesPorUsuario(usuario.getId(), "COMUM");
        }

        // Outros tipos (ADMIN, GESTOR, VISUALIZADOR) podem ver todas
        return solicitacaoDAO.getSolicitacoesPorUsuario(usuario.getId(), usuario.getUserType().toString());
    }

    public List<Solicitacao> getSolicitacoesPorStatus(StatusSolicitacao status, int idTipoUsuario, int idUser) throws SQLException {
        return solicitacaoDAO.getSolicitacoesPorStatus(status, idTipoUsuario, idUser);
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
            // System.out.println("Acesso negado: Usuário não tem permissão para aprovar ou reprovar solicitações.");
            Alert alert = new Alert(Alert.AlertType.WARNING, "Acesso negado: Usuário não tem permissão para aprovar ou reprovar solicitações.");
            alert.show();
        }
    }
}
