package DAO;

import Entities.Usuario;
import Servicoes.TelaCadastroPessoa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    private Connection connection;

    public UsuarioDAO(Connection connection) {
        this.connection = connection;
    }

    // Método para validar CPF
    private boolean validarCpfNaPessoa(String cpf) throws SQLException {
        String sql = "SELECT COUNT(*) FROM pessoas WHERE cpf = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Verifica se o CPF existe na tabela Pessoa
                }
            }
        }
        return false;
    }

    public Usuario getUsuarioByLogin(String login, String senha) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE login = ? AND senha = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, login);
            stmt.setString(2, senha);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String retrievedLogin = rs.getString("login");
                    String retrievedSenha = rs.getString("senha");
                    String cpf = rs.getString("cpf");

                    // Obter permissões do usuário
                    List<String> permissoes = getPermissoesByUsuarioId(id);

                    return new Usuario(id, retrievedLogin, retrievedSenha, permissoes, cpf);
                }
            }
        }
        return null;//
    }

    private List<String> getPermissoesByUsuarioId(int usuarioId) throws SQLException {
        List<String> permissoes = new ArrayList<>();
        String sql = """
            SELECT p.nome_permissao 
            FROM permissoes p
            INNER JOIN tipos_usuarios_permissoes tup ON p.id = tup.id_permissao
            INNER JOIN usuarios u ON tup.id_tipo_usuario = u.id_tipo_usuario
            WHERE u.id = ?
            """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    permissoes.add(rs.getString("nome_permissao"));
                }
            }
        }
        return permissoes;
    }

    public void inserirUsuario(Usuario usuario) throws SQLException {
        // Valida o CPF antes de inserir o usuário
        if (!validarCpfNaPessoa(usuario.getCpf())) {
            throw new IllegalArgumentException("CPF não cadastrado na tabela Pessoa!");
        }

        String sql = "INSERT INTO usuarios (login, senha, cpf) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, usuario.getLogin());
            stmt.setString(2, usuario.getSenha());
            stmt.setString(3, usuario.getCpf()); // Usando CPF, não idPessoa
            stmt.executeUpdate();

            // Recupera o id gerado automaticamente
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int userId = generatedKeys.getInt(1);
                    usuario.setId(userId);
                    inserirPermissoesUsuario(usuario);
                }
            }
        }
    }

    private void inserirPermissoesUsuario(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO tipos_usuarios_permissoes (id_tipo_usuario, id_permissao) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (String permissao : usuario.getPermissoes()) {
                stmt.setInt(1, usuario.getId()); // id_tipo_usuario, assumindo que o id do usuário mapeia para o tipo do usuário
                stmt.setString(2, permissao); // insere a permissão
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }
}
