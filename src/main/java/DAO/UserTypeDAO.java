package DAO;

import Entities.UserType;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class UserTypeDAO {
    private final Connection connection;

    // Mapeamento de UserType com o ID do banco, que facilita ao converter entre enum e banco de dados
    private static final Map<UserType, Integer> userTypeToIdMap = new HashMap<>();
    private static final Map<Integer, UserType> idToUserTypeMap = new HashMap<>();

    public UserTypeDAO(Connection connection) {
        this.connection = connection;
        loadUserTypes();  // Carregar o mapeamento ao inicializar a DAO
    }

    // Método para carregar o mapeamento do banco para o enum UserType
    private void loadUserTypes() {
        String query = "SELECT id, nome FROM tipos_usuarios";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");

                // Convertendo o nome do banco para o enum correspondente
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

    // Retorna o ID do banco correspondente ao UserType fornecido
    public int getUserTypeId(UserType userType) {
        return userTypeToIdMap.getOrDefault(userType, -1);
    }

    // Retorna o UserType correspondente ao ID do banco fornecido
    public UserType getUserTypeById(int id) {
        return idToUserTypeMap.get(id);
    }

    // CASO A SE PENSAR *---* OPCIONAL
    // Exemplo de um método para adicionar um novo tipo de usuário no banco (se necessário)
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
