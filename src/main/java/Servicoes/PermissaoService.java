package Servicoes;

import Entities.UserType;
import Entities.Usuario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermissaoService {

    private final Map<UserType, List<String>> permissoesPorTipoUsuario;

    public PermissaoService() {
        permissoesPorTipoUsuario = new HashMap<>();
        permissoesPorTipoUsuario.put(UserType.ADMIN, List.of("aprovarReprovarSolicitacoes", "visualizarTodasSolicitacoes", "gerenciarUsuarios", "realizarSolicitacoes"));
        permissoesPorTipoUsuario.put(UserType.GESTOR, List.of("aprovarReprovarSolicitacoes", "visualizarTodasSolicitacoes", "realizarSolicitacoes"));
        permissoesPorTipoUsuario.put(UserType.VISUALIZADOR, List.of("visualizarTodasSolicitacoes", "realizarSolicitacoes"));
        permissoesPorTipoUsuario.put(UserType.COMUM, List.of("realizarSolicitacoes"));
    }

    public List<String> getPermissoesByUserType(UserType userType) {
        return permissoesPorTipoUsuario.getOrDefault(userType, new ArrayList<>());
    }

    // Método genérico para verificar se um tipo de usuário tem uma permissão específica
    private boolean hasPermissao(Usuario usuario, String permissao) {
        return permissoesPorTipoUsuario.getOrDefault(usuario.getUserType(), List.of()).contains(permissao);
    }

    // Métodos específicos de verificação de permissões
    public boolean aprovarReprovarSolicitacoes(Usuario usuario) {
        return hasPermissao(usuario, "aprovarReprovarSolicitacoes");
    }

    public boolean visualizarTodasSolicitacoes(Usuario usuario) {
        return hasPermissao(usuario, "visualizarTodasSolicitacoes");
    }

    public boolean gerenciarUsuarios(Usuario usuario) {
        return hasPermissao(usuario, "gerenciarUsuarios");
    }

    public boolean realizarSolicitacoes(Usuario usuario) {
        return hasPermissao(usuario, "realizarSolicitacoes");
    }
}
