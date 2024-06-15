package Servicoes;

import java.util.ArrayList;
import java.util.List;

public class SistemaSolicitacao {

    private static SistemaSolicitacao instance;
    private List<Solicitacao> solicitacoes;

    private SistemaSolicitacao() {
        solicitacoes = new ArrayList<>();
    }

    public static synchronized SistemaSolicitacao getInstance() {
        if (instance == null) {
            instance = new SistemaSolicitacao();
        }
        return instance;
    }

    public void adicionarSolicitacao (Solicitacao solicitacao) {
        solicitacoes.add(solicitacao);
    }

    public List<Solicitacao> getSolicitacoesPendentes() {
        List<Solicitacao> pendentes = new ArrayList<>();
        for (Solicitacao s : solicitacoes) {
            if (s.getStatus() == String.valueOf(StatusSolicitacao.PENDENTE)) {
                pendentes.add(s);
            }
        }
        return pendentes;
    }
    public void aprovarSolicitacao(Solicitacao solicitacao) {
        solicitacao.setStatus(String.valueOf(StatusSolicitacao.APROVADA));
    }

    public void rejeitarSolicitacao(Solicitacao solicitacao) {
        solicitacao.setStatus(String.valueOf(StatusSolicitacao.REJEITADA));
    }

}
