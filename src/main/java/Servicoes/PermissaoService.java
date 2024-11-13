package Servicoes;

import Entities.UserType;
import Entities.Usuario;

public class PermissaoService {
    private UserType userType;

    public PermissaoService(UserType userType) {
        this.userType = userType;
    }

    public boolean aprovarReprovarSolicitacoes(Usuario usuario) {
        return userType == UserType.ADMIN || userType == UserType.GESTOR;
    }

    public boolean visualizarTodasSolicitacoes(Usuario usuario) {
        return userType == UserType.ADMIN || userType == UserType.GESTOR || userType == UserType.VISUALIZADOR;
    }

    public boolean gerenciarUsuarios(Usuario usuario) {
        return userType == UserType.ADMIN;
    }

    // Todos usuários podem realizar solicitações
    public boolean realizarSolicitacoes(Usuario usuario) {
        return true;
    }
}
