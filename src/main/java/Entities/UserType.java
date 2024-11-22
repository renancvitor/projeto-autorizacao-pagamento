package Entities;

public enum UserType {
    ADMIN(1),
    GESTOR(2),
    VISUALIZADOR(3),
    COMUM(4);

    private final int id;

    UserType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static int getId(UserType type) {
        return type.getId();
    }

    public static UserType fromId(int id) {
        for (UserType type : values()) {
            if (type.id == id) {
                return type;
            }
        }
        throw new IllegalArgumentException("ID de tipo de usuário inválido: " + id);
    }
}
