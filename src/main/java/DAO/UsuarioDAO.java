package DAO;

import Entities.UserType;
import Entities.Usuario;
import Servicoes.PermissaoService;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.List;

public class UsuarioDAO {
    private Connection connection;
    private final PermissaoService permissaoService;

    public UsuarioDAO(Connection connection) {
        this.connection = connection;
        this.permissaoService = new PermissaoService();
    }

    private int getIdPessoaByCpf(String cpf) throws SQLException {
        String sql = "SELECT idpessoa FROM pessoa WHERE cpf = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("idpessoa");
                }
            }
        }
        return -1;
    }

    private boolean isUsuarioExistente(int idPessoa) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE idpessoa = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idPessoa);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    private UserType getUserTypeFromId(int idTipoUsuario) {
        return switch (idTipoUsuario) {
            case 1 -> UserType.ADMIN;
            case 2 -> UserType.GESTOR;
            case 3 -> UserType.VISUALIZADOR;
            case 4 -> UserType.COMUM;
            default -> throw new IllegalArgumentException("Tipo de usuário inválido: " + idTipoUsuario);
        };
    }

    private int getIdFromUserType(UserType userType) {
        return switch (userType) {
            case ADMIN -> 1;
            case GESTOR -> 2;
            case VISUALIZADOR -> 3;
            case COMUM -> 4;
        };
    }

//    public Usuario getUsuarioByLogin(String login, String senha) throws SQLException {
//        String sql = "SELECT * FROM usuarios WHERE login = ? AND senha = ?";
//        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
//            stmt.setString(1, login);
//            stmt.setString(2, senha);
//            try (ResultSet rs = stmt.executeQuery()) {
//                if (rs.next()) {
//                    int id = rs.getInt("id");
//                    String retrievedLogin = rs.getString("login");
//                    String retrievedSenha = rs.getString("senha");
//                    String cpf = rs.getString("cpf");
//                    int idTipoUsuario = rs.getInt("id_tipo_usuario");
//                    UserType userType = getUserTypeFromId(idTipoUsuario);
//
//                    List<String> permissoes = getPermissoesByUsuarioId(id);
//
//                    return new Usuario(id, retrievedLogin, retrievedSenha, permissoes, cpf, idTipoUsuario, userType);
//                }
//            }
//        }
//        return null;
//    }

    public Usuario getUsuarioByLogin(String login, String senha) throws SQLException {
        String sql = "SELECT id, login, senha, cpf, id_tipo_usuario FROM usuarios WHERE login = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, login);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String retrievedLogin = rs.getString("login");
                    String retrievedSenha = rs.getString("senha");
                    String cpf = rs.getString("cpf");
                    int idTipoUsuario = rs.getInt("id_tipo_usuario");

                    String senhaHash = hashSenha(senha);

                    if (senhaHash.equals(retrievedSenha)) {
                        UserType userType = getUserTypeFromId(idTipoUsuario);
                        List<String> permissoes = getPermissoesByUsuarioId(id);

                        return new Usuario(id, retrievedLogin, retrievedSenha, permissoes, cpf, idTipoUsuario, userType);
                    }
                }
            }
        } catch (NoSuchAlgorithmException e) {
            throw new SQLException("Erro ao gerar o hash da senha.", e);
        }
        return null;
    }

    public List<String> getPermissoesByUsuarioId(int usuarioId) throws SQLException {
        String sql = "SELECT id_tipo_usuario FROM usuarios WHERE id = ?";
        int tipoUsuarioId = -1;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    tipoUsuarioId = rs.getInt("id_tipo_usuario");
                }
            }
        }

        UserType userType = switch (tipoUsuarioId) {
            case 1 -> UserType.ADMIN;
            case 2 -> UserType.GESTOR;
            case 3 -> UserType.VISUALIZADOR;
            case 4 -> UserType.COMUM;
            default -> null;
        };

        return (userType != null) ? permissaoService.getPermissoesByUserType(userType) : List.of();
    }

    public void inserirUsuario(Usuario usuario) throws SQLException {
        int idPessoa = getIdPessoaByCpf(usuario.getCpf());
        if (idPessoa == -1) {
            throw new IllegalArgumentException("CPF não cadastrado na tabela Pessoa!");
        }

        if (isUsuarioExistente(idPessoa)) {
            throw new IllegalArgumentException("Esta pessoa já tem um usuário vinculado!");
        }

        String sql = "INSERT INTO usuarios (login, senha, idpessoa, id_tipo_usuario, cpf) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, usuario.getLogin());
            stmt.setString(2, usuario.getSenha());
            stmt.setInt(3, idPessoa); // Usa o idpessoa para vincular o usuário
            stmt.setInt(4, getIdFromUserType(usuario.getUserType())); // Obtém o id_tipo_usuario do UserType
            stmt.setString(5, usuario.getCpf());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int userId = generatedKeys.getInt(1);
                    usuario.setId(userId);
                    consultarPermissoesUsuario(usuario);
                }
            }
        }
    }

    private int obterIdPermissaoPorNome(String nomePermissao) throws SQLException {
        String sql = "SELECT id FROM permissoes WHERE nome_permissao = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nomePermissao);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }
        throw new IllegalArgumentException("Permissão não encontrada: " + nomePermissao);
    }

    private void consultarPermissoesUsuario(Usuario usuario) throws SQLException {
        String sql = "SELECT p.nome_permissao FROM tipos_usuarios_permissoes tp "
                + "JOIN permissoes p ON tp.id_permissao = p.id "
                + "WHERE tp.id_tipo_usuario = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, usuario.getId());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String permissao = rs.getString("nome_permissao");
                    usuario.adicionarPermissao(permissao);
                }
            }
        }
    }

    public boolean verificarSenhaPorUsuario(String username, String senhaAtual) throws SQLException {
        try {
            String senhaHash = hashSenha(senhaAtual);

            String query = "SELECT senha FROM usuarios WHERE login = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, username);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String senhaArmazenada = rs.getString("senha");

                        if (senhaArmazenada.equals(senhaAtual)) {
                            atualizarSenhaParaHash(username, senhaHash);
                            return true;
                        } else if (senhaArmazenada.equals(senhaHash)) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
            }
            return false;
        } catch (NoSuchAlgorithmException e) {
            throw new SQLException("Erro ao gerar hash da senha.", e);
        }
    }

    private void atualizarSenhaParaHash(String username, String senhaHash) throws SQLException {
        String query = "UPDATE usuarios SET senha = ? WHERE login = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, senhaHash);
            stmt.setString(2, username);
            stmt.executeUpdate();
            System.out.println("Senha migrada para hash com sucesso para o usuário: " + username);
        }
    }

    public boolean alterarSenhaPorUsuario(String username, String novaSenha) throws SQLException {
        try {
            String senhaHash = hashSenha(novaSenha);
            String query = "UPDATE usuarios SET senha = ? WHERE login = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, senhaHash);
                stmt.setString(2, username);
                int rowsUpdated = stmt.executeUpdate();
                return rowsUpdated > 0;
            }
        } catch (NoSuchAlgorithmException e) {
            throw new SQLException("Erro ao gerar hash da nova senha.", e);
        }
    }

    private String hashSenha(String senha) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = md.digest(senha.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }
}