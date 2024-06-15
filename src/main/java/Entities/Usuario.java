package Entities;

public class Usuario {

    public String name;
    public String setor;

    public Usuario(String name, String setor) {
        this.name = name;
        this.setor = setor;
    }

    public String getName() {
        return name;
    }

    public String getSetor() {
        return setor;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }
}
