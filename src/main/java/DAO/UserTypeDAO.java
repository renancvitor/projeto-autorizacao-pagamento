package DAO;

import Entities.UserType;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class UserTypeDAO {
    private final Connection connection;

    private static final Map<UserType, Integer> userTypeToIdMap = new HashMap<>();
    private static final Map<Integer, UserType> idToUserTypeMap = new HashMap<>();

    public UserTypeDAO(Connection connection) {
        this.connection = connection;
        loadUserTypes();
    }

    private void loadUserTypes() {
        String query = "SELECT id, nome FROM tipos_usuarios";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");

                UserType userType = UserType.valueOf(nome.toUpperCase());
                userTypeToIdMap.put(userType, id);
                idToUserTypeMap.put(id, userType);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erro ao carregar tipos de usuários do banco.");
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: Tipo de usuário no banco não corresponde ao enum UserType.");
        }
    }

    public int getUserTypeId(UserType userType) {
        return userTypeToIdMap.getOrDefault(userType, -1);
    }

    public UserType getUserTypeById(int id) {
        return idToUserTypeMap.get(id);
    }

    public void addUserType(UserType userType) throws SQLException {
        String query = "INSERT INTO tipos_usuarios (nome) VALUES (?)";

        try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, userType.name());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    userTypeToIdMap.put(userType, id);
                    idToUserTypeMap.put(id, userType);
                }
            }
        }
    }
}
