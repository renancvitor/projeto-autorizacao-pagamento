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
        } else if (usuario.hasPermission("REALIZAR_SOLICITAOES")) {
            return solicitacaoDAO.getSolicitacoesPorUsuario(usuario.getId());
        }
        return List.of(); // Retorna uma lista vazia se o usuário não tiver permissões específicas
    }
}
