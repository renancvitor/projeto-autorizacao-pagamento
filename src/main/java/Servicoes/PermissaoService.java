package Servicoes;

import Entities.Usuario;

public class PermissaoService {

    public boolean aprovarReprovarSolicitacoes(Usuario usuario) {
        return usuario.hasPermission("APROVAR_REPROVAR_SOLICITACOES");
    }

    public boolean visualizarTodasSolicitacoes(Usuario usuario) {
        return usuario.hasPermission("VISUALIZAR_TODAS_SOLICITACOES");
    }

    public boolean gerenciarUsuarios(Usuario usuario) {
        return usuario.hasPermission("GERENCIAR_USUARIOS");
    }

    public boolean realizarSolicitacoes(Usuario usuario) {
        return usuario.hasPermission("REALIZAR_SOLICITACOES");
    }
}
