package Application;

import Entities.UserType;
import Entities.Usuario;
import DAO.UsuarioDAO;
import java.sql.SQLException;
import java.util.List;

public class CadastroUsuarios {
    private UsuarioDAO usuarioDAO;

    public CadastroUsuarios(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    public void cadastrarUsuario(int id, String login, String senha, List<String> permissoes, String cpf, int idTipoUsuario, UserType userType) throws SQLException {
        Usuario usuarioLogado = usuarioDAO.getUsuarioByLogin(login, senha);

        if (usuarioLogado.isAdmin()) {
            Usuario novoUsuario = new Usuario(0, login, senha, permissoes, cpf, idTipoUsuario, userType);
            usuarioDAO.inserirUsuario(novoUsuario);
        } else {
            throw new RuntimeException("Apenas administradores podem cadastrar novos usuários.");
        }
    }
}
