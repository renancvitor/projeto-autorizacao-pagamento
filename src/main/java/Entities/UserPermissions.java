package Entities;

import java.util.*;

public class UserPermissions {
    private static final Map<UserType, List<String>> permissionsMap = new HashMap<>();

    static {
        permissionsMap.put(UserType.ADMIN, Arrays.asList("GERENCIAR_USUARIOS", "REALIZAR_SOLICITAOES",
                "VISUALIZAR_TODAS_SOLICITACOES", "APROVAR_REPROVAR_SOLICITAÇOES"));
        permissionsMap.put(UserType.GESTOR, Arrays.asList("REALIZAR_SOLICITAOES",
                "VISUALIZAR_TODAS_SOLICITACOES", "APROVAR_REPROVAR_SOLICITAÇOES"));
        permissionsMap.put(UserType.VISUALIZADOR, Arrays.asList("REALIZAR_SOLICITAOES",
                "VISUALIZAR_TODAS_SOLICITACOES"));
        permissionsMap.put(UserType.COMUM, Collections.singletonList("REALIZAR_SOLICITAOES"));
    }

    public static List<String> getPermissions(UserType userType) {
        return permissionsMap.getOrDefault(userType, Collections.emptyList());
    }
}
