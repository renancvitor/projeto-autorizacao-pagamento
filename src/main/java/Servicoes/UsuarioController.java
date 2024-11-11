package Servicoes;

import Entities.Usuario;

public class UsuarioController {
    private PermissaoService permissaoService;

    public UsuarioController(PermissaoService permissaoService) {
        this.permissaoService = permissaoService;
    }

    public void realizarAcaoDeGerenciamento(Usuario usuario) {
        if (permissaoService.gerenciarUsuarios(usuario)) {
            // Realiza a ação de gerenciamento, pois o usuário tem permissão
        } else {
            // Exibe uma mensagem de erro ou impede a ação
        }
    }
}
