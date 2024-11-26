package Entities;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AlterarSenha {

    private Connection connection;

    public AlterarSenha(Connection connection) {
        this.connection = connection;
    }

    public int getIdUsuarioByCpf(String cpf) throws SQLException {
        String sql = "SELECT id FROM usuarios WHERE cpf = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }
        return -1;
    }

    public boolean atualizarSenhaPorCpf(String cpf, String novaSenha) throws SQLException, NoSuchAlgorithmException {
        int idUsuario = getIdUsuarioByCpf(cpf);

        if (idUsuario == -1) {
            return false;
        }

        String senhaHash = hashSenha(novaSenha);

        String sql = "UPDATE usuarios SET senha = ? WHERE idusuario = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, senhaHash);
            stmt.setInt(2, idUsuario);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
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